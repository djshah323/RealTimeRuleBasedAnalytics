package com.realanalytics.RealAnalytics.Data.Geo;

public class GeoIP {	
    private String countryName;
    private String city;
    private String latitude;
    private String longitude;
    
    private GeoIP() {
    	
    }
    
    public String getCountry() {
  		return countryName;		
  	}
    
    private void setCountry(String country) {
		this.countryName = country;		
	}
	
	public String getCity() {
		return city;
	}
	
	private void setCity(String city) {
		this.city = city;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	private void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	private void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public static class GeoIPBuilder {
		
		private GeoIP geoIPInstance;
		
		private GeoIPBuilder() {
			geoIPInstance = new GeoIP();
		}
		
		public static GeoIPBuilder builder() {
			return new GeoIPBuilder();
		}
		
		public GeoIPBuilder setCountry(String country) {
			geoIPInstance.setCountry(country);
			return this;
		}
		
		public GeoIPBuilder setCity(String city) {
			geoIPInstance.setCity(city);
			return this;
		}
		
		public GeoIPBuilder setLatitude(String lat) {
			geoIPInstance.setLatitude(lat);
			return this;
		}
		
		public GeoIPBuilder setLongitude(String lon) {
			geoIPInstance.setLongitude(lon);
			return this;
		}
		
		public GeoIP build() {
			return geoIPInstance;
		}
 	}


}