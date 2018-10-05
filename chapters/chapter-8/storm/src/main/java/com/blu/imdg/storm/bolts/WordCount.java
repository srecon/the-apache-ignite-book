package com.blu.imdg.storm.bolts;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class WordCount extends BaseBasicBolt {
    // Static field for ignite
    private static final String IGNITE_FIELD = "ignite";
    //Create logger for this class
    private static final Logger logger = LogManager.getLogger(WordCount.class);

    //For holding words and counts
    Map<String, Integer> counts = new HashMap<String, Integer>();

    //execute is called to process tuples
    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        //Get the word contents from the tuple
        String word = tuple.getString(0);
        //Have we counted any already?
        Integer count = counts.get(word);
        if (count == null)
            count = 0;
        //Increment the count and store it
        count++;
        counts.put(word, count);
        //Emit the word and the current count
        //collector.emit(new Values(IGNITE_FIELD, count));
        TreeMap<String, Integer> words = new TreeMap<>();
        words.put(word,count);

        collector.emit(new Values(words));
        //Log information
        logger.info("Emitting a count of " + count + " for word " + word);
    }

    //Declare that we will emit a tuple containing two fields; word and count
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(IGNITE_FIELD));
    }
}
