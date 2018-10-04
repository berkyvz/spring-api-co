package com.catchopportunity.springapico.database;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class DatabaseHandler {
	private final String URL = "jdbc:mysql://159.65.226.217:3306/catchopportunity?useSSL=false";
	private final String USERNAME = "userco";
	private final String PASSWORD = "berkaan123";
	
	Connection conn;

	public Connection connectDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection(URL, USERNAME, PASSWORD);
			System.out.println("Database is connected.");
			return conn;
		} catch (Exception e) {
			System.out.println("Could not initialize the database.");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	
	public String closeDB() throws SQLException {
		conn.close();
		return "Databse connection closed";
	}
}
