package com.realanalytics.RealAnalytics.Kafka.Streams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Dao.PipelineRepository;
import com.realanalytics.RealAnalytics.Pipeline.Pipeline;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Rule;
import com.realanalytics.RealAnalytics.Pipeline.Rule.RuleSandBox;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
import java.util.TreeMap;

@Configuration
public class KafkaStreamsBuilder {
	
	@Value("${kafka.bootstrap.servers}")
	private String bootstrapServers;
	
	private static final Logger logger = 
            LoggerFactory.getLogger(KafkaStreamsBuilder.class);
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private PipelineRepository repo;
	
	@Autowired
	private RuleSandBox sandBox;
	
    @Bean
    public KafkaStreams kafkaStreams(KafkaProperties kafkaProperties,
    						@Value("${spring.application.name}") String appName) {
        final Properties props = new Properties();
        props.putAll(kafkaProperties.getProperties());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, appName);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.STATE_DIR_CONFIG, "data");
        final KafkaStreams kafkaStreams = new KafkaStreams(kafkaStreamTopology(), props);
        kafkaStreams.start();
        return kafkaStreams;
    }

    @SuppressWarnings("unchecked")
   	@Bean
    public Topology kafkaStreamTopology() {
    	final StreamsBuilder builder = new StreamsBuilder(); 
    	Pipeline p = repo.findOne(); 	
    	TreeMap<Integer, Rule> r =  p.parseRules();
    	sandBox.setRules(r);
    	return sandBox.apply(p, builder).build();
    }
}