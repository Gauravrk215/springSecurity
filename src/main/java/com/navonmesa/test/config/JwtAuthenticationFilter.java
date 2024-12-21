package com.realnet.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.realnet.logging.NoLogging;
import com.realnet.session.Service.TokenBlacklistService;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service1.AppUserServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private TokenProvider jwtTokenUtil;

	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/path/to/allow").permitAll()// allow
																												// CORS
																												// option
																												// calls
				.antMatchers("/resources/**").permitAll().anyRequest().authenticated().and().formLogin().and()
				.httpBasic();
	}

//	//	prevoius it also working

//	@NoLogging
//	@Override
//	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
//			throws IOException, ServletException {
//		String header = req.getHeader(JWTConstant.HEADER_STRING);
//		//System.out.println("HEADER => {}" + header);
//		String username = null;
//		String email = null;
//		String authToken = null;
//		if (header != null && header.startsWith(JWTConstant.TOKEN_PREFIX)) {
//			authToken = header.replace(JWTConstant.TOKEN_PREFIX, "");
//			try {
//				username = jwtTokenUtil.getUsernameFromToken(authToken);
//				//logger.info("getting username from token : {}" + username);
//				email = jwtTokenUtil.getEmailFromToken(authToken);
//				//logger.info("getting email from token : {}" + email);
//				//System.out.println("email => {}" + email);
//				
//			} catch (IllegalArgumentException e) {
//				logger.error("an error occured during getting username from token", e);
//			} catch (ExpiredJwtException e) {
//				logger.warn("the token is expired and not valid anymore", e);
//			} catch (SignatureException e) {
//				logger.error("Authentication Failed. Username or Password not valid.");
//			}
//		} else {
//			logger.warn("couldn't find bearer string, will ignore the header");
//		}
//		//if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//			UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//
//			if (jwtTokenUtil.validateToken(authToken, userDetails)) {
//				UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthentication(authToken,
//						SecurityContextHolder.getContext().getAuthentication(), userDetails);
//				// UsernamePasswordAuthenticationToken authentication = new
//				// UsernamePasswordAuthenticationToken(userDetails, null, Arrays.asList(new
//				// SimpleGrantedAuthority("ROLE_ADMIN")));
//				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
//				logger.debug("authenticated user " + email + ", setting security context");
//				SecurityContextHolder.getContext().setAuthentication(authentication);
//			}
//		}
//		chain.doFilter(req, res);
//	}

//	by gk, for token expire

	@Autowired
	private AppUserServiceImpl userService;

	@Autowired
	private TokenBlacklistService tokenBlacklistService;

	@NoLogging
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		String header = req.getHeader(JWTConstant.HEADER_STRING); // Get the JWT token from the header
		String authToken = null;
		String email = null;

		if (header != null && header.startsWith(JWTConstant.TOKEN_PREFIX)) {
			authToken = header.replace(JWTConstant.TOKEN_PREFIX, "");

			try {
				email = jwtTokenUtil.getEmailFromToken(authToken); // Extract the email from the token
			} catch (IllegalArgumentException e) {
				logger.error("An error occurred while getting the username from the token", e);
			} catch (ExpiredJwtException e) {
				logger.warn("The token is expired and not valid anymore", e);
			} catch (SignatureException e) {
				logger.error("Authentication Failed. Invalid token signature.");
			}
		} else {
			logger.warn("Couldn't find bearer string, ignoring the header.");
		}

		// Continue with email if it's not null and no authentication exists in the
		// context

		Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
		if (email != null && authentication2 == null) {

			// Step 1: Check if the token is blacklisted
			boolean tokenBlacklisted = tokenBlacklistService.isTokenBlacklisted(authToken);
			if (tokenBlacklisted) {
				logger.warn("Token is blacklisted: " + authToken);
				res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Respond with unauthorized
				res.getWriter().write("Token has been blacklisted");
				return; // Do not continue the filter chain
			}

			// Step 2: Check if the user is deactivated
			AppUser user = userService.findUserByEmail(email); // Assuming you have a method to find user by email
			if (user == null || !user.isActive()) { // Check if the user is deactivated
				logger.warn("User is deactivated or not found: " + email);
				res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Respond with unauthorized
				res.getWriter().write("User is deactivated or not found");
				return; // Do not continue the filter chain
			}

			// Step 3: Proceed with user validation if not blacklisted or deactivated
			UserDetails userDetails = userDetailsService.loadUserByUsername(email);

			if (jwtTokenUtil.validateToken(authToken, userDetails)) {
				// Create an authentication token
				UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthentication(authToken,
						SecurityContextHolder.getContext().getAuthentication(), userDetails);

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				logger.debug("Authenticated user " + email + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		// Continue with the filter chain
		chain.doFilter(req, res);
	}
}