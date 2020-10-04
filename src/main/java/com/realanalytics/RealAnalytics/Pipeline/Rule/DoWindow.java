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
	public KStream<String, Record> apply(Condition condition, KStream<String, Record> stream) {
		KStream<String, Record> streamSendBack = stream;
		String conditionLogText = condition != null ? 
				condition.getClass().getSimpleName() : "None";
		this.logger.info("Action: " + DoWindow.class.getSimpleName());
		this.logger.info("Verb size: " + verb.size());
		this.logger.info("Condition: " + conditionLogText);
		if (condition != null) {
			streamSendBack = streamSendBack.filter((key, rec) -> {
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
		streamSendBack = streamSendBack.groupBy((key, rec) -> key)
			.windowedBy(SessionWindows.with(Duration.ofMinutes(t)))
			.count(Materialized.<String, Long, SessionStore<Bytes, byte[]>>as(attr)
	                .withKeySerde(Serdes.String())
	                .withValueSerde(Serdes.Long()))
			.toStream()
			.map((key, value) -> new KeyValue<String, Record>(key.key(), 
					new Record.RecordBuilder()
					.addAttr("count", Long.toString(value))
					.build()));
		return streamSendBack;
	}
}
