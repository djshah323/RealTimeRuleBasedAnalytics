package com.realanalytics.RealAnalytics.Pipeline.Rule;

import java.time.Duration;
import java.util.LinkedList;
import java.util.Stack;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.state.SessionStore;

import com.realanalytics.RealAnalytics.Pipeline.Record;

public class DoWindow extends Action implements HasVerb {

	private Stack<Verb> verb;

	public DoWindow(String verb) {
		this.verb = Verb.resolveVerbs(verb);
	}
	
	@Override
	public void apply(Condition condition, KStream<String, Record> stream) {
		if (condition != null) {
			stream.filter((key, rec) -> {
				return condition.evaluate(key, rec);
			});	
		}
		Verb v = verb.pop();
		String windowMeta = v.call(null, null, null);
		String attr = windowMeta.split(",")[0];
		String timeWindow = windowMeta.split(",")[1];
		if (timeWindow == null || timeWindow.isEmpty()) 
			timeWindow = "5";
		Integer t = Integer.parseInt(timeWindow);
		stream.groupBy((key, rec) -> key)
		.windowedBy(SessionWindows.with(Duration.ofMinutes(t)))
		.count(Materialized.<String, Long, SessionStore<Bytes, byte[]>>as(attr)
                .withKeySerde(Serdes.String())
                .withValueSerde(Serdes.Long()));
	}
}
