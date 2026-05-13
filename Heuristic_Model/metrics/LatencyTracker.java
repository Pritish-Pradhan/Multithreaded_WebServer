package metrics;

import java.util.concurrent.atomic.AtomicLong;

public class LatencyTracker {

    private final AtomicLong totalLatency =
            new AtomicLong(0);

    private final AtomicLong completedRequests =
            new AtomicLong(0);

    public void recordLatency(long latencyMs) {

        totalLatency.addAndGet(latencyMs);

        completedRequests.incrementAndGet();
    }

    public double getAverageLatency() {

        long completed =
                completedRequests.get();

        if (completed == 0) {
            return 0;
        }

        return (double) totalLatency.get()
                / completed;
    }

    public long getCompletedRequests() {
        return completedRequests.get();
    }
}