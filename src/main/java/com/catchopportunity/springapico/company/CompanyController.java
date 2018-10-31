package com.catchopportunity.springapico.company;

import java.util.ArrayList;
import java.util.Base64;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.catchopportunity.springapico.opportunity.Opportunity;
import com.catchopportunity.springapico.opportunity.OpportunityService;

@RestController
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private OpportunityService opportunityService;

	// USUAL THINGS

	@RequestMapping(method = RequestMethod.GET, value = "/company") // GET -> return All Companies.
	public ResponseEntity<?> getCompanies() {
		return ResponseEntity.status(HttpStatus.OK).body(companyService.getList());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/company/{id}") // GET (id)-> return Company with id. SECURED
	public ResponseEntity<?> getCompanyByID(@PathVariable("id") int id, @CookieValue("AuthSession") Cookie c) {
		Company com = companyService.getCompanyWithID(id, c.getValue());
		if (com.getCoid() == 0)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(com);
		else
			return ResponseEntity.status(HttpStatus.OK).body(com);
	}

	@RequestMapping(method = RequestMethod.GET, value = "company/me")
	public ResponseEntity<?> getCompanyInfos(@CookieValue("AuthSession") Cookie authSession) {
		if (authSession.getValue().equals("Logged-Out")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
			Company com = companyService.getCompanyFromToken(authSession.getValue());
			if (com != null) {
				return ResponseEntity.status(HttpStatus.OK).body(com);
			} else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/company") // POST -> add Company to the list.
	public ResponseEntity<?> registerCompany(@RequestBody Company company) {
		boolean isSaved = companyService.addCompany(company);
		if (isSaved)
			return ResponseEntity.status(HttpStatus.CREATED).build();
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/company/{id}") // DELETE (id) -> delete the company with id
																			// SECURED
	public ResponseEntity<?> deleteCompany(@PathVariable("id") int id, @CookieValue("AuthSession") Cookie authSession,
			HttpServletResponse response) {
		boolean isDeleted = companyService.deleteCompany(id, authSession.getValue());
		if (isDeleted) {
			deleteToken(authSession, response);
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/company/{id}") // PUT (id) -> update the company with id
																			// (Cannot change id and email) SECURED.
	public ResponseEntity<?> updateCompany(@RequestBody Company companyNew, @PathVariable("id") int id,
			@CookieValue("AuthSession") Cookie authSession, HttpServletResponse servletRepsonse) {

		boolean response = companyService.updateCompany(companyNew, id, authSession.getValue());
		if (response) {
			String up = companyNew.getEmail() + ":" + companyNew.getPassword();
			String tokenNew = new String(Base64.getEncoder().encode(up.getBytes()));
			authSession.setValue(tokenNew);
			authSession.setPath("/");
			servletRepsonse.addCookie(authSession);
			return ResponseEntity.status(HttpStatus.OK).body(companyNew);
		}
		return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
	}

	// LOGIN LOGOUT

	@RequestMapping(method = RequestMethod.POST, value = "company/init")
	public ResponseEntity<?> initiliazeToken(HttpServletResponse response) {
		Cookie authSession = new Cookie("AuthSession", "");
		authSession.setValue("Logged-Out");
		authSession.setPath("/");
		response.addCookie(authSession);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@RequestMapping(method = RequestMethod.POST, value = "company/login") // POST ( Company) -> gives token if you able
	public ResponseEntity<Company> getToken(@RequestBody Company company, HttpServletResponse response) {
		Company c = companyService.getCompanyWithEmailAndPasswordObject(company);
		if (c != null) {
			String empa = c.getEmail() + ":" + c.getPassword();
			String token = new String(Base64.getEncoder().encode(empa.getBytes()));
			Cookie authSession = new Cookie("AuthSession", "");
			authSession.setValue(token);
			authSession.setPath("/");
			response.addCookie(authSession);
			return ResponseEntity.status(HttpStatus.OK).body(c);
		} else {
			Cookie authSession = new Cookie("AuthSession", "");
			authSession.setValue("Logged-Out");
			authSession.setPath("/");
			response.addCookie(authSession);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "company/logout") // POST -> Deletes token
	public ResponseEntity<?> deleteToken(@CookieValue("AuthSession") Cookie authSession, HttpServletResponse response) {
		authSession.setValue("Logged-Out");
		authSession.setPath("/");
		response.addCookie(authSession);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
