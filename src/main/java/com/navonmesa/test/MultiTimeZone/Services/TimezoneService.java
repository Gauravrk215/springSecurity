package com.realnet.MultiTimeZone.Services;

import org.springframework.stereotype.Service;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.Date;

@Service
public class TimezoneService {

	public ZonedDateTime convertToUserTimezone(Date date, String userTimezone) {
		ZoneId targetZoneId;

		// Default timezone is UTC
		if (userTimezone == null || userTimezone.isEmpty()) {
			targetZoneId = ZoneId.of("UTC");
		} else {
			targetZoneId = ZoneId.of(userTimezone);
		}

		// Convert Date to ZonedDateTime in UTC
		ZonedDateTime utcZonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"));

		// Convert to target timezone
		return utcZonedDateTime.withZoneSameInstant(targetZoneId);
	}
}