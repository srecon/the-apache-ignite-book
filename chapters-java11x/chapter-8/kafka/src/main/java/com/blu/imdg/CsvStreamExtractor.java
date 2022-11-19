package com.blu.imdg;

import org.apache.ignite.stream.StreamSingleTupleExtractor;
import org.apache.kafka.connect.sink.SinkRecord;

import java.util.AbstractMap;
import java.util.Map;

public class CsvStreamExtractor implements StreamSingleTupleExtractor<SinkRecord, String, String> {

    public Map.Entry<String, String> extract(SinkRecord sinkRecord) {
        System.out.println("SinkRecord:"+ sinkRecord.value().toString());

        String[] parts = sinkRecord.value().toString().split(",");

        String key = ((String[])parts[2].split("="))[1];
        String val= ((String[])parts[7].split("="))[1];

        return new AbstractMap.SimpleEntry<String, String>(key, val);
    }
}
