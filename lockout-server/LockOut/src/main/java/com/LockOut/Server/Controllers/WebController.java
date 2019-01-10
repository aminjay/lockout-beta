package com.LockOut.Server.Controllers;

import javax.activation.*;
import java.util.*;
import javax.mail.Authenticator;
import javax.mail.*;
import javax.mail.internet.*;
import javax.naming.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.LockOut.Server.Models.User;
import com.LockOut.Server.Repositories.UserRepository;
import com.sun.mail.util.MailSSLSocketFactory;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/help")
public class WebController {

	@Autowired
	public JavaMailSenderImpl emailSender;

	@Autowired
	private UserRepository userRepository;

	/**
	 * Sends email to recover passwords
	 * @param name
	 * 	user name to be recovered
	 * @param email
	 * 	email of user name to be recovered
	 * @return
	 * 	Status of operation
	 */
	@RequestMapping("/recoverPassword")
	public @ResponseBody String sendmail(@RequestParam String name, @RequestParam String email) {
		User recover = userRepository.findByUserName(name);
		if(recover == null) {
			return "Invalid user";
		}
		else if(!(recover.getEmail().equals(email))) {
			return "Invalid email address";
		}
		
		
		//session for message usage
		Session session = Session.getInstance(emailSender.getJavaMailProperties(), new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("lockouthelpers", "H3xVC118");
			}
			
		});
		//setup message
		SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(recover.getEmail()); 
        message.setSubject("Password Recovery"); 
        message.setText("Your password is: " + recover.getPassword());
        emailSender.send(message);
        return "Done";
		
	}
}
