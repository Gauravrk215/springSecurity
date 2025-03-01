package com.realnet.WhoColumn.Services;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.MultiTimeZone.Services.TimezoneService;
import com.realnet.WhoColumn.Entity.Who_column;
import com.realnet.users.service1.AppUserService;

@Service
public class WhoColumnService {
	@Autowired
	private TimezoneService timezoneService;

	@Autowired
	private AppUserService userService;

	public String getConvertedDateTime(Who_column whoColumn) {

		String timezone = userService.getLoggedInUser().getMultitime();
		// Retrieve the timezone (if null, UTC is used by default)
//		String timezone = whoColumn.getMultitimeZone() != null ? whoColumn.getMultitimeZone() : "UTC";

		// Convert the 'createdAt' and 'updatedAt' dates
		String createdAtConverted = convertDateToTimezone(whoColumn.getCreatedAt(), timezone);
		String updatedAtConverted = convertDateToTimezone(whoColumn.getUpdatedAt(), timezone);

		return "Created At: " + createdAtConverted + "\nUpdated At: " + updatedAtConverted;
	}

	private String convertDateToTimezone(Date date, String timezone) {
		if (date == null) {
			return "Date not available";
		}

		// Convert to the user's timezone using the TimezoneService
		ZonedDateTime convertedDateTime = timezoneService.convertToUserTimezone(date, timezone);

		// Format the converted date into a readable string
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
		return convertedDateTime.format(formatter);
	}

}
