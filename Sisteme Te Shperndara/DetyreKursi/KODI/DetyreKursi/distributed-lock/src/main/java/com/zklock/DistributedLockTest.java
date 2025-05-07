package com.zklock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistributedLockTest {
    private static final Logger LOG = LoggerFactory.getLogger(DistributedLockTest.class);
    private static final String ZK_CONNECTION = "localhost:2181,localhost:2182,localhost:2183,localhost:2184,localhost:2185";
    private static final String LOCK_PATH = "/locks/counter";
    private static final int NUMBER_OF_THREADS = 4;
    private static final int OPERATIONS_PER_THREAD = 4;

    private static AtomicInteger sharedCounter = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        LOG.info("Starting Distributed Lock Test with {} threads, {} operations each",
                NUMBER_OF_THREADS, OPERATIONS_PER_THREAD);

        runWithoutLocks();
        Thread.sleep(2000);
        sharedCounter.set(0);
        runWithLocks();
    }

    private static void runWithoutLocks() throws Exception {
        LOG.info("=== TEST WITHOUT LOCKS ===");
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(NUMBER_OF_THREADS);

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            Thread t = new Thread(new Worker(startSignal, doneSignal, i, false));
            threads.add(t);
            t.start();
        }

        startSignal.countDown();
        LOG.info("All threads started");

        doneSignal.await();
        LOG.info("All threads completed");
        LOG.info("Final counter value: {} (Expected: {})",
                sharedCounter.get(), NUMBER_OF_THREADS * OPERATIONS_PER_THREAD);
    }

    private static void runWithLocks() throws Exception {
        LOG.info("=== TEST WITH LOCKS ===");
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(NUMBER_OF_THREADS);

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            Thread t = new Thread(new Worker(startSignal, doneSignal, i, true));
            threads.add(t);
            t.start();
        }

        startSignal.countDown();
        LOG.info("All threads started");

        doneSignal.await();
        LOG.info("All threads completed");
        LOG.info("Final counter value: {} (Expected: {})",
                sharedCounter.get(), NUMBER_OF_THREADS * OPERATIONS_PER_THREAD);
    }
    static class Worker implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;
        private final int id;
        private final boolean useLock;

        public Worker(CountDownLatch startSignal, CountDownLatch doneSignal, int id, boolean useLock) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
            this.id = id;
            this.useLock = useLock;
        }

        @Override
        public void run() {
            try {
                startSignal.await();
                LOG.info("Worker {} starting", id);

                ZKDistributedLock lock = null;
                if (useLock) {
                    lock = new ZKDistributedLock(ZK_CONNECTION, LOCK_PATH);
                }

                for (int i = 0; i < OPERATIONS_PER_THREAD; i++) {
                    try {
                        if (useLock) {
                            if (!lock.acquire(30, TimeUnit.SECONDS)) {
                                LOG.error("Worker {} failed to acquire lock, skipping operation", id);
                                continue;
                            }
                        }

                        int currentValue = sharedCounter.get();

                        Thread.sleep((long) (Math.random() * 10));

                        sharedCounter.set(currentValue + 1);
                        LOG.info("Worker {} incremented counter to {}", id, sharedCounter.get());

                    } catch (Exception e) {
                        LOG.error("Worker {} error: {}", id, e.getMessage(), e);
                    } finally {
                        if (useLock && lock != null && lock.isAcquired()) {
                            try {
                                lock.release();
                            } catch (Exception e) {
                                LOG.error("Worker {} error releasing lock: {}", id, e.getMessage(), e);
                            }
                        }
                    }
                }

                if (lock != null) {
                    try {
                        lock.close();
                    } catch (Exception e) {
                        LOG.error("Worker {} error closing lock: {}", id, e.getMessage(), e);
                    }
                }

                LOG.info("Worker {} completed all operations", id);
            } catch (Exception e) {
                LOG.error("Worker {} encountered an error: {}", id, e.getMessage(), e);
            } finally {
                doneSignal.countDown();
            }
        }
    }
}