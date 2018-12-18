package com.catchopportunity.springapico.opportunity;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.catchopportunity.springapico.company.Company;
import com.catchopportunity.springapico.company.CompanyService;
import com.catchopportunity.springapico.database.DatabaseHandler;

@Service
public class OpportunityService {

	DatabaseHandler dbHandler = new DatabaseHandler();
	CompanyService companyService = new CompanyService();

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
			dbHandler.closeDB();
			return oppList;
		} catch (Exception e) {

			dbHandler.closeDB();
			return null;
		}
	}

	public Opportunity getOpportunityWithID(int id) {
		dbHandler.connectDB();
		try {
			ResultSet rs = dbHandler.executeGetQuery("SELECT * FROM Opportunity WHERE oid=" + id + ";");

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

	public ArrayList<Opportunity> getOpportunitiesOwnedByCompany(int coid) {
		ArrayList<Opportunity> theList = new ArrayList<Opportunity>();
		ArrayList<Integer> comOpp = new ArrayList<Integer>();
		dbHandler.connectDB();
		try {
			ResultSet rs = dbHandler.executeGetQuery("SELECT * FROM Generate WHERE coid=" + coid + ";");
			while (rs.next()) {
				int oid = rs.getInt("oid");
				comOpp.add(oid);
			}
			for (int i = 0; i < comOpp.size(); i++) {
				rs = dbHandler.executeGetQuery("SELECT * FROM Opportunity WHERE oid =" + comOpp.get(i));
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
					theList.add(o);
				}
			}
			dbHandler.closeDB();
			return theList;
		} catch (Exception e) {
			dbHandler.closeDB();
			return null;
		}
	}

	public boolean addOpportunity(Opportunity opportunity, int coid) {
		dbHandler.connectDB();
		try {
			System.out.println(opportunity.getPrice());
			dbHandler.executeSetQuery(
					"INSERT INTO Opportunity(price, count , city , latitude , longitude , desc1 , desc2 , desc3 ) "
							+ "VALUES ('" + opportunity.getPrice() + "' , '" + opportunity.getCount() + "' , '"
							+ opportunity.getCity() + "' ," + " '" + opportunity.getLatitude() + "' , '"
							+ opportunity.getLongitude() + "' , '" + opportunity.getDesc1() + "' , " + "'"
							+ opportunity.getDesc2() + "',  '" + opportunity.getDesc3() + "') ;");
		} catch (Exception e) {
			dbHandler.closeDB();
			return false;
		}
		try {
			ArrayList<Opportunity> list = getAllOpportunities();
			Opportunity o = list.get(list.size() - 1); // this is not a good way :(
			dbHandler.connectDB();
			dbHandler.executeSetQuery("INSERT INTO Generate(oid , coid) VALUES ( " + o.getOid() + " , " + coid + ");");
		} catch (Exception e) {
			dbHandler.closeDB();
			return false;
		}
		dbHandler.closeDB();
		return true;
	}

	public Company getCompanyIdFromOpportunityId(int oid) {
		dbHandler.connectDB();
		int coid = -1;
		try {
			ResultSet rs = dbHandler.executeGetQuery("SELECT * FROM Generate WHERE oid= " + oid + "  ;");
			while (rs.next()) {
				coid = rs.getInt("coid");
			}
			if (coid != -1) {

				Company c = companyService.getCompanyWithID(coid);
				dbHandler.closeDB();
				return c;
			}
		} catch (Exception e) {
			dbHandler.closeDB();
			return null;
		}
		dbHandler.closeDB();
		return null;

	}

	public boolean deleteOpportunity(int index, int coid) {
		index--;
		dbHandler.connectDB();
		Opportunity deletingOp = getOpportunitiesOwnedByCompany(coid).get(index);
		dbHandler.connectDB();
		try {
			dbHandler.executeSetQuery("DELETE  FROM Opportunity WHERE oid=" + deletingOp.getOid() + ";");
			dbHandler.executeSetQuery("DELETE  FROM Generate WHERE oid=" + deletingOp.getOid() + ";");
			dbHandler.executeSetQuery("DELETE FROM Reserve WHERE oid=" + deletingOp.getOid() + ";");
		} catch (Exception e) {
			dbHandler.closeDB();
			return false;
		}

		dbHandler.closeDB();
		return true;

	}

}
