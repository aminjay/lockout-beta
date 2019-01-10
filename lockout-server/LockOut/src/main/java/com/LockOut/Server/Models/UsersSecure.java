package com.LockOut.Server.Models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.LockOut.Server.Utils.Util;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "user")
public class UsersSecure {
    
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	
	// other fields can be anything like this
    private String userName;
    
    private String email;
    
    // standard 1, moderator 2, admin 5
    private int status;
    
    private int pointsTotal;
    
    private int pointsAvailable;
    
    private int seasonPoints;
    
    private int profilePictureSelection;
    
    @OneToMany(cascade= CascadeType.ALL)
    private List<StudyDay> days;
    
    //josh inserts
    private String groups;
    private String friends;
    private final String DEL = "/";
    //josh inserts end
    
    
    
    public void initSocial()
    {
    	groups= "";
    	friends= "";
    }


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPointsTotal() {
		return pointsTotal;
	}

	public void setPointsTotal(int pointsTotal) {
		this.pointsTotal = pointsTotal;
	}

	public int getPointsAvailable() {
		return pointsAvailable;
	}

	public void setPointsAvailable(int pointsAvailable) {
		this.pointsAvailable = pointsAvailable;
	}

	public List<StudyDay> getDays() {
		return days;
	}

	public void setDays(List<StudyDay> days) {
		this.days = days;
	}
	
	public void addDay(StudyDay day) {
		days.add(day);
	}
	
	
	//Josh inserts
	public List<String> getFriendsList()
	{
		return Util.parser(friends, DEL);
		
	}
	
	public List<String> getGroupsList()
	{
		return Util.parser(groups, DEL);
	}
	
	public void addGroup(Groups group)
	{
		String groupName = group.getGroupName();
		groups = groups + DEL + groupName;
	}
	
	public void addFriend(User friend)
	{
		String friendName = friend.getUserName();
		friends= friends+DEL+friendName;
	}
	
	public void rmFriend(User friend)
	{
		String rmName = friend.getUserName();
		friends = Util.rmString(friends, DEL, rmName);
	}
	
	public void rmGroup(Groups group)
	{
		String rmName = group.getGroupName();
		groups = Util.rmString(groups, DEL, rmName);
	}


	public int getProfilePictureSelection() {
		return profilePictureSelection;
	}


	public void setProfilePictureSelection(int profilePictureSelection) {
		this.profilePictureSelection = profilePictureSelection;
	}


	public int getSeasonPoints() {
		return seasonPoints;
	}


	public void setSeasonPoints(int seasonPoints) {
		this.seasonPoints = seasonPoints;
	}
    
}
