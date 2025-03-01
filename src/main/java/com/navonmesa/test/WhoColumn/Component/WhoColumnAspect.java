package com.realnet.WhoColumn.Component;

import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.realnet.MultiTimeZone.Services.TimezoneService;
import com.realnet.WhoColumn.Entity.Who_column;
import com.realnet.users.entity1.AppUser;

@Aspect
@Component
public class WhoColumnAspect {

	@Autowired
	private TimezoneService timezoneService;

	@PersistenceContext
	private EntityManager entityManager;

	@Before("execution(* org.springframework.data.jpa.repository.JpaRepository.save(..)) && args(entity)")
	public void applyTimezone(Object entity) {
		if (entity instanceof Who_column) {
			Who_column whoColumn = (Who_column) entity;

			// Fetching user timezone
			Long createdByUserId = whoColumn.getCreatedBy(); // Assuming you store `createdBy` as user ID
			AppUser appUser = entityManager.find(AppUser.class, createdByUserId);

			if (appUser != null) {
				String userTimezone = appUser.getMultitime(); // Retrieve user's timezone

				// Convert timestamps
				if (whoColumn.getCreatedAt() != null) {
					ZonedDateTime convertedCreatedAt = timezoneService.convertToUserTimezone(whoColumn.getCreatedAt(),
							userTimezone);
					whoColumn.setCreatedAt(Date.from(convertedCreatedAt.toInstant()));
				}

				if (whoColumn.getUpdatedAt() != null) {
					ZonedDateTime convertedUpdatedAt = timezoneService.convertToUserTimezone(whoColumn.getUpdatedAt(),
							userTimezone);
					whoColumn.setUpdatedAt(Date.from(convertedUpdatedAt.toInstant()));
				}
			}
		}
	}
}