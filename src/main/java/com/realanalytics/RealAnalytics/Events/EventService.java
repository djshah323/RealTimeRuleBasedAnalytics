/**
 * 
 */
package com.realanalytics.RealAnalytics.Events;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

 

import static com.realanalytics.RealAnalytics.Events.Utils.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Applications.AppReferer;
import com.realanalytics.RealAnalytics.Applications.Events.ApplicationEvent;
import com.realanalytics.RealAnalytics.Dao.AnalyticEventRepository;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;
import com.realanalytics.RealAnalytics.Events.Services.EventMapper;
import com.realanalytics.RealAnalytics.Events.Services.EventSanity;
import com.realanalytics.RealAnalytics.Exceptions.BadEventException;
import com.realanalytics.RealAnalytics.Exceptions.IllegalAppNameException;
import com.realanalytics.RealAnalytics.Kafka.Producer.KafkaEventPublisher;

/**
 * @author SDhaval
 *
 */

@RestController
public final class EventService {
	
	public static final String RAW_EVENT_TOPIC = "raw_events";
	
	private static final Logger logger = 
	            LoggerFactory.getLogger(EventService.class);
		
	@Autowired
	ObjectMapper mapper ;
	
	@Autowired
	private EventSanity<?> eventSanity;
	
	@Autowired
	private EventMapper eventMapper;
	
	@Autowired
	private KafkaEventPublisher eventPublishingService;
	
	@Autowired
	private AnalyticEventRepository eventRepo;
	
	
	@RequestMapping(value = "v1/events/{appName}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postEvent(@PathVariable String appName, 
						   @RequestBody Map<String, Object> rawEvent) {
		try {
			/*
			 * Publish the event ASAP to kafka to free up the receiver rest endpoints
			 */
			AppReferer app = Utils.getAppDetails(appName);
			eventPublishingService.sendRawEvent(app.name(), 
					mapper.writeValueAsString(rawEvent));
		} catch (IllegalAppNameException e) {
			logger.error("Illegal app name");
			return new ResponseEntity<>(response(e.getMessage()), HttpStatus.BAD_REQUEST);
		} catch (JsonProcessingException e) {
			logger.error("Error parsing payload");
			return new ResponseEntity<>(response(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response("success"), HttpStatus.CREATED);
	}
	
	
	public void process(String appName, String eventJson) {
		try {
			AppReferer app;
			ApplicationEvent appevent;
			AnalyticEvent ae;		
			/*
			 * Fetch application description using the kafka key
			 */
			app = Utils.getAppDetails(appName);
			
			/*
			 * Check if the raw event is usable i.e it contains all the properties as required by application
			 */
			appevent = eventSanity.check(app, eventJson);			
			logger.info("ApplicationEvent parsed: " + mapper
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(appevent));
			/*
			 * Create an Analytic event out of it. Analytic event is the common event format on top which 
			 * analytic logic is written. Multiple app finally generate this event. 
			 */
			ae = eventMapper.createAnalyticEvent(appevent);		
			logger.info("AnalyticEvent created: " + mapper
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(ae));
			
			/*
			 * Publish the analytic event for further processing by KafkaStreams. 
			 */
			eventPublishingService.sendAnalyticEvent(ae);	
			logger.info("Analytic Event pushed to kakfa for further analysis" );
			/*
			 * Commit the event in mongo. 
			 */
			eventRepo.save(ae);
			logger.info("Analytic Event saved to Mongo");
			
		} catch(IllegalAppNameException e) {
			logger.error("Illegal app name");
		} catch (BadEventException e) {
			logger.error("BadEventException parsing raw event");
		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException parsing raw event");
		}
	}
}
