/**
 * 
 */
package com.realanalytics.RealAnalytics.Identity;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realanalytics.RealAnalytics.Dao.UserRepository;
import com.realanalytics.RealAnalytics.Data.User;

/**
 * @author SDhaval
 *
 */
@RestController
public class IdentityService {
	
	@Autowired
	private UserRepository repo;
	
	@PostMapping("/v1/users")
	public void createUser(@RequestBody User user) {
		repo.save(user);
	}
	
	@GetMapping("v1/users/{id}")
	public User getUser(@PathVariable String id) {
		return repo.findById(id);
	}
	
	@GetMapping("v1/users")
	public List getUserAll() {
		return repo.findAll();
	}
	
	@GetMapping("v1/users/$select")
	public User getUserByEmail(@RequestParam(value="email")String email) {
		String id = java.util.UUID.nameUUIDFromBytes(email.getBytes())
				.toString();
		return repo.findById(id);
	}
}
