package com.catchopportunity.springapico.opportunity;

public class OpportunityItem {
	
	Opportunity opportunity;
    private String distance;
    private String name;
    
    public OpportunityItem() {
    	
    }
    
	public OpportunityItem(Opportunity opportunity, String distance, String name) {
		super();
		this.opportunity = opportunity;
		this.distance = distance;
		this.name = name;
	}
	public Opportunity getOpportunity() {
		return opportunity;
	}
	public void setOpportunity(Opportunity opportunity) {
		this.opportunity = opportunity;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    

}
