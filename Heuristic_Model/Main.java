import controller.AdaptiveController;
import estimator.ThreadPoolSizeEstimator;
import manager.DynamicThreadPoolManager;
import metrics.MetricsCollector;
import metrics.SystemMetrics;
import predictor.TemaPredictor;
import task.SimulatedRequest;
import metrics.LatencyTracker;

public class Main {

    public static void main(String[] args)
            throws InterruptedException {

        TemaPredictor predictor =
                new TemaPredictor(0.2);

        ThreadPoolSizeEstimator estimator =
                new ThreadPoolSizeEstimator(
                        50,
                        1.2,
                        2,
                        100
                );

        DynamicThreadPoolManager manager =
                new DynamicThreadPoolManager(
                        2,
                        100
                );

        LatencyTracker latencyTracker =
                new LatencyTracker();

        MetricsCollector collector =
                new MetricsCollector(
                        manager.getExecutor(),
                        latencyTracker
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
                                latencyTracker
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

        manager.shutdown();
    }
}