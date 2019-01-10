package com.LockOut.Server.Scheduling;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.LockOut.Server.Models.StudyDay;
import com.LockOut.Server.Models.User;
import com.LockOut.Server.Repositories.StudyDayRepository;
import com.LockOut.Server.Repositories.UserRepository;
import com.LockOut.Server.Utils.StatCalculations;


@Component
public class ScheduledTasks {
	
	private static int pastDay;
	private static Calendar currDay;
	private static int pastMonth;
	//keeps status of setup
	private boolean setup = false;
	private StatCalculations statCalculator = new StatCalculations();
	private static long DayinMillis = 86400000;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StudyDayRepository studyDayRepository;
	
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	
	private void initializeDay() {
		pastDay = -1;
		pastMonth = -1;
		currDay = Calendar.getInstance();
		setup = true;
		/*User Admin = new User();
		Admin.setUserName("Andy");
		Admin.setPassword("PVC1");
		Admin.setDays(null);
		Admin.initSocial();
		Admin.setStatus(5);
		userRepository.save(Admin);*/
		
	}
	
	
	@Scheduled(fixedDelay = 5000)
	@Transactional
	public void StudyDayUpdate() {
		
		if(!setup) {
		initializeDay();
		}
		StudyDay newDay;
		currDay = Calendar.getInstance();
		if(currDay.get(Calendar.DAY_OF_MONTH) != pastDay) {
			pastDay = currDay.get(Calendar.DAY_OF_MONTH);
			for(User user : userRepository.findAll()) {
				newDay = new StudyDay();
				//get current date with Calendar
				int day = currDay.get(Calendar.DAY_OF_MONTH);
				int month = currDay.get(Calendar.MONTH) +1;
				int year = currDay.get(Calendar.YEAR);
				newDay.setDate(day, month, year);
				Hibernate.initialize(user.getDays());
				//check if day already exists
				if(user.getDays() == null) {
					user.setDays(new ArrayList<StudyDay>());
					studyDayRepository.save(newDay);
					user.addDay(newDay);
					userRepository.save(user);
				}
				else if(statCalculator.dayExists(newDay.getDay(), newDay.getMonth(), newDay.getYear(), user.getDays().iterator()) == null) {
					studyDayRepository.save(newDay);
					user.addDay(newDay);
					userRepository.save(user);
				}
			}
			
			//done now Log
			log.info("User days have been updated. The time is now {}", dateFormat.format(new Date()));
		}
		
	}
	
	@Scheduled(fixedDelay = 5000)
	@Transactional
	public void SeasonHandler() {
		Hibernate.initialize(userRepository);
		if(!setup) {
		initializeDay();
		}
		currDay = Calendar.getInstance();
		if(currDay.get(Calendar.MONTH) != pastMonth) {//currently set to monthly basis
			pastMonth = currDay.get(Calendar.MONTH);
			for(User user : userRepository.findAll()) {
				user.setSeasonPoints(0);//reset season points
				userRepository.save(user);
			}
			
			//done now Log
			log.info("User season points have been reset. The time is now {}", dateFormat.format(new Date()));
		}
		
		
	}
}
