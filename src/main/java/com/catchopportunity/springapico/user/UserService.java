package com.catchopportunity.springapico.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Service;
import com.catchopportunity.springapico.database.DatabaseHandler;
import com.catchopportunity.springapico.tokenmanager.TokenManager;

@Service
public class UserService {

	DatabaseHandler dbHandler = new DatabaseHandler();
	TokenManager tokenManager = new TokenManager();

	public User getUserFromToken(String token) {
		dbHandler.connectDB();
		String[] ep = tokenManager.decodeUserToken(token);
		String email = ep[0];
		String password = ep[1];
		try {
			ResultSet rs = dbHandler.executeGetQuery(
					"SELECT * FROM User WHERE email='" + email + "'  AND password='" + password + "';  ");
			while (rs.next()) {
				int uid = rs.getInt("uid");
				String email1 = rs.getString("email");
				String password1 = rs.getString("password");
				String latitude = rs.getString("latitude");
				String longitude = rs.getString("longitude");

				User u = new User(uid, email1, password1, latitude, longitude);
				return u;
			}

		} catch (Exception e) {
			return null;
		}
		return null;

	}

	public boolean addUser(User user) {
		dbHandler.connectDB();
		
		if(user.getLatitude().length() > 9) {
		user.setLatitude(user.getLatitude().substring(0,10));
		}
		if(user.getLongitude().length() > 9) {
		user.setLongitude(user.getLongitude().substring(0,10));
		}
		if (user.getEmail() == null && user.getPassword() == null && user.getLatitude() == null
				&& user.getLongitude() == null) {
			return false;
		}

		try {
			dbHandler.connectDB();
			dbHandler.executeSetQuery("INSERT INTO User(email,password,latitude,longitude) " + "VALUES (   '"
					+ user.getEmail() + "' ,  '" + user.getPassword() + "' , '" + user.getLatitude() + "' ,  '"
					+ user.getLongitude() + "'  );");
			return true;
		} catch (Exception e) {
			return false;
		}

	}


	public User userLogin(String token) {
		dbHandler.connectDB();
		try {
		String[] up = tokenManager.decodeUserToken(token);
		String email = up[0];
		String password = up[1];
		User user = getUserWithEmailPassword(email, password);
		return user;
		
		}catch (Exception e) {
			return null;
		}
	}

	public User getUserWithEmailPassword(String email, String password) {
		dbHandler.connectDB();
		ResultSet rs = dbHandler
				.executeGetQuery("SELECT * FROM User WHERE email='" + email + "'  AND password='" + password + "'  ;");
		try {
			while (rs.next()) {

				int uid = rs.getInt("uid");
				String email1 = rs.getString("email");
				String password1 = rs.getString("password");
				String latitude = rs.getString("latitude");
				String longitude = rs.getString("longitude");

				User u = new User(uid, email1, password1, latitude, longitude);
				return u;
			}
		} catch (SQLException e) {
			return null;
		}
		return null;
	}

}
