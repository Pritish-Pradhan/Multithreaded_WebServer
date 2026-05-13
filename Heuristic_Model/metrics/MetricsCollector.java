package metrics;

import java.util.concurrent.ThreadPoolExecutor;

public class MetricsCollector {

    private final ThreadPoolExecutor executor;
    private final LatencyTracker latencyTracker;

    public MetricsCollector(
        ThreadPoolExecutor executor,
        LatencyTracker latencyTracker
    ) {

        this.executor = executor;
        this.latencyTracker = latencyTracker;
    }

    public SystemMetrics collect() {

        return new SystemMetrics(

                executor.getActiveCount(),

                executor.getQueue().size(),

                executor.getCompletedTaskCount(),

                executor.getPoolSize(),

                executor.getCorePoolSize(),
                
                latencyTracker.getAverageLatency()
        );
    }
}