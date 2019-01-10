package com.LockOut.Server.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.LockOut.Server.Models.User;
import com.LockOut.Server.Repositories.UserRepository;

@Controller
@RequestMapping(path = "/PowerUser")
public class AdminController {
	private int powercode = 564331;
	@Autowired
	private UserRepository userRepository;

	/**
	 * 
	 * @param adminName
	 * 	Name of user you want to be admin
	 * @param adminCode
	 * 	static code used to setup first admin on system boot
	 * @return
	 * 	returns status of operation
	 */
	@RequestMapping(path = "/addAdminWithCode")
	public @ResponseBody String adminUpdateUser(@RequestParam String adminName, @RequestParam int adminCode) {
		// This returns a JSON or XML with the users
		User newAdmin = userRepository.findByUserName(adminName);
		if(newAdmin == null) {
			return "invalid user given";
		}
		else if(adminCode != powercode) {
			return "invalid admin code";
		} else {//user is valid and code is correct
			newAdmin.setStatus(5);
			userRepository.save(newAdmin);
			return "user Upgraded to Admin";
		}
		
	}
	
	/**
	 * 
	 * @param adminName
	 * 	name of admin you want to demote
	 * @param adminCode
	 * 	static admin code used if admins blocked out on emergecy
	 * @return
	 * 	Status of operation
	 */
	@RequestMapping(path = "/demoteAdminWithCode")
	public @ResponseBody String adminDemoteUser(@RequestParam String adminName, @RequestParam int adminCode) {
		// This returns a JSON or XML with the users
		User newAdmin = userRepository.findByUserName(adminName);
		if(newAdmin == null) {
			return "invalid user given";
		}
		else if(adminCode != powercode) {
			return "invalid admin code";
		} else {//user is valid and code is correct
			newAdmin.setStatus(0);//demote to regular user
			userRepository.save(newAdmin);
			return "user Demoted to Standard";
		}
		
	}
	
	/**
	 * 
	 * @return All users with open props
	 */
	@GetMapping(path = "/allOpenProps")
	public @ResponseBody Iterable<User> adminGetAllUsers() {
		
		if (userRepository.findAll().iterator().hasNext() == true) {
			return userRepository.findAll();
		} else {
			return null;
		}
	}

	/**
	 * Deletes a user using an admin account
	 * @param adminName
	 * 	admin user name
	 * @param adminPassword
	 * 	admin user password
	 * @param userName
	 * 	user name of the user to be deleted
	 * @return
	 * 	status of operation
	 */
	@GetMapping(path = "/delete") // Map ONLY GET Requests
	public @ResponseBody String adminDeleteUser(@RequestParam String adminName, @RequestParam String adminPassword,
			@RequestParam String userName) {
		User Admin = userRepository.findByUserName(adminName);
		if(Admin == null 
				|| Admin.getStatus() != 5) {
			return "invalid admin";
		}
		if (Admin.getPassword().equals(adminPassword)) {
			User ToDelete = userRepository.findByUserName(userName);
			if (ToDelete != null) {
				userRepository.delete(ToDelete);
				return "Deleted";
			} else {
				return "invalid user name, no user deleted";
			}
		}
		else {
			return "invalid admin user name or password";
		}
	}
	
	/**
	 * For cleansing purposes on system boots to be removed
	 * @param adminName
	 * 	admin user name
	 * @param adminPassword
	 * 	admin password
	 * @return
	 * 	status of operation
	 */
	@GetMapping(path = "/deleteAll")
	public @ResponseBody String deleteAllUsers(@RequestParam String adminName, @RequestParam String adminPassword) {
		// This returns a JSON or XML with the users
		User Admin = userRepository.findByUserName(adminName);
		if(Admin == null 
				|| Admin.getStatus() != 5) {
			return "invalid admin";
		}
		if (Admin.getPassword().equals(adminPassword)) {
			userRepository.deleteAll();
			return "all users successfully deleted";
		}
		else {
			return"invalid admin credentials";
		}
	}

}
