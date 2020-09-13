package com.realanalytics.RealAnalytics.Policy;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.realanalytics.RealAnalytics.Dao.PolicyRepository;
import com.realanalytics.RealAnalytics.Data.StrmPolicy;

/**
 * @author SDhaval
 *
 */
@RestController
public class PolicyService {
	
	@Autowired
	private PolicyRepository repo;
	
	@PostMapping("/v1/policy/stream")
	public void createUser(@RequestBody StrmPolicy policy) {
		repo.save(policy);
	}
	
	@GetMapping("v1/policy/stream/{id}")
	public Policy getUser(@PathVariable String id) {
		return repo.findById(id);
	}
	
	@GetMapping("v1/policy/stream")
	public List getUserAll() {
		return repo.findAll();
	}

}
