package com.LockOut.Server.ReturnObjects;

import com.LockOut.Server.Models.User;

public class UserPoints {
	
	private String userName;
	
	private int availablePoints;
	
	private int totalPoints;
	
	private int seasonPoints;
	
	private int profilePictureSelection;
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAvailablePoints() {
		return availablePoints;
	}

	public void setAvailablePoints(int availablePoints) {
		this.availablePoints = availablePoints;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	
	public void SetupFromUser(User user) {
		this.userName = user.getUserName();
		this.availablePoints = user.getPointsAvailable();
		this.totalPoints = user.getPointsTotal();
		this.profilePictureSelection = user.getProfilePictureSelection();
		this.seasonPoints = user.getSeasonPoints();
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
