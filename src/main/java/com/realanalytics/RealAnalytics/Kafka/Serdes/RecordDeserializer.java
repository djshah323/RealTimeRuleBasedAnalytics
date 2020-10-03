package com.realanalytics.RealAnalytics.Kafka.Serdes;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Pipeline.Record;

public class RecordDeserializer implements Deserializer {

	 @Override 
	 public void close() {
	 
	 }
	 
	@Override
	public Record deserialize(String topic, byte[] data) {
		 ObjectMapper mapper = new ObjectMapper();
		   Record ae = null;
		   try {
			   ae = mapper.readValue(data, Record.class);
		   } catch (Exception e) {
		     e.printStackTrace();
		   }
		   return ae;
	}

}
