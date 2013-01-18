package com.konichiwa.settings;

import com.konichiwa.client.UserDetail;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MyProfile {
	
	private static final String KEY_GENDER = "MY_PREF_GENDER";
	private static final String KEY_INTEREST = "MY_PREF_INTEREST";
	private static final String KEY_AGE = "MY_PREF_AGE";
	private static final String KEY_BG = "MY_PREF_BG";
	
	private static final String MY_PROFILE_XML = "com.jasonshin.profile_xml";
	private SharedPreferences appSharedPrefs;
    private Editor prefsEditor;

	
	public MyProfile(Context context){
		this.appSharedPrefs = context.getSharedPreferences(MY_PROFILE_XML, Activity.MODE_PRIVATE);
		this.prefsEditor = this.appSharedPrefs.edit();
	}
	
	public void setGender(String gender){
		prefsEditor.putString(this.KEY_GENDER, gender);
        prefsEditor.commit();
	}

	public void setInterest(String interest){
		prefsEditor.putString(this.KEY_INTEREST, interest);
        prefsEditor.commit();
	}
	
	public void setAge(int age){
		prefsEditor.putInt(this.KEY_AGE, age);
        prefsEditor.commit();
	}
	
	
	public void setBackground(int i){
		prefsEditor.putInt(this.KEY_BG, i);
		prefsEditor.commit();
	}
	
	public int getBackground(){
		return appSharedPrefs.getInt(this.KEY_BG, 2);
	}
	
	public String getGender(){
		return appSharedPrefs.getString(this.KEY_GENDER, "not specified!");
	}
	
	public String getCountry(Context context){
		return new UserDetail(context).getCountry();
	}
	
	public String getInterest(){
		return appSharedPrefs.getString(this.KEY_INTEREST, "not specified!");
	}
	
	public int getAge(){
		return appSharedPrefs.getInt(this.KEY_AGE, 0);
	}

}
