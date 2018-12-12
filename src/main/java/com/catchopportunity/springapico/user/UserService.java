package com.catchopportunity.springapico.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.stereotype.Service;

import com.catchopportunity.springapico.company.Company;
import com.catchopportunity.springapico.database.DatabaseHandler;
import com.catchopportunity.springapico.helper.DistanceCalculator;
import com.catchopportunity.springapico.helper.TokenManager;
import com.catchopportunity.springapico.opportunity.Opportunity;
import com.catchopportunity.springapico.opportunity.OpportunityItem;
import com.catchopportunity.springapico.opportunity.OpportunityService;
import com.mysql.fabric.xmlrpc.base.Array;

@Service
public class UserService {

	DatabaseHandler dbHandler = new DatabaseHandler();
	DistanceCalculator distanceCalculator = new DistanceCalculator();
	TokenManager tokenManager = new TokenManager();
	OpportunityService opportunityService = new OpportunityService();

	public User getUserFromToken(String token) {
		dbHandler.connectDB();
		String[] ep = tokenManager.decodeUserToken(token);
		String email = ep[0];
		String password = ep[1];
		try {
			ResultSet rs = dbHandler.executeGetQuery(
					"SELECT * FROM User WHERE email='" + email + "'  AND password='" + password + "';  ");
			while (rs.next()) {
				int uid = rs.getInt("uid");
				String email1 = rs.getString("email");
				String password1 = rs.getString("password");
				String latitude = rs.getString("latitude");
				String longitude = rs.getString("longitude");

				User u = new User(uid, email1, password1, latitude, longitude);
				dbHandler.closeDB();
				return u;
			}

		} catch (Exception e) {
			dbHandler.closeDB();
			return null;
		}
		dbHandler.closeDB();
		return null;

	}

	public boolean addUser(User user) {
		dbHandler.connectDB();

		if (user.getLatitude().length() > 9) {
			user.setLatitude(user.getLatitude().substring(0, 10));
		}
		if (user.getLongitude().length() > 9) {
			user.setLongitude(user.getLongitude().substring(0, 10));
		}
		if (user.getEmail() == null && user.getPassword() == null && user.getLatitude() == null
				&& user.getLongitude() == null) {
			dbHandler.closeDB();
			return false;
		}

		try {
			dbHandler.connectDB();
			dbHandler.executeSetQuery("INSERT INTO User(email,password,latitude,longitude) " + "VALUES (   '"
					+ user.getEmail() + "' ,  '" + user.getPassword() + "' , '" + user.getLatitude() + "' ,  '"
					+ user.getLongitude() + "'  );");
			dbHandler.closeDB();
			return true;
		} catch (Exception e) {
			dbHandler.closeDB();
			return false;
		}

	}

	public ArrayList<User> getUserList() {
		dbHandler.connectDB();
		ArrayList<User> theUserList = new ArrayList<User>();
		try {
			ResultSet rs = dbHandler.executeGetQuery("SELECT * FROM User;");
			while (rs.next()) {
				int uid = rs.getInt("uid");
				String email1 = rs.getString("email");
				String password1 = rs.getString("password");
				String latitude = rs.getString("latitude");
				String longitude = rs.getString("longitude");

				User u = new User(uid, email1, password1, latitude, longitude);
				theUserList.add(u);
			}

		} catch (Exception e) {
			dbHandler.closeDB();
			return null;
		}
		dbHandler.closeDB();
		return theUserList;

	}

	private boolean tokenChecker(String token) {
		dbHandler.connectDB();
		ArrayList<User> list = getUserList();

		for (int i = 0; i < list.size(); i++) {
			if (token.equals(tokenManager.encodeUserEmailPassword(list.get(i).getEmail(), list.get(i).getPassword()))) {
				dbHandler.closeDB();
				return true;
			}
		}
		dbHandler.closeDB();
		return false;

	}

	public UserToken login(User user) {
		dbHandler.connectDB();

		try {
			ResultSet rs = dbHandler.executeGetQuery("SELECT * FROM User WHERE email= '" + user.getEmail()
					+ "' AND password= '" + user.getPassword() + "';");
			while (rs.next()) {
				int uid = rs.getInt("uid");
				String email = rs.getString("email");
				String password = rs.getString("password");
				String latitude = rs.getString("latitude");
				String longitude = rs.getString("longitude");
				String token = tokenManager.encodeUserEmailPassword(user.getEmail(), user.getPassword());

				UserToken userto = new UserToken(uid, email, password, latitude, longitude, token);
				dbHandler.closeDB();
				return userto;
			}
		} catch (Exception e) {
			dbHandler.closeDB();
			return null;
		}
		dbHandler.closeDB();
		return null;
	}

	public User getUserWithEmailPassword(String email, String password) {
		dbHandler.connectDB();
		ResultSet rs = dbHandler
				.executeGetQuery("SELECT * FROM User WHERE email='" + email + "'  AND password='" + password + "'  ;");
		try {
			while (rs.next()) {

				int uid = rs.getInt("uid");
				String email1 = rs.getString("email");
				String password1 = rs.getString("password");
				String latitude = rs.getString("latitude");
				String longitude = rs.getString("longitude");

				User u = new User(uid, email1, password1, latitude, longitude);
				dbHandler.closeDB();
				return u;
			}
		} catch (SQLException e) {
			dbHandler.closeDB();
			return null;
		}
		dbHandler.closeDB();
		return null;
	}

	public ArrayList<OpportunityItem> getOpportunityListItem(String latitudee, String longitudee) {
		dbHandler.connectDB();
		double latitude = Double.parseDouble(latitudee);
		double longitude = Double.parseDouble(longitudee);

		ArrayList<Opportunity> opportunityList = opportunityService.getAllOpportunities();
		ArrayList<OpportunityItem> OpportunityListItems = new ArrayList<OpportunityItem>();

		try {
			for (int i = 0; i < opportunityList.size(); i++) {
				double distance = distanceCalculator.calculateDistance(latitude, longitude,
						Double.parseDouble(opportunityList.get(i).getLatitude()),
						Double.parseDouble(opportunityList.get(i).getLongitude()));
				Company ownerName = opportunityService.getCompanyIdFromOpportunityId(opportunityList.get(i).getOid());
				OpportunityListItems
						.add(new OpportunityItem(opportunityList.get(i), distance + "", ownerName.getName()));
			}

		} catch (Exception e) {

			dbHandler.closeDB();
			return null;
		}
		dbHandler.closeDB();
		return OpportunityListItems;
	}

	public boolean reserveOpportunity(String token, int id) {
		dbHandler.connectDB();
		if (!tokenChecker(token)) {
			dbHandler.closeDB();
			return false;
		}
		User user = getUserFromToken(token);
		Opportunity opportunity = opportunityService.getOpportunityWithID(id);
		dbHandler.connectDB();

		if (opportunity.getCount().equals("0")) {
			dbHandler.closeDB();
			return false;
		}

		int count = Integer.parseInt(opportunity.getCount());

		try {
			dbHandler.executeSetQuery(
					"INSERT INTO Reserve (uid , oid) VALUES( " + user.getUid() + " , " + opportunity.getOid() + " );");
			dbHandler.executeSetQuery(
					"UPDATE Opportunity SET count = '" + (count - 1) + "' WHERE oid= " + opportunity.getOid() + ";");
		} catch (Exception e) {
			dbHandler.closeDB();
			return false;
		}

		dbHandler.closeDB();
		return true;

	}

	public UserToken updateUser(User user, String token) {
		dbHandler.connectDB();
		if (!tokenChecker(token)) {
			dbHandler.closeDB();
			return null;
		}

		User oldUser = getUserFromToken(token);

		if (user.getPassword() == null || user.getPassword().length() < 5) {
			dbHandler.closeDB();
			return null;
		}
		if (user.getLatitude() == (null)) {
			user.setLatitude(oldUser.getLatitude());
		}
		if (user.getLongitude() == (null)) {
			user.setLongitude(user.getLongitude());
		}

		if (!user.getEmail().equals(oldUser.getEmail())) {
			dbHandler.closeDB();
			return null;
		}

		if (user.getLatitude().length() > 9) {
			user.setLatitude(user.getLatitude().substring(0, 10));
		}
		if (user.getLongitude().length() > 9) {
			user.setLongitude(user.getLongitude().substring(0, 10));
		}
		if (user.getEmail() == null && user.getPassword() == null && user.getLatitude() == null
				&& user.getLongitude() == null) {
			dbHandler.closeDB();
			return null;
		}
		String tokenNew = "";
		dbHandler.connectDB();
		try {
			dbHandler.executeSetQuery("UPDATE User SET password = '" + user.getPassword() + "' , " + "latitude = '"
					+ user.getLatitude() + "' , " + "longitude = '" + user.getLongitude() + "' WHERE uid = "
					+ oldUser.getUid() + ";");

			tokenNew = tokenManager.encodeUserEmailPassword(user.getEmail(), user.getPassword());

		} catch (Exception e) {
			dbHandler.closeDB();
			return null;
		}
		user.setUid(oldUser.getUid());
		UserToken tu = new UserToken(user, tokenNew);
		dbHandler.closeDB();
		return tu;

	}

	public Boolean deleteOneReserve(String token, int id) {
		dbHandler.connectDB();

		if (!tokenChecker(token)) {
			dbHandler.closeDB();
			return false;
		}

		User user = getUserFromToken(token);

		int deleting_rid = -1;
		try {
			dbHandler.connectDB();
			dbHandler.connectDB();
			ResultSet rs = dbHandler
					.executeGetQuery("SELECT * FROM Reserve WHERE oid=" + id + " AND uid=" + user.getUid() + "; ");

			while (rs.next()) {
				deleting_rid = rs.getInt("rid");
			}

		} catch (Exception e) {
			dbHandler.closeDB();
			return false;
		}

		int oldcount = Integer.parseInt(opportunityService.getOpportunityWithID(id).getCount());
		try {
			dbHandler.connectDB();
			dbHandler.executeSetQuery("DELETE FROM Reserve WHERE rid= " + deleting_rid + ";");
			dbHandler.executeSetQuery("UPDATE Opportunity SET count='" + (oldcount + 1) + "' WHERE oid= " + id + ";");
			dbHandler.closeDB();
		} catch (Exception e) {
			dbHandler.closeDB();
			return false;
		}

		return true;

	}

	public ArrayList<OpportunityItem> getMyList(String token) {
		dbHandler.connectDB();

		if (!tokenChecker(token)) {
			dbHandler.closeDB();
			return null;
		}

		User user = getUserFromToken(token);
		ArrayList<OpportunityItem> allItems = getOpportunityListItem(user.getLatitude(), user.getLongitude());

		ArrayList<Integer> myOpIds = new ArrayList<Integer>();
		try {
			dbHandler.connectDB();
			ResultSet rs = dbHandler.executeGetQuery("SELECT * FROM Reserve WHERE uid=" + user.getUid() + ";");

			while (rs.next()) {
				myOpIds.add(rs.getInt("oid"));
			}
		} catch (Exception e) {
			dbHandler.closeDB();
			System.out.println(e.getMessage());
			return null;
		}
		ArrayList<OpportunityItem> myList = new ArrayList<OpportunityItem>();
		for (int i = 0; i < allItems.size(); i++) {
			OpportunityItem selectedOItem = allItems.get(i);
			for (int j = 0; j < myOpIds.size(); j++) {
				if (myOpIds.get(j) == selectedOItem.getOpportunity().getOid()) {
					// add this to list
					myList.add(selectedOItem);
				}
			}
		}

		dbHandler.closeDB();
		return myList;
	}

	public ArrayList<OpportunityItem> searchBy(String type, String input, String token) {

		if (!tokenChecker(token)) {
			return null;
		}

		User user = getUserFromToken(token);

		if (type.equals("Distance")) {
			int distanceInM = Integer.parseInt(input);
			ArrayList<OpportunityItem> list = getItemMaxDistance(user, distanceInM);
			return list;
		}
		if (type.equals("Description")) {
			String description = input;
			ArrayList<OpportunityItem> list = getItemFromDescription(user, description);
			return list;
		}
		if (type.equals("City")) {
			String city = input;
			ArrayList<OpportunityItem> list = getItemFromCity(user, city);
			return list;
		}
		if (type.equals("Id")) {
			int oid = Integer.parseInt(input);
			ArrayList<OpportunityItem> list = getOpportunityFromID(user, oid);
			return list;
		}

		return null;
	}

	private ArrayList<OpportunityItem> getOpportunityFromID(User user, int oid) {
		ArrayList<OpportunityItem> allItems = getOpportunityListItem(user.getLatitude(), user.getLongitude());
		ArrayList<OpportunityItem> returnn = new ArrayList<OpportunityItem>();
		for (int i = 0; i < allItems.size(); i++) {
			if (allItems.get(i).getOpportunity().getOid() == oid) {
				returnn.add(allItems.get(i));
			}
		}
		return returnn;
	}

	private ArrayList<OpportunityItem> getItemMaxDistance(User user, int distanceInM) {
		ArrayList<OpportunityItem> allItems = getOpportunityListItem(user.getLatitude(), user.getLongitude());
		ArrayList<OpportunityItem> returnn = new ArrayList<OpportunityItem>();
		for (int i = 0; i < allItems.size(); i++) {
			int distanceActual = (int) Double.parseDouble(allItems.get(i).getDistance());
			if (distanceActual < distanceInM) {
				returnn.add(allItems.get(i));
			}
		}
		return returnn;
	}

	private ArrayList<OpportunityItem> getItemFromCity(User user, String city) {
		ArrayList<OpportunityItem> allItems = getOpportunityListItem(user.getLatitude(), user.getLongitude());
		ArrayList<OpportunityItem> returnn = new ArrayList<OpportunityItem>();
		for (int i = 0; i < allItems.size(); i++) {
			if (allItems.get(i).getOpportunity().getCity().equals(city)) {
				returnn.add(allItems.get(i));
			}
		}
		return returnn;
	}

	private ArrayList<OpportunityItem> getItemFromDescription(User user, String description) {
		ArrayList<OpportunityItem> allItems = getOpportunityListItem(user.getLatitude(), user.getLongitude());
		ArrayList<OpportunityItem> returnn = new ArrayList<OpportunityItem>();
		for (int i = 0; i < allItems.size(); i++) {
			String desc1 = allItems.get(i).getOpportunity().getDesc1();
			String desc2 = allItems.get(i).getOpportunity().getDesc2();
			String desc3 = allItems.get(i).getOpportunity().getDesc3();

			if (desc1.contains(description) || desc2.contains(description) || desc3.contains(description)) {
				returnn.add(allItems.get(i));
			}

		}
		return returnn;
	}

	public Opportunity reservetionDone(String token, int oid) {
		if (!tokenChecker(token)) {
			return null;
		}

		User user = getUserFromToken(token);
		Opportunity o = opportunityService.getOpportunityWithID(oid);
		boolean isok = false;

		try {
			int reservationID = -1;
			dbHandler.connectDB();
			ResultSet rs = dbHandler
					.executeGetQuery("SELECT * FROM Reserve WHERE oid= " + oid + " AND uid=" + user.getUid() + ";");
			while (rs.next()) {
				reservationID = rs.getInt("rid");
			}
			dbHandler.executeSetQuery("DELETE FROM Reserve WHERE rid=" + reservationID + " ;");
			isok = true;
			if(reservationID == -1) {
				dbHandler.closeDB();
				return null;
			}
		} catch (Exception e) {
			dbHandler.closeDB();
			return null;
		}

		if (isok) {
			return o;

		} else {
			return null;
		}
	}

}
