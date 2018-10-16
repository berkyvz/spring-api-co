package com.catchopportunity.springapico.opportunity;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.catchopportunity.springapico.database.DatabaseHandler;

@Service
public class OpportunityService {
	
	DatabaseHandler dbHandler = new DatabaseHandler();

	public ArrayList<Opportunity> getAllOpportunities() {
		ArrayList<Opportunity> oppList = new ArrayList<Opportunity>();
		
		dbHandler.connectDB();
		try {
			ResultSet rs = dbHandler.executeGetQuery("SELECT * FROM Opportunity;");
			
			while (rs.next()) {
				int oid = rs.getInt("oid");
				String latitude = rs.getString("latitude");
		        String longitude = rs.getString("longitude");
		        String count = rs.getString("count");
		        String desc1 = rs.getString("desc1");
		        String desc2 = rs.getString("desc2");
		        String desc3 = rs.getString("desc3");
		        String price = rs.getString("price");
		        String city = rs.getString("city");
		        
		        Opportunity o = new Opportunity(oid, latitude, longitude, count, desc1, desc2, desc3, price, city);
		        oppList.add(o);
			}
			
			return oppList;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Opportunity getOpportunityWithID(int id) {
		dbHandler.connectDB();
		try {
			ResultSet rs = dbHandler.executeGetQuery("SELECT * FROM Opportunity WHERE oid="+id+";");

			while (rs.next()) {
				int oid = rs.getInt("oid");
				String latitude = rs.getString("latitude");
		        String longitude = rs.getString("longitude");
		        String count = rs.getString("count");
		        String desc1 = rs.getString("desc1");
		        String desc2 = rs.getString("desc2");
		        String desc3 = rs.getString("desc3");
		        String price = rs.getString("price");
		        String city = rs.getString("city");
		        
		        Opportunity o = new Opportunity(oid, latitude, longitude, count, desc1, desc2, desc3, price, city);
				
		        dbHandler.closeDB();
				return o;
				
			}
		} catch (Exception e) {
			dbHandler.closeDB();
			return null;
		}
		dbHandler.closeDB();
		return null;
	}

}
