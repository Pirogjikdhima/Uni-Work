package com.zkqueue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ZKDistributedQueue<T> implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(ZKDistributedQueue.class);
    private final AtomicInteger queueSize = new AtomicInteger(0);
    private final CuratorFramework client;
    private final String queuePath;
    private final DistributedQueue<T> queue;
    private boolean isStarted = false;

    static class ObjectSerializer<T> implements QueueSerializer<T> {
        @Override
        public byte[] serialize(T item) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(item);
                return bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("Error serializing queue item", e);
            }
        }

        @Override
        public T deserialize(byte[] bytes) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                return (T) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("Error deserializing queue item", e);
            }
        }
    }

    public ZKDistributedQueue(String connectionString, String queuePath, QueueConsumer<T> consumer) {
        this.queuePath = queuePath;

        client = CuratorFrameworkFactory.newClient(
                connectionString,
                new ExponentialBackoffRetry(1000, 3));
        client.start();

        QueueBuilder<T> builder = QueueBuilder.builder(
                client,
                consumer,
                new ObjectSerializer<>(),
                queuePath);

        queue = builder.buildQueue();

        LOG.info("ZKDistributedQueue initialized with path: {}", queuePath);
    }

    public void start() throws Exception {
        LOG.info("Starting queue at path: {}", queuePath);
        queue.start();
        isStarted = true;
        LOG.info("Queue started successfully at path: {}", queuePath);
    }


    public void put(T item) throws Exception {
        LOG.debug("Adding item to queue at path: {}", queuePath);
        queue.put(item);
        queueSize.incrementAndGet();
        LOG.debug("Item added successfully to queue at path: {}", queuePath);
    }


    public int size() throws Exception {
        return queueSize.get();
    }

    @Override
    public void close() throws Exception {
        try {
            if (isStarted) {
                LOG.info("Closing queue at path: {}", queuePath);
                queue.close();
                LOG.info("Queue closed successfully at path: {}", queuePath);
            }
        } finally {
            client.close();
            LOG.info("ZKDistributedQueue client closed for path: {}", queuePath);
        }
    }
}