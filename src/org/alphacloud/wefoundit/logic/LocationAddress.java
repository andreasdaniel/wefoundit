package org.alphacloud.wefoundit.logic;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

public class LocationAddress {
	private final String TAG = "LocationAddress";
	private Geocoder locator;
	private double latitude;
	private double longitude;
	
	public LocationAddress(Context context, double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.locator = new Geocoder(context, Locale.ENGLISH);
	}
	
	public String getAddress() {
		String output = null;
		List<Address> addresses = null;
		try {
			addresses = this.locator.getFromLocation(latitude, longitude, 1);
			Log.d(TAG,"addrSize=" + addresses.size());
			if(addresses.size() > 0) {
				Address address = addresses.get(0);
				StringBuilder addrBuilder = new StringBuilder();
				
				//String addr = address.getFeatureName();
				String city = address.getLocality();
				String district = address.getSubAdminArea();
				String state = address.getAdminArea();
				String country = address.getCountryName();
				
				for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
					addrBuilder.append(address.getAddressLine(i)).append(", ");
				}
				
				output = addrBuilder.toString() + city + " " + district + " " + state + " " + country;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
	}
}
