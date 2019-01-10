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

import com.LockOut.Server.Models.BaseMessage;
import com.LockOut.Server.Models.GroupMessage;
import com.LockOut.Server.Models.Message;
import com.LockOut.Server.Models.User;
import com.LockOut.Server.Repositories.GroupMessageRepository;
import com.LockOut.Server.Repositories.MessageRepository;
import com.LockOut.Server.Repositories.UserRepository;
@Controller
@RequestMapping(path = "/messaging")
public class MessageController {
	@Autowired
	private GroupMessageRepository groupMessageRepository;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(path= "/allGroup")
	public @ResponseBody Iterable<GroupMessage> allGroups()
	{
		return groupMessageRepository.findAll();
	}
	
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Message> all()
	{
		return messageRepository.findAll();
	}
	
	@GetMapping(path = "/send")
	public @ResponseBody String sendMessage(@RequestParam String from, @RequestParam String to, @RequestParam String msg)
	{
			Message m = new Message();
			m.initMessage();
			m.setMessage(msg);
			m.setRecipient(to);
			m.setSender(from);
			messageRepository.save(m);
			return "Sent!";
	}
	
	@GetMapping(path = "/inbox")
	public @ResponseBody ArrayList<BaseMessage> inbox(@RequestParam String recipient)
	{
		ArrayList<BaseMessage> toReturn = new ArrayList<BaseMessage>(); 
		ArrayList<Message> msgList = (ArrayList<Message>) messageRepository.findByRecipient(recipient);
		toReturn.addAll(msgList);
		User tempUser = userRepository.findByUserName(recipient);
		ArrayList<String> groups = (ArrayList<String>) tempUser.getGroupsList();
		Iterator I = groups.iterator();
		while (I.hasNext())
		{
			ArrayList<GroupMessage> temp = (ArrayList<GroupMessage>) groupMessageRepository.findByRecipient((String)I.next());
			toReturn.addAll(temp);
		}
		return toReturn;
	}
	
	@GetMapping(path = "/sent")
	public @ResponseBody Iterable<Message> sent(@RequestParam String sender)
	{
		return messageRepository.findBySender(sender);
	}
	
	@GetMapping(path = "/Conv")
	public @ResponseBody ArrayList<ArrayList<Message>> getConversations(@RequestParam String username)
	{
		ArrayList<Message> temp = (ArrayList<Message>) messageRepository.findByRecipientOrSender(username, username);
		ArrayList<ArrayList<Message>> toReturn = new ArrayList<ArrayList<Message>>();
		Iterator I = temp.iterator();
		while (temp.size()>0)
		{
			Message msg = (Message) temp.get(0);
			String toFind;
			if (!msg.getRecipient().contentEquals(username))
			{
				toFind = msg.getRecipient();
			}
			else
			{
				toFind = msg.getSender();
			}
			toReturn.add((ArrayList<Message>) findCorrespondance(toFind, temp));
		}
		return toReturn;
	}
	private List<Message> findCorrespondance(String toFind, List<Message> msgList)
	{
		ArrayList<Message> toReturn =  new ArrayList<Message>();
		Iterator I = msgList.iterator();
		while(I.hasNext())
		{
			Message msg = (Message) I.next();
			if (msg.getRecipient().contentEquals(toFind)||msg.getSender().contentEquals(toFind))
			{
				I.remove();
				toReturn.add(msg);
			}
		}
		return toReturn;
	}
	
	//Groups messages
	@GetMapping(path="toGroup")
	public @ResponseBody String sendToGroup(@RequestParam String from, @RequestParam String to, 
			@RequestParam String msg)
	{
		GroupMessage temp = new GroupMessage();
		temp.initMessage();
		temp.setMessage(msg);
		temp.setRecipient(to);
		temp.setSender(from);
		groupMessageRepository.save(temp);
		return "Sent!";
	}
	
}
