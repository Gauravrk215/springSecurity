package com.realnet.MultiTimeZone.Component;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.realnet.MultiTimeZone.Services.TimezoneService;
import com.realnet.WhoColumn.Entity.Who_column;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service1.AppUserService;

@Aspect
@Component
public class WhoColumnAspect {

	@Autowired
	private TimezoneService timezoneService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private AppUserService userService;

//	@Transactional
////	@Before("execution(* org.springframework.data.jpa.repository.JpaRepository.save(..)) && args(entity)")
//	@AfterReturning(pointcut = "execution(* org.springframework.data.jpa.repository.JpaRepository.find*(..))", returning = "entity")
//
//	public void applyTimezone(Object entity) {
//		if (entity instanceof Who_column) {
//			Who_column whoColumn = (Who_column) entity;
//
//			// Null check for createdBy
////			Long createdByUserId = whoColumn.getCreatedBy();
////			if (createdByUserId == null) {
////				return; // Avoid processing if no user info is available
////			}
//
//			AppUser appUser = userService.getLoggedInUser();
//
//			// Fetch user timezone
////			AppUser appUser = entityManager.find(AppUser.class, createdByUserId);
//			if (appUser == null || appUser.getMultitime() == null) {
//				return; // Skip if user or timezone is not found
//			}
//
//			String userTimezone = appUser.getMultitime();
//
//			try {
//				if (whoColumn.getCreatedAt() != null) {
//					ZonedDateTime convertedCreatedAt = timezoneService.convertToUserTimezone(whoColumn.getCreatedAt(),
//							userTimezone);
//					whoColumn.setCreatedAt(Date.from(convertedCreatedAt.toInstant()));
//				}
//
//				if (whoColumn.getUpdatedAt() != null) {
//					ZonedDateTime convertedUpdatedAt = timezoneService.convertToUserTimezone(whoColumn.getUpdatedAt(),
//							userTimezone);
//					whoColumn.setUpdatedAt(Date.from(convertedUpdatedAt.toInstant()));
//				}
//			} catch (Exception e) {
//				System.err.println("Timezone conversion error: " + e.getMessage());
//			}
//		}
//	}

	@Transactional
	@AfterReturning(pointcut = "execution(* org.springframework.data.jpa.repository.JpaRepository.find*(..))", returning = "entity")
	public void applyTimezone(Object entity) {
		if (entity == null) {
			return; // Agar entity null hai to kuch mat karo
		}

		AppUser appUser = userService.getLoggedInUser();
		if (appUser == null || appUser.getMultitime() == null) {
			return; // Agar user ya timezone nahi mila to skip karo
		}

		String userTimezone = appUser.getMultitime();

		try {
			if (entity instanceof Collection<?>) {
				// Agar entity ek collection hai to har element ko process karo
				for (Object obj : (Collection<?>) entity) {
					convertWhoColumn(obj, userTimezone);
				}
			} else {
				// Single entity case
				convertWhoColumn(entity, userTimezone);
			}
		} catch (Exception e) {
			System.err.println("Timezone conversion error: " + e.getMessage());
		}
	}

	private void convertWhoColumn(Object obj, String userTimezone) {
		if (obj instanceof Who_column) {
			Who_column whoColumn = (Who_column) obj;

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