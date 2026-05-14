package metrics;

public class SystemMetrics {

    private final int activeThreads;
    private final int queueSize;
    private final long completedTasks;
    private final int currentPoolSize;
    private final int corePoolSize;
    private final double averageLatency;
    private final long rejectedRequests;

    private final double highPriorityLatency;
    private final double mediumPriorityLatency;
    private final double lowPriorityLatency;

    private final long highRequestCount;
    private final long mediumRequestCount;
    private final long lowRequestCount;

    public SystemMetrics(
        int activeThreads,
        int queueSize,
        long completedTasks,
        int currentPoolSize,
        int corePoolSize,
        double averageLatency,
        long rejectedRequests,
        double highPriorityLatency,
        double mediumPriorityLatency,
        double lowPriorityLatency,

        long highRequestCount,
        long mediumRequestCount,
        long lowRequestCount
    ) {

        this.activeThreads = activeThreads;
        this.queueSize = queueSize;
        this.completedTasks = completedTasks;
        this.currentPoolSize = currentPoolSize;
        this.corePoolSize = corePoolSize;
        this.averageLatency = averageLatency;

        this.rejectedRequests =
                rejectedRequests;

        this.highPriorityLatency =
                highPriorityLatency;

        this.mediumPriorityLatency =
                mediumPriorityLatency;

        this.lowPriorityLatency =
                lowPriorityLatency;

        this.highRequestCount =
                highRequestCount;

        this.mediumRequestCount =
                mediumRequestCount;

        this.lowRequestCount =
                lowRequestCount;
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

    public long getRejectedRequests() {
        return rejectedRequests;
    }
    public double getHighPriorityLatency() {
        return highPriorityLatency;
    }

    public double getMediumPriorityLatency() {
        return mediumPriorityLatency;
    }

    public double getLowPriorityLatency() {
        return lowPriorityLatency;
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
                "Rejected Requests: " + rejectedRequests + "\n" +
                "\n=== SLA METRICS ==="

                + "\nHIGH Priority Avg Latency: "
                + highPriorityLatency + " ms"

                + "\nHIGH Requests Completed: "
                + highRequestCount

                + "\nMEDIUM Priority Avg Latency: "
                + mediumPriorityLatency + " ms"

                + "\nMEDIUM Requests Completed: "
                + mediumRequestCount

                + "\nLOW Priority Avg Latency: "
                + lowPriorityLatency + " ms"

                + "\nLOW Requests Completed: "
                + lowRequestCount +
                "\n=========================\n";
    }
}