package predictor;

public class TemaPredictor {

    /*
     * TEMA smoothing factor
     */

    private final double alpha;

    /*
     * Momentum amplification factor
     */

    private final double beta;

    /*
     * EMA levels
     */

    private Double ema1;
    private Double ema2;
    private Double ema3;

    /*
     * Previous observed rate
     */

    private Double previousRate;

    public TemaPredictor(
            double alpha,
            double beta
    ) {

        this.alpha = alpha;

        this.beta = beta;
    }

    /**
     * Predict next workload
     */
    public double predict(
            double currentRate
    ) {

        /*
         * EMA LEVEL 1
         */

        if (ema1 == null) {

            ema1 = currentRate;

        } else {

            ema1 =
                    alpha * currentRate
                    + (1 - alpha) * ema1;
        }

        /*
         * EMA LEVEL 2
         */

        if (ema2 == null) {

            ema2 = ema1;

        } else {

            ema2 =
                    alpha * ema1
                    + (1 - alpha) * ema2;
        }

        /*
         * EMA LEVEL 3
         */

        if (ema3 == null) {

            ema3 = ema2;

        } else {

            ema3 =
                    alpha * ema2
                    + (1 - alpha) * ema3;
        }

        /*
         * CORE TEMA
         */

        double tema =
                (3 * ema1)
                - (3 * ema2)
                + ema3;

        /*
         * MOMENTUM TERM
         */

        double momentum = 0;

        if (previousRate != null) {

            momentum =
                    currentRate
                    - previousRate;
        }

        /*
         * FINAL PREDICTION
         */

        double prediction =
                tema
                + (beta * momentum);

        /*
         * Store previous rate
         */

        previousRate =
                currentRate;

        return Math.max(
                prediction,
                0
        );
    }
}