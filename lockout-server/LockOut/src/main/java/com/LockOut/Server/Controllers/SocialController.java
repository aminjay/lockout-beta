package com.LockOut.Server.Controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.LockOut.Server.Models.FriendRequest;
import com.LockOut.Server.Models.Groups;
import com.LockOut.Server.Models.User;
import com.LockOut.Server.Repositories.FriendRequestRepository;
import com.LockOut.Server.Repositories.GroupsRepository;
import com.LockOut.Server.Repositories.UserRepository;


//Author: Josh
@Controller
@RequestMapping(path = "/social")
public class SocialController {
	
	@Autowired
	private FriendRequestRepository friendRequestRepository;
	@Autowired
	private GroupsRepository groupsRepository;
	@Autowired
	private UserRepository userRepository;
	
	
	@GetMapping(path="/test")
	public @ResponseBody String test()
	{
		return "worked";
	}
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Groups> allGroups()
	{
		return groupsRepository.findAll();
	}
	@GetMapping(path="/getGroups") // Map ONLY GET Requests
	public @ResponseBody Iterable<Groups> getGroups(@RequestParam String name)
	{
		Iterable<String> temp = userRepository.findByUserName(name).getGroupsList();
		ArrayList<Groups> toReturn = new ArrayList<Groups>();
		Iterator<String> sI = temp.iterator();
		while (sI.hasNext())
		{
			toReturn.add(groupsRepository.findByGroupName(sI.next()));
		}
		return toReturn;
	}
	
	@GetMapping(path="/getFriends") // Map ONLY GET Requests
	public @ResponseBody Iterable<User> getFriends(@RequestParam String name)
	{
		Iterable<String> temp = userRepository.findByUserName(name).getFriendsList();
		ArrayList<User> toReturn = new ArrayList<User>();
		Iterator<String> sI = temp.iterator();
		while (sI.hasNext())
		{
			toReturn.add(userRepository.findByUserName(sI.next()));
		}
		return toReturn;
	}
	
	@GetMapping(path="/FindByGroupName") // Map ONLY GET Requests
	public @ResponseBody Groups findByGroupName(@RequestParam String name)
	{
		return groupsRepository.findByGroupName(name);
	}
	
	@GetMapping(path="/addMember") // Map ONLY GET Requests
	public @ResponseBody String addMember(@RequestParam String name, @RequestParam String groupName)
	{
		Groups group = groupsRepository.findByGroupName(groupName);
		User user = userRepository.findByUserName(name);
		group.addMember(user);
		user.addGroup(group);
		groupsRepository.save(group);
		return "Saved";
	}
	
	@GetMapping(path="/addMembers") // Map ONLY GET Requests
	public @ResponseBody String addMember(@RequestParam String groupname, @RequestParam List<String> members)
	{
		Iterator<String> I = members.iterator();
		while (I.hasNext())
		{
			String tempname = (String) I.next();
			addMember(tempname, groupname);
		}
		return "saved";
	}
	
	@GetMapping(path = "/sendRequest")
	public @ResponseBody String sendRequest(@RequestParam String username, @RequestParam String friendname)
	{
		FriendRequest fr = new FriendRequest();
		fr.init();
		fr.setFrom(username);
		fr.setTo(friendname);
		friendRequestRepository.save(new FriendRequest());
		return "request sent";
	}
	
	@GetMapping(path = "/acceptRequest")
	public @ResponseBody String accept(@RequestParam int requestId)
	{
		FriendRequest fr = friendRequestRepository.findById(requestId);
		User user1 = userRepository.findByUserName(fr.getFrom());
		User user2 = userRepository.findByUserName(fr.getTo());
		user1.addFriend(user2);
		user2.addFriend(user1);
		userRepository.save(user1);
		userRepository.save(user2);
		friendRequestRepository.delete(fr);
		return "accepted";
	}
	@GetMapping(path = "/declineRequest")
	public @ResponseBody String decline(@RequestParam int requestId)
	{
		FriendRequest fr = friendRequestRepository.findById(requestId);
		friendRequestRepository.delete(fr);
		return "declines";
	}
	
	@GetMapping(path="/addFriend") // Map ONLY GET Requests
	public @ResponseBody String addFriend(@RequestParam String username, @RequestParam String friendname)
	{
		User user = userRepository.findByUserName(username);
		User friend = userRepository.findByUserName(friendname);
		user.addFriend(friend);
		userRepository.save(user);
		friend.addFriend(user);
		userRepository.save(friend);
		return "Saved friend";
	}
	
	@GetMapping(path="/addFriends") // Map ONLY GET Requests
	public @ResponseBody String addFriend(@RequestParam String username, @RequestParam List<String> friends)
	{
		Iterator<String> I = friends.iterator();
		while (I.hasNext())
		{
			String tempname = (String) I.next();
			addFriend(username, tempname);
		}
		return "Saved all";
	}
	
	@GetMapping(path="/newGroup") // Map ONLY GET Requests
	public @ResponseBody String newGroup(@RequestParam String groupName)
	{
		Iterable<Groups> temp = groupsRepository.findAll();
		Iterator<Groups> I = temp.iterator();
		while(I.hasNext())
		{
			if(I.next().getGroupName().equals(groupName))
			{
				return "Group with that name already exists, try another name.";
			}
		}
		Groups group = new Groups();
		group.setName(groupName);
		group.initGroup();
		groupsRepository.save(group);
		return "Saved group";
	}
	
	@GetMapping(path="/deleteGroup")
	public @ResponseBody String deleteGroup(@RequestParam String groupName)
	{
		Groups group = findByGroupName(groupName);
		if (group==null)
		{
			return "No such group exists.";
		}
		List<String> memberList = group.getMembersList();
		Iterator<String> mI = memberList.iterator();
		while (mI.hasNext())
		{
			User temp = userRepository.findByUserName(mI.next());
			temp.rmGroup(group);
			userRepository.save(temp);
		}
		groupsRepository.delete(group);
		return "Deleted group";
	}
	
	@GetMapping(path="/deleteFriend")
	public @ResponseBody String deleteFriend(@RequestParam String username, @RequestParam String friendName)
	{
		User user = userRepository.findByUserName(username);
		User friend = userRepository.findByUserName(friendName);
		user.rmFriend(friend);
		friend.rmFriend(user);
		userRepository.save(user);
		userRepository.save(friend);
		return "deleted";
	}
	
	@GetMapping(path="/clearAll")
	public @ResponseBody String clear()
	{
		userRepository.deleteAll();
		groupsRepository.deleteAll();
		return "Mmmmmm Schorched Earth";
	}
	
}

