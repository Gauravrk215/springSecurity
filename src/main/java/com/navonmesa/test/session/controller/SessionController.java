package com.realnet.session.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.realnet.config.EmailService;
import com.realnet.config.TokenProvider;
import com.realnet.fnd.response.EntityResponse;
import com.realnet.fnd.response.OperationResponse;
import com.realnet.fnd.response.OperationResponse.ResponseStatusEnum;
import com.realnet.logging1.entity.AppUserLog;
import com.realnet.logging1.service.LoggingService;
import com.realnet.session.Repository.TokenRepository;
import com.realnet.session.Service.TokenBlacklistService;
import com.realnet.session.entity.AboutWork;
import com.realnet.session.entity.SessionItem;
import com.realnet.session.entity.Token;
import com.realnet.session.response.SessionResponse;
import com.realnet.users.entity.LoginUser;
import com.realnet.users.entity.Role;
import com.realnet.users.entity.Sys_Accounts;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.entity1.AppUserSessions;
import com.realnet.users.entity1.Registration;
import com.realnet.users.response.MessageResponse;
import com.realnet.users.service.AboutWorkService;
import com.realnet.users.service1.AppUserServiceImpl;
import com.realnet.users.service1.AppUserSessionsServiceImpl;
import com.realnet.users.service1.SysAccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

/*
This is a dummy rest controller, for the purpose of documentation (/session) path is map to a filter
 - This will only be invoked if security is disabled
 - If Security is enabled then SessionFilter.java is invoked
 - Enabling and Disabling Security is done at config/applicaton.properties 'security.ignored=/**'
*/
@ApiIgnore
@Api(tags = { "Authentication" })
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private LoggingService loggingService;
	@Autowired
	private TokenProvider jwtTokenUtil;

	@Autowired
	private AppUserServiceImpl appUserServiceImpl;

	@Autowired
	private AppUserServiceImpl userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AboutWorkService aboutworkservice;

	@Autowired
	private SysAccountService sysAccountService;

	@Autowired
	private AppUserSessionsServiceImpl appUserSessionsServiceImpl;

	@ApiIgnore
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Will return a security token, which must be passed in every request", response = SessionResponse.class) })
	@RequestMapping(value = "/session", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public SessionResponse newSession(@RequestBody LoginUser loginRequest, HttpServletRequest request,
			HttpSession session1) {

		AppUser user = userService.findUserByEmail(loginRequest.getEmail());

		Boolean active = user.isActive();
		if (active == null || !active) {
			SessionResponse resp = new SessionResponse();
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setOperationMessage("Inactive User");
			return resp;
		}

		Long account_id = user.getAccount().getAccount_id();
		Sys_Accounts account = sysAccountService.getBYId(account_id);
		Boolean activeAcc = account.getActive();
		if (activeAcc == null || !activeAcc) {
			SessionResponse resp = new SessionResponse();
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setOperationMessage("Inactive Account");
			return resp;
		}

		try {

			final Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			final String token = jwtTokenUtil.generateToken(authentication);

			System.out.println("authentication.getName() =>" + authentication.getName()); // email

			AppUser loggedInUser = userService.getLoggedInUser();
			MDC.put("USER", loggedInUser.getUsername());
			// System.out.println("/session logged in user -> " + loggedInUser);

//			List<String> loggedInUserRoles = new ArrayList<String>();
			StringBuilder roleString = new StringBuilder();
			roleString.append(loggedInUser.getUsrGrp().getGroupName());
//			.forEach(role -> {
////				loggedInUserRoles.add(role.getName());
//				roleString.append(role.getName() + ", ");
//			});
			// String role = roleString.toString().substring(0,
			// roleString.toString().lastIndexOf(","));
			// List<String> roleList = Arrays.asList(role.split("\\s*,\\s*"));

			SessionResponse resp = new SessionResponse();
			SessionItem sessionItem = new SessionItem();
			sessionItem.setToken(token);
			sessionItem.setUserId(loggedInUser.getUserId());
			sessionItem.setFullname(loggedInUser.getFullName());
			sessionItem.setFirstName(loggedInUser.getFullName());
			// sessionItem.setUsername(loggedInUser.getUsername());
			sessionItem.setEmail(loggedInUser.getEmail());
			// sessionItem.setRoles(roleList);
			Set<Role> roles = loggedInUser.getRoles();
			List<String> roleList = new ArrayList<>();
			for (Role ro : roles) {
				roleList.add(ro.getDescription());
			}
			sessionItem.setRoles(roleList);
			// sessionItem.setRoles(loggedInUser.getUsrGrp().getGroupName());
			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setOperationMessage("Login Success");
			resp.setItem(sessionItem);

			InetAddress ip;
			StringBuilder sb = new StringBuilder();
			try {
				ip = InetAddress.getLocalHost();
				System.out.println("Current IP address : " + ip.getHostAddress());
				NetworkInterface network = NetworkInterface.getByInetAddress(ip);
				byte[] mac = network.getHardwareAddress();
				System.out.print("Current MAC address : ");

				for (int i = 0; i < mac.length; i++) {
					sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
				}
				System.out.println(sb.toString());
			} catch (Exception e) {
				System.out.println("error is .." + e);
			}

			AppUserSessions session = new AppUserSessions();

			session.setUserId(loggedInUser);
			session.setLastAccessDate(new Date());
			session.setLogintime(new Date());
//			session.setLogouttime(new Date());
			session.setSessionId(RequestContextHolder.currentRequestAttributes().getSessionId());
			// String ip = request.getHeader("X-Forward-For");
			// String ip = getClientIp();
			// String ip = getClientIp(request);
			String ip1 = request.getRemoteAddr();
			session.setClientIp(ip1);
			session.setMacid(sb.toString());
			appUserSessionsServiceImpl.add(session);
			AppUserLog s = loggingService.generate(loggedInUser);
			// AppUserLog s = null;
			if (s != null) {
				session1.setAttribute("LogginLevel", s.getLogLevel());
				session1.setAttribute("generate_log", s.getGenerateLog());
				session1.setAttribute("LogFileName", s.getLogFileName());
			} else {
				session1.setAttribute("generate_log", "N");
			}

			saveTokenForUser(user.getUsername(), user.getUserId(), token);

			return resp;
		} catch (Exception e) {
			LOGGER.error("Login Failed " + e.getMessage());
			System.out.print(e.getMessage());
			SessionResponse resp = new SessionResponse();
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setOperationMessage("Login Failed");
			return resp;
		}

	}

	@Autowired
	private TokenRepository tokenRepository; // Injecting the token repository

	// Method to save the token
	public void saveTokenForUser(String username, Long userId, String token) {
		Token newToken = new Token();
		newToken.setUsername(username);
		newToken.setUserId(userId);

		newToken.setToken(token);
		newToken.setCreatedAt(LocalDateTime.now());
		tokenRepository.save(newToken); // Save token in DB
	}

//	//logout
//		@GetMapping("/logout")
//		public ResponseEntity<?> logoutUser(@RequestParam("sessionId") String sessionId) throws IOException {
//		    // Find the session by session ID in the database
//		    AppUserSessions userSession = appUserSessionsServiceImpl.findBySessionId(sessionId);
//
//		    if (userSession != null) {
//		      
//		            
//		            if (userSession.getLogouttime() != null) {
//		                
//		                return new ResponseEntity<>("Session not found or already invalidated", HttpStatus.BAD_REQUEST);
//		            }
//
//		        // Update logout time in the database and mark the session as invalid
//		        userSession.setLogouttime(new Date());
//		        appUserSessionsServiceImpl.update(userSession);  // Update the session record in the database
//
//		     
//		        // Clear the Spring Security context to ensure the user is logged out at the security level
//		        SecurityContextHolder.clearContext();
//
//		        return new ResponseEntity<>("Logged out successfully, session ID: " + sessionId, HttpStatus.OK);
//		    } else {
//		        // If the session is not found in the database
//		        return new ResponseEntity<>("Invalid session ID", HttpStatus.NOT_FOUND);
//		    }
//		}

	// logout
	@GetMapping("/logout")
	public ResponseEntity<?> logoutUser(HttpSession session2) throws IOException {

//			if(session1.getAttribute("generate_log").equals("Y")) {
//		    Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
//		    Path filePath = Paths.get(root.toString(),"logs",session1.getAttribute("LogFileName").toString());
//				File f=filePath.toFile();
//				FileWriter fw = new FileWriter(f,true);
//				fw.write("Logout\n");
//				fw.close();			
//			}

//			String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
//			AppUserSessions session = appUserSessionsRepository.findBySessionId(session2.toString());
//			session.setLogouttime(new Date());			
//			appUserSessionsServiceImpl.add(session);
		return new ResponseEntity<>("Logged out succesfully", HttpStatus.OK);
	}
	// logout

	public String getClientIp(HttpServletRequest request) {
		final String LOCALHOST_IPV4 = "127.0.0.1";
		final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
		String ipAddress = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}

		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}

		if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
				try {
					InetAddress inetAddress = InetAddress.getLocalHost();
					ipAddress = inetAddress.getHostAddress();
				} catch (UnknownHostException e) {
					System.out.println("error is .." + e);
				}
			}
		}

		if (!StringUtils.isEmpty(ipAddress) && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
			ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
		}

		return ipAddress;
	}

	@ApiOperation(value = "Send Email For OTP")
	@PostMapping("/user/send_email")
	public ResponseEntity<?> userviaadmin(HttpServletRequest request, @RequestBody Registration reg) {
		String email = reg.getEmail();
		AppUser appUser = new AppUser();

		AppUser user = userService.findUserByEmail(email);
		if (user != null && user.isIsComplete()) {
			return ResponseEntity.badRequest().body(new MessageResponse(email + " already exist"));
		} else {
			if (user != null && !user.isIsComplete()) {
				appUser = user;
			}
//			Random random = new Random();
			SecureRandom random = new SecureRandom();

			int otp = 100000 + random.nextInt(900000);
			userService.adduserbyemail(appUser, String.valueOf(otp), email);

			String subject = "Email Verification";
			String url = String.valueOf(otp);
			emailService.sendEmail(email, subject, url);
			return new ResponseEntity<>(new EntityResponse("Otp send successfully"), HttpStatus.OK);
		}

	}

//	RESEND OTP
	@PostMapping("/user/resend_otp")
	public ResponseEntity<?> resendotp(@RequestParam String email) {

		AppUser user = userService.findUserByEmail(email);
		if (user == null) {
			return ResponseEntity.badRequest().body(new MessageResponse(email + " not exist"));
		} else {
//			Random random = new Random();
			SecureRandom random = new SecureRandom();

			int otp = 100000 + random.nextInt(900000);
			userService.resendotp(otp, email);
			String subject = "Email Verification";
			String url = String.valueOf(otp);
			emailService.sendEmail(email, subject, url);
			return new ResponseEntity<>(new EntityResponse("resend Otp send successfully"), HttpStatus.OK);
		}

	}

//	OTP VERIFICATION
	@PostMapping("/user/otp_verification")
	public ResponseEntity<?> otpverfication(@RequestParam String email, @RequestParam String otp) {
		email = email.replaceAll(" ", "+");
		AppUser user = userService.findUserByEmail(email);
		if (user == null) {
			return ResponseEntity.badRequest().body(new MessageResponse(user + " not exist"));
		}
		String random_no = user.getRandom_no();
		if (random_no.equalsIgnoreCase(otp)) {
			return new ResponseEntity<>(new EntityResponse("OTP Verified"), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new EntityResponse("Wrong OTP"), HttpStatus.BAD_REQUEST);
		}
	}
//	user Registration

	@PostMapping("/addOneAppUser")
	public ResponseEntity<?> addOneUser(@RequestBody Registration reg) {
		System.out.println(reg);
//		if (appUserRepository.findByEmail(reg.getEmail()) != null) {
//			return ResponseEntity.badRequest().body(new MessageResponse("email already exist"));
//		}
		AppUser a = appUserServiceImpl.addOneUser(reg);
		return new ResponseEntity<>(a, HttpStatus.OK);
	}

	@ApiOperation(value = "Add new cluodnsure", response = OperationResponse.class)
	@PostMapping("/aboutwork")
	public AppUser addNewCustomer(@RequestBody AboutWork aboutWork) {

		System.out.println("about work controller started");

		// save acccount info
		AboutWork about = aboutworkservice.adddata(aboutWork);
		Sys_Accounts sys = new Sys_Accounts();
		sys.setAccount_id(about.getId());
		sysAccountService.save(sys);

		// save user with accout id
		AppUser user = new AppUser();
		user.setChangePassw(aboutWork.getPassword());
		user.setEmail(aboutWork.getEmail());
		user.setMob_no(aboutWork.getMobile());
		AppUser userResister = userService.userResister(user, about.getId());
		return userResister;
	}

	// GET USER BY USERID // TOKEN FREE
	@GetMapping("/getuser/{id}")
	public ResponseEntity<?> getChatUserById(@PathVariable Long id) {
		AppUser u = appUserServiceImpl.getById(id).get();
		return new ResponseEntity<>(u, HttpStatus.OK);
	}

//	all session logout

	@Autowired
	private SessionRegistry sessionRegistry;

	@DeleteMapping("/api/logout-user/{userId}")
	public String logoutUserSessions(@PathVariable Long userId) {

		Optional<AppUser> oneUser = userService.getOneUser(userId);

		if (!oneUser.isPresent()) {
			return "User not  found"; // Agar user nahi mila

		}
		String username = oneUser.get().getUsername();
		// Get all logged-in users (principals)
		List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

		for (Object principal : allPrincipals) {
			if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
				org.springframework.security.core.userdetails.UserDetails user = (org.springframework.security.core.userdetails.UserDetails) principal;

				if (user.getUsername().equals(username)) {
					List<SessionInformation> sessions = sessionRegistry.getAllSessions(user, false);
					for (SessionInformation session : sessions) {
						session.expireNow(); // Expire the session
					}
					return "All sessions for user '" + username + "' have been logged out.";
				}
			}

		}

		return "User not  found!.."; // Agar user nahi mila
	}

	@Autowired
	private TokenBlacklistService tokenBlacklistService;

	@PostMapping("/api/deactivate-user/{userId}")
	public String deactivateUser(@PathVariable Long userId) {
		Optional<AppUser> userOptional = userService.getOneUser(userId);

		if (!userOptional.isPresent()) {
			return "User not found";
		}

		AppUser user = userOptional.get();
		user.setActive(false); // Set user as inactive
		userService.insertOrSaveUser(user); // Save user state in the database

		// Step 1: Invalidate JWT Tokens (Blacklist)
		String username = user.getUsername();
		tokenBlacklistService.blacklistTokensForUser(username); // Add all user tokens to blacklist

		// Step 2: Expire all sessions for the user
		expireUserSessions(username);

		return "User '" + username + "' has been deactivated and logged out from all sessions.";
	}

	private void expireUserSessions(String username) {
		// Get all logged-in users (principals)
		List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

		for (Object principal : allPrincipals) {
			if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
				org.springframework.security.core.userdetails.UserDetails userDetails = (org.springframework.security.core.userdetails.UserDetails) principal;

				// If username matches, expire all sessions
				if (userDetails.getUsername().equals(username)) {
					List<SessionInformation> sessions = sessionRegistry.getAllSessions(userDetails, false);
					for (SessionInformation session : sessions) {
						session.expireNow(); // Expire the session to log the user out
					}
				}
			}
		}
	}

}