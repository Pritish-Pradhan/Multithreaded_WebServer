import controller.AdaptiveController;
import estimator.ThreadPoolSizeEstimator;
import manager.DynamicThreadPoolManager;
import metrics.LatencyTracker;
import metrics.MetricsCollector;
import metrics.MetricsLogger;
import metrics.SystemMetrics;
import predictor.TemaPredictor;
import task.RequestPriority;
import task.SimulatedRequest;

public class Main {

    public static void main(String[] args)
            throws Exception {

        TemaPredictor predictor =
                new TemaPredictor(0.2,0.25);

        ThreadPoolSizeEstimator estimator =
                new ThreadPoolSizeEstimator(
                        100,
                        1.8,
                        2,
                        100
                );
        LatencyTracker latencyTracker =
                new LatencyTracker();

        DynamicThreadPoolManager manager =
                new DynamicThreadPoolManager(
                        2,
                        100,
                        latencyTracker
                );

        MetricsCollector collector =
                new MetricsCollector(
                        manager.getExecutor(),
                        latencyTracker
                );

        MetricsLogger logger =
                new MetricsLogger(
                        "metrics.csv"
                );

        AdaptiveController controller =
        new AdaptiveController(
                2,
                100,
                20,
                2,
                200
        );

        double[] traffic = {
                100,
                120,
                130,
                160,
                220,
                300,
                280,
                260,
                240,
                200
        };

        int requestId = 1;

        for (double currentLoad : traffic) {

    System.out.println(
            "\nCurrent Traffic: "
                    + currentLoad
                    + " req/sec"
    );

        /*
        * STEP 1
        * Predict workload
        */

        double predicted =
                predictor.predict(currentLoad);

        /*
        * STEP 2
        * Estimate thread pool size
        */

        int predictedPoolSize =
                estimator.estimate(predicted);

        /*
        * STEP 3
        * Simulate incoming requests
        */

        int simulatedRequests =
                (int) currentLoad;

        long arrivalInterval =
                Math.max(1,
                        1000 / simulatedRequests);

        for (int i = 0;
                i < simulatedRequests;
                i++) {

                manager.submitTask(
                        new SimulatedRequest(
                                requestId++,
                                100,
                                latencyTracker,
                                generatePriority()
                        )
                );

                Thread.sleep(arrivalInterval);
        }

        /*
        * STEP 4
        * Allow executor stabilization
        */

        Thread.sleep(300);

        /*
        * STEP 5
        * Collect runtime metrics
        */

        SystemMetrics metrics =
                collector.collect();

        System.out.println(metrics);
                logger.log(
                currentLoad,
                metrics
        );

        /*
        * STEP 6
        * Adaptive calibration
        */

        int adjustedPoolSize =
                controller.calibrate(
                        predictedPoolSize,
                        metrics
                );

        /*
        * STEP 7
        * Resize thread pool
        */

        manager.resizePool(adjustedPoolSize);
        }
        logger.close();
        manager.shutdown();
    }
    private static RequestPriority generatePriority() {

        double random =
                Math.random();

        /*
        * Example distribution
        *
        * 20% HIGH
        * 50% MEDIUM
        * 30% LOW
        */

        if (random < 0.20) {

                return RequestPriority.HIGH;

        } else if (random < 0.70) {

                return RequestPriority.MEDIUM;

        } else {

                return RequestPriority.LOW;
        }
    }
}