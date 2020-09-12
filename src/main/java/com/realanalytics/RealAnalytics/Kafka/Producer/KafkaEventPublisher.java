package com.realanalytics.RealAnalytics.Kafka.Producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;
import com.realanalytics.RealAnalytics.Kafka.KafkaConstants;

@Service
public class KafkaEventPublisher {
	
  private static final Logger logger = 
            LoggerFactory.getLogger(KafkaEventPublisher.class);
	
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;
  
  @Autowired
  ObjectMapper mapper;
  
  public void sendRawEvent(String app, String message) {
	  ListenableFuture<SendResult<String, String>> future =
			  this.kafkaTemplate.send(KafkaConstants.RAW_EVENTS_TOPIC, app, message);
	  future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
	      @Override
	      public void onSuccess(SendResult<String, String> result) {
	    	logger.info("Raw event delivered with offset {}",
	          result.getRecordMetadata().offset());
	      }	  
	      @Override
	      public void onFailure(Throwable ex) {
	    	logger.warn("Unable to deliver raw event [{}]. {}", 
	          message,
	          ex.getMessage());
	      }
	    });
  }

  public void sendAnalyticEvent(AnalyticEvent ae) {	  
	ListenableFuture<SendResult<String, String>> future;
	try {
		future = this.kafkaTemplate.send(KafkaConstants.ANALYTIC_EVENT_TOPIC, ae.getEventActorId(),
				mapper.writeValueAsString(ae));
		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
		      @Override
		      public void onSuccess(SendResult<String, String> result) {
		    	logger.info("Analytic event delivered with offset {}",
		      result.getRecordMetadata().offset());
			  }	  
			  @Override
			  public void onFailure(Throwable ex) {
				logger.warn("Unable to deliver message [{}]. {}", 
			      ae,
			      ex.getMessage());
			  }
		});
	} catch (JsonProcessingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
}
