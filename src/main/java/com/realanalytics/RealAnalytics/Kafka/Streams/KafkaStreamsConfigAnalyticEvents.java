package com.realanalytics.RealAnalytics.Kafka.Streams;

import com.realanalytics.RealAnalytics.Kafka.KafkaConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Dao.PipelineRepository;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;
import com.realanalytics.RealAnalytics.Kafka.Serdes.AnalyticEventDeserializer;
import com.realanalytics.RealAnalytics.Kafka.Serdes.AnalyticEventSerializer;
import com.realanalytics.RealAnalytics.Kafka.Serdes.StrmNotificationDeserializer;
import com.realanalytics.RealAnalytics.Kafka.Serdes.StrmNotificationSerializer;
import com.realanalytics.RealAnalytics.Notification.StrmNotification;
import com.realanalytics.RealAnalytics.Pipeline.Pipeline;
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
public class KafkaStreamsConfigAnalyticEvents {
	
	@Value("${kafka.bootstrap.servers}")
	private String bootstrapServers;
	
	private static final Logger logger = 
            LoggerFactory.getLogger(KafkaStreamsConfigAnalyticEvents.class);
	
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

    
    public static Predicate<String, String[]> casePredicate() {
    	  return (key, values) -> {
    		  return true;
    	  };
    }
    
    public static Predicate<String, Object> notNull() {
  	  return (key, value) -> {
  		  if (value != null)
  			  return true;
  		  else
  			  return false;
  	  };
    }
    
    public static Predicate<String, String> print() {
    	  return (key, value) -> {
    		  if (value != null)
    			  logger.info(value);
    			  return true;
    	  };
      }
    
    @SuppressWarnings("unchecked")
   	@Bean
    public Topology kafkaStreamTopology() {
    	Pipeline p = null;
    	if (repo.findAll().size() > 0) {
    		p = repo.findAll().get(0);
    	}  	
    	final StreamsBuilder builder = new StreamsBuilder();   	
    	builder.stream(KafkaConstants.ANALYTIC_EVENT_TOPIC, 
        		Consumed.with(Serdes.String(), Serdes.String()));
    	TreeMap<Integer, Rule> r =  p.parseRules();
    	Set<Integer> keys =r.keySet();
        for (Iterator i = keys.iterator(); i.hasNext();) {
          Integer key = (Integer) i.next();
          Rule value =  r.get(key);
          value.apply(builder);
        }
        
    	return builder.build();
    }
}