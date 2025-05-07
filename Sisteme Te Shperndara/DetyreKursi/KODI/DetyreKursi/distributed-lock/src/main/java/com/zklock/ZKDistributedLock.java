package com.zklock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ZKDistributedLock implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(ZKDistributedLock.class);

    private final CuratorFramework client;
    private final InterProcessMutex lock;
    private final String lockPath;
    private boolean isAcquired = false;

    public ZKDistributedLock(String connectionString, String lockPath) {
        this.lockPath = lockPath;

        client = CuratorFrameworkFactory.newClient(
                connectionString,
                new ExponentialBackoffRetry(1000, 3));
        client.start();

        lock = new InterProcessMutex(client, lockPath);

    }

    public boolean acquire(long time, TimeUnit unit) throws Exception {
        LOG.info("Attempting to acquire lock at path: {} with timeout: {} {}", lockPath, time, unit);
        boolean acquired = lock.acquire(time, unit);
        isAcquired = acquired;
        if (acquired) {
            LOG.info("Lock acquired successfully at path: {}", lockPath);
        } else {
            LOG.warn("Failed to acquire lock at path: {} within timeout: {} {}", lockPath, time, unit);
        }
        return acquired;
    }

    public void release() throws Exception {
        if (isAcquired) {
            LOG.info("Releasing lock at path: {}", lockPath);
            lock.release();
            isAcquired = false;
        } else {
            LOG.warn("Attempted to release lock that was not acquired at path: {}", lockPath);
        }
    }

    public boolean isAcquired() {
        return isAcquired && lock.isOwnedByCurrentThread();
    }

    @Override
    public void close() throws Exception {
        try {
            if (isAcquired) {
                release();
            }
        } finally {
            client.close();
        }
    }
}