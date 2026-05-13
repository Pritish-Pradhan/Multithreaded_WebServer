package metrics;

public class SystemMetrics {

    private final int activeThreads;
    private final int queueSize;
    private final long completedTasks;
    private final int currentPoolSize;
    private final int corePoolSize;
    private final double averageLatency;

    public SystemMetrics(
            int activeThreads,
            int queueSize,
            long completedTasks,
            int currentPoolSize,
            int corePoolSize,
            double averageLatency
    ) {

        this.activeThreads = activeThreads;
        this.queueSize = queueSize;
        this.completedTasks = completedTasks;
        this.currentPoolSize = currentPoolSize;
        this.corePoolSize = corePoolSize;
        this.averageLatency = averageLatency;
    }

    public int getActiveThreads() {
        return activeThreads;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public int getCurrentPoolSize() {
        return currentPoolSize;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public double getAverageLatency() {
        return averageLatency;
    }

    @Override
    public String toString() {

        return "\n=== SYSTEM METRICS ===\n" +
                "Active Threads: " + activeThreads + "\n" +
                "Queue Size: " + queueSize + "\n" +
                "Completed Tasks: " + completedTasks + "\n" +
                "Current Pool Size: " + currentPoolSize + "\n" +
                "Core Pool Size: " + corePoolSize + "\n" +
                "Average Latency: " + averageLatency + " ms\n" +
                "=========================\n";
    }
}