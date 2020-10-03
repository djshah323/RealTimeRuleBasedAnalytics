package com.realanalytics.RealAnalytics.Pipeline;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.realanalytics.RealAnalytics.Data.StrmPolicy;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Action;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Condition;
import com.realanalytics.RealAnalytics.Pipeline.Rule.Rule;
import com.realanalytics.RealAnalytics.Pipeline.Rule.RuleUtil;

@Repository
public class Pipeline {
	
	private static final Logger logger = 
            LoggerFactory.getLogger(Pipeline.class);
	
	@Id
	private String name;
	
	private String inputTopic;
	
	private String outputTopic;
	
	private Map<String, String> input;
	
	private List<Map<String, Object>> rule;
	
	//jaskson yaml parser
	public static class PipelineBuilder {
		
		private Pipeline custom = null;
		
		public PipelineBuilder(String yaml) {
			ObjectMapper	mapper = new ObjectMapper(new YAMLFactory());
			mapper.findAndRegisterModules();
			try {
				custom = mapper.readValue(yaml, Pipeline.class);
			} catch (IOException e) {
				logger.error("Exception reading yaml resource" + e.getMessage());
			}
		}
		
		public Pipeline build() {
			return custom;
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInputTopic() {
		return inputTopic;
	}

	public void setInputTopic(String inputTopic) {
		this.inputTopic = inputTopic;
	}

	public String getOutputTopic() {
		return outputTopic;
	}

	public void setOutputTopic(String outputTopic) {
		this.outputTopic = outputTopic;
	}

	public Map<String, String> getInput() {
		return input;
	}

	public void setInput(Map<String, String> input) {
		this.input = input;
	}

	public List<Map<String, Object>> getRule() {
		return rule;
	}

	public void setRule(List<Map<String, Object>> rule) {
		this.rule = rule;
	}
	
	public TreeMap<Integer, Rule> parseRules() {	
		TreeMap<Integer, Rule> ruleSet = new TreeMap();	
		Class[] signatureForIf = {String.class, String.class, String.class};
		Class[] signatureForDo = {String.class};
		
		for(Map<String, Object> ruleItem: rule) {
			String name = ruleItem.get("name").toString();
			Integer weight = (Integer) ruleItem.get("weight");				
			if (name != null && !name.isEmpty()) {
				Rule newRule = new Rule(name);
				Iterator it = ruleItem.entrySet().iterator();			
			    while (it.hasNext()) {
			    	try {
				        Map.Entry<String, Object> pair = (Map.Entry)it.next();
				        if (pair.getKey().startsWith(RuleUtil.IF_KEY) 
				        	|| pair.getKey().startsWith(RuleUtil.IF_VALUE)) {
				        	Map<String, Object> verb = (Map<String, Object>) pair.getValue();
				        	Map.Entry<String,Object> verbEntry = verb.entrySet().iterator().next();
				        	
			        	   if (pair.getKey().startsWith(RuleUtil.IF_KEY)) {
					        	newRule.condition(
						        	(Condition) RuleUtil.instantiate(Condition.conditionClasses, 
						        			RuleUtil.IF_KEY, 
						        			signatureForIf, 
						        			new Object[] {pair.getKey(),
						        					verbEntry.getKey(), 
						        					(String) verbEntry.getValue()}));
					        } else if (pair.getKey().startsWith(RuleUtil.IF_VALUE)) {
					        	newRule.condition(
						        	(Condition) RuleUtil.instantiate(Condition.conditionClasses, 
						        			RuleUtil.IF_VALUE, 
						        			signatureForIf, 
						        			new Object[] {pair.getKey(),
						        					verbEntry.getKey(),
						        					(String) verbEntry.getValue()}));
					        }
				        } else if (pair.getKey().startsWith("do")) {
				         	newRule.action(
						        	(Action) RuleUtil.instantiate(Action.actionClasses, 
						        			pair.getKey(), 
						        			signatureForDo, 
						        			new Object[] {pair.getValue()}));
				       }
			    } catch (Throwable e) {
			    	logger.error("Unable to parse rules:");
			    	e.printStackTrace();
				}
			}
		    ruleSet.put(weight, newRule);
		 }
	  }
	  return ruleSet;
	}
}
