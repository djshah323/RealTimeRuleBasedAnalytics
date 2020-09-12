package com.realanalytics.RealAnalytics.Kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
public class KafkaAdminSettings {

	@Value("${kafka.bootstrap.servers}")
	private String bootstrapServers;
	
	@Bean
	public KafkaAdmin admin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		return new KafkaAdmin(configs);
	}
	
	@Bean
	public NewTopic rawEvent() {
		return TopicBuilder.name(KafkaConstants.RAW_EVENTS_TOPIC)
				.partitions(2)
				.replicas(1)
				.config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
				.build();
	}
	
	@Bean
	public NewTopic analyticevent() {
		return TopicBuilder.name(KafkaConstants.ANALYTIC_EVENT_TOPIC)
				.partitions(2)
				.replicas(1)
				.config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
				.build();
	}
	
	@Bean
	public NewTopic loginCase() {
		return TopicBuilder.name(KafkaConstants.LOGIN_CASE_TOPIC)
				.partitions(2)
				.replicas(1)
				.config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
				.build();
	}
	
	
	@Bean
	public NewTopic locCase() {
		return TopicBuilder.name(KafkaConstants.LOC_CASE_TOPIC)
				.partitions(2)
				.replicas(1)
				.config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
				.build();
	}
	
	
	@Bean
	public Map<String, Object> producerConfigs() {
	    Map<String, Object> props = new HashMap<>();
	    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
	      bootstrapServers);
	    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
	      StringSerializer.class);
	    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
	      StringSerializer.class);
	    return props;
	}
	
	 @Bean
	 public ProducerFactory<String, String> producerFactory() {
	    return new DefaultKafkaProducerFactory<>(producerConfigs());
	 }

	  @Bean
	  public KafkaTemplate<String, String> kafkaTemplate() {
	    return new KafkaTemplate<>(producerFactory());
	  }
	  
	  @Bean
	  public Map<String, Object> consumerConfigs() {
	    Map<String, Object> props = new HashMap<>();
	    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
	      bootstrapServers);
	    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
	      StringDeserializer.class);
	    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
	  	      StringDeserializer.class);
	    return props;
	  }
	  
	  @Bean
	  public ConsumerFactory<String, String> consumerFactory() {
	    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	  }

	  @Bean
	  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
	    ConcurrentKafkaListenerContainerFactory<String, String> factory =
	      new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(consumerFactory());
	    return factory;
	  }
}


