package com.catchopportunity.springapico.user;

import java.util.ArrayList;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.catchopportunity.springapico.database.DatabaseHandler;
import com.catchopportunity.springapico.opportunity.Opportunity;
import com.catchopportunity.springapico.opportunity.OpportunityItem;
import com.catchopportunity.springapico.opportunity.OpportunityService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private OpportunityService opportunityService;

	@RequestMapping(method = RequestMethod.POST, value = "/user/login") // takes header as
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		UserToken useer = userService.login(user);
		if (useer != null) {
			return ResponseEntity.status(HttpStatus.OK).body(useer);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user/register") // takes user and save it to DB.
	public ResponseEntity<?> registerUser(@RequestBody User user) {

		Boolean isOk = userService.addUser(user);
		if (isOk) {
			User savedUSer = userService.getUserWithEmailPassword(user.getEmail(), user.getPassword());
			return ResponseEntity.status(HttpStatus.OK).body(savedUSer);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/user") // takes user and save it to DB.
	public ResponseEntity<?> updateUSer(@RequestHeader("Auth") String token, @RequestBody User user) {
		System.out.println(user.getLatitude() + " " + user.getLongitude());
		UserToken ut = userService.updateUser(user, token);
		if (ut != null) {
			return ResponseEntity.status(HttpStatus.OK).body(ut);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/user/opportunityitems") // 
	public ResponseEntity<?> getHotOpportunity(@RequestHeader("Auth") String token) {
		User u = userService.getUserFromToken(token);

		if (u != null) {
			ArrayList<OpportunityItem> theList = userService.getOpportunityListItem(u.getLatitude(), u.getLongitude());

			if (theList == null) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(theList);
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user/reserve/{id}") // takes id and seve this opportunity to
																				// his db
	public ResponseEntity<?> takeOpportunity(@RequestHeader("Auth") String token, @PathVariable("id") int id) {
		boolean isOk = userService.reserveOpportunity(token, id);
		if (isOk) {
			return ResponseEntity.status(HttpStatus.OK).body(opportunityService.getOpportunityWithID(id));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/user/reserve/{id}") // takes id and seve this opportunity
																					// to his db
	public ResponseEntity<?> deleteOpportunity(@RequestHeader("Auth") String token, @PathVariable("id") int id) {
		Opportunity o = opportunityService.getOpportunityWithID(id);
		boolean isDeleted = userService.deleteOneReserve(token, id);
		if (isDeleted) {
			return ResponseEntity.status(HttpStatus.OK).body(o);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

	}

	@RequestMapping(method = RequestMethod.GET, value = "/user/opportunity") // get opportunities reserved
	public ResponseEntity<?> getMyOpportunity(@RequestHeader("Auth") String token) {
		ArrayList<OpportunityItem> mylist = userService.getMyList(token);

		if (mylist != null) {
			return ResponseEntity.status(HttpStatus.OK).body(mylist);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/user/search") // get opportunities reserved
	public ResponseEntity<?> getMyOpportunity(@RequestParam Map<String,String> requestParams , @RequestHeader("Auth") String token) {
		String type=requestParams.get("type");
		String input=requestParams.get("input");
		ArrayList<OpportunityItem> filteredList = userService.searchBy(type , input , token);
		if(filteredList != null) {
			return ResponseEntity.status(HttpStatus.OK).body(filteredList);
		}
		else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
		}
		

	}
	
	
	
	
	
	
	

}
