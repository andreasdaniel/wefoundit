package org.alphacloud.wefoundit.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.alphacloud.wefoundit.logic.ShareData;
import org.alphacloud.wefoundit.model.Category;
import org.alphacloud.wefoundit.model.City;
import org.alphacloud.wefoundit.model.Country;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;

public class NativeDatabaseParser {
	// fields
	private Context mContext;
	
	public NativeDatabaseParser(Context context) {
		this.mContext = context;
	}
	
	public void parseNativeDatabase() {
		Log.d("CatController", "getXML");
		
		List<Category> categories = new ArrayList<Category>();
		List<Country> countries = new ArrayList<Country>();
		List<City> cities = new ArrayList<City>();
		Map<String, List<City>> districts = new TreeMap<String, List<City>>();
		
		categories.add(new Category(0, "<category>"));
		countries.add(new Country("0", "<country>"));
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(Assets.getNativeDB(mContext));
			
			Log.d("CatController", "begin parsing");
			
			NodeList mainNode = doc.getElementsByTagName("table");
			System.out.println(mainNode.getLength());
			for(int i = 0; i < mainNode.getLength(); i++) {
				Element el = (Element) mainNode.item(i);
				
				// extracting category
				if(el.getAttribute("name").compareTo("category") ==  0) {
					NodeList nodes = el.getElementsByTagName("column");
					
					Element e1 = (Element) nodes.item(0);
					Element e2 = (Element) nodes.item(1);
					int id;
					String name;
					if(e1.getAttribute("name").compareTo("categoryId") == 0) {
						id = Integer.parseInt(e1.getFirstChild().getNodeValue());
						name = e2.getFirstChild().getNodeValue();
					} else {
						id = Integer.parseInt(e2.getFirstChild().getNodeValue());
						name = e1.getFirstChild().getNodeValue();
					}
					
					Category category = new Category(id, name);
					categories.add(category);
				}
				
				// extracting city
				else if(el.getAttribute("name").compareTo("city") ==  0) {
					NodeList nodes = el.getElementsByTagName("column");
					
					int id = 0;
					String name = null;
					String district = null;
					String countryCode = null;
					
					for(int j = 0; j < nodes.getLength(); j++) {
						Element e = (Element) nodes.item(j);
						
						if(e.getAttribute("name").compareTo("cityId") == 0) {
							id = Integer.parseInt(e.getFirstChild().getNodeValue());
						}
						else if(e.getAttribute("name").compareTo("cityName") == 0) {
							name = e.getFirstChild().getNodeValue();
						}
						else if(e.getAttribute("name").compareTo("cityDistrict") == 0) {
							if(!e.hasChildNodes())
								district = "";
							else
								district = e.getFirstChild().getNodeValue();
						}
						else if(e.getAttribute("name").compareTo("countryCode") == 0) {
							countryCode = e.getFirstChild().getNodeValue();
						}
					}
					
					City city = new City(id, name, district, countryCode);
					cities.add(city);
					
					if(!districts.containsKey(district)) {
						List<City> cityList = new ArrayList<City>();
						districts.put(district, cityList);
					}
					districts.get(district).add(city);
				}
				
				// extracting country
				else if(el.getAttribute("name").compareTo("country") ==  0) {
					NodeList nodes = el.getElementsByTagName("column");
					
					String id = null;
					String name = null;
					
					for(int j = 0; j < nodes.getLength(); j++) {
						Element e = (Element) nodes.item(j);
						
						if(e.getAttribute("name").compareTo("countryCode") == 0) {
							id = e.getFirstChild().getNodeValue();
						}
						else if(e.getAttribute("name").compareTo("countryName") == 0) {
							name = e.getFirstChild().getNodeValue();
						}
					}
					
					Country country = new Country(id, name);
					countries.add(country);
					
				}
			}
			
			ShareData.CATEGORIES = categories;
			ShareData.CITIES = cities;
			ShareData.COUNTRIES = countries;
			//ShareData.DISTRICTS = districts;
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
