package com.catchopportunity.springapico.database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class DatabaseHandler {
	private final String URL = "jdbc:mysql://159.65.226.217:3306/catchopportunity?useSSL=false";
	private final String USERNAME = "userco";
	private final String PASSWORD = "berkaan123";
	
	Connection conn;
	Statement st;
	

	public Connection connectDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection(URL, USERNAME, PASSWORD);
			st = (Statement) conn.createStatement();
			System.out.println("Database is connected.");
			return conn;
		} catch (Exception e) {
			System.out.println("Could not initialize the database.");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	

	
	
	public void closeDB(){
		try {
			conn.close();
			System.out.println("Database connection closed");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR while closing connection");
		}
	}
	
	public ResultSet executeQuery(String query) {
		ResultSet rs;
		try {
			rs = st.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
	}
}
