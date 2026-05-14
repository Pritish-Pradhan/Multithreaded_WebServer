package manager;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import metrics.LatencyTracker;
import metrics.MetricsCollector;
import metrics.SystemMetrics;

public class DynamicThreadPoolManager {

    private final ThreadPoolExecutor executor;
    private final LatencyTracker latencyTracker;

    public DynamicThreadPoolManager(
    int corePoolSize,
    int maxPoolSize,
    LatencyTracker latencyTracker
    ) {

        this.latencyTracker =
                latencyTracker;

        executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(500)
        );
    }

    /**
     * Dynamically resize thread pool
     */
    public void resizePool(int newSize) {

    int currentMax = executor.getMaximumPoolSize();

    // Increasing pool size
    if (newSize > currentMax) {

        executor.setMaximumPoolSize(newSize);
        executor.setCorePoolSize(newSize);

    }
    // Decreasing pool size
    else {

        executor.setCorePoolSize(newSize);
        executor.setMaximumPoolSize(newSize);
    }

    System.out.println(
            "[POOL RESIZED] New Pool Size = "
                    + newSize
    );
}

    /**
     * Submit simulated request/task
     */
    public java.util.concurrent.Future<?> submitTask(Runnable task) {

        try {

            return executor.submit(task);

        }
        catch (java.util.concurrent.RejectedExecutionException e) {

            latencyTracker.recordRejection();

            System.out.println(
                    "[OVERLOAD CONTROL] " +
                    "Request rejected → Queue full"
            );

            return null;
        }
    }

    /**
     * Shutdown executor
     */
    public void shutdown() {
        executor.shutdown();
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public SystemMetrics collectMetrics() {

        MetricsCollector collector =
                new MetricsCollector(
                        executor,
                        latencyTracker
                );

        return collector.collect();
    }
} 