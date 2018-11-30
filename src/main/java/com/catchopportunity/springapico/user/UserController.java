package com.catchopportunity.springapico.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.POST, value = "user/login/{token}")
	public ResponseEntity<?> loginUser(@PathVariable("token") String token) {
		User loginUser = userService.userLogin(token);
		if(loginUser != null) {
			return ResponseEntity.status(HttpStatus.OK).body(loginUser);
		}
		else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
