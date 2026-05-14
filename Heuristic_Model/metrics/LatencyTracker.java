package metrics;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import task.RequestPriority;

public class LatencyTracker {

    /*
     * Sliding window size
     */

    private static final int WINDOW_SIZE = 100;

    /*
     * Overall latency window
     */

    private final Queue<Long> latencyWindow =
            new LinkedList<>();

    private long windowLatencySum = 0;

    /*
     * SLA-aware latency tracking
     */

    private final Queue<Long>
    highLatencyWindow =
            new LinkedList<>();

    private final Queue<Long>
    mediumLatencyWindow =
            new LinkedList<>();

    private final Queue<Long>
    lowLatencyWindow =
            new LinkedList<>();

    private long highWindowSum = 0;
    private long mediumWindowSum = 0;
    private long lowWindowSum = 0;

    private long highRequestCount = 0;
    private long mediumRequestCount = 0;
    private long lowRequestCount = 0;

    /*
     * Request counters
     */

    private final AtomicLong completedRequests =
            new AtomicLong(0);

    private final AtomicLong rejectedRequests =
            new AtomicLong(0);

    /**
     * Record request latency
     */
    public synchronized void recordLatency(
            long latencyMs,
            RequestPriority priority
    ) {

        /*
         * Sliding window
         */

        latencyWindow.add(latencyMs);

        windowLatencySum += latencyMs;

        if (latencyWindow.size() >
                WINDOW_SIZE) {

            long removed =
                    latencyWindow.remove();

            windowLatencySum -= removed;
        }

        /*
         * SLA-aware metrics
         */

        switch (priority) {

            case HIGH:

                highLatencyWindow.add(latencyMs);

                highWindowSum += latencyMs;

                if (highLatencyWindow.size()
                        > WINDOW_SIZE) {

                    long removed =
                            highLatencyWindow.remove();

                    highWindowSum -= removed;
                }

                highRequestCount++;
                break;

            case MEDIUM:

                mediumLatencyWindow.add(latencyMs);

                mediumWindowSum += latencyMs;

                if (mediumLatencyWindow.size()
                        > WINDOW_SIZE) {

                    long removed =
                            mediumLatencyWindow.remove();

                    mediumWindowSum -= removed;
                }

                mediumRequestCount++;
                break;

            case LOW:

                lowLatencyWindow.add(latencyMs);

                lowWindowSum += latencyMs;

                if (lowLatencyWindow.size()
                        > WINDOW_SIZE) {

                    long removed =
                            lowLatencyWindow.remove();

                    lowWindowSum -= removed;
                }

                lowRequestCount++;
                break;
        }
        completedRequests.incrementAndGet();
    }

    /**
     * Sliding-window latency
     */
    public synchronized double getAverageLatency() {

        if (latencyWindow.isEmpty()) {
            return 0;
        }

        return (double) windowLatencySum
                / latencyWindow.size();
    }

    /*
     * HIGH priority metrics
     */

    public synchronized double
    getHighPriorityLatency() {

        if (highRequestCount == 0) {
            return 0;
        }

        return highLatencyWindow.isEmpty()
        ? 0
        : (double) highWindowSum
        / highLatencyWindow.size();
    }

    public synchronized long getHighRequestCount() {

        return highRequestCount;
    }

    /*
     * MEDIUM priority metrics
     */

    public synchronized double
    getMediumPriorityLatency() {

        if (mediumRequestCount == 0) {
            return 0;
        }

        return mediumLatencyWindow.isEmpty()
        ? 0
        : (double) mediumWindowSum
        / mediumLatencyWindow.size();
    }

    public synchronized long getMediumRequestCount() {

        return mediumRequestCount;
    }

    /*
     * LOW priority metrics
     */

    public synchronized double
    getLowPriorityLatency() {

        if (lowRequestCount == 0) {
            return 0;
        }

        return lowLatencyWindow.isEmpty()
        ? 0
        : (double) lowWindowSum
        / lowLatencyWindow.size();
    }

    public synchronized  long getLowRequestCount() {

        return lowRequestCount;
    }

    /*
     * Rejection tracking
     */

    public void recordRejection() {

        rejectedRequests.incrementAndGet();
    }

    public long getCompletedRequests() {

        return completedRequests.get();
    }

    public long getRejectedRequests() {

        return rejectedRequests.get();
    }
}