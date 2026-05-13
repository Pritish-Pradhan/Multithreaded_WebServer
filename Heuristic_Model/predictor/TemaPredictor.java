package predictor;

public class TemaPredictor {

    // Smoothing factor
    private final double alpha;

    // EMA levels
    private Double ema1;
    private Double ema2;
    private Double ema3;

    public TemaPredictor(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Updates predictor with latest observed request rate
     * and returns predicted next request rate.
     */
    public double predict(double currentRate) {

        // EMA Level 1
        if (ema1 == null) {
            ema1 = currentRate;
        } else {
            ema1 = alpha * currentRate
                    + (1 - alpha) * ema1;
        }

        // EMA Level 2
        if (ema2 == null) {
            ema2 = ema1;
        } else {
            ema2 = alpha * ema1
                    + (1 - alpha) * ema2;
        }

        // EMA Level 3
        if (ema3 == null) {
            ema3 = ema2;
        } else {
            ema3 = alpha * ema2
                    + (1 - alpha) * ema3;
        }

        // TEMA Calculation
        return (3 * ema1)
                - (3 * ema2)
                + ema3;
    }
}