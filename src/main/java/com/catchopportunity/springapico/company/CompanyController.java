package com.catchopportunity.springapico.company;

import java.util.ArrayList;
import java.util.Base64;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

	// USUAL THINGS

	@Autowired
	private CompanyService companyService;

	@RequestMapping(method = RequestMethod.GET, value = "/company") // GET -> return All Companies.
	public ArrayList<Company> getCompanies() {
		return companyService.getList();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/company/{id}") // GET (id)-> return Company with id. SECURED
	public Company getCompanyByID(@PathVariable("id") int id, @CookieValue("AuthSession") Cookie c) {

		return companyService.getCompanyWithID(id, c.getValue());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/company") // POST -> add Company to the list.
	public void registerCompany(@RequestBody Company company) {
		companyService.addCompany(company);
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
		System.out.println("oldValue" + authSession.getValue());
		boolean response = companyService.updateCompany(companyNew, id, authSession.getValue());
		if (response) {
			String up = companyNew.getEmail() + ":" + companyNew.getPassword();
			String tokenNew = new String(Base64.getEncoder().encode(up.getBytes()));
			authSession.setValue(tokenNew);
			authSession.setPath("/");
			System.out.println("newValue" + authSession.getValue());
			servletRepsonse.addCookie(authSession);
			return ResponseEntity.status(HttpStatus.OK).body(companyNew);
		}
		return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
	}

	// LOGIN LOGOUT

	@RequestMapping(method = RequestMethod.POST, value = "company/login") // POST ( Company) -> gives token if you able
																			// to take it.
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
