package com.catchopportunity.springapico.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

import com.catchopportunity.springapico.database.DatabaseHandler;

@Service
public class CompanyService {

	DatabaseHandler dbHandler = new DatabaseHandler();

	public ArrayList<Company> getList() {
		ArrayList<Company> list = new ArrayList<Company>();
		dbHandler.connectDB();
		try {
			ResultSet rs = dbHandler.executeQuery("SELECT * FROM Company");
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

	public String addCompany(Company company) {
		// TODO Auto-generated method stub
		return company.getName() + "added to List";
	}

}
