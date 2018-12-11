package com.catchopportunity.springapico.user;

public class UserToken {

	private int uid;
	private String email;
	private String password;
	private String latitude;
	private String longitude;
	private String token;

	public UserToken(User user, String token) {
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.latitude = user.getLatitude();
		this.longitude = user.getLongitude();
		this.uid = user.getUid();
		this.token = token;
	}

	public UserToken() {

	}

	public UserToken(int uid, String email, String password, String latitude, String longitude, String token) {
		super();
		this.uid = uid;
		this.email = email;
		this.password = password;
		this.latitude = latitude;
		this.longitude = longitude;
		this.token = token;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
