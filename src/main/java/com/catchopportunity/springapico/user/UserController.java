package com.catchopportunity.springapico.user;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	
	@RequestMapping(method = RequestMethod.GET, value = "/user")
	public ArrayList<User> hi() {
		ArrayList<User> list = new ArrayList<User>();
		list.add(new User(1, "berkyavuz@gmail.com", "1234", "34.2131", "21.2431"));
		return list;
	}
}
