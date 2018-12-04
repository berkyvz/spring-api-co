package com.catchopportunity.springapico.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/user/login") // takes header as 
	public ResponseEntity<?> loginUser(@RequestHeader("Auth") String token) {
		User loginUser = userService.userLogin(token);
		if(loginUser != null) {
			return ResponseEntity.status(HttpStatus.OK).body(loginUser);
		}
		else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/user/register") // takes user and save it to DB.
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		
		Boolean isOk = userService.addUser(user);
		if(isOk) {
			User savedUSer = userService.getUserWithEmailPassword(user.getEmail(), user.getPassword());
			return ResponseEntity.status(HttpStatus.OK).body(savedUSer);
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
	}
	
	
	
	
	
	
	
	
}
