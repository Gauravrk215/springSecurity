//package com.realnet.CredentialDatabase.Config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.realnet.CredentialDatabase.Service.SurevaultService;
//
//import java.util.Properties;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class Databaseconfig {
//
//	@Autowired
//	private SurevaultService surevaultService;
//
//	@Bean
//	public DataSource dataSource() throws JsonMappingException {
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//
//		try {
//			dataSource.setUrl(surevaultService.getSurevaultCredentials("databaseUrl"));
//			dataSource.setUsername(surevaultService.getSurevaultCredentials("databaseuserName"));
//			dataSource.setPassword(surevaultService.getSurevaultCredentials("databasePassword"));
//
//		} catch (JsonProcessingException e) {
//			// Handle exceptions or log errors
//			e.printStackTrace();
//		}
//
//		// Other DataSource configurations
//
//		return dataSource;
//	}
//}
