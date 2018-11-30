package com.catchopportunity.springapico.user;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.catchopportunity.springapico.database.DatabaseHandler;
import com.catchopportunity.springapico.tokenmanager.TokenManager;

@Service
public class UserService {

	DatabaseHandler dbHandler = new DatabaseHandler();
	TokenManager tokenManager = new TokenManager();

	public User userLogin(String token) {
		 String[] up = tokenManager.decodeUserToken(token);
		 String email = up[0];
		 String password = up[1];
		 User user = getUserWithEmailPassword(email, password);
		 return user;
	 }

	public User getUserWithEmailPassword(String email, String password) {
		ResultSet rs = dbHandler
				.executeGetQuery("SELECT * FROM User WHERE email = '" + email + "' AND password='" + password + "';");
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
