package com.catchopportunity.springapico.company;

public class Company {

	private int coid;
	private String email;
	private String password;
	private String name;
	private String city;
	private String latitude;
	private String longitude;
	private String phone;
	
	public Company() {
		
	}
	
	public Company(int coid, String email, String password, String name, String city, String latitude, String longitude,
			String phone) {
		super();
		this.coid = coid;
		this.email = email;
		this.password = password;
		this.name = name;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
		this.phone = phone;
	}
	public int getCoid() {
		return coid;
	}
	public void setCoid(int coid) {
		this.coid = coid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	

}
