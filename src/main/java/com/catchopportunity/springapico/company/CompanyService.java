package com.catchopportunity.springapico.company;

import java.util.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

	public ArrayList<Company> getList() {
		Company c = new Company();
		c.setCity("Istanbul");
		c.setCoid(0);
		c.setEmail("berkyavuz@gmail.com");
		c.setPassword("admin");
		c.setLatitude("34");
		c.setLongitude("34");
		c.setPhone("05387049432");
		c.setName("CatchOpportunity");
		ArrayList< Company > theList = new ArrayList<Company>();
		theList.add(c);
		return theList;
	}

	public String addCompany(Company company) {
		// TODO Auto-generated method stub
		return company.getName() + "added to List";
	}

}
