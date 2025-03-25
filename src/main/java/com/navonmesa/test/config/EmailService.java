package com.realnet.config;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.realnet.utils.Port_Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;

	public void sendSimpleMessage(String from, String to, String subject, String text) throws MailException {
		SimpleMailMessage message = new SimpleMailMessage();
		log.debug("email to sent..");
		// message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
		log.debug("email sent successfully");
	}

	public void sendEmailWithAttachment(String to, String subject, String text) throws MessagingException, IOException {

		MimeMessage msg = mailSender.createMimeMessage();

		// true = multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		log.debug("email to sent..");
		helper.setTo(to);

		helper.setSubject(subject);

		// default = text/plain
		// helper.setText("Check attachment for image!");

		// true = text/html
		// helper.setText("<h1>Check attachment for image!</h1>", true);

		helper.setText(text, true);

		// hard coded a file path
		// FileSystemResource file = new FileSystemResource(new
		// File("path/android.png"));
		// helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));
		mailSender.send(msg);
		log.debug("email sent successfully");
	}

	public void constructEmail(String em, String subject, String url) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(url);
		email.setTo(em);
		mailSender.send(email);

	}

	// FOR ADD USER VIA ADMIN
	public void sendEmail(String sendTo, String subject, String body) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(sendTo);
		try {

			mailSender.send(email);
			System.out.println(" email sent to " + sendTo);

		} catch (Exception e) {

			System.out.println(" invalid email " + e);
			// TODO: handle exception
		}

	}

//	send mail via setu
	public ResponseEntity<?> sendEmailViaSetu(String email, String message, String templateName, String gatewayName) {

//		template name = notification_template, gateway name = email_gateway
		try {

			String jsonData = "{\r\n" + "  \"job_type\": \"Email\",\r\n" + "  \"send_to\": \"" + email.trim()
					+ "\",\r\n" + "  \"cc\": \"cc@example.com\",\r\n"
//					+ "  \"attachment\": \"sample-file.txt\",\r\n"
					+ "  \"gatewaydone\": \"N\",\r\n" + "  \"template_name\": \"" + templateName.trim() + "\",\r\n"
					+ "  \"replacement_string\": \"Hello, {name} " + message + "!\",\r\n" + "  \"gatewayName\": \""
					+ gatewayName.trim() + "\"\r\n" + "}\r\n";

			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
			queryParams.add("data", jsonData);

			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(queryParams, headers);

			String apiUrl2 = Port_Constant.SURE_SETU_DOMAIN
					+ "/token/Surecommunication/communication/jobtable/Com_jobTable"; // Replace with the
			// actual API URL

			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl2, requestEntity, String.class);

			return ResponseEntity.ok(responseEntity.getBody());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

}
