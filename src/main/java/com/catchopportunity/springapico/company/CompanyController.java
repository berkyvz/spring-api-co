package com.catchopportunity.springapico.company;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@RequestMapping(method = RequestMethod.GET, value = "/company") //GET -> return All Companies.
	public ArrayList<Company> getCompanies() {
		return companyService.getList();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/company/{id}") //GET (id)->  return Company with id.
	public Company getCompanyByID(@PathVariable("id") int id) {
		id = id - 1 ;
		return companyService.getList().get(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/company") // POST -> add Company to the list.
	public String registerCompany(@RequestBody Company company) {
		return companyService.addCompany(company);
	}
	

}
