package org.alphacloud.wefoundit.model;

import java.util.ArrayList;
import java.util.List;

public class City {
	// fields
	private int id;
	private String name;
	private String district;
	private String countryCode;
		
	public City(int id, String name, String district, String countryCode) {
		this.id = id;
		this.name = name;
		this.district = district;
		this.countryCode = countryCode;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public static List<City> getCitiesByCountry(List<City> cities, String countryCode) {
		List<City> selectedCity = new ArrayList<City>();
		
		selectedCity.add(new City(0, "<city>", "<district>", "0"));
		
		for(City city : cities) {
			if(city.getCountryCode().compareTo(countryCode) == 0) {
				selectedCity.add(city);
			}
		}
		
		return selectedCity;
	}
	
	@Override
	public String toString() {
		return name + " - " + district;
	}
}
