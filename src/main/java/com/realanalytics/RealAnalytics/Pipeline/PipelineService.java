package com.realanalytics.RealAnalytics.Pipeline;

import static com.realanalytics.RealAnalytics.Events.Utils.response;

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

import com.realanalytics.RealAnalytics.Dao.PipelineRepository;
import com.realanalytics.RealAnalytics.Pipeline.Pipeline.PipelineBuilder;

@RestController
public class PipelineService {

	private static final Logger logger = 
            LoggerFactory.getLogger(Pipeline.class);
	
	@Autowired
	private PipelineRepository repo;
	
	@RequestMapping(value = "v1/pipeline",
			method = RequestMethod.POST,
			consumes = MediaType.TEXT_PLAIN_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postEvent(@RequestBody String yaml) {
		try {
			PipelineBuilder pBuilder = new Pipeline.PipelineBuilder(yaml);
			Pipeline newPipeline = pBuilder.build();
			repo.save(newPipeline);
		} catch (Exception e) {
			return new ResponseEntity<>(response(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response("success"), HttpStatus.CREATED);
	}
}
