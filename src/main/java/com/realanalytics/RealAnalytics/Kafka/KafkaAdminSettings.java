package com.realanalytics.RealAnalytics.Kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaAdminSettings {

	@Bean
	public KafkaAdmin admin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.104");
		return new KafkaAdmin(configs);
	}
	
	@Bean
	public NewTopic topic1() {
		return TopicBuilder.name(KafkaConstants.RAW_EVENTS_TOPIC)
				.partitions(2)
				.replicas(1)
				.config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
				.build();
	}
	
	@Bean
	public NewTopic topic2() {
		return TopicBuilder.name(KafkaConstants.LOGIN_CASE_TOPIC)
				.partitions(2)
				.replicas(1)
				.config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
				.build();
	}
	
	@Bean
	public NewTopic topic3() {
		return TopicBuilder.name(KafkaConstants.LOGIN_CASE_ANALYSIS_TOPIC)
				.partitions(2)
				.replicas(1)
				.config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
				.build();
	}
	
	@Bean
	public NewTopic topic4() {
		return TopicBuilder.name(KafkaConstants.LOC_CASE_TOPIC)
				.partitions(2)
				.replicas(1)
				.config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
				.build();
	}
	
	@Bean
	public NewTopic topic5() {
		return TopicBuilder.name(KafkaConstants.LOGIN_CASE_ANALYSIS_TOPIC)
				.partitions(2)
				.replicas(1)
				.config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
				.build();
	}
}


