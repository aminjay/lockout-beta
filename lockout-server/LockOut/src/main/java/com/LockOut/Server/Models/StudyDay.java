package com.LockOut.Server.Models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class StudyDay {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	
	private int day;
	
	//1-12
	private int month;
	
	//0000-9999
	private int year;
	
	@OneToMany(cascade= CascadeType.ALL)
    private List<Statistic> statsAM;
	
	@OneToMany(cascade= CascadeType.ALL)
    private List<Statistic> statsPM;
	
	
	public List<Statistic> getStatsAM() {
		return statsAM;
	}

	public List<Statistic> getStatsPM() {
		return statsPM;
	}
	
	public void setStats(List<Statistic> stats) {
		if(stats.get(0).getHours() < 12) {
		this.statsAM = stats;
		}
		else {
		this.statsPM = stats;	
		}
	}
	
	public void addStat(Statistic stat) {
		initStats();
		if(stat.getTime() < 12) {
		statsAM.add(stat);
		}
		else {
		statsPM.add(stat);
		}
	}
	
	
	public void setDate(int day, int month, int year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	//for use outside the table
	
	//compare days
	public boolean sameDate(StudyDay otherDay) {
		if(this.day == otherDay.day 
				&& this.month == otherDay.month
				&& this.year == otherDay.year) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//initialize AM and PM stats when first adding
	private void initStats() {
		if(this.statsAM == null) {
			this.statsAM = new ArrayList<Statistic>();
			this.statsPM = new ArrayList<Statistic>();
		}
	}
	
	public double getAMtotal() {
		double sum =0;
		for(Statistic s : statsAM) {
			sum += s.getHours();
			sum += s.getMinutes() / (100.00);
		}
		return sum;
	}
	
	public double getPMtotal() {
		double sum =0;
		for(Statistic s : statsPM) {
			sum += s.getHours();
			sum += s.getMinutes() / (100.00);
		}
		return sum;
	}
	
	public double getDaytotal() {
		return getAMtotal() + getPMtotal();
	}
	
	
	public int RelationTo(StudyDay other) {
		
		//are they equal
		if(this.sameDate(other)) {
			return 0;
		}
		// the year of this is earlier than other so automatically before
		else if(this.getYear() < other.getYear()){
			return -1;
		}
		//the year of this is greater than other so automatically after
		else if(this.getYear() > other.getYear()) {
			return 1;
		}
		//the year is equal
		else {
			//the month is earlier so automatically before
			if(this.getMonth() < other.getMonth()) {
				return -1;
			}
			//the month is later so automatically after
			else if(this.getMonth() > other.getMonth()) {
				return 1;
			}
			//same year and month
			else {
				if(this.getDay() < other.getDay()) {
					return -1;
				}
				else {
					return 1;
				}
				
			}
		}
			
		
	}
	
}
