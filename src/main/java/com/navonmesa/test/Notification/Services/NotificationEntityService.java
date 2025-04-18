package com.realnet.Notification.Services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.realnet.Notification.Entity.NotEntity;
import com.realnet.Notification.Repository.NotRepo;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service1.AppUserServiceImpl;

@Service
public class NotificationEntityService {

	@Autowired
	private NotRepo notRepo;

	@Autowired
	private AppUserServiceImpl userService;

	public ResponseEntity<?> setnotification(String jobType, String wireframeName) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		AppUser loggedInUser = userService.getLoggedInUser();

		NotEntity notEntity = new NotEntity();
		notEntity.setTime(dateFormat.format(cal.getTime()));
		notEntity.setNotification(loggedInUser.getUsername() + jobType + wireframeName);
		notEntity.setUser_id(loggedInUser.getUserId());
		notRepo.save(notEntity);

		return new ResponseEntity<>("notification set", HttpStatus.CREATED);
	}

}
