package com.realanalytics.RealAnalytics.Pipeline.Rule;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.stereotype.Service;

import com.realanalytics.RealAnalytics.Kafka.Serdes.RecordDeserializer;
import com.realanalytics.RealAnalytics.Kafka.Serdes.RecordSerdes;
import com.realanalytics.RealAnalytics.Kafka.Serdes.RecordSerializer;
import com.realanalytics.RealAnalytics.Pipeline.Pipeline;
import com.realanalytics.RealAnalytics.Pipeline.Record;

@Service
public class RuleSandBox {

	private TreeMap<Integer, Rule> rules;

	public RuleSandBox() {
		
	}
	
	public void setRules(TreeMap<Integer, Rule> r) {
		rules = r;
	}

	public StreamsBuilder apply(Pipeline p, StreamsBuilder builder) {
		try {
			KStream<String, Record> stream = 
			    	builder.stream(p.getInputTopic(), 
			        		Consumed.with(Serdes.String(), new RecordSerdes()));
			Set<Integer> keys =rules.keySet();
	        for (Iterator i = keys.iterator(); i.hasNext();) {
	          Integer key = (Integer) i.next();
	          Rule value =  rules.get(key);
	          stream = value.apply(stream);
	        }   
	        stream.to(p.getOutputTopic(), Produced.with(Serdes.String(), new RecordSerdes()));
	        return builder;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return builder;
	}
	
}
