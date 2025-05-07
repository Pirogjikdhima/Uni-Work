package com.zkqueue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DistributedQueueTest {
    private static final Logger LOG = LoggerFactory.getLogger(DistributedQueueTest.class);
    private static final String ZK_CONNECTION = "localhost:2181,localhost:2182,localhost:2183";
    private static final String QUEUE_PATH = "/queues/test";

    private static final int NUM_PRODUCERS = 3;
    private static final int NUM_CONSUMER_THREADS = 3;
    private static final int ITEMS_PER_PRODUCER = 2;
    private static final int TOTAL_ITEMS = NUM_PRODUCERS * ITEMS_PER_PRODUCER;

    private static final AtomicInteger processedCount = new AtomicInteger(0);
    private static final List<String> processedItems = new ArrayList<>();

    public static class QueueMessage implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private final String producerId;
        private final int itemId;
        private final long timestamp;

        public QueueMessage(String producerId, int itemId) {
            this.producerId = producerId;
            this.itemId = itemId;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return String.format("Message(producer=%s, id=%d, time=%d)", producerId, itemId, timestamp);
        }

        public String getUniqueId() {
            return producerId + "-" + itemId;
        }
    }

    public static void main(String[] args) {
        LOG.info("Starting shared queue test with thread pool consumers");

        CountDownLatch completionLatch = new CountDownLatch(TOTAL_ITEMS);
        ExecutorService consumerExecutor = Executors.newFixedThreadPool(NUM_CONSUMER_THREADS);

        try (ZKDistributedQueue<QueueMessage> sharedQueue = new ZKDistributedQueue<>(
                ZK_CONNECTION,
                QUEUE_PATH,
                new ThreadPooledConsumer("shared-consumer", consumerExecutor, completionLatch)
        )) {
            sharedQueue.start();
            LOG.info("Shared queue consumer started");

            ExecutorService producerExecutor = Executors.newFixedThreadPool(NUM_PRODUCERS);
            for (int i = 0; i < NUM_PRODUCERS; i++) {
                String producerId = "producer-" + i;
                producerExecutor.submit(new Producer(producerId, sharedQueue, ITEMS_PER_PRODUCER));
            }

            producerExecutor.shutdown();
            producerExecutor.awaitTermination(1, TimeUnit.MINUTES);

            LOG.info("Waiting for all items to be processed...");
            boolean allProcessed = completionLatch.await(5, TimeUnit.MINUTES);

            if (allProcessed) {
                LOG.info("✅ All items processed successfully!");
            } else {
                LOG.warn("❗ Timeout occurred. Processed: {}/{}", processedCount.get(), TOTAL_ITEMS);
            }

            int unique = new HashSet<>(processedItems).size();
            if (unique != processedItems.size()) {
                LOG.warn("❗ Duplicate messages detected: {} unique / {} total", unique, processedItems.size());
            } else {
                LOG.info("✔️ No duplicates. All items processed once.");
            }

        } catch (Exception e) {
            LOG.error("Fatal error in queue test", e);
        } finally {
            consumerExecutor.shutdownNow();
            LOG.info("Shutdown complete.");
        }
    }

    public static class Producer implements Runnable {
        private final String producerId;
        private final ZKDistributedQueue<QueueMessage> queue;
        private final int numItems;

        public Producer(String producerId, ZKDistributedQueue<QueueMessage> queue, int numItems) {
            this.producerId = producerId;
            this.queue = queue;
            this.numItems = numItems;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < numItems; i++) {
                    QueueMessage msg = new QueueMessage(producerId, i);
                    queue.put(msg);
                    LOG.info("Producer {} -> {}", producerId, msg);
                }
            } catch (Exception e) {
                LOG.error("Producer {} failed", producerId, e);
            }
        }
    }

    public static class ThreadPooledConsumer implements QueueConsumer<QueueMessage> {
        private final String name;
        private final ExecutorService threadPool;
        private final CountDownLatch latch;

        public ThreadPooledConsumer(String name, ExecutorService threadPool, CountDownLatch latch) {
            this.name = name;
            this.threadPool = threadPool;
            this.latch = latch;
        }

        @Override
        public void consumeMessage(QueueMessage message) {
            threadPool.submit(() -> {
                try {
                    LOG.info("[{}] Processing: {}", name, message);
                    synchronized (processedItems) {
                        processedItems.add(message.getUniqueId());
                    }
                    processedCount.incrementAndGet();
                    latch.countDown();
                } catch (Exception e) {
                    LOG.error("[{}] Error processing message: {}", name, message, e);
                }
            });
        }

        @Override
        public void stateChanged(CuratorFramework client, org.apache.curator.framework.state.ConnectionState newState) {
            LOG.info("[{}] State changed: {}", name, newState);
        }
    }
}
