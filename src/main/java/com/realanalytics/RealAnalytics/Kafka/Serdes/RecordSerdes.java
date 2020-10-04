package com.realanalytics.RealAnalytics.Kafka.Serdes;

import org.apache.kafka.common.serialization.Serdes.WrapperSerde;

import com.realanalytics.RealAnalytics.Pipeline.Record;

public class RecordSerdes extends WrapperSerde<Record> {
	
	public RecordSerdes() {
        super(new RecordSerializer(), new RecordDeserializer());
    }
}
