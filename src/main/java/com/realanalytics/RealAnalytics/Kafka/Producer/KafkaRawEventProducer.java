package com.realanalytics.RealAnalytics.Kafka.Producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.realanalytics.RealAnalytics.Kafka.KafkaConstants;

@Service
public class KafkaRawEventProducer {
	  private static final Logger logger = 
            LoggerFactory.getLogger(KafkaRawEventProducer.class);
	
	  @Autowired
	  private KafkaTemplate<String, String> kafkaTemplate;
	  
	  public void sendMessage(String app, String message) {
		  ListenableFuture<SendResult<String, String>> future =
				  this.kafkaTemplate.send(KafkaConstants.RAW_EVENTS_TOPIC, app, message);
		  future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
		      @Override
		      public void onSuccess(SendResult<String, String> result) {
		    	logger.info("Message delivered with offset {}",
		          result.getRecordMetadata().offset());
		      }	  
		      @Override
		      public void onFailure(Throwable ex) {
		    	logger.warn("Unable to deliver message [{}]. {}", 
		          message,
		          ex.getMessage());
		      }
		    });
	  }

}
