package com.LockOut.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sun.glass.ui.Application;

@EnableScheduling
@SpringBootApplication
public class LockOutApplication extends SpringBootServletInitializer {

	private static Class applicationClass = LockOutApplication.class;
	
	public static void main(String[] args) {
		SpringApplication.run(LockOutApplication.class, args);
	}
	
	
}
