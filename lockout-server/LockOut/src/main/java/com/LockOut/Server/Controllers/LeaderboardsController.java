package com.LockOut.Server.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.LockOut.Server.Models.User;
import com.LockOut.Server.Repositories.UserRepository;
import com.LockOut.Server.ReturnObjects.UserPoints;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/leaderboard")
public class LeaderboardsController {

	@Autowired
	private UserRepository userRepository;

	/**
	 * 
	 * @param mode
	 * 	1 for season points and 0 for total points
	 * @return
	 * 	the list of descending users for leader board purposes
	 */
	@GetMapping(path = "/getFullLeaderboard") // Map ONLY GET Requests
	public @ResponseBody List<UserPoints> fullLeaderboards(@RequestParam int mode) {
		List<User> fullList;
		if (mode == 1) {// sesason mode
			fullList = userRepository.findAllByOrderBySeasonPointsDesc();
		} else {// total points mode
			fullList = userRepository.findAllByOrderByPointsTotalDesc();
		}
		List<UserPoints> SecureList = new ArrayList<UserPoints>();
		UserPoints userToAdd;
		if (fullList.isEmpty()) {
			return null;
		} else {

			for (User user : fullList) {
				userToAdd = new UserPoints();
				userToAdd.SetupFromUser(user);
				SecureList.add(userToAdd);
			}
			return SecureList;
		}

	}

	/**
	 * Get leader boards with only friends of a user
	 * @param userName
	 * 	get friends of this user
	 * @param mode
	 * 	1 filters by seasonal points and 0 filters by total points
	 * @return
	 * 	returns list of descending users
	 */
	@GetMapping(path = "/getLeaderboardFilteredFriends") // Map ONLY GET Requests
	public @ResponseBody List<UserPoints> friendLeaderboards(@RequestParam String userName, @RequestParam int mode) {
		User user = userRepository.findByUserName(userName);
		List<User> fullList;
		if (mode == 1) {// season mode
			fullList = userRepository.findAllByOrderBySeasonPointsDesc();
		} else {// total points mode
			fullList = userRepository.findAllByOrderByPointsTotalDesc();
		}
		List<UserPoints> friends = new ArrayList<UserPoints>();
		UserPoints userToAdd;

		for (User userOnList : fullList) {// go through list
			if (user.getFriendsList().contains(userOnList.getUserName())) {// if a friend add to list
				userToAdd = new UserPoints();// moves to userPoints containing only the necessary data
				userToAdd.SetupFromUser(userOnList);
				friends.add(userToAdd);
			}
		}
		if (friends.isEmpty()) {
			return null;
		}

		return friends;
	}

	/**
	 * leader board with group members only
	 * @param groupName
	 * 	name of the group to filter by
	 * @param mode
	 * 	mode is 1 for seasonal and 0 for total
	 * @return
	 * 	list of users in the group descending
	 */
	@GetMapping(path = "/getLeaderboardFilteredGroup") // Map ONLY GET Requests
	public @ResponseBody List<UserPoints> groupLeaderboards(@RequestParam String groupName, int mode) {
		List<User> fullList;
		if (mode == 1) {// season mode
			fullList = userRepository.findAllByOrderBySeasonPointsDesc();
		} else {// total mode
			fullList = userRepository.findAllByOrderByPointsTotalDesc();
		}
		List<UserPoints> group = new ArrayList<UserPoints>();
		UserPoints userToAdd;

		for (User userOnList : fullList) {// go through list
			if (userOnList.getGroupsList().contains(groupName)) {// if a friend add to list
				userToAdd = new UserPoints();// moves to userPoints containing only the necessary data
				userToAdd.SetupFromUser(userOnList);
				group.add(userToAdd);
			}
		}
		if (group.isEmpty()) {
			return null;
		}

		return group;
	}

	/**
	 * gives a certain length leader board
	 * @param max
	 * 	number of people you want
	 * @param mode
	 * 	seasonal mode is 1 total points mode is 0
	 * @return
	 * 	list of max number of users in leader board descending order
	 */
	@GetMapping(path = "/getLeaderboardFilteredNumber") // Map ONLY GET Requests
	public @ResponseBody List<UserPoints> filteredLeaderboards(@RequestParam int max, @RequestParam int mode) {
		List<User> fullList;
		if (mode == 1) {//season mode
			fullList = userRepository.findAllByOrderBySeasonPointsDesc();
		} else {//total points
			fullList = userRepository.findAllByOrderByPointsTotalDesc();
		}
		List<UserPoints> filtered = new ArrayList<UserPoints>();
		UserPoints userToAdd;
		int counter = 0;
		for (User userOnList : fullList) {// go through list
			if (counter == max) {
				break;
			} else {
				counter++;
			}
			userToAdd = new UserPoints();
			userToAdd.SetupFromUser(userOnList);
			filtered.add(userToAdd);
		}
		if (filtered.isEmpty()) {
			return null;
		}
		return filtered;
	}
}
