package com.blu.imdg;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

public final class LineCount {

    private static final Logger log = LogManager.getLogger(LineCount.class);

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            log.error("Usage: LineCount <file>");
            System.exit(1);
        }

        var logFile = args[0];
        var appName = """
            Line count Application""";
        var spark = SparkSession.builder().appName(appName).getOrCreate();
        var logData = spark.read().textFile(logFile).cache();

        var numAs = logData.filter(s -> s.contains("a")).count();
        var numBs = logData.filter(s -> s.contains("b")).count();

        log.info("Lines with a: {}, lines with b: {}", numAs, numBs);

        spark.stop();
    }
}
