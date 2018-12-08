package com.catchopportunity.springapico.company;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.catchopportunity.springapico.database.DatabaseHandler;
import com.catchopportunity.springapico.tokenmanager.TokenManager;

@Service
public class CompanyService {

	DatabaseHandler dbHandler = new DatabaseHandler();
	TokenManager tokenManager = new TokenManager();

	public ArrayList<Company> getListSecure() {
		ArrayList<Company> list = new ArrayList<Company>();
		dbHandler.connectDB();
		try {
			ResultSet rs = dbHandler.executeGetQuery("SELECT * FROM Company");
			while (rs.next()) {
				Company c = new Company();
				int coid = rs.getInt("coid");
				c.setCoid(coid);
				String email = rs.getString("email");
				c.setEmail(email);
				String password = rs.getString("password");
				c.setPassword(password);
				String name = rs.getString("name");
				c.setName(name);
				String city = rs.getString("city");
				c.setCity(city);
				String latitude = rs.getString("latitude");
				c.setLatitude(latitude);
				String longitude = rs.getString("longitude");
				c.setLongitude(longitude);
				String phone = rs.getString("phone");
				c.setPhone(phone);

				c.setPassword(null);

				list.add(c);
			}
			dbHandler.closeDB();
			return list;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String tokenCreator(String email , String password){
		return tokenManager.encodeCompanyEmailPassword(email, password);
	}
	
	public ArrayList<Company> getList() {
		ArrayList<Company> list = new ArrayList<Company>();
		dbHandler.connectDB();
		try {
			ResultSet rs = dbHandler.executeGetQuery("SELECT * FROM Company");
			while (rs.next()) {
				Company c = new Company();
				int coid = rs.getInt("coid");
				c.setCoid(coid);
				String email = rs.getString("email");
				c.setEmail(email);
				String password = rs.getString("password");
				c.setPassword(password);
				String name = rs.getString("name");
				c.setName(name);
				String city = rs.getString("city");
				c.setCity(city);
				String latitude = rs.getString("latitude");
				c.setLatitude(latitude);
				String longitude = rs.getString("longitude");
				c.setLongitude(longitude);
				String phone = rs.getString("phone");
				c.setPhone(phone);

				list.add(c);
			}
			dbHandler.closeDB();
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean tokenChecker(String token) {
		ArrayList<Company> list = getList();
		
		for (int i = 0; i < list.size(); i++) {
			System.out.println("KIYAS -> " + token +" VS " + list.get(i).getEmail()+":"+list.get(i).getPassword());
			if(token.equals(tokenManager.encodeCompanyEmailPassword(list.get(i).getEmail(), list.get(i).getPassword()))) {
				return true;
			}
		}
		return false;
		
	}

	public Company getCompanyWithID(int id) {

		Company returnedCompany = new Company();
		try {
			dbHandler.connectDB();
			ResultSet rs = dbHandler.executeGetQuery("SELECT * FROM Company WHERE coid = " + id + ";");
			while (rs.next()) {
				returnedCompany.setCoid(rs.getInt("coid"));
				returnedCompany.setEmail(rs.getString("email"));
				returnedCompany.setPassword(rs.getString("password"));
				returnedCompany.setCity(rs.getString("city"));
				returnedCompany.setLatitude(rs.getString("latitude"));
				returnedCompany.setLongitude(rs.getString("longitude"));
				returnedCompany.setName(rs.getString("name"));
				returnedCompany.setPhone(rs.getString("phone"));
			}
		} catch (Exception e) {
			return null;
		}
		
		returnedCompany.setPassword(null);
		dbHandler.closeDB();
		return returnedCompany;
	}

	public boolean addCompany(Company company) {

		try {
			if (company.getEmail().equals("") || company.getPassword().equals("") || company.getCity().equals("")
					|| company.getLatitude().equals("") || company.getName().equals("")
					|| company.getPhone().equals("")) {
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}

		dbHandler.connectDB();
		try {
			dbHandler.executeSetQuery(
					"INSERT INTO Company(email , password , name , city , latitude , longitude ,phone) VALUES " + "( '"
							+ company.getEmail() + "' , '" + company.getPassword() + "' , '" + company.getName()
							+ "' , '" + company.getCity() + "' , '" + company.getLatitude() + "' , '"
							+ company.getLongitude() + "' , '" + company.getPhone() + "');");
		} catch (Exception e) {
			dbHandler.closeDB();
			return false;
		}
		dbHandler.closeDB();
		return true;

	}

	public boolean deleteCompany(int id, String token) {
		if (!tokenChecker(token)) {
			return false;
		}
		String username = tokenManager.decodeCompanyToken(token)[0];
		String password = tokenManager.decodeCompanyToken(token)[1];
		int realID = getCompanyIdWithEmailAndPassword(username, password);
		dbHandler.connectDB();
		if (realID == id) {
			try {
				dbHandler.executeSetQuery("DELETE FROM Company WHERE coid= " + id + ";");
				dbHandler.executeSetQuery("DELETE FROM Generate WHERE coid= " + id + ";");
				dbHandler.closeDB();
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		dbHandler.closeDB();
		return false;
	}

	public boolean updateCompany(Company companyNew, int id, String token) {
		dbHandler.connectDB();

		if (!tokenChecker(token)) {
			return false;
		}

		if (companyNew.getCity() == null || companyNew.getPassword() == null || companyNew.getLatitude() == null
				|| companyNew.getLatitude() == null || companyNew.getName() == null || companyNew.getPhone() == null
				|| companyNew.getCity() == null) {
			dbHandler.closeDB();
			return false;
		}

		if (token.equals("Logged-Out")) {
			return false;
		}

		String email = tokenManager.decodeCompanyToken(token)[0];
		String password = tokenManager.decodeCompanyToken(token)[1];
		int realID = getCompanyIdWithEmailAndPassword(email, password);

		if (realID == id) {
			try {
				dbHandler.executeSetQuery("UPDATE Company SET password = '" + companyNew.getPassword() + "' , "
						+ "name = '" + companyNew.getName() + "' , " + "city = '" + companyNew.getCity() + "' , "
						+ "latitude = '" + companyNew.getLatitude() + "' , " + "longitude = '"
						+ companyNew.getLongitude() + "' ," + "phone = '" + companyNew.getPhone() + "' "
						+ "WHERE coid = " + id + ";");
				dbHandler.closeDB();
				return true;

			} catch (Exception e) {
				dbHandler.closeDB();
				return false;
			}
		}

		dbHandler.closeDB();
		return false;
	}

	public CompanyToken getCompanyWithEmailAndPasswordObject(Company company) { // Get hall company email password JSON
		// Object.
		dbHandler.connectDB();
		try {
			ResultSet rs = dbHandler.executeGetQuery("SELECT * FROM Company WHERE email='" + company.getEmail()
					+ "' AND password = '" + company.getPassword() + "';");

			while (rs.next()) {
				CompanyToken c = new CompanyToken();
				
				int coid = rs.getInt("coid");
				c.setCoid(coid);
				String email = rs.getString("email");
				c.setEmail(email);
				String password = rs.getString("password");
				c.setPassword(password);
				String name = rs.getString("name");
				c.setName(name);
				String city = rs.getString("city");
				c.setCity(city);
				String latitude = rs.getString("latitude");
				c.setLatitude(latitude);
				String longitude = rs.getString("longitude");
				c.setLongitude(longitude);
				String phone = rs.getString("phone");
				c.setPhone(phone);
				
				c.setToken(tokenManager.encodeCompanyEmailPassword(email, password));

				return c;
			}
		} catch (Exception e) {
			dbHandler.closeDB();
			return null;
		}
		dbHandler.closeDB();
		return null;
	}

	public int getCompanyIdWithEmailAndPassword(String email, String password) { // returns ID if the company exist.
		dbHandler.connectDB();
		try {
			ResultSet rs = dbHandler.executeGetQuery(
					"SELECT * FROM Company WHERE email='" + email + "' AND password = '" + password + "';");

			while (rs.next()) {
				return rs.getInt("coid");
			}
		} catch (Exception e) {
			dbHandler.closeDB();
			return -1;
		}
		dbHandler.closeDB();
		return -1;
	}

	public Company getCompanyFromToken(String token) {

		if (!tokenChecker(token)) {
			return null;
		}

		String[] ep = tokenManager.decodeCompanyToken(token);
		String email = ep[0];
		String password = ep[1];
		Company company = new Company();
		company.setEmail(email);
		company.setPassword(password);
		

		CompanyToken ctoken = getCompanyWithEmailAndPasswordObject(company);
		company.setCity(ctoken.getCity());
		company.setCoid(ctoken.getCoid());
		company.setEmail(email);
		company.setLatitude(ctoken.getLatitude());
		company.setLongitude(ctoken.getLongitude());
		company.setName(ctoken.getName());
		company.setPassword(password);
		company.setPhone(ctoken.getPhone());

		return company;

	}

	public Boolean isExist(String email, String token) {
		try {
			Company c = getCompanyFromToken(token);

			if (email.equals(c.getEmail())) {
				return true;
			}

		} catch (Exception e) {
			return false;
		}

		return false;

	}

}
