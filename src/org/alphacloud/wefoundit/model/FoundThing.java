package org.alphacloud.wefoundit.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FoundThing implements Parcelable {
	
	private int foundId;
	private int foundCat;
	private int foundUser;
	private String foundDesc;
	private String foundDate;
	private double foundLong;
	private double foundLat;
	private String foundTempEmail;
	private String foundTempPhone;
	private int foundIsClaim;
	
	public String cat;
	public String loc;
	
	public FoundThing() {
		
	}
	
	public int getFoundId() {
		return foundId;
	}

	public void setFoundId(int foundId) {
		this.foundId = foundId;
	}

	public int getFoundCat() {
		return foundCat;
	}

	public void setFoundCat(int foundCat) {
		this.foundCat = foundCat;
	}

	public int getFoundUser() {
		return foundUser;
	}

	public void setFoundUser(int foundUser) {
		this.foundUser = foundUser;
	}

	public String getFoundDesc() {
		return foundDesc;
	}

	public void setFoundDesc(String foundDesc) {
		this.foundDesc = foundDesc;
	}

	public String getFoundDate() {
		return foundDate;
	}

	public void setFoundDate(String foundDate) {
		this.foundDate = foundDate;
	}

	public double getFoundLong() {
		return foundLong;
	}

	public void setFoundLong(double foundLong) {
		this.foundLong = foundLong;
	}

	public double getFoundLat() {
		return foundLat;
	}

	public void setFoundLat(double foundLat) {
		this.foundLat = foundLat;
	}

	public String getFoundTempEmail() {
		return foundTempEmail;
	}

	public void setFoundTempEmail(String foundTempEmail) {
		this.foundTempEmail = foundTempEmail;
	}

	public String getFoundTempPhone() {
		return foundTempPhone;
	}

	public void setFoundTempPhone(String foundTempPhone) {
		this.foundTempPhone = foundTempPhone;
	}

	public int getFoundIsClaim() {
		return foundIsClaim;
	}

	public void setFoundIsClaim(int foundIsClaim) {
		this.foundIsClaim = foundIsClaim;
	}

	// Parcelable Part
	public FoundThing(Parcel in) {
		foundId = in.readInt();
		foundCat = in.readInt();
		foundUser = in.readInt();
		foundDesc = in.readString();
		foundDate = in.readString();
		foundLong = in.readDouble();
		foundLat = in.readDouble();
		foundTempEmail = in.readString();
		foundTempPhone = in.readString();
		foundIsClaim = in.readInt();
		
		cat = in.readString();
		loc = in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static final Parcelable.Creator<FoundThing> CREATOR = new Parcelable.Creator<FoundThing>() {  
	    
        public FoundThing createFromParcel(Parcel in) {  
            return new FoundThing(in);  
        }  
   
        public FoundThing[] newArray(int size) {  
            return new FoundThing[size];  
        }  
          
    };  
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.foundId);
		dest.writeInt(this.foundCat);
		dest.writeInt(this.foundUser);
		dest.writeString(foundDesc);
		dest.writeString(foundDate);
		dest.writeDouble(foundLong);
		dest.writeDouble(foundLat);
		dest.writeString(foundTempEmail);
		dest.writeString(foundTempPhone);
		dest.writeInt(foundIsClaim);
		
		dest.writeString(cat);
		dest.writeString(loc);
	}
	
}
