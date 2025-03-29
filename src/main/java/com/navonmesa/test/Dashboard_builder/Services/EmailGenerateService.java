//package com.realnet.Dashboard_builder.Services;
//
//import java.io.File;
//import java.util.Properties;
//
//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//
//import com.realnet.Gateway.Entity.Gateway_t;
//import com.realnet.Gateway.Services.Gateway_Service;
//
//public class EmailGenerateService {
//	
//	@Autowired
//	private JavaMailSender mailSender;
//	
//	@Autowired
//	private Gateway_Service gateway_Service;
//	
//	public boolean sendEmailGateway(Long id, String to, String subject, String htmlContent, String cc) {
//	    // Email to database code start
//	    Gateway_t getdetails = gateway_Service.getdetailsbyId(id);
//
//	    String host = getdetails.getHost();
//	    String username = getdetails.getUserName();
//	    String password = getdetails.getPassword();
//
//	    // SMTP server properties
//	    Properties props = new Properties();
//	    props.setProperty("mail.smtp.host", host);
//	    props.setProperty("mail.smtp.port", "587");
//	    props.setProperty("mail.smtp.auth", "true");
//	    props.setProperty("mail.smtp.starttls.enable", "true");
//
//	    try {
//	        Session session = Session.getInstance(props, new Authenticator() {
//	            protected PasswordAuthentication getPasswordAuthentication() {
//	                return new PasswordAuthentication(username, password);
//	            }
//	        });
//
//	        // Create a new message
//	        MimeMessage message = new MimeMessage(session);
//
//	        // Set the sender
//	        message.setFrom(new InternetAddress(username));
//
//	        // Set the recipient
//	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
//
//	        // Set the CC recipient if provided
//	        if (cc != null) {
//	            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
//	        }
//
//	        // Set the subject
//	        message.setSubject(subject);
//
//	        // Set the content
//	        message.setContent(htmlContent, "text/html");
//
//	        // Send the message
//	        Transport.send(message);
//
//	        return true;
//	    } catch (MessagingException e) {
//	        e.printStackTrace();
//	        return false;
//	    }
//	}
//	
//	
//	
//	
//	public String sendEmailGatewayWithAttachment(Long id, String to, String subject, String htmlContent, String cc, File file) {
//	    Gateway_t getdetails = gateway_Service.getdetailsbyId(id);
//	    if (getdetails != null) {
//	        String host = getdetails.getHost();
//	        String username = getdetails.getUserName();
//	        String password = getdetails.getPassword();
//
//	        // SMTP server properties
//	        Properties props = new Properties();
//	        props.setProperty("mail.smtp.host", host);
//	        props.setProperty("mail.smtp.port", "587");
//	        props.setProperty("mail.smtp.auth", "true");
//	        props.setProperty("mail.smtp.starttls.enable", "true");
//
//	        try {
//	            Session session = Session.getInstance(props, new Authenticator() {
//	                protected PasswordAuthentication getPasswordAuthentication() {
//	                    return new PasswordAuthentication(username, password);
//	                }
//	            });
//
//	            MimeMessage mimeMessage = new MimeMessage(session);
//	            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//	            mimeMessageHelper.setTo(to);
//	            mimeMessageHelper.setSubject(subject);
//	            mimeMessageHelper.setText(htmlContent, true);
//	            mimeMessageHelper.setCc(cc);
//	            mimeMessageHelper.addAttachment(file.getName(), file);
//
//	            Transport.send(mimeMessage);
//
//	            return "Mail sent successfully";
//	        } catch (MessagingException e) {
//	            return "Error while sending mail: " + e.getMessage();
//	        }
//	    } else {
//	        return "Error: Gateway not found";
//	    }
//	}
//
//
//}
