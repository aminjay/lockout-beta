package com.LockOut.Server.Models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class BaseMessage {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private String sender;
	private boolean sent;
	private String recipient;
	private String message;
	
	public void initMessage()
	{
		sender = "";
		recipient = "";
		message = "";
		sent = false;
	}
	
	public boolean wasSent()
	{
		return sent;
	}
	
	public String getSender()
	{
		return sender;
	}
	
	public String getRecipient()
	{
		return recipient;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public void setSender(String from)
	{
		sender = from;
	}
	
	public void setRecipient(String to)
	{
		recipient = to;
	}
	
	public void setMessage(String msg)
	{
		message = msg;
	}
	
}
