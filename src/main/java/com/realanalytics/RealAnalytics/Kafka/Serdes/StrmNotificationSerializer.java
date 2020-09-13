package com.realanalytics.RealAnalytics.Kafka.Serdes;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Notification.StrmNotification;

@SuppressWarnings("rawtypes")
public class StrmNotificationSerializer implements Serializer {
	 
	 @Override public void close() {
		 
	 }
	 
	@Override
	public byte[] serialize(String topic, Object data) {
		 byte[] retVal = null;
		 ObjectMapper objectMapper = new ObjectMapper();
		 try {
		     retVal = objectMapper
		    		 .writeValueAsString((StrmNotification)data)
		    		 .getBytes();
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
		 return retVal;
	}
}