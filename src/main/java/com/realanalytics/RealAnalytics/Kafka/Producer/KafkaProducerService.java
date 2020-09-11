package com.realanalytics.RealAnalytics.Kafka.Producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate; 
import org.springframework.stereotype.Service;

import com.realanalytics.RealAnalytics.Data.AnalyticEvent;
import com.realanalytics.RealAnalytics.Events.Cases.EventCase;
import com.realanalytics.RealAnalytics.Kafka.KafkaUtils;

@Service
public class KafkaProducerService {
	 private static final Logger logger = 
	            LoggerFactory.getLogger(KafkaProducerService.class);
	 
	@Autowired
	private KafkaTemplate<String, AnalyticEvent> kafkaTemplate;
	  
	public void send(EventCase c) {
		kafkaTemplate.send(KafkaUtils.getTopicForCase(c.getCaseName()), c.getEvent());
	}
}
