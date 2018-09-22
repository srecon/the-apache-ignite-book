package com.blu.imdg.dataframe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import org.apache.spark.sql.ignite.IgniteSparkSession;


public class IgniteDataFrame {

    private static final String IGNITE_CONFIG_XML = "ignite.config.xml";

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: ignite.spark.query-1.0.jar PATH_TO/IgniteSparkApp");
            System.exit(1);
        }

        String config_path = args[0];

        IgniteSparkSession igniteSession = IgniteSparkSession.builder()
                .appName("Spark Ignite catalog example")
                .master("local")
                .config("spark.executor.instances", "2")
                //Only additional option to refer to Ignite cluster.
                .igniteConfig(config_path+"/"+IGNITE_CONFIG_XML)
                .getOrCreate();

//      This will print out info about all SQL tables existed in Ignite.
        igniteSession.catalog().listTables().show();

        Dataset<Row> res = igniteSession.sql("" +
                "select f.ORIGIN_AIRPORT_ID, a.DESCRIPTION, count(f.ID) from FLIGHTS f " +
                " join  AIRPORTS a on a.Code=f.ORIGIN_AIRPORT_ID" +
                " group by f.ORIGIN_AIRPORT_ID, a.DESCRIPTION order by count(f.ID)"
                );
        res.foreach(v -> {
            System.out.println("Row="+v);
        });

        igniteSession.stop();
    }
}
