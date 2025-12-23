package com.jayjd.boxtop.nanohttpd.interfas;

import android.os.Build;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ManageableThreadPool {
    private final ExecutorService executor;
    private final Set<Future<?>> futures;

    public ManageableThreadPool(int poolSize) {
        this.executor = Executors.newFixedThreadPool(poolSize);
        this.futures = Collections.synchronizedSet(new HashSet<>());
    }

    public Future<?> submit(Runnable task) {
        Future<?> future = executor.submit(() -> {
            // 检查中断状态
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            task.run();
        });
        futures.add(future);
        return future;
    }

    public void shutdownNow() {
        // 取消所有Future
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            futures.forEach(future -> future.cancel(true));
        } else {
            for (Future<?> future : futures) {
                future.cancel(true);
            }
        }
        // 关闭线程池
        executor.shutdownNow();
        futures.clear();
    }

    public void shutdownGracefully(long timeout, TimeUnit unit) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(timeout, unit)) {
                shutdownNow();
            }
        } catch (InterruptedException e) {
            shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
