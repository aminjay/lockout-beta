package stest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application extends org.springframework.boot.web.support.SpringBootServletInitializer {
	private static Class applicationClass = Application.class;
		
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}

