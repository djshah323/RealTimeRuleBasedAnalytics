/**
 * 
 */
package com.realanalytics.RealAnalytics.Dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.realanalytics.RealAnalytics.Data.StrmPolicy;

/**
 * @author SDhaval
 *
 */
@Repository
public class PolicyRepository {
	
	@Autowired
    private MongoTemplate mongoTemplate;
	
	public List findAll() {
	      return mongoTemplate.findAll(StrmPolicy.class);
	}
	
	public StrmPolicy save(StrmPolicy policy) {
		mongoTemplate.save(policy);
		return policy;
	}
	
	public StrmPolicy findById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, StrmPolicy.class);
	}
	
	public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, StrmPolicy.class);
    }
}
