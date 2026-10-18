package com.blu.imdg.dataframe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.ignite.IgniteSparkSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class IgniteDataFrame {

    private static final String IGNITE_CONFIG_XML = "ignite.config.xml";
    private static final Logger log = LogManager.getLogger(IgniteDataFrame.class);

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            log.error("Usage: ignite.spark.query-1.0.jar PATH_TO/IgniteSparkApp");
            System.exit(1);
        }

        var configPath = args[0];

        var igniteSession = IgniteSparkSession.builder()
                .appName("Spark Ignite dataframe example")
                .master("local")
                .config("spark.executor.instances", "2")
                .igniteConfig(configPath + "/" + IGNITE_CONFIG_XML)
                .getOrCreate();

        log.info("Listing all SQL tables in Ignite:");
        igniteSession.catalog().listTables().show();

        var sqlQuery = """
            select f.ORIGIN_AIRPORT_ID, a.DESCRIPTION, count(f.ID) from FLIGHTS f
             join  AIRPORTS a on a.Code=f.ORIGIN_AIRPORT_ID
             group by f.ORIGIN_AIRPORT_ID, a.DESCRIPTION order by count(f.ID)
            """;
        log.info("Executing query: {}", sqlQuery);

        Dataset<Row> res = igniteSession.sql(sqlQuery);
        res.foreach(v -> {
            log.info("Row={}", v);
        });

        //igniteSession.stop();
    }
}
