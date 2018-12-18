package com.catchopportunity.springapico.company;

import java.util.ArrayList;
import java.util.Base64;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catchopportunity.springapico.opportunity.Opportunity;
import com.catchopportunity.springapico.opportunity.OpportunityService;

@RestController
public class CompanyController {

	@Autowired
	private CompanyService companyService;


	// USUAL THINGS

	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value = "/company") // GET -> return All Companies.
	public ResponseEntity<?> getCompanies() {
		return ResponseEntity.status(HttpStatus.OK).body(companyService.getListSecure());
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value = "/company/{id}") // GET -> takes header and id returns company.
	public ResponseEntity<?> getCompanyByID(@PathVariable("id") int id) {
		Company com = companyService.getCompanyWithID(id);
		if (com.getCoid() == 0)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(com);
		else
			return ResponseEntity.status(HttpStatus.OK).body(com);
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value = "/company/me") // GET -> takes header and returns the company.
	public ResponseEntity<?> getCompanyInfos(@RequestHeader("AuthSession") String token) {

		Company com = companyService.getCompanyFromToken(token);
		if (com != null) {
			return ResponseEntity.status(HttpStatus.OK).body(com);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}

	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, value = "/company") // adds company to list.
	public ResponseEntity<?> registerCompany(@RequestBody Company company) {
		boolean isSaved = companyService.addCompany(company);
		if (isSaved)
			return ResponseEntity.status(HttpStatus.CREATED).build();
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.DELETE, value = "/company/{id}") // DELETE (id) ->takes header token and
																			// delete the company with id

	public ResponseEntity<?> deleteCompany(@PathVariable("id") int id, @RequestHeader("AuthSession") String token) {

		boolean isDeleted = companyService.deleteCompany(id, token);
		if (isDeleted) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.PUT, value = "/company/{id}") // takes new company model and token header for
																			// update all the datas of this id's company
																			//
	public ResponseEntity<?> updateCompany(@RequestBody Company companyNew, @PathVariable("id") int id,
			@RequestHeader("AuthSession") String token) {

		boolean response = companyService.updateCompany(companyNew, id, token);
		if (response) {
			Company c = companyService.getCompanyWithIDNotSecure(id);
			String newToken = companyService.tokenCreator(c.getEmail(), c.getPassword());
			CompanyToken tc = new CompanyToken(c , newToken);
			return ResponseEntity.status(HttpStatus.OK).body(tc);
		}
		return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
	}

	// LOGIN LOGOUT

	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, value = "/company/login") // takes email and password returns company													// with token
	public ResponseEntity<CompanyToken> getToken(@RequestBody Company company) {
		
		CompanyToken c = companyService.getCompanyWithEmailAndPasswordObject(company);
		if (c != null) {
			return ResponseEntity.status(HttpStatus.OK).body(c);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, value = "/company/dashboardcheck") //takes email and token and returns if the user exist
	public ResponseEntity<?> dashboardCheck(@RequestBody CompanyToken company) {
		String token = company.getToken();
		String email = company.getEmail();
		Boolean isExist = companyService.isExist(email, token);

		if (isExist) {
			Company c = companyService.getCompanyFromToken(company.getToken());
			if (c != null) {
				return ResponseEntity.status(HttpStatus.OK).body(c);
			}
			else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

}
