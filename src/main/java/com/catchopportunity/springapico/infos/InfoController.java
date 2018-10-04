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
		methodList.add(new Info("GET", "/company", "Just get the all Company List."));
		methodList.add(new Info("GET", "/company/id", "get a Company with id"));
		methodList.add(new Info("POST", "/company", "add a company to a List. It takes a Company JSON object."));

		return methodList;
	}
}
