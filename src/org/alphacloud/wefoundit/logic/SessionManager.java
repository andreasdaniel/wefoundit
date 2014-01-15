package org.alphacloud.wefoundit.logic;

import org.alphacloud.wefoundit.model.SharedPrefInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	//fields
	private SharedPreferences sharedPref;
	private Editor editor;
	private Context context;
	
	public SessionManager(Context context) {
		this.context = context;
		this.sharedPref = this.context.getSharedPreferences(SharedPrefInfo.PREF_NAME, SharedPrefInfo.PRIVATE_MODE);
		this.editor = this.sharedPref.edit();
	}
	
	public void createLoginSession(String username, String email, int id, String phone) {
		editor.putBoolean(SharedPrefInfo.SESSION_IS_LOGIN_LABEL, true);
		editor.putString(SharedPrefInfo.SESSION_USERNAME_LABEL, username);
		editor.putString(SharedPrefInfo.SESSION_EMAIL_LABEL, email);
		editor.putInt(SharedPrefInfo.SESSION_USERID_LABEL, id);
		editor.putString(SharedPrefInfo.SESSION_PHONE_LABEL, phone);
		
		editor.commit();
	}
	
	public boolean isLogin() {
		return sharedPref.getBoolean(SharedPrefInfo.SESSION_IS_LOGIN_LABEL, false);
	}
	
	public void logout() {
		removeLoginSession();
	}
	
	public int getUserID() {
		return sharedPref.getInt(SharedPrefInfo.SESSION_USERID_LABEL, -1);
	}
	
	public String getUsername() {
		return sharedPref.getString(SharedPrefInfo.SESSION_USERNAME_LABEL, "<error>");
	}
	
	public String getUserEmail() {
		return sharedPref.getString(SharedPrefInfo.SESSION_EMAIL_LABEL, "");
	}
	
	public String getUserPhone() {
		return sharedPref.getString(SharedPrefInfo.SESSION_PHONE_LABEL, "");
	}
	
	public void removeLoginSession() {
		editor.remove(SharedPrefInfo.SESSION_USERNAME_LABEL);
		editor.remove(SharedPrefInfo.SESSION_EMAIL_LABEL);
		editor.remove(SharedPrefInfo.SESSION_USERID_LABEL);
		editor.remove(SharedPrefInfo.SESSION_USERID_LABEL);
		editor.remove(SharedPrefInfo.SESSION_PHONE_LABEL);
		editor.putBoolean(SharedPrefInfo.SESSION_IS_LOGIN_LABEL, false);
		
		editor.commit();
	}
}
