package com.realanalytics.RealAnalytics.Kafka.Consumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.realanalytics.RealAnalytics.Events.EventService;

import org.apache.kafka.clients.consumer.ConsumerRecord;

 
@Service
public class KafkaRawEventConsumer {
    private final Logger logger = 
            LoggerFactory.getLogger(KafkaRawEventConsumer.class);
    
    @Autowired
    private EventService eventService;
    
    @KafkaListener(topics = EventService.RAW_EVENT_TOPIC, 
            groupId = "event_gen")
    public void consume(ConsumerRecord record) 
    {
        eventService.process((String)record.key(), (String)record.value());
    }
}
