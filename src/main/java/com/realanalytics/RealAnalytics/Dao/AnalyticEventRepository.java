/**
 * 
 */
package com.realanalytics.RealAnalytics.Dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.realanalytics.RealAnalytics.Data.AnalyticEvent;

/**
 * @author SDhaval
 *
 */
@Repository
public class AnalyticEventRepository {
	
	@Autowired
    private MongoTemplate mongoTemplate;
	
	public List findAll() {
	      return mongoTemplate.findAll(AnalyticEvent.class);
	}
	
	public AnalyticEvent save(AnalyticEvent ev) {
		mongoTemplate.save(ev);
		return ev;
	}
	
	
	public AnalyticEvent findById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, AnalyticEvent.class);
	}
	
	public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, AnalyticEvent.class);
    }
}
