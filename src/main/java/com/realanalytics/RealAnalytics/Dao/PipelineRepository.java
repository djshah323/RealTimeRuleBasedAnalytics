package com.realanalytics.RealAnalytics.Dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.realanalytics.RealAnalytics.Pipeline.Pipeline;

@Repository
public class PipelineRepository {
	
	@Autowired
    private MongoTemplate mongoTemplate;
	
	public List<Pipeline> findAll() {
	      return mongoTemplate.findAll(Pipeline.class);
	}
	
	public void save(Pipeline p) {
		mongoTemplate.save(p);
	}
	
	public Pipeline findOne() {
		return findAll().get(0);
	}
}
