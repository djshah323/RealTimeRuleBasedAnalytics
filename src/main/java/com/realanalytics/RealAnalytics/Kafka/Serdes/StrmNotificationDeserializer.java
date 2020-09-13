package com.realanalytics.RealAnalytics.Kafka.Serdes;


import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Notification.StrmNotification;

@SuppressWarnings("rawtypes")
public class StrmNotificationDeserializer implements Deserializer {
	 @Override 
	 public void close() {
	 
	 }
	 
	 @Override
	 public StrmNotification deserialize(String arg0, byte[] arg1) {
	   ObjectMapper mapper = new ObjectMapper();
	   StrmNotification ae = null;
	   try {
		   ae = mapper.readValue(arg1, StrmNotification.class);
	   } catch (Exception e) {
	     e.printStackTrace();
	   }
	   return ae;
	 }
}
