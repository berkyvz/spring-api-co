package com.catchopportunity.springapico.opportunity;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class OpportunityController {

	@Autowired
	private OpportunityService opportunityService;

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
	
	

}
