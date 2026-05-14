package controller;

import metrics.SystemMetrics;

public class AdaptiveController {

    private final int minThreads;
    private final int maxThreads;

    // Queue thresholds
    private final int highQueueThreshold;
    private final int lowQueueThreshold;

    // SLA threshold
    private final double latencyThresholdMs;
    private int cooldownCounter = 0;

    /*
     * Previous metrics
     */

    private double previousLatency = 0;

    private int previousQueueSize = 0;
    private static final int COOLDOWN_PERIOD = 3;

    public AdaptiveController(
            int minThreads,
            int maxThreads,
            int highQueueThreshold,
            int lowQueueThreshold,
            double latencyThresholdMs
    ) {

        this.minThreads = minThreads;

        this.maxThreads = maxThreads;

        this.highQueueThreshold =
                highQueueThreshold;

        this.lowQueueThreshold =
                lowQueueThreshold;

        this.latencyThresholdMs =
                latencyThresholdMs;
    }

    /**
     * Adaptive calibration
     */
    public int calibrate(
            int predictedPoolSize,
            SystemMetrics metrics
    ) {
        if (cooldownCounter > 0) {

                cooldownCounter--;

                System.out.println(
                        "[HYSTERESIS] Cooldown active → " +
                        "Maintaining current pool size"
                );

                return metrics.getCurrentPoolSize();
        }


        int queueSize =
                metrics.getQueueSize();

        int activeThreads =
                metrics.getActiveThreads();

        int currentPoolSize =
                metrics.getCurrentPoolSize();

        double avgLatency =
                metrics.getAverageLatency();
        int adjustedPoolSize =
                predictedPoolSize;
        /*
         * UTILIZATION RATIO
         */

        double utilization =
                currentPoolSize == 0
                ? 0
                : (double) activeThreads
                / currentPoolSize;

        /*
         * PERFORMANCE TRENDS
         */

        boolean latencyWorsened =
                previousLatency > 0 &&
                avgLatency >
                previousLatency * 1.1;

        boolean latencyImproving =
                previousLatency > 0 &&
                avgLatency <
                previousLatency * 0.9;

        boolean queueGrowing =
                queueSize >
                previousQueueSize;

        boolean queueShrinking =
                queueSize <
                previousQueueSize;

        /*
         * EMERGENCY OVERLOAD
         */

        if (avgLatency >
                latencyThresholdMs * 5) {

            int emergencyThreads =
                    Math.max(
                            4,
                            queueSize / 40
                    );

            adjustedPoolSize =
                Math.max(
                predictedPoolSize,
                currentPoolSize + emergencyThreads
                );

            System.out.println(
                    "[EMERGENCY CONTROL] " +
                    "Critical SLA violation → " +
                    "Emergency scaling activated"
            );
        }

        /*
         * SLA DEGRADATION
         */

        else if (avgLatency >
                latencyThresholdMs
                && latencyWorsened) {

            int extraThreads =
                    Math.max(
                            2,
                            queueSize / 75
                    );

            adjustedPoolSize =
                        Math.max(
                        predictedPoolSize,
                        currentPoolSize + extraThreads
                        );

            System.out.println(
                    "[SLA CONTROL] " +
                    "Latency worsening → " +
                    "Adaptive scaling"
            );
        }

        /*
         * CONGESTION DETECTION
         */

        else if (queueSize >
                highQueueThreshold
                && utilization > 0.75
                && queueGrowing) {

            int extraThreads =
                    Math.max(
                            1,
                            queueSize / 50
                    );

            adjustedPoolSize =
                Math.max(
                predictedPoolSize,
                currentPoolSize + extraThreads
                );

            System.out.println(
                    "[ADAPTIVE CONTROL] " +
                    "High utilization congestion → " +
                    "Increasing threads"
            );
        }

        /*
         * LATENCY RECOVERY
         */

        else if (latencyImproving
                && queueShrinking) {

            adjustedPoolSize =
                    currentPoolSize;

            System.out.println(
                    "[ADAPTIVE CONTROL] " +
                    "Latency recovering → " +
                    "Maintaining stability"
            );
        }

        /*
         * SMART SCALE-DOWN
         */

        else if (queueSize == 0
                && utilization < 0.30
                && avgLatency <
                latencyThresholdMs * 0.75) {

            adjustedPoolSize =
                    Math.max(
                            minThreads,
                            currentPoolSize - 2
                    );

            System.out.println(
                    "[ADAPTIVE CONTROL] " +
                    "Low utilization recovery → " +
                    "Reducing threads"
            );
        }

        /*
         * Stable operating region
         */

        else {

            adjustedPoolSize =
                    currentPoolSize;

            System.out.println(
                    "[ADAPTIVE CONTROL] " +
                    "System stable"
            );
        }

        /*
         * Clamp limits
         */

        adjustedPoolSize =
                Math.max(
                        minThreads,
                        adjustedPoolSize
                );

        adjustedPoolSize =
                Math.min(
                        maxThreads,
                        adjustedPoolSize
                );
        if (adjustedPoolSize
                != currentPoolSize) {

                cooldownCounter =
                        COOLDOWN_PERIOD;
        }

        /*
         * Debug info
         */

        System.out.println(
                "[ADAPTIVE CONTROL] " +
                "Predicted Size = "
                + predictedPoolSize
                + ", Adjusted Size = "
                + adjustedPoolSize
        );

        /*
         * Update historical metrics
         */

        previousLatency =
                avgLatency;

        previousQueueSize =
                queueSize;

        return adjustedPoolSize;
    }
}

