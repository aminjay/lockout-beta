package com.LockOut.Server.Controllers;


import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.LockOut.Server.Models.Statistic;
import com.LockOut.Server.Models.StudyDay;
import com.LockOut.Server.Models.User;
import com.LockOut.Server.Repositories.StatisticsRepository;
import com.LockOut.Server.Repositories.StudyDayRepository;
import com.LockOut.Server.Repositories.UserRepository;
import com.LockOut.Server.Utils.StatCalculations;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/stats")
public class StatisticsController {
	@Autowired
	private StatisticsRepository statisticsRepository;
	
	@Autowired
	private StudyDayRepository studyDayRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private StatCalculations StatCalculator = new StatCalculations();
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Statistic> getAllStats() {
		// This returns a JSON or XML with the users
		if(statisticsRepository.findAll().iterator().hasNext() == true) {
			return statisticsRepository.findAll();
		}
		else {
			return null;
		}
	}
	
	@GetMapping(path="/addstat") // Map ONLY GET Requests
	public @ResponseBody String addNewStat (@RequestParam String name
			, @RequestParam String password, @RequestParam int day, 
			@RequestParam int month, @RequestParam int year, @RequestParam int hours, 
			@RequestParam int time, @RequestParam int minutes) {
		User user = userRepository.findByUserName(name);
		if(user != null) {
		//set up variables to fill and use
		Statistic Stat = new Statistic();
		List<StudyDay> userDays = user.getDays();
		Iterator<StudyDay> iterDays = userDays.iterator();
		StudyDay MatchingDay = StatCalculator.dayExists(day, month, year, iterDays);
		
		Stat.setAll(minutes, hours, time);
		statisticsRepository.save(Stat);
		if(MatchingDay != null) {//the user already has a day for this day
			MatchingDay.addStat(Stat);
			studyDayRepository.save(MatchingDay);
		}
		else { //the day being added to is not yet existing
			StudyDay newDay = new StudyDay();
			newDay.setDay(day);
			newDay.setMonth(month);
			newDay.setYear(year);
			newDay.addStat(Stat);
			studyDayRepository.save(newDay);
			user.addDay(newDay);
			
		}
		userRepository.save(user);
		
		return "New Stat added";
		}
		else {
			return "invalid user";
		}
	}
	
	@GetMapping(path="/getStatforInterval")
	public @ResponseBody List<StudyDay> getDayStat(@RequestParam String userName,
			@RequestParam int startDay, @RequestParam int startMonth,
			@RequestParam int startYear, @RequestParam int endDay, 
			@RequestParam int endMonth, @RequestParam int endYear) {
		
		//setup needed variables
		User user = userRepository.findByUserName(userName);
		if(user != null ) {
		StudyDay startStudyDay = new StudyDay();
		StudyDay endStudyDay = new StudyDay();
		startStudyDay.setDate(startDay, startMonth, startYear);
		endStudyDay.setDate(endDay, endMonth, endYear);
		
		//send variable to StatCalculator to be sorted
		List<StudyDay> stats = StatCalculator.getStatsOverInterval(startStudyDay, endStudyDay, user.getDays());
		if(stats.isEmpty()) {
			return null;
		}
		else {
			return stats;
		}
		}
		else {
			return null;
		}
		
	}
	

}
