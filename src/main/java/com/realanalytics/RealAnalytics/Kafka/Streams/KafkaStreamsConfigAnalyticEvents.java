package com.realanalytics.RealAnalytics.Kafka.Streams;

import static com.realanalytics.RealAnalytics.Kafka.Streams.Utils.buildApplicationByKey;
import static com.realanalytics.RealAnalytics.Kafka.Streams.Utils.buildUserByKey;
import static com.realanalytics.RealAnalytics.Kafka.Streams.Utils.buildCountryByKey;
import static com.realanalytics.RealAnalytics.Kafka.Streams.Utils.buildDeviceByKey;
import static com.realanalytics.RealAnalytics.Kafka.Streams.Utils.parseKey;
import static com.realanalytics.RealAnalytics.Kafka.Streams.Utils.fetchFromParsedKey;

import com.realanalytics.RealAnalytics.Kafka.KafkaConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;
import com.realanalytics.RealAnalytics.Events.EventService;
import com.realanalytics.RealAnalytics.Kafka.Serdes.AnalyticEventDeserializer;
import com.realanalytics.RealAnalytics.Kafka.Serdes.AnalyticEventSerializer;
import com.realanalytics.RealAnalytics.Kafka.Serdes.StrmNotificationDeserializer;
import com.realanalytics.RealAnalytics.Kafka.Serdes.StrmNotificationSerializer;
import com.realanalytics.RealAnalytics.Notification.StrmNotification;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
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
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
public class KafkaStreamsConfigAnalyticEvents {
	
	@Value("${kafka.bootstrap.servers}")
	private String bootstrapServers;
	
	private static final Logger logger = 
            LoggerFactory.getLogger(KafkaStreamsConfigAnalyticEvents.class);
	
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
    
    private String[] extractMultipleItems(AnalyticEvent analytic) {
    	/*
    	 * This can be configured via a policy to extract all data of interest.
    	 */
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
	
    @SuppressWarnings("unchecked")
	@Bean
    public Topology kafkaStreamTopology() {
    	final StreamsBuilder builder = new StreamsBuilder();	
		KStream<String, String[]> cases = builder.stream(KafkaConstants.ANALYTIC_EVENT_TOPIC, 
        		Consumed.with(Serdes.String(), 
        				new Serdes.WrapperSerde<AnalyticEvent>(new AnalyticEventSerializer(), 
        						new AnalyticEventDeserializer()) {     			
        		}))
        		.filter(KafkaStreamsConfigAnalyticEvents.notNull())
        		.map((key, analytic) -> new KeyValue<String, String[]>(key, extractMultipleItems(analytic)));
		/*
		 * Split to a device login case
		 */
		cases.filter(KafkaStreamsConfigAnalyticEvents.casePredicate())
			 .map((key, analytic) -> new KeyValue<String, String>(key, analytic[KafkaConstants.LOGIN_CASE]))
			 .to(KafkaConstants.LOGIN_CASE_TOPIC, Produced.with(Serdes.String(), Serdes.String()));
		
		/*
		 * Split to a location login case
		 */
		
		cases.filter(KafkaStreamsConfigAnalyticEvents.casePredicate())
			 .map((key, analytic) -> new KeyValue<String, String>(key, analytic[KafkaConstants.LOC_CASE]))
			 .to(KafkaConstants.LOC_CASE_TOPIC, Produced.with(Serdes.String(), Serdes.String()));    
		
		/*
         * Process login_case and location_case 
         * to produce streaming notifications 
         * 
         */
    	KStream<String, String> loginCase = 
    			builder.stream(KafkaConstants.LOGIN_CASE_TOPIC, Consumed.with(Serdes.String(), 
    					Serdes.String()));
    	
    	//loginCase.filter(KafkaStreamsConfigAnalyticEvents.print());
    	KStream<String, Map<String,String>> loginCaseT = loginCase.map((key, value) -> {
			try {
				return new KeyValue<String, Map<String, String>>
								(key, mapper.readValue(value, HashMap.class));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		});
    	
    	/*
    	 * By User
    	 */
    	loginCaseT.filter(KafkaStreamsConfigAnalyticEvents.notNull())
    		.map((key, value) -> {
				try {
					return new KeyValue<String, String>(buildUserByKey(value),
							mapper.writeValueAsString(value));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			})
    		// group by key so we can count by session windows
    		.groupBy((key, value) -> key)
            // window by session
            .windowedBy(SessionWindows.with(Duration.ofMinutes(5)))
            // count play events per session
            .count(Materialized.<String, Long, SessionStore<Bytes, byte[]>>as("loginRateByUser")
                .withKeySerde(Serdes.String())
                .withValueSerde(Serdes.Long()))
            // convert to a stream so we can map the key to a string
            .toStream()
            // map key to a readable string
            .map((key, value) -> new KeyValue<String, StrmNotification>(key.key(), 
            		new StrmNotification(value, 
	            		fetchFromParsedKey(Utils.EVT_ACT_POS, parseKey(key.key())),
	            		key.window().start(),
	            		key.window().end(), 
	            		fetchFromParsedKey(Utils.OP_POS, parseKey(key.key())),
	            		fetchFromParsedKey(Utils.MATCH_POS, parseKey(key.key())).split(":")[0],
	            		fetchFromParsedKey(Utils.MATCH_POS, parseKey(key.key())).split(":")[1])))
            // write to play-events-per-session topic
            .to(KafkaConstants.NOTIF_TOPIC, Produced.with(Serdes.String(), 
            		new Serdes.WrapperSerde<StrmNotification>(
            						new StrmNotificationSerializer(), 
            						new StrmNotificationDeserializer()) {}));
    	/*
    	 * By Applications
    	 */
      	loginCaseT.filter(KafkaStreamsConfigAnalyticEvents.notNull())
		.map((key, value) -> {
			try {
				return new KeyValue<String, String>(buildApplicationByKey(value),
						mapper.writeValueAsString(value));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		})
		// group by key so we can count by session windows
		.groupBy((key, value) -> key)
        // window by session
        .windowedBy(SessionWindows.with(Duration.ofMinutes(15)))
        // count play events per session
        .count(Materialized.<String, Long, SessionStore<Bytes, byte[]>>as("loginRateByApplication")
            .withKeySerde(Serdes.String())
            .withValueSerde(Serdes.Long()))
        // convert to a stream so we can map the key to a string
        .toStream()
        // map key to a readable string
        .map((key, value) -> new KeyValue<String, StrmNotification>(key.key(), 
        		new StrmNotification(value, 
            		fetchFromParsedKey(Utils.EVT_ACT_POS, parseKey(key.key())),
            		key.window().start(),
            		key.window().end(), 
            		fetchFromParsedKey(Utils.OP_POS, parseKey(key.key())),
            		fetchFromParsedKey(Utils.MATCH_POS, parseKey(key.key())).split(":")[0],
            		fetchFromParsedKey(Utils.MATCH_POS, parseKey(key.key())).split(":")[1])))
        // write to play-events-per-session topic
        .to(KafkaConstants.NOTIF_TOPIC, Produced.with(Serdes.String(), 
        		new Serdes.WrapperSerde<StrmNotification>(
        						new StrmNotificationSerializer(), 
        						new StrmNotificationDeserializer()) {}));
    	
        
    	/*
    	 * By Device
    	 */
      	loginCaseT.filter(KafkaStreamsConfigAnalyticEvents.notNull())
		.map((key, value) -> {
			try {
				return new KeyValue<String, String>(buildDeviceByKey(value),
						mapper.writeValueAsString(value));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		})
		// group by key so we can count by session windows
		.groupBy((key, value) -> key)
        // window by session
        .windowedBy(SessionWindows.with(Duration.ofMinutes(5)))
        // count play events per session
        .count(Materialized.<String, Long, SessionStore<Bytes, byte[]>>as("loginRateByDevice")
            .withKeySerde(Serdes.String())
            .withValueSerde(Serdes.Long()))
        // convert to a stream so we can map the key to a string
        .toStream()
        // map key to a readable string
        .map((key, value) -> new KeyValue<String, StrmNotification>(key.key(), 
        		new StrmNotification(value, 
            		fetchFromParsedKey(Utils.EVT_ACT_POS, parseKey(key.key())),
            		key.window().start(),
            		key.window().end(), 
            		fetchFromParsedKey(Utils.OP_POS, parseKey(key.key())),
            		fetchFromParsedKey(Utils.MATCH_POS, parseKey(key.key())).split(":")[0],
            		fetchFromParsedKey(Utils.MATCH_POS, parseKey(key.key())).split(":")[1])))
        // write to play-events-per-session topic
        .to(KafkaConstants.NOTIF_TOPIC, Produced.with(Serdes.String(), 
        		new Serdes.WrapperSerde<StrmNotification>(
        						new StrmNotificationSerializer(), 
        						new StrmNotificationDeserializer()) {}));
      	
      	
      	KStream<String, String> locCase = 
    			builder.stream(KafkaConstants.LOC_CASE_TOPIC, Consumed.with(Serdes.String(), 
    					Serdes.String()));
      	
      //locCase.filter(KafkaStreamsConfigAnalyticEvents.print());
    	KStream<String, Map<String,String>> locCaseT = locCase.map((key, value) -> {
			try {
				return new KeyValue<String, Map<String, String>>
								(key, mapper.readValue(value, HashMap.class));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		});
    	
    	/*
    	 * By Country
    	 */
    	locCaseT.filter(KafkaStreamsConfigAnalyticEvents.notNull())
		.map((key, value) -> {
			try {
				return new KeyValue<String, String>(buildCountryByKey(value),
						mapper.writeValueAsString(value));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		})
		// group by key so we can count by session windows
		.groupBy((key, value) -> key)
        // window by session
        .windowedBy(SessionWindows.with(Duration.ofMinutes(5)))
        // count play events per session
        .count(Materialized.<String, Long, SessionStore<Bytes, byte[]>>as("loginRateByCountry")
            .withKeySerde(Serdes.String())
            .withValueSerde(Serdes.Long()))
        // convert to a stream so we can map the key to a string
        .toStream()
        // map key to a readable string
        .map((key, value) -> new KeyValue<String, StrmNotification>(key.key(), 
        		new StrmNotification(value, 
            		fetchFromParsedKey(Utils.EVT_ACT_POS, parseKey(key.key())),
            		key.window().start(),
            		key.window().end(), 
            		fetchFromParsedKey(Utils.OP_POS, parseKey(key.key())),
            		fetchFromParsedKey(Utils.MATCH_POS, parseKey(key.key())).split(":")[0],
            		fetchFromParsedKey(Utils.MATCH_POS, parseKey(key.key())).split(":")[1])))
        // write to play-events-per-session topic
        .to(KafkaConstants.NOTIF_TOPIC, Produced.with(Serdes.String(), 
        		new Serdes.WrapperSerde<StrmNotification>(
        						new StrmNotificationSerializer(), 
        						new StrmNotificationDeserializer()) {}));
      	
        return builder.build();
    }
  
}