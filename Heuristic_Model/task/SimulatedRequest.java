package task;

import metrics.LatencyTracker;

public class SimulatedRequest
        implements Runnable,
        Comparable<SimulatedRequest> {

    private final int requestId;

    private final long processingTimeMs;

    private final long arrivalTime;

    private final LatencyTracker latencyTracker;

    /*
     * SLA priority
     */

    private final RequestPriority priority;

    public SimulatedRequest(
            int requestId,
            long processingTimeMs,
            LatencyTracker latencyTracker,
            RequestPriority priority
    ) {

        this.requestId = requestId;

        this.processingTimeMs =
                processingTimeMs;

        this.latencyTracker =
                latencyTracker;

        this.priority =
                priority;

        this.arrivalTime =
                System.currentTimeMillis();
    }

        @Override
        public void run() {

        long start =
                System.currentTimeMillis();

        try {

                Thread.sleep(processingTimeMs);

        }
        catch (InterruptedException e) {

                Thread.currentThread().interrupt();
        }

        long latency =
                System.currentTimeMillis()
                - start;

        latencyTracker.recordLatency(
                latency,
                priority
        );
        }

    /*
     * PRIORITY SCHEDULING
     */

    @Override
    public int compareTo(
            SimulatedRequest other
    ) {

        return Integer.compare(
                other.priority.getValue(),
                this.priority.getValue()
        );
    }
}