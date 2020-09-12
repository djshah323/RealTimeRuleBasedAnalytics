package com.realanalytics.RealAnalytics.Kafka.Serdes;


import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;

@SuppressWarnings("rawtypes")
public class AnalyticEventDeserializer implements Deserializer {
	 @Override 
	 public void close() {
	 
	 }
	 
	 @Override
	 public AnalyticEvent deserialize(String arg0, byte[] arg1) {
	   ObjectMapper mapper = new ObjectMapper();
	   AnalyticEvent ae = null;
	   try {
		   ae = mapper.readValue(arg1, AnalyticEvent.class);
	   } catch (Exception e) {
	     e.printStackTrace();
	   }
	   return ae;
	 }
}
