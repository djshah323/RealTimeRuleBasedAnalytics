package com.realanalytics.RealAnalytics.Events.Services;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.realanalytics.RealAnalytics.Applications.Events.ApplicationEvent;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;
import com.realanalytics.RealAnalytics.Data.Geo.GeoIPService;

@Service
public class EventMapper {

	private static final Logger logger = 
            LoggerFactory.getLogger(EventMapper.class);
	
	@Autowired
	private GeoIPService geoIPService;

	public AnalyticEvent createAnalyticEvent(ApplicationEvent appevent) {	
		AnalyticEvent ae = null;
		try {
			ae = appevent.analyticEvent();
			ae.setGeoIP(geoIPService.getLocation(ae.getSrcIp()));
		} catch (IOException | GeoIp2Exception e) {
			logger.warn("Cannot construct GeoIP" );
		}
		return ae;
	}


}
