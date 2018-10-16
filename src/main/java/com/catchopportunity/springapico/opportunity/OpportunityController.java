package com.catchopportunity.springapico.opportunity;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
public class OpportunityController {
	
	@Autowired
	private OpportunityService opportunityService;

	@RequestMapping( method=RequestMethod.GET , value="opportunity")
	public ArrayList<Opportunity> getAllOpportunities() {
		return opportunityService.getAllOpportunities();
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "opportunity/{id}")
	public Opportunity getOpportunityWithID(@PathVariable("id") int id) {
		return opportunityService.getOpportunityWithID(id);
	}
	
}
