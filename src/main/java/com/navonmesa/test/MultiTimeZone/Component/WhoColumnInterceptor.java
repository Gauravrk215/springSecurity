//package com.realnet.MultiTimeZone.Component;
//
//import java.io.Serializable;
//import java.time.ZonedDateTime;
//import java.util.Date;
//import java.util.TimeZone;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//
//import org.hibernate.EmptyInterceptor;
//import org.hibernate.type.Type;
//import org.springframework.stereotype.Component;
//
//import com.realnet.MultiTimeZone.Services.TimezoneService;
//import com.realnet.WhoColumn.Entity.Who_column;
//import com.realnet.users.entity1.AppUser;
//
//@Component
//public class WhoColumnInterceptor extends EmptyInterceptor {
//
//	private final EntityManagerFactory entityManagerFactory;
//	private final TimezoneService timezoneService;
//
//	public WhoColumnInterceptor(EntityManagerFactory entityManagerFactory, TimezoneService timezoneService) {
//		this.entityManagerFactory = entityManagerFactory;
//		this.timezoneService = timezoneService;
//	}
//
//	@Override
//	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
//		if (entity instanceof Who_column) {
//			Who_column whoColumn = (Who_column) entity;
//
//			// Fetch user timezone
//			Long createdByUserId = whoColumn.getCreatedBy();
//			EntityManager entityManager = entityManagerFactory.createEntityManager();
//			AppUser appUser = entityManager.find(AppUser.class, createdByUserId);
//
//			if (appUser != null) {
//				String userTimezone = appUser.getMultitime();
//				TimeZone timeZone = TimeZone.getTimeZone(userTimezone);
//
//				// Convert timestamps
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
//			}
//
//			entityManager.close();
//		}
//		return false;
//	}
//}