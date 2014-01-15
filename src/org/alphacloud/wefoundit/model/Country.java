package org.alphacloud.wefoundit.model;

import org.alphacloud.wefoundit.logic.ShareData;

public class Country {
	// fields
	private String code;
	private String name;
	
	public Country(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static String getCountryNameByCode(String code) {
		String name = "";
		
		for(Country c : ShareData.COUNTRIES) {
			if(c.getCode().compareToIgnoreCase(code) == 0) {
				name = c.getName();
				break;
			}
		}
		
		return name;
	}
	
}
