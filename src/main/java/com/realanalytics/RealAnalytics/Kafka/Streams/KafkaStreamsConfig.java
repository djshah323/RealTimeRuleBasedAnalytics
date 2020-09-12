package com.realanalytics.RealAnalytics.Kafka.Streams;

import com.realanalytics.RealAnalytics.Kafka.KafkaConstants;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.state.SessionStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Properties;

@Configuration
public class KafkaStreamsConfig {
	
	@Value("${kafka.bootstrap.servers}")
	private String bootstrapServers;
	
    @Bean
    public KafkaStreams kafkaStreams(KafkaProperties kafkaProperties,
                                     @Value("${spring.application.name}") String appName) {
        final Properties props = new Properties();

        props.putAll(kafkaProperties.getProperties());
        // stream config centric ones
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, appName);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.STATE_DIR_CONFIG, "data");

        final KafkaStreams kafkaStreams = new KafkaStreams(kafkaStreamTopology(), props);
        kafkaStreams.start();
        return kafkaStreams;
    }

    @Bean
    public Topology kafkaStreamTopology() {
        final StreamsBuilder streamsBuilder = new StreamsBuilder();
        streamsBuilder.stream(KafkaConstants.ANALYTIC_EVENT_TOPIC, Consumed.with(Serdes.String(), Serdes.String()))
            // group by key so we can count by session windows
            .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
            // window by session
            .windowedBy(SessionWindows.with(Duration.ofMinutes(1)))
            // count play events per session
            .count(Materialized.<String, Long, SessionStore<Bytes, byte[]>>as("loginsPerMinute")
                .withKeySerde(Serdes.String())
                .withValueSerde(Serdes.Long()))
            // convert to a stream so we can map the key to a string
            .toStream()
            // map key to a readable string
            .map((key, value) -> new KeyValue<>(key.key() + "@" + key.window().start() + "->" + key.window().end(), value))
            // write to play-events-per-session topic
            .to(KafkaConstants.LOGIN_CASE_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
        return streamsBuilder.build();
    }

}