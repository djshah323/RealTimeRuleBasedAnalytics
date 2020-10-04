package com.realanalytics.RealAnalytics.PipelineTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.realanalytics.RealAnalytics.Pipeline.Attribute;
import com.realanalytics.RealAnalytics.Pipeline.Pipeline;
import com.realanalytics.RealAnalytics.Pipeline.Record;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Rule;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PipelineRules {
	
	private Pipeline fetchPipeline() {
		try {
			File pipelineConfig = ResourceUtils.getFile("classpath:pipeline.yaml");
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			mapper.findAndRegisterModules();
			return mapper.readValue(pipelineConfig, Pipeline.class);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Test
	public void pipelineParseTest() {
		Pipeline p = fetchPipeline();
		assertNotNull(p);
		Map<Integer, Rule> rules = p.parseRules();
		assertNotNull(rules);
		assertEquals(7, rules.size());
		Iterator  ruleItr = rules.keySet().iterator();
		while(ruleItr.hasNext()) {
			Rule r = rules.get(ruleItr.next());
			assertNotNull(r);
			assertNotNull(r.getName());
		}
	}
	
	@Test
	public void pipelineDataTest() {
		Pipeline p = fetchPipeline();
		assertNotNull(p);
		ObjectMapper mapper = new ObjectMapper();
		String jsonEvent = 
			"{\"createDate\": \"2020-09-09T01:54:38\","
					+ "\"eventId\": \"9a63369e-22e0-4683-8989-4d99e8a203f0\","
					+ "\"srcIp\": \"70.42.129.126\","
					+ "\"user\": \"administrator\","
					+ "\"userId\":\"0326a094-de82-4cc1-b711-345bcc5ae22c\","
					+ "\"email\":\"admin@sumoskope.onmicrosoft.com\","
					+ "\"country\": \"India\","
					+ "\"deviceType\": \"browser\","
				    + "\"deviceName\": \"mozilla\","
				    + "\"deviceAuth\": \"OAuth\","
				    + "\"application\": \"Azure\"}";
		try {
			Map<String, Object> event = (HashMap<String, Object>) 
					mapper.readValue(jsonEvent, HashMap.class);
			Record newRec = new Record();
			Iterator<String> pipelineAttrs = p.getInput().keySet().iterator();
			while(pipelineAttrs.hasNext()) {
				String attrname = pipelineAttrs.next();
				String type = p.getInput().get(attrname);
				Attribute newAttr = new Attribute(attrname, type);
				newAttr.setValue(event.get(attrname));
				newRec.add(newAttr);
			} 
			assertTrue(true);
			assertNotNull(newRec.get("createDate"));
			assertNotNull(newRec.get("eventId"));
			assertNotNull(newRec.get("srcIp"));
			assertNotNull(newRec.get("user"));
			assertNotNull(newRec.get("userId"));
			assertNotNull(newRec.get("email"));
			assertNotNull(newRec.get("country"));
			assertNotNull(newRec.get("city"));
			assertNotNull(newRec.get("deviceType"));
			assertNotNull(newRec.get("deviceName"));
			assertNotNull(newRec.get("deviceAuth"));
			assertNotNull(newRec.get("application"));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
