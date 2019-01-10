package com.LockOut.Server.Models;


//not a table only used for stat understanding
public class StatAverage {
	private int hours;
	
	private int numdays;
	
	private int average;
	
	public int getAverage() {
		return average;
	}

	public void setAverage() {
		if(numdays == 0) {
			average = 0;
		}
		else {
		this.average = (this.hours / this.numdays);
		}
		
	}

	public StatAverage() {
		hours = 0;
		numdays = 0;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getNumdays() {
		return numdays;
	}

	public void setNumdays(int numdays) {
		this.numdays = numdays;
	}
	
	
}
