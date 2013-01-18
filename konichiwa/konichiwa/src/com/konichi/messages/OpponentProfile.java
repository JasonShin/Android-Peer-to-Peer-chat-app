package com.konichi.messages;

public class OpponentProfile {

	private String gender = "";
	private String country = "";
	private String interest = "";
	private String age = "";
	
	
	
	public OpponentProfile(String gender, String country, String interest, String age) {
		this.gender = gender;
		this.country = country;
		this.interest = interest;
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public String getCountry() {
		return country;
	}

	public String getInterest() {
		return interest;
	}

	public String getAge() {
		return age;
	}

	

	
	
}
