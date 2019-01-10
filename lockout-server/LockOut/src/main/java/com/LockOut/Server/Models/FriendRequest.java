package com.LockOut.Server.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FriendRequest {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private String from;
	private String to;
	
	public String getFrom()
	{
		return from;
	}
	public String getTo()
	{
		return to;
	}
	public void setFrom(String from)
	{
		this.from = from;
	}
	public void setTo(String to)
	{
		this.to= to;
	}
	public void init()
	{
		from = "";
		to = "";
	}

}
