package metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MetricsLogger {

    private final PrintWriter writer;

    public MetricsLogger(String fileName)
            throws IOException {

        writer =
                new PrintWriter(
                        new FileWriter(fileName)
                );

        /*
         * CSV Header
         */

        writer.println(
                "traffic,"
                + "poolSize,"
                + "activeThreads,"
                + "queueSize,"
                + "avgLatency,"
                + "highLatency,"
                + "mediumLatency,"
                + "lowLatency,"
                + "rejectedRequests"
        );
    }

    /**
     * Write one metrics snapshot
     */
    public void log(
            double traffic,
            SystemMetrics metrics
    ) {

        writer.println(

                traffic + ","

                + metrics.getCurrentPoolSize() + ","

                + metrics.getActiveThreads() + ","

                + metrics.getQueueSize() + ","

                + metrics.getAverageLatency() + ","

                + metrics.getHighPriorityLatency() + ","

                + metrics.getMediumPriorityLatency() + ","

                + metrics.getLowPriorityLatency() + ","

                + metrics.getRejectedRequests()
        );

        writer.flush();
    }

    /**
     * Close file
     */
    public void close() {

        writer.close();
    }
}