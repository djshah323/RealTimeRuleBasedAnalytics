package com.realanalytics.RealAnalytics.Data.Geo;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

@Service
public class GeoIPService {
	
	private static final Logger logger = 
            LoggerFactory.getLogger(GeoIPService.class);
	
	private DatabaseReader dbReader;

	private static final Pattern PATTERN = Pattern.compile(
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	
	public GeoIPService() {
		try {
			File database = ResourceUtils.getFile("classpath:GeoLite2-City.mmdb");
			dbReader = new DatabaseReader.Builder(database).build();
		} catch (IOException e) {
			logger.error("Exception loading geoip resource" + e.getMessage());
		}
	}
	
	public GeoIP getLocation(String ip) 
			throws IOException, GeoIp2Exception {
		if(ip != null && PATTERN.matcher(ip).matches()) {
			 InetAddress ipAddress = InetAddress.getByName(ip);
		        CityResponse response = dbReader.city(ipAddress);       
		        String countryName = response.getCountry().getName();
		        String cityName = response.getCity().getName();
		        String latitude = 
		          response.getLocation().getLatitude().toString();
		        String longitude = 
		          response.getLocation().getLongitude().toString();
			     return GeoIP.GeoIPBuilder.builder()
					        .setCountry(countryName)
					        .setCity(cityName)
					        .setLatitude(latitude)
					        .setLongitude(longitude)
					        .build();
		}
        return null;
    }
}
