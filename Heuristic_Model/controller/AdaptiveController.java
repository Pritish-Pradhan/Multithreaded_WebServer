package controller;

import metrics.SystemMetrics;

public class AdaptiveController {

    private final int minThreads;
    private final int maxThreads;

    // Queue thresholds
    private final int highQueueThreshold;
    private final int lowQueueThreshold;

    // SLA latency threshold
    private final double latencyThresholdMs;

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
     * Adaptive SLA-aware calibration
     */
    public int calibrate(
            int predictedPoolSize,
            SystemMetrics metrics
    ) {

        int adjustedPoolSize =
                predictedPoolSize;

        int queueSize =
                metrics.getQueueSize();

        int activeThreads =
                metrics.getActiveThreads();

        int currentPoolSize =
                metrics.getCurrentPoolSize();

        double avgLatency =
                metrics.getAverageLatency();

        /*
         * SLA VIOLATION DETECTION
         */

        if (avgLatency > latencyThresholdMs) {

            adjustedPoolSize += 3;

            System.out.println(
                    "[SLA CONTROL] " +
                    "Latency SLA violated → Aggressive scaling"
            );
        }

        /*
         * CONGESTION DETECTION
         */

        else if (queueSize > highQueueThreshold
                && activeThreads >= currentPoolSize) {

            adjustedPoolSize += 2;

            System.out.println(
                    "[ADAPTIVE CONTROL] " +
                    "High congestion detected → Increasing threads"
            );
        }

        /*
         * UNDERUTILIZATION DETECTION
         */

        else if (queueSize <= lowQueueThreshold
                && activeThreads < currentPoolSize / 2) {

            adjustedPoolSize -= 1;

            System.out.println(
                    "[ADAPTIVE CONTROL] " +
                    "Underutilization detected → Reducing threads"
            );
        }

        /*
         * Stable operating region
         */

        else {

            System.out.println(
                    "[ADAPTIVE CONTROL] " +
                    "System stable"
            );
        }

        // Clamp limits
        adjustedPoolSize =
                Math.max(minThreads,
                        adjustedPoolSize);

        adjustedPoolSize =
                Math.min(maxThreads,
                        adjustedPoolSize);

        return adjustedPoolSize;
    }
}












// package controller;

// import metrics.SystemMetrics;

// public class AdaptiveController {

//     private final int minThreads;
//     private final int maxThreads;

//     // Queue thresholds
//     private final int highQueueThreshold;
//     private final int lowQueueThreshold;

//     public AdaptiveController(
//             int minThreads,
//             int maxThreads,
//             int highQueueThreshold,
//             int lowQueueThreshold
//     ) {

//         this.minThreads = minThreads;
//         this.maxThreads = maxThreads;
//         this.highQueueThreshold = highQueueThreshold;
//         this.lowQueueThreshold = lowQueueThreshold;
//     }

//     /**
//      * Calibrate predicted pool size using runtime metrics
//      */
//     public int calibrate(
//             int predictedPoolSize,
//             SystemMetrics metrics
//     ) {

//         int adjustedPoolSize =
//                 predictedPoolSize;

//         // Current runtime state
//         int queueSize =
//                 metrics.getQueueSize();

//         int activeThreads =
//                 metrics.getActiveThreads();

//         int currentPoolSize =
//                 metrics.getCurrentPoolSize();

//         /*
//          * OVERLOAD DETECTION
//          */

//         // Queue building up AND all threads busy
//         if (queueSize > highQueueThreshold
//                 && activeThreads >= currentPoolSize) {

//             adjustedPoolSize += 2;

//             System.out.println(
//                     "[ADAPTIVE CONTROL] " +
//                     "High congestion detected → Increasing threads"
//             );
//         }

//         /*
//          * UNDERUTILIZATION DETECTION
//          */

//         // Queue almost empty AND many idle threads
//         else if (queueSize <= lowQueueThreshold
//                 && activeThreads < currentPoolSize / 2) {

//             adjustedPoolSize -= 1;

//             System.out.println(
//                     "[ADAPTIVE CONTROL] " +
//                     "Underutilization detected → Reducing threads"
//             );
//         }

//         // Clamp limits
//         adjustedPoolSize =
//                 Math.max(minThreads, adjustedPoolSize);

//         adjustedPoolSize =
//                 Math.min(maxThreads, adjustedPoolSize);

//         return adjustedPoolSize;
//     }
// }