package org.alphacloud.wefoundit.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class LostThing implements Parcelable {
	
	private int lostId;
	private int lostCat;
	private int lostUser;
	private int lostCity;
	private String lostUploadDate;
	private String lostDate;
	private String lostDesc;
	private String lostAdd;
	private int lostIsFound;
	private String lostPhone;
	private String lostEmail;
	
	public String loc;
	public String cat;
	
	private List<String> picURLs;
	
	public LostThing() {
		
	}
	
	public String getLostPhone() {
		return lostPhone;
	}

	public void setLostPhone(String lostPhone) {
		this.lostPhone = lostPhone;
	}

	public String getLostEmail() {
		return lostEmail;
	}

	public void setLostEmail(String lostEmail) {
		this.lostEmail = lostEmail;
	}
	
	public int getLostId() {
		return lostId;
	}

	public void setLostId(int lostId) {
		this.lostId = lostId;
	}

	public int getLostCat() {
		return lostCat;
	}

	public void setLostCat(int lostCat) {
		this.lostCat = lostCat;
	}

	public int getLostUser() {
		return lostUser;
	}

	public void setLostUser(int lostUser) {
		this.lostUser = lostUser;
	}

	public int getLostCity() {
		return lostCity;
	}

	public void setLostCity(int lostCity) {
		this.lostCity = lostCity;
	}

	public String getLostUploadDate() {
		return lostUploadDate;
	}

	public void setLostUploadDate(String lostUploadDate) {
		this.lostUploadDate = lostUploadDate;
	}

	public String getLostDate() {
		return lostDate;
	}

	public void setLostDate(String lostDate) {
		this.lostDate = lostDate;
	}

	public String getLostDesc() {
		return lostDesc;
	}

	public void setLostDesc(String lostDesc) {
		this.lostDesc = lostDesc;
	}

	public String getLostAdd() {
		return lostAdd;
	}

	public void setLostAdd(String lostAdd) {
		this.lostAdd = lostAdd;
	}

	public int getLostIsFound() {
		return lostIsFound;
	}

	public void setLostIsFound(int lostIsFound) {
		this.lostIsFound = lostIsFound;
	}

	public List<String> getPicURLs() {
		return picURLs;
	}

	public void setPicURLs(List<String> picURLs) {
		this.picURLs = picURLs;
	}
	
	// Parcelable parts
	public LostThing(Parcel in) {
		lostId = in.readInt();
		lostCat = in.readInt();
		lostUser = in.readInt();
		lostCity = in.readInt();
		lostUploadDate = in.readString();
		lostDate = in.readString();
		lostDesc = in.readString();
		lostAdd = in.readString();
		lostIsFound = in.readInt();
		lostPhone = in.readString();
		lostEmail = in.readString();
		
		loc = in.readString();
		cat = in.readString();
		
		picURLs = new ArrayList<String>();	
		in.readList(picURLs, String.class.getClassLoader());
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(lostId);
		dest.writeInt(lostCat);
		dest.writeInt(lostUser);
		dest.writeInt(lostCity);
		dest.writeString(lostUploadDate);
		dest.writeString(lostDate);
		dest.writeString(lostDesc);
		dest.writeString(lostAdd);
		dest.writeInt(lostIsFound);
		dest.writeString(lostPhone);
		dest.writeString(lostEmail);
		
		dest.writeString(loc);
		dest.writeString(cat);
		
		dest.writeList(picURLs);
	}
	
	public static final Parcelable.Creator<LostThing> CREATOR = new Parcelable.Creator<LostThing>() {  
	    
        public LostThing createFromParcel(Parcel in) {  
            return new LostThing(in);  
        }  
   
        public LostThing[] newArray(int size) {  
            return new LostThing[size];  
        }  
          
    };  
	
}
