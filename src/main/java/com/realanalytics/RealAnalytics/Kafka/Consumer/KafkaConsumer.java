package com.realanalytics.RealAnalytics.Kafka.Consumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.realanalytics.RealAnalytics.Events.EventService;
import com.realanalytics.RealAnalytics.Kafka.KafkaConstants;
import com.realanalytics.RealAnalytics.Policy.PolicyProcessor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

 
@Service
public class KafkaConsumer {
    private final Logger logger = 
            LoggerFactory.getLogger(KafkaConsumer.class);
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private PolicyProcessor processor;
    
    @KafkaListener(topics = EventService.RAW_EVENT_TOPIC, 
            groupId = "event_gen")
    public void consume(ConsumerRecord record) 
    {
        eventService.process((String)record.key(), (String)record.value());
    }
    
    @KafkaListener(topics = KafkaConstants.NOTIF_TOPIC, 
            groupId = "notif_grp")
    public void consume_notifications(ConsumerRecord record) 
    {
    	processor.handle((String)record.key(), (String)record.value());
    }
}
