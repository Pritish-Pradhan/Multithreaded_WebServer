package estimator;

public class ThreadPoolSizeEstimator {

    private final double avgTaskTimeMs;
    private final double bufferFactor;
    
    private final int minThreads;
    private final int maxThreads;

    public ThreadPoolSizeEstimator(
            double avgTaskTimeMs,
            double bufferFactor,
            int minThreads,
            int maxThreads
    ) {

        this.avgTaskTimeMs = avgTaskTimeMs;
        this.bufferFactor = bufferFactor;
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
    }

    public int estimate(double predictedRate) {

        double estimatedThreads =
                (predictedRate * avgTaskTimeMs) / 1000.0;

        estimatedThreads *= bufferFactor;

        int finalSize =
                (int)Math.ceil(estimatedThreads);

        finalSize = Math.max(minThreads, finalSize);
        finalSize = Math.min(maxThreads, finalSize);

        return finalSize;
    }
}