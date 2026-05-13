package task;

import metrics.LatencyTracker;

public class SimulatedRequest implements Runnable {

    private final int requestId;
    private final long processingTimeMs;

    private final LatencyTracker latencyTracker;

    public SimulatedRequest(
            int requestId,
            long processingTimeMs,
            LatencyTracker latencyTracker
    ) {

        this.requestId = requestId;
        this.processingTimeMs = processingTimeMs;
        this.latencyTracker = latencyTracker;
    }

    @Override
    public void run() {

        long startTime =
                System.currentTimeMillis();

        try {

            Thread.sleep(processingTimeMs);

        }
        catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }

        long endTime =
                System.currentTimeMillis();

        long latency =
                endTime - startTime;

        latencyTracker.recordLatency(latency);
    }
}