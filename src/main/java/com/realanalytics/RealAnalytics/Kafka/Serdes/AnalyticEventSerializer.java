package com.realanalytics.RealAnalytics.Kafka.Serdes;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;

@SuppressWarnings("rawtypes")
public class AnalyticEventSerializer implements Serializer {
	 
	 @Override public void close() {
		 
	 }
	 
	@Override
	public byte[] serialize(String topic, Object data) {
		 byte[] retVal = null;
		 ObjectMapper objectMapper = new ObjectMapper();
		 try {
		     retVal = objectMapper
		    		 .writeValueAsString((AnalyticEvent)data)
		    		 .getBytes();
		 } catch (Exception e) {
		     e.printStackTrace();
		 }
		 return retVal;
	}
}