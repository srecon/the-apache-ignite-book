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

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.IOException;
import java.util.Map;

public class TextQueryExample {

    private static final Logger log = LoggerFactory.getLogger(TextQueryExample.class);

    private static final String SCAN_QUERY = "scanquery";
    private static final String TEXT_QUERY = "textquery";
    private static final String COMPANY_CACHE_NAME = TextQueryExample.class.getSimpleName() + "-company";

    public static void main(String[] args) throws Exception {
        try (var ignite = Ignition.start("example-ignite.xml")) {
            log.info("Text query example.");

            var employeeCacheCfg = new CacheConfiguration<Long, Company>(COMPANY_CACHE_NAME);
            employeeCacheCfg.setCacheMode(CacheMode.PARTITIONED);
            employeeCacheCfg.setIndexedTypes(Long.class, Company.class);

            try (var employeeCache = ignite.createCache(employeeCacheCfg)) {
                if (args.length <= 0) {
                    log.error("Usage! java -jar textquery.jar scanquery|textquery");
                    System.exit(1);
                }

                initialize();

                if (args[0].equalsIgnoreCase(SCAN_QUERY)) {
                    scanQuery();
                    log.info("Scan query example finished.");
                } else if (args[0].equalsIgnoreCase(TEXT_QUERY)) {
                    textQuery();
                    log.info("Text query example finished.");
                }
            }
        }
    }

    private static void initialize() throws InterruptedException, IOException {
        var companyCache = Ignition.ignite().cache(COMPANY_CACHE_NAME);
        companyCache.clear();

        var CSV_Headers = new String[]{"ID", "CAT", "COMPANY_NAME", "EMAIL", "ADDRESS", "CITY", "STATE", "ZIPCODE", "PHONE_NUMBER", "FAX_NUMBER", "SIC_CODE", "SIC_DESCRIPTION", "WEB_ADDRESS"};
        var inputStream = TextQueryExample.class.getClassLoader().getResourceAsStream("USA_NY_email_addresses.csv");

        try (var in = new InputStreamReader(inputStream)) {
            var records = CSVFormat.DEFAULT
                .withHeader(CSV_Headers)
                .withFirstRecordAsHeader()
                .parse(in);

            for (var record : records) {
                var company = new Company(
                    Long.valueOf(record.get("ID")),
                    record.get("CAT"),
                    record.get("COMPANY_NAME"),
                    record.get("EMAIL"),
                    record.get("ADDRESS"),
                    record.get("CITY"),
                    record.get("STATE"),
                    record.get("ZIPCODE"),
                    record.get("PHONE_NUMBER"),
                    record.get("FAX_NUMBER"),
                    record.get("SIC_CODE"),
                    record.get("SIC_DESCRIPTION"),
                    record.get("WEB_ADDRESS")
                );
                companyCache.put(Long.valueOf(record.get("ID")), company);
            }
        }

        // Wait 1 second to ensure all nodes processed put requests.
        Thread.sleep(1000);
    }

    private static void textQuery() {
        var cache = Ignition.ignite().cache(COMPANY_CACHE_NAME);

        var john = new TextQuery<Integer, Company>(Company.class, "John");
        var primavera = new TextQuery<Integer, Company>(Company.class, "beauty saloon");

        log("==So many companies with information about 'John'==", cache.query(john).getAll());
        log("==A company which name with ' beauty salon'==", cache.query(primavera).getAll());
    }

    private static void scanQuery() {
        var companyCache = Ignition.ignite().cache(COMPANY_CACHE_NAME);

        try (var cursor = companyCache.query(new ScanQuery<Long, Company>((k, p) -> p.getCity().equalsIgnoreCase("NEW YORK")))) {
            cursor.forEach(entry -> log("Key = " + entry.getKey() + ", Value = " + entry.getValue()));
        }
    }

    private static void log(String msg) {
        log.info("\t{}", msg);
    }

    private static void log(String msg, Iterable<?> col) {
        log.info("\t{}", msg);
        col.forEach(c -> log.info("\t\t{}", c));
    }

    private static void logDecorated(String msg, Iterable<?> col) {
        log.info("\t{}", msg);
        col.forEach(c -> log.info("\t\t{}", c));
    }
}
