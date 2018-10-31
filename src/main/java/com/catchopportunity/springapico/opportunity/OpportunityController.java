package com.catchopportunity.springapico.opportunity;

import org.springframework.web.bind.annotation.RestController;

import com.catchopportunity.springapico.company.Company;
import com.catchopportunity.springapico.company.CompanyService;

import java.util.ArrayList;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class OpportunityController {

	@Autowired
	private OpportunityService opportunityService;
	@Autowired
	private CompanyService companyService;

	@RequestMapping(method = RequestMethod.GET, value = "opportunity") // Get all opportunities.
	public ResponseEntity<?> getAllOpportunities() {
		return ResponseEntity.status(HttpStatus.OK).body(opportunityService.getAllOpportunities());
	}

	@RequestMapping(method = RequestMethod.GET, value = "opportunity/{id}") // get opportunity with id.
	public ResponseEntity<?> getOpportunityWithID(@PathVariable("id") int id) {
		Opportunity o = opportunityService.getOpportunityWithID(id);
		if (o.getOid() == 0)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		else
			return ResponseEntity.status(HttpStatus.OK).body(o);
	}

	@RequestMapping(method = RequestMethod.GET, value = "company/opportunity") // returning cookie's opportunity;
	public ResponseEntity<?> getCompanyOpportunities(@CookieValue("AuthSession") Cookie authSession) {
		if (authSession.getValue().equals("Logged-Out")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<Opportunity>());
		} else {
			Company company = companyService.getCompanyFromToken(authSession.getValue());
			return ResponseEntity.status(HttpStatus.OK)
					.body(opportunityService.getOpportunitiesOwnedByCompany(company.getCoid()));
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/opportunity") // adding opportunity . Cookie needed.
	public ResponseEntity<?> generateOpportunity(@RequestBody Opportunity opportunity,
			@CookieValue("AuthSession") Cookie authSession) {
		if (authSession.getValue().equals("Logged-Out")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
			Company company = companyService.getCompanyFromToken(authSession.getValue());
			int coid = company.getCoid();
			boolean isAdded = opportunityService.addOpportunity(opportunity, coid);
			if (isAdded) {
				Opportunity newAdded = opportunityService.getOpportunitiesOwnedByCompany(coid)
						.get(opportunityService.getOpportunitiesOwnedByCompany(coid).size() - 1);
				return ResponseEntity.status(HttpStatus.OK).body(newAdded);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
			}
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "company/opportunity/{index}") // returning Companies'
																						// opportunity by index
	public ResponseEntity<?> getCompanyOpportunitiesbyID(@PathVariable("index") int index,
			@CookieValue("AuthSession") Cookie authSession) {
		index = index - 1;
		if (authSession.getValue().equals("Logged-Out")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<Opportunity>());
		} else {
			Company company = companyService.getCompanyFromToken(authSession.getValue());
			return ResponseEntity.status(HttpStatus.OK)
					.body(opportunityService.getOpportunitiesOwnedByCompany(company.getCoid()).get(index));
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "company/opportunity/{index}")
	public ResponseEntity<?> deleteOpportunity(@CookieValue("AuthSession") Cookie authSession,
			@PathVariable("index") int index) {
		if (authSession.getValue().equals("Logged-Out")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
			Company com = companyService.getCompanyFromToken(authSession.getValue());
			boolean isDeleted = opportunityService.deleteOpportunity(index, com.getCoid());
			if (isDeleted) {
				return ResponseEntity.status(HttpStatus.OK).build();
			} else {
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
			}
		}
	}

}
