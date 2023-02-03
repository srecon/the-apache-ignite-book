package com.blu.imdg;

import com.blu.imdg.model.Company;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import java.io.*;


public class TextQueryExample {

    private static Logger logger = LoggerFactory.getLogger(TextQueryExample.class);
    private static final String SCAN_QUERY = "scanquery";
    private static final String TEXT_QUERY = "textquery";

    /*
      Partitioned cache name to store companies.
      */
    private static final String COMPANY_CACHE_NAME = TextQueryExample.class.getSimpleName() + "-company";

    /**
     * This is an entry point of TextQueryExample, the ignite configuration lies upon resources directory as
     * example-ignite.xml.
     *
     * @param args Command line arguments, none required.
     */
    public static void main(String[] args) throws Exception {

        //The ignite configuration lies below resources directory as example-ignite.xml.
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {

            logger.info("Text. Sql query example.");

            CacheConfiguration<Long, Company> employeeCacheCfg = new CacheConfiguration<>(COMPANY_CACHE_NAME);

            employeeCacheCfg.setCacheMode(CacheMode.PARTITIONED);
            employeeCacheCfg.setIndexedTypes(Long.class, Company.class);

            try (
                    IgniteCache<Long, Company> employeeCache = ignite.createCache(employeeCacheCfg)
            ) {
                if (args.length <= 0) {
                    logger.error("Usages! java -jar .\\target\\cache-store-runnable.jar scanquery|textquery");
                    System.exit(0);
                }
                initialize();

                if (args[0].equalsIgnoreCase(SCAN_QUERY)) {
                    scanQuery();
                    log("Scan query example finished.");
                } else if (args[0].equalsIgnoreCase(TEXT_QUERY)) {
                    textQuery();
                    log("Text query example finished.");
                }

            }
        }
    }

    /**
     * Let's fill ignite cache with test data.
     *
     * @throws InterruptedException In case of error.
     */
    private static void initialize() throws InterruptedException, IOException {
        IgniteCache<Long, Company> companyCache = Ignition.ignite().cache(COMPANY_CACHE_NAME);

        // Clear caches before start.
        companyCache.clear();

        // put Companies from CSV file
        String[] CSV_Headers = {"ID","CAT","COMPANY_NAME","EMAIL","ADDRESS","CITY","STATE","ZIPCODE","PHONE_NUMBER","FAX_NUMBER","SIC_CODE","SIC_DESCRIPTION","WEB_ADDRESS"};
        InputStream inputStream = TextQueryExample.class.getClassLoader().getResourceAsStream("USA_NY_email_addresses.csv");

        Reader in = new InputStreamReader(inputStream);

        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(CSV_Headers)
                .withFirstRecordAsHeader()
                .parse(in);
        for (CSVRecord record : records) {
            Company company = new Company(Long.valueOf(record.get("ID")),record.get("CAT"), record.get("COMPANY_NAME"), record.get("EMAIL"),record.get("ADDRESS"),record.get("CITY"),record.get("STATE"),record.get("ZIPCODE"),record.get("PHONE_NUMBER"),record.get("FAX_NUMBER"),record.get("SIC_CODE"),record.get("SIC_DESCRIPTION"),record.get("WEB_ADDRESS"));
            companyCache.put(Long.valueOf(record.get("ID")),company);
        }
        // Wait 1 second to be sure that all nodes processed put requests.
        Thread.sleep(1000);
    }


    /**
     * Example for TEXT queries using LUCENE-based indexing of people's job name.
     */
    private static void textQuery() {
        IgniteCache<Integer, Company> cache = Ignition.ignite().cache(COMPANY_CACHE_NAME);

        //  Query for all companies which has a text "John".
        TextQuery<Integer, Company> john = new TextQuery<>(Company.class, "John");

        //  Query for all companies which has a text "primavera".
        TextQuery<Integer, Company> primavera = new TextQuery<>(Company.class, "beauty saloon");

        log("==So many companies with information about 'John'==", cache.query(john).getAll());
        log("==A company which name with ' beauty salon'==", cache.query(primavera).getAll());
    }

    private static void scanQuery() {
        IgniteCache<Long, Company> companyCache = Ignition.ignite().cache(COMPANY_CACHE_NAME);

        //  Query for all companies which the city 'NEW YORK' - NewYork.
        QueryCursor<Cache.Entry<Long, Company>> cursor = companyCache.query(new ScanQuery<Long, Company>((k, p) -> p.getCity().equalsIgnoreCase("NEW YORK") ));

        cursor.forEach(entry->log("Key = " + entry.getKey() + ", Value = " + entry.getValue()));

        cursor.close();

    }

    /**
     * Prints message to logger.
     *
     * @param msg String.
     */
    private static void log(String msg) {
        logger.info("\t" + msg);
    }

    /**
     * Prints message to logger.
     *
     * @param msg String.
     */
    private static void log(String msg, Iterable<?> col) {
        logger.info("\t" + msg);
        col.forEach(c -> logger.info("\t\t" + c));
    }

    /**
     * Prints message and resultset to logger.
     *
     * @param msg String.
     * @param col Iterable
     */
    private static void logDecorated(String msg, Iterable<?> col) {
        logger.info("\t" + msg);
        col.forEach(c -> logger.info("\t\t" + c));
    }

}
