package com.realanalytics.RealAnalytics.Kafka.Streams;

import com.realanalytics.RealAnalytics.Kafka.KafkaConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;
import com.realanalytics.RealAnalytics.Kafka.Serdes.AnalyticEventDeserializer;
import com.realanalytics.RealAnalytics.Kafka.Serdes.AnalyticEventSerializer;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {
	
	public static final int LOGIN_CASE = 0;
	public static final int LOC_CASE = 1;
	
	@Value("${kafka.bootstrap.servers}")
	private String bootstrapServers;
	
	@Autowired
	ObjectMapper mapper;
	
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
    
    public static Predicate<String, AnalyticEvent> notNull() {
  	  return (key, values) -> {
  		  if (values != null)
  			  return true;
  		  else
  			  return false;
  	  };
    }
    
    @Bean
    public Topology kafkaStreamTopology() {
    	final StreamsBuilder builder = new StreamsBuilder();	
        @SuppressWarnings("unchecked")
		KStream<String, String[]> cases = builder.stream(KafkaConstants.ANALYTIC_EVENT_TOPIC, 
        		Consumed.with(Serdes.String(), 
        				new Serdes.WrapperSerde<AnalyticEvent>(new AnalyticEventSerializer(), 
        						new AnalyticEventDeserializer()) {     			
        		}))
        		.filter(KafkaStreamsConfig.notNull())
        		.map((key, analytic) -> new KeyValue<String, String[]>(key, extractMultipleItems(analytic)));
		
		cases.filter(KafkaStreamsConfig.casePredicate())
		.map((key, analytic) -> new KeyValue<String, String>(key, analytic[LOGIN_CASE]))
		.to(KafkaConstants.LOGIN_CASE_TOPIC, Produced.with(Serdes.String(), Serdes.String()));
		
		cases.filter(KafkaStreamsConfig.casePredicate())
		.map((key, analytic) -> new KeyValue<String, String>(key, analytic[LOC_CASE]))
		.to(KafkaConstants.LOC_CASE_TOPIC, Produced.with(Serdes.String(), Serdes.String()));
        
        return builder.build();
    }

    private String[] extractMultipleItems(AnalyticEvent analytic) {
    	String[] ret = new String[2];
    	List<String> subEventArray = new ArrayList<String>();
    	subEventArray.add(extractLoginDevices(analytic));
    	subEventArray.add(extractGeoInfo(analytic));
    	return subEventArray.toArray(ret);
    }
    
	private String extractLoginDevices(AnalyticEvent analytic) {
		try {
			return mapper.writeValueAsString(analytic.extractLoginDevices());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private String extractGeoInfo(AnalyticEvent analytic) {
		try {
			return mapper.writeValueAsString(analytic.extractGeoInfo());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}