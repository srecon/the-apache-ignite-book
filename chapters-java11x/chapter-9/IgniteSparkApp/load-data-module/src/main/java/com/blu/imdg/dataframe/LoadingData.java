package com.blu.imdg.dataframe;

import org.apache.ignite.spark.IgniteDataFrameSettings;
import org.apache.spark.sql.ignite.IgniteSparkSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class LoadingData {

    private static final String IGNITE_CONFIG_XML = "ignite.config.xml";
    private static final Logger log = LogManager.getLogger(LoadingData.class);

    public static void main(String[] args) {

        if (args.length < 1) {
            log.error("Usage: ignite.spark.load.module-1.0.jar PATH_TO/IgniteSparkApp");
            System.exit(1);
        }

        var configPath = args[0];

        var igniteSession = IgniteSparkSession.builder()
                .appName("Spark Ignite data loading example")
                .master("local")
                .config("spark.executor.instances", "2")
                .igniteConfig(configPath + "/" + IGNITE_CONFIG_XML)
                .getOrCreate();

        var flightsPath = configPath + "/scripts" + "/664600583_T_ONTIME_sample2.csv";
        log.info("Loading FLIGHTS table from {}", flightsPath);
        igniteSession.read()
                .format("com.databricks.spark.csv")
                .option("header", "true")
                .option("mode", "DROPMALFORMED")
                .csv(flightsPath)
                .write()
                .format(IgniteDataFrameSettings.FORMAT_IGNITE())
                .option(IgniteDataFrameSettings.OPTION_CONFIG_FILE(), configPath + "/" + IGNITE_CONFIG_XML)
                .option(IgniteDataFrameSettings.OPTION_CREATE_TABLE_PRIMARY_KEY_FIELDS(), "id")
                .option(IgniteDataFrameSettings.OPTION_CREATE_TABLE_PARAMETERS(), "template=replicated")
                .saveAsTable("FLIGHTS");

        var airportsPath = configPath + "/scripts" + "/L_AIRPORT_ID2.csv";
        log.info("Loading AIRPORTS table from {}", airportsPath);
        igniteSession.read()
                .format("com.databricks.spark.csv")
                .option("header", "true")
                .option("mode", "DROPMALFORMED")
                .csv(airportsPath)
                .write()
                .format(IgniteDataFrameSettings.FORMAT_IGNITE())
                .option(IgniteDataFrameSettings.OPTION_CONFIG_FILE(), configPath + "/" + IGNITE_CONFIG_XML)
                .option(IgniteDataFrameSettings.OPTION_CREATE_TABLE_PRIMARY_KEY_FIELDS(), "Code")
                .option(IgniteDataFrameSettings.OPTION_CREATE_TABLE_PARAMETERS(), "template=replicated")
                .saveAsTable("AIRPORTS");

        log.info("Data loading complete. Stopping Spark session.");
        igniteSession.stop();
    }
}
