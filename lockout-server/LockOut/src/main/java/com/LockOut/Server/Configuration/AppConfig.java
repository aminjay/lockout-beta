package com.LockOut.Server.Configuration;

import java.security.GeneralSecurityException;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sun.mail.util.MailSSLSocketFactory;

@Configuration
@EnableTransactionManagement
public class AppConfig {

	@Bean
	public JavaMailSenderImpl getJavaMailSender() throws GeneralSecurityException {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	     
	    mailSender.setUsername("lockouthelpers@gmail.com");
	    mailSender.setPassword("H3xVC118");
	     
	    Properties props = mailSender.getJavaMailProperties();
	    MailSSLSocketFactory sf = new MailSSLSocketFactory();
	    sf.setTrustAllHosts(true); 
	    props.put("mail.imaps.ssl.trust", "smtp.gmail.com");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.socketFactory.port", "465");
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.port", "465");
	    props.put("mail.transport.protocol", "smtp");
	    
	    
	    return mailSender;
	}
}
