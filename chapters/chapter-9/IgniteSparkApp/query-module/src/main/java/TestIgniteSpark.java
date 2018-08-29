import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.ignite.IgniteSparkSession;

import java.util.ArrayList;
import java.util.List;

public class TestIgniteSpark {

    public static final String IGNITE_CONFIG_XML = "ignite.config.xml";

    public static void main(String[] args) throws Exception {


//        SparkSession spark = SparkSession.builder()
//                .appName("Example Program")
//                .master("local")
//                .config("spark.executor.instances", "2")
//                .getOrCreate();

        IgniteSparkSession igniteSession = IgniteSparkSession.builder()
                .appName("Spark Ignite catalog example")
                .master("local")
                .config("spark.executor.instances", "2")
                //Only additional option to refer to Ignite cluster.
                .igniteConfig(IGNITE_CONFIG_XML)
                .getOrCreate();

// This will print out info about all SQL tables existed in Ignite.
        igniteSession.catalog().listTables().show();

//        igniteSession.catalog().getTable("FLIGHTS").cr\\
        Dataset<Row> res = igniteSession.sql("" +
                "select f.ORIGIN_AIRPORT_ID, a.DESCRIPTION, count(f.ID) from FLIGHTS f " +
                " join  AIRPORTS a on a.Code=f.ORIGIN_AIRPORT_ID" +
                " group by f.ORIGIN_AIRPORT_ID, a.DESCRIPTION order by count(f.ID)"
                );
        res.foreach(v -> {
            System.out.println("v="+v);
        });



        igniteSession.stop();
    }
}
