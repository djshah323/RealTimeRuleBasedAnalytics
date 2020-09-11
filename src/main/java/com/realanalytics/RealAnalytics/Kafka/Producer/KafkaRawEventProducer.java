package com.realanalytics.RealAnalytics.Kafka.Producer;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate; 
import org.springframework.stereotype.Service;

import com.realanalytics.RealAnalytics.Kafka.KafkaConstants;

@Service
public class KafkaRawEventProducer {
	
	  private static final Logger logger = 
            LoggerFactory.getLogger(KafkaProducerService.class);
	
	  @Autowired
	  private KafkaTemplate<String, String> kafkaTemplate;
	  
	  public void sendMessage(String app, String message) {
		  this.kafkaTemplate.send(KafkaConstants.RAW_EVENTS_TOPIC, app, message);
	  }

}
