package com.catchopportunity.springapico.opportunity;

import org.springframework.web.bind.annotation.RestController;

import com.catchopportunity.springapico.company.Company;
import com.catchopportunity.springapico.company.CompanyService;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class OpportunityController {

	@Autowired
	private OpportunityService opportunityService;
	@Autowired
	private CompanyService companyService;

	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value = "opportunity") // Get all opportunities.
	public ResponseEntity<?> getAllOpportunities() {
		return ResponseEntity.status(HttpStatus.OK).body(opportunityService.getAllOpportunities());
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value = "opportunity/{id}") // get opportunity with id.
	public ResponseEntity<?> getOpportunityWithID(@PathVariable("id") int id) {
		Opportunity o = opportunityService.getOpportunityWithID(id);
		if (o.getOid() == 0)
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		else
			return ResponseEntity.status(HttpStatus.OK).body(o);
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value = "company/opportunity") // returning cookie's opportunity;
	public ResponseEntity<?> getCompanyOpportunities(@RequestHeader("AuthSession") String token) {

		Company company = companyService.getCompanyFromToken(token);
		if (company != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(opportunityService.getOpportunitiesOwnedByCompany(company.getCoid()));

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		}

	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.POST, value = "/opportunity") // adding opportunity . Cookie needed.
	public ResponseEntity<?> generateOpportunity(@RequestBody Opportunity opportunity,
			@RequestHeader("AuthSession") String token) {

		Company company = companyService.getCompanyFromToken(token);
		if (company == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
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

	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, value = "company/opportunity/{index}") // returning Companies'
																						// opportunity by index
	public ResponseEntity<?> getCompanyOpportunitiesbyID(@PathVariable("index") int index,
			@RequestHeader("AuthSession") String token) {
		index = index - 1;

		Company company = companyService.getCompanyFromToken(token);
		if (company == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK)
					.body(opportunityService.getOpportunitiesOwnedByCompany(company.getCoid()).get(index));
		}

	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.DELETE, value = "company/opportunity/{index}")
	public ResponseEntity<?> deleteOpportunity(@RequestHeader("AuthSession") String token,
			@PathVariable("index") int index) {

		Company com = companyService.getCompanyFromToken(token);
		if (com == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
			boolean isDeleted = opportunityService.deleteOpportunity(index, com.getCoid());
			if (isDeleted) {
				return ResponseEntity.status(HttpStatus.OK).build();
			} else {
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
			}
		}
	}
	
	
	

}
