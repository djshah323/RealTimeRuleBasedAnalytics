package com.realanalytics.RealAnalytics.Pipeline.Rule;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;

public class RuleUtil {

	public static final String IF_KEY = "if-key";
	public static final String IF_VALUE = "if-value";
	
	public static final String DO_MAP = "do-map";
	public static final String DO_ALLOW = "do-allow";
	public static final String DO_DENY = "do-deny";
	public static final String DO_WINDOW = "do-window";
	
	public static final String VERB_ANY = "#any";
	public static final String VERB_KEY = "#key";
	public static final String VERB_VALUE = "#value";
	public static final String VERB_STRING = "#string";
	public static final String VERB_PATTERN = "#pattern";
	public static final String VERB_CONCAT = "#concat";
	public static final String VERB_WINDOW = "#window";
	
	public static Map createMap(Object pairs[])
	{
		TreeMap map = new TreeMap();
		for (int i = 0; i < (pairs.length - 1); i += 2)
		{
			map.put(pairs[i], pairs[i+1]);
		}
		return map;
	}
	
	public static Object instantiate(Map map, String key, Class signature[], Object args[])
			throws Throwable
		{
			try
			{
				Class theClass = (Class)map.get(key);
				if (theClass == null)
				{
					return null;
				}
				Constructor constructor = theClass.getConstructor(signature);
				if (constructor == null)
				{
					return null;
				}
				return constructor.newInstance(args);
			}
			catch(InvocationTargetException e)
			{
				throw e.getCause();
			}
			catch(Throwable t)
			{
				// throw away anything that wouldn't have happened if we called constructor directly
				t.printStackTrace();
			}
			return null;
		}
	
}
