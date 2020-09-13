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

import com.realanalytics.RealAnalytics.Alerts.StrmAlert;

/**
 * @author SDhaval
 *
 */
@Repository
public class AlertRepository {
	
	@Autowired
    private MongoTemplate mongoTemplate;
	
	public List<StrmAlert> findAll() {
	      return mongoTemplate.findAll(StrmAlert.class);
	}
	
	public StrmAlert save(StrmAlert alert) {
		mongoTemplate.save(alert);
		return alert;
	}
	
	public StrmAlert findById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, StrmAlert.class);
	}
	
	public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, StrmAlert.class);
    }
}
