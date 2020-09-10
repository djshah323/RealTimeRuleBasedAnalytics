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

import com.realanalytics.RealAnalytics.Data.User;

/**
 * @author SDhaval
 *
 */
@Repository
public class UserRepository {
	
	@Autowired
    private MongoTemplate mongoTemplate;
	
	public List findAll() {
	      return mongoTemplate.findAll(User.class);
	}
	
	public User save(User user) {
		mongoTemplate.save(user);
		return user;
	}
	
	public User update(User user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(user.getId()));
        Update update = new Update();
        update.set("appname", user.getAppname());
        return mongoTemplate.findAndModify(query, update, User.class);
    }
	
	public User findById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, User.class);
	}
	
	public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, User.class);
    }
}
