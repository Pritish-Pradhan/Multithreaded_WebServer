package manager;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DynamicThreadPoolManager {

    private final ThreadPoolExecutor executor;

    public DynamicThreadPoolManager(
            int corePoolSize,
            int maxPoolSize
    ) {

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
    public void submitTask(Runnable task) {
        executor.submit(task);
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
} 