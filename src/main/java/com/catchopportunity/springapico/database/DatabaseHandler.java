package com.catchopportunity.springapico.database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aspectj.weaver.patterns.IScope;

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
		
			if(conn == null) {
				Class.forName("com.mysql.jdbc.Driver");
				conn = (Connection) DriverManager.getConnection(URL, USERNAME, PASSWORD);
				st = (Statement) conn.createStatement();
				
			}
			if(conn.isClosed()) {
				Class.forName("com.mysql.jdbc.Driver");
				conn = (Connection) DriverManager.getConnection(URL, USERNAME, PASSWORD);
				st = (Statement) conn.createStatement();
				
			}
			
			
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
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR while closing connection");
		}
	}
	
	public String executeSetQuery(String query) {
		try {
			st.executeUpdate(query);
			return "OK";
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public ResultSet executeGetQuery(String query) {
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
