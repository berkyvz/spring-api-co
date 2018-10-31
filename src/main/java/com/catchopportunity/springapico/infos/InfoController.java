package com.catchopportunity.springapico.infos;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

	
	
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ArrayList<Info> getMethodInfos() {
		ArrayList<Info> methodList = new ArrayList<Info>();

		// Company Controller
		methodList.add(new Info("POST", "/company/init", "It initiliaze the cookie . It is needed for every person who visit the web site."));
		methodList.add(new Info("GET", "/company", "Just get the all Company List."));
		methodList.add(new Info("GET", "/company/id", "get a Company with id"));
		methodList.add(new Info("GET", "/company/me", "Get all info about the company which is already logged in."));
		methodList.add(new Info("POST", "/company", "add a company to a List. It takes a Company JSON object."));
		methodList.add(new Info("DELETE", "/company/{id}" ,"delete the copmany with id"));
		methodList.add(new Info("PUT", "/company/id", "update the company with id (Cannot change id and email)"));
		methodList.add(new Info("POST", "/company/login", "Takes Company email password and returns token as Cookie"));
		methodList.add(new Info("POST", "/company/logout", "Takes Cookie and delete the token from it"));
			
		//Opportunity Controller
		methodList.add(new Info("GET", "/opportunity", "Get all opportunities that can be listed."));
		methodList.add(new Info("GET", "/opportunity/{id}", "Get opportunity with id"));
		methodList.add(new Info("GET", "/company/opportunity", "It needs cookie to understant who you are. returns opportunities that the company owned."));
		methodList.add(new Info("GET", "/company/opportunity/{index}", "It takes an index and returns the index of the opportunities of the company who logged in"));
		methodList.add(new Info("POST", "/opportunity", "Adding a opportunity. It needs Cookie."));
		methodList.add(new Info("DELETE", "/company/opportunity/{index}", "Deletes the opportunity from company. It needs cookie also ou must know the index of the Opportunity  [NOT ID]"));
		
		
		return methodList;
	}
}
