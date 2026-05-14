package server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import controller.AdaptiveController;
import estimator.ThreadPoolSizeEstimator;
import manager.DynamicThreadPoolManager;
import metrics.LatencyTracker;
import predictor.TemaPredictor;
import task.RequestPriority;
import task.SimulatedRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class AdaptiveHttpServer {

    public static void main(String[] args)
            throws Exception {

        TemaPredictor predictor =
                new TemaPredictor(0.2, 0.25);

        ThreadPoolSizeEstimator estimator =
                new ThreadPoolSizeEstimator(
                        100,
                        1.5,
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

        AdaptiveController controller =
                new AdaptiveController(
                        2,
                        100,
                        20,
                        2,
                        200
                );

        AtomicInteger requestCounter =
                new AtomicInteger(0);

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(8080),
                        0
                );
        server.setExecutor(
                java.util.concurrent.Executors
                        .newCachedThreadPool()
                );

        server.createContext(
                "/request",
                new HttpHandler() {

            @Override
            public void handle(
                    HttpExchange exchange
            ) throws IOException {

                int requestId =
                        requestCounter.incrementAndGet();

                /*
                 * SLA priority
                 */

                RequestPriority priority =
                        generatePriority();

                /*
                 * Submit request
                 */

                java.util.concurrent.Future<?> future =
                        manager.submitTask(
                                new SimulatedRequest(
                                        requestId,
                                        100,
                                        latencyTracker,
                                        priority
                                )
                        );


                try {

                        if (future != null) {

                                future.get();

                        }

                }
                catch (Exception e) {

                        System.out.println(
                                "Request timeout/error: "
                                + requestId
                        );

                        e.printStackTrace();
                }

                /*
                 * Predictive resizing
                 */

                if (requestCounter.get() % 20 == 0) {

                        double predictedTraffic =
                                predictor.predict(
                                        requestCounter.get()
                                );

                        int predictedPoolSize =
                                estimator.estimate(
                                        predictedTraffic
                                );

                        int adjustedPoolSize =
                                controller.calibrate(
                                        predictedPoolSize,
                                        manager.collectMetrics()
                                );

                        manager.resizePool(
                                adjustedPoolSize
                        );
                }

                String response =
                        "Request processed";

                exchange.sendResponseHeaders(
                        200,
                        response.length()
                );

                OutputStream os =
                        exchange.getResponseBody();

                os.write(response.getBytes());

                os.close();
            }
        });

        server.start();

        System.out.println(
                "Adaptive HTTP Server running on port 8080"
        );
    }

    private static RequestPriority
    generatePriority() {

        double random = Math.random();

        if (random < 0.20) {
            return RequestPriority.HIGH;
        }
        else if (random < 0.70) {
            return RequestPriority.MEDIUM;
        }
        else {
            return RequestPriority.LOW;
        }
    }
}