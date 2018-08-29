import org.apache.ignite.spark.IgniteDataFrameSettings;
import org.apache.spark.sql.ignite.IgniteSparkSession;

public class InitIgniteData {
    public static final String IGNITE_CONFIG_XML = "ignite.config.xml";


    public static void main(String[] args) {
        String config_path = args[0];
        System.out.println("config_path="+config_path);

        IgniteSparkSession igniteSession = IgniteSparkSession.builder()
                .appName("Spark Ignite catalog example")
                .master("local")
                .config("spark.executor.instances", "2")
                //Only additional option to refer to Ignite cluster.
                .igniteConfig(IGNITE_CONFIG_XML)
                .getOrCreate();



        igniteSession.read()
                .format("com.databricks.spark.csv")
                .option("header", "true")
                .option("mode", "DROPMALFORMED")
                .csv("file://"+config_path+"/664600583_T_ONTIME_sample2.csv")
        .       write()
                .format(IgniteDataFrameSettings.FORMAT_IGNITE())
                .option(IgniteDataFrameSettings.OPTION_CONFIG_FILE(), IGNITE_CONFIG_XML)
//                .option(IgniteDataFrameSettings.OPTION_TABLE(), "FLIGHTS1")
                .option(IgniteDataFrameSettings.OPTION_CREATE_TABLE_PRIMARY_KEY_FIELDS(), "id")
                .option(IgniteDataFrameSettings.OPTION_CREATE_TABLE_PARAMETERS(), "template=replicated")
                .saveAsTable("FLIGHTS");

        igniteSession.read()
                .format("com.databricks.spark.csv")
                .option("header", "true")
                .option("mode", "DROPMALFORMED")
                .csv("file://"+config_path+"/L_AIRPORT_ID2.csv")
        .       write()
                .format(IgniteDataFrameSettings.FORMAT_IGNITE())
                .option(IgniteDataFrameSettings.OPTION_CONFIG_FILE(), IGNITE_CONFIG_XML)
//                .option(IgniteDataFrameSettings.OPTION_TABLE(), "FLIGHTS1")
                .option(IgniteDataFrameSettings.OPTION_CREATE_TABLE_PRIMARY_KEY_FIELDS(), "Code")
                .option(IgniteDataFrameSettings.OPTION_CREATE_TABLE_PARAMETERS(), "template=replicated")
                .saveAsTable("AIRPORTS");

    }
}
