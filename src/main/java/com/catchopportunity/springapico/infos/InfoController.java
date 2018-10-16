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
		methodList.add(new Info("IMPORTANT ABOUT COOKIE", "/company/login", "You must send a POST request to "
				+ "login a email and password as a initliaze and use the other rest APIs.(SEND FALSE EMAIL AND PASSWORD IT DOESNT MATTER.)"));
		methodList.add(new Info("GET", "/company", "Just get the all Company List."));
		methodList.add(new Info("GET", "/company/id", "get a Company with id"));
		methodList.add(new Info("POST", "/company", "add a company to a List. It takes a Company JSON object."));
		methodList.add(new Info("DELETE", "/company/{id}" ,"delete the copmany with id"));
		methodList.add(new Info("PUT", "/company/id", "update the company with id (Cannot change id and email)"));
		methodList.add(new Info("POST", "/company/login", "Takes Company email password and returns token as Cookie"));
		methodList.add(new Info("POST", "/company/logout", "Takes Cookie and delete the token from it"));
		methodList.add(new Info("GET", "/opportunity", "Get all opportunities that can be listed."));
		methodList.add(new Info("GET", "/opportunity/{id}", "Get opportunity with id"));

		return methodList;
	}
}
