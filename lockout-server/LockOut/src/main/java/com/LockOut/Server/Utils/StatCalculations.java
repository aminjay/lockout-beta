package com.LockOut.Server.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.LockOut.Server.Models.StatAverage;
import com.LockOut.Server.Models.Statistic;
import com.LockOut.Server.Models.StudyDay;

public class StatCalculations {
	
	
	
	
	public List<StatAverage> arrayToList(StatAverage averages[]){
		//convert
		List<StatAverage> WEEK = new ArrayList<StatAverage>();
		for(int i=0; i<7; i++) {
			averages[i].setAverage();
		}
		WEEK.add(averages[0]);
		WEEK.add(averages[1]);
		WEEK.add(averages[2]);
		WEEK.add(averages[3]);
		WEEK.add(averages[4]);
		WEEK.add(averages[5]);
		WEEK.add(averages[6]);
		
		return WEEK;
		
		
		
	}
	
	public StudyDay dayExists(int day, int month, int year, Iterator<StudyDay> iterDays) {
		//setup days to use
		StudyDay currDay = new StudyDay();
		StudyDay TestDay;
		currDay.setDay(day);
		currDay.setMonth(month);
		currDay.setYear(year);
		
		//check for an already existing day
		while(iterDays.hasNext()) {
			TestDay = iterDays.next();
			if(currDay.sameDate(TestDay)) {
				return TestDay;
			}
		}
		return null;
	}
	
	
	public List<StudyDay> getStatsOverInterval(StudyDay startDay, StudyDay endDay, List<StudyDay> AllStats) {
		List<StudyDay> withinInterval = new ArrayList<StudyDay>();
		for(StudyDay sd : AllStats) {
			if(((sd.RelationTo(startDay) == 0) || (sd.RelationTo(startDay) == 1)) //day is at or after start day
					&& ((sd.RelationTo(endDay) == 0) || (sd.RelationTo(endDay) == -1))) {  //day is before or at end day
				withinInterval.add(sd); //add to list that fits interval
			}
		}
		
	return withinInterval;
	}
	
	//Returns relationship to 
	/*private int RelationTo(StudyDay first, StudyDay second) {
		
		//are they equal
		if(first.sameDate(second)) {
			return 0;
		}
		// the year of first is earlier than second so automatically before
		else if(first.getYear() < second.getYear()){
			return -1;
		}
		//the year of first is greater than second so automatically after
		else if(first.getYear() > second.getYear()) {
			return 1;
		}
		//the year is equal
		else {
			//the month is earlier so automatically before
			if(first.getMonth() < second.getMonth()) {
				return -1;
			}
			//the month is later so automatically after
			else if(first.getMonth() > second.getMonth()) {
				return 1;
			}
			//same year and month
			else {
				if(first.getDay() < second.getDay()) {
					return -1;
				}
				else {
					return 1;
				}
				
			}
		}
			
		
	}*/
}
