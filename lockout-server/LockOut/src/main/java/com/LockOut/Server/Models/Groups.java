package com.LockOut.Server.Models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.LockOut.Server.Utils.Util;

import javax.persistence.JoinTable;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;


//Author: Josh
@Entity
public class Groups {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer Id;
	
	private String members;
	
	private final String DEL = "/";
	
	private String groupName;
	
	public Groups()
	{
	}
	
	public void initGroup()
	{
		members = "";
	}
	
	public String getGroupName()
	{
		return groupName;
	}
	
	public Integer getId()
	{
		return Id;
	}
	
	public List<String> getMembersList()
	{
		return Util.parser(members, DEL);
	}
	
	public void addMember(User newMember)
	{
		String memberName = newMember.getUserName();
		members = members+DEL+memberName;
	}
	
	public void setName(String name)
	{
		groupName = name;
	}
	
	public void rmMember(User user)
	{
		String rmName = user.getUserName();
		Util.rmString(members, DEL, rmName);
	}
	
	
}
