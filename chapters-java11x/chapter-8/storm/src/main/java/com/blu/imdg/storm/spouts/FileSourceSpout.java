package com.blu.imdg.storm.spouts;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;


import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;


public class FileSourceSpout extends BaseRichSpout {
    private static final Logger LOGGER = LogManager.getLogger(FileSourceSpout.class);
    private SpoutOutputCollector outputCollector;
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.outputCollector = spoutOutputCollector;
    }

    /**
     * Read file from classpath to generate source.
     * */
    @Override
    public void nextTuple() {

        try {
            Path filePath = Paths.get(this.getClass().getClassLoader().getResource("source.csv").toURI());
            try(Stream<String> lines = Files.lines(filePath)){
                lines.forEach(line ->{
                    outputCollector.emit(new Values(line));
                });

            } catch(IOException e){
                LOGGER.error(e.getMessage());
            }

        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage());
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("trafficLog"));

    }
}
