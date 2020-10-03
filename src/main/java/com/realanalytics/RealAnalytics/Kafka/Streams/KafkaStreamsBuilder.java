package com.realanalytics.RealAnalytics.Kafka.Streams;

import com.realanalytics.RealAnalytics.Kafka.KafkaConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Dao.PipelineRepository;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;
import com.realanalytics.RealAnalytics.Kafka.Serdes.AnalyticEventDeserializer;
import com.realanalytics.RealAnalytics.Kafka.Serdes.AnalyticEventSerializer;
import com.realanalytics.RealAnalytics.Kafka.Serdes.RecordDeserializer;
import com.realanalytics.RealAnalytics.Kafka.Serdes.RecordSerializer;
import com.realanalytics.RealAnalytics.Kafka.Serdes.StrmNotificationDeserializer;
import com.realanalytics.RealAnalytics.Kafka.Serdes.StrmNotificationSerializer;
import com.realanalytics.RealAnalytics.Notification.StrmNotification;
import com.realanalytics.RealAnalytics.Pipeline.Pipeline;
import com.realanalytics.RealAnalytics.Pipeline.Record;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Rule;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.state.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
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
    	Pipeline p = repo.findOne(); 	
    	final StreamsBuilder builder = new StreamsBuilder(); 
    	@SuppressWarnings("rawtypes")
		KStream<String, Record> stream = 
		    	builder.stream(p.getInputTopic(), 
		        		Consumed.with(Serdes.String(), new Serdes.WrapperSerde<Record>(
								new RecordSerializer(), 
								new RecordDeserializer())));
    	TreeMap<Integer, Rule> r =  p.parseRules();
    	Set<Integer> keys =r.keySet();
        for (Iterator i = keys.iterator(); i.hasNext();) {
          Integer key = (Integer) i.next();
          Rule value =  r.get(key);
          value.apply(stream);
        }    
        stream.to(p.getOutputTopic(), Produced.with(Serdes.String(), new Serdes.WrapperSerde<Record>(
								new RecordSerializer(), 
								new RecordDeserializer())));
    	return builder.build();
    }
}