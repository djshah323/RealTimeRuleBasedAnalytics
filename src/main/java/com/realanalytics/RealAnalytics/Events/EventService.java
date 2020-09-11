/**
 * 
 */
package com.realanalytics.RealAnalytics.Events;

import java.io.IOException;
import java.util.HashMap;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realanalytics.RealAnalytics.Applications.AppReferer;
import com.realanalytics.RealAnalytics.Dao.AnalyticEventRepository;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;
import com.realanalytics.RealAnalytics.Events.Cases.EventCase;
import com.realanalytics.RealAnalytics.Events.Cases.EventCaseParser;
import com.realanalytics.RealAnalytics.Events.Services.EventMapper;
import com.realanalytics.RealAnalytics.Events.Services.EventSanity;
import com.realanalytics.RealAnalytics.Exceptions.IllegalAppNameException;
import com.realanalytics.RealAnalytics.Identity.BadEventException;
import com.realanalytics.RealAnalytics.Kafka.Producer.KafkaProducerService;
import com.realanalytics.RealAnalytics.Kafka.Producer.KafkaRawEventProducer;

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
	private EventSanity eventSanity;
	
	@Autowired
	private EventMapper eventMapper;
	
	@Autowired
	private EventCaseParser eventCaseParser;
	
	@Autowired
	private KafkaRawEventProducer eventPushService;
	
	@Autowired
	private AnalyticEventRepository eventRepo;
	
	@Autowired
	private KafkaProducerService casePushService;
	
	@RequestMapping(value = "v1/events/{appName}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postEvent(@PathVariable String appName, 
						   @RequestBody Map<String, Object> rawEvent) {
		try {
			AppReferer app = Utils.getAppDetails(appName);
			ObjectMapper mapper = new ObjectMapper();
			String rawEventJson = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(rawEvent);
			eventPushService.sendMessage(app.name(), rawEventJson);
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
			AnalyticEvent ae;		
			@SuppressWarnings("unchecked")
			Map<String, Object> rawEvent = (HashMap<String, Object>) 
								new ObjectMapper().readValue(eventJson, HashMap.class);			
			AppReferer app = Utils.getAppDetails(appName);
			eventSanity.check(app, rawEvent);
			ae = eventMapper.translateToAnalyticEvent(app, rawEvent);
			for(EventCase ec : eventCaseParser.buildCases(ae)) {
				casePushService.send(ec);
			}
			eventRepo.save(ae);
		} catch(IllegalAppNameException e) {
			logger.error("Illegal app name");
		} catch (JsonParseException|JsonMappingException e) {
			logger.error("Exception parsing raw event");
		}  catch (IOException e) {
			logger.error("IO Exception parsing raw event");
		} catch (BadEventException e) {
			logger.error("BadEventException parsing raw event");
		}
	}
}
