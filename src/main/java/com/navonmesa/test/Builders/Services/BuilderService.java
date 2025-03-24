package com.realnet.Builders.Services;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.realnet.Builders.Entity.Builder_entity_t;
import com.realnet.Builders.Repos.BuilderRepository;
import com.realnet.Dashboard1.Entity.Dashbord1_Line;
import com.realnet.Dashboard1.Entity.Dashbord_Header;
import com.realnet.Dashboard1.Repository.Dashboard_lineRepository;
import com.realnet.Dashboard1.Service.HeaderService;
import com.realnet.Rpt_builder2.Entity.Rpt_builder2_t;
import com.realnet.Rpt_builder2.Services.Rpt_builder2_Service;
import com.realnet.Rpt_builder2_lines.Entity.Rpt_builder2_lines_t;
import com.realnet.Rpt_builder2_lines.Services.Rpt_builder2_lines_Service;
import com.realnet.api_registery.Entity.Api_registery_header;
import com.realnet.api_registery.Entity.Api_registery_line;
import com.realnet.api_registery.Services.Api_registery_headerService;
import com.realnet.api_registery.Services.Api_registery_lineService;
import com.realnet.fnd.entity1.MenuDet;
import com.realnet.fnd.service1.SecmenuDetailService;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service1.AppUserServiceImpl;

@Service
public class BuilderService {

	@Autowired
	private SecmenuDetailService secmenuDetailService;

	@Autowired
	private HeaderService headerService;

	@Autowired
	private Dashboard_lineRepository dashboard_lineRepository;

	@Autowired
	private Api_registery_headerService api_registery_headerService;

	@Autowired
	private Api_registery_lineService api_registery_lineService;

	@Autowired
	private BuilderRepository builderRepository;

	@Autowired
	private Rpt_builder2_lines_Service rLinesService;

	@Autowired
	private Rpt_builder2_Service reBuilder2Service;

	@Autowired
	private AppUserServiceImpl userServiceImpl;

	public void callotherService() throws IOException {

		executeDump(true);

		// ADD OTHER SERVICE

		System.out.println("dashboard and menu inserted...");

	}

//	add Custom sec menu detail

	public ResponseEntity<?> addCustomMenu(String tableName, String MenuType) {

//		default menu that is Transcation
		Long menuid = 1577l;

		if (MenuType.equalsIgnoreCase("Masters") || MenuType.equalsIgnoreCase("Master")) {
			menuid = 1601l;

		}

		MenuDet menuDet = null;
		Builder_entity_t entity_t = builderRepository.findByjobTypeAndName(tableName, "Menu");

		if (entity_t == null) {
			System.out.println("now inserting menu");

			menuDet = secmenuDetailService.customsecmenuadd(tableName, menuid);

			savebuilderentity(tableName, "Menu");
		} else {
			System.out.println(tableName + " menu already have");

		}

		return new ResponseEntity<>(menuDet, HttpStatus.CREATED);
	}

//	add dashboard
	public Dashbord_Header SaveDashboard(String dashboardname, String description, String model) {

		Dashbord_Header dash = null;
		Builder_entity_t entity_t = builderRepository.findByjobTypeAndName(dashboardname, "Dashboard");

		if (entity_t == null) {
			System.out.println("now inserting dashboard");

			Dashbord_Header dashbord_Header = new Dashbord_Header();
			dashbord_Header.setDashboard_name(dashboardname);
			dashbord_Header.setDescription(description);
			dashbord_Header.setObject_type("form");
			dashbord_Header.setSub_object_type("only header");

			dash = headerService.Savedata(dashbord_Header);
			Dashbord1_Line line = new Dashbord1_Line();

			line.setModel(model);
			line.setHeader_id(dash.getId().toString());
			line.setDashbord_Header(dash);
			dashboard_lineRepository.save(line);

			savebuilderentity(dashboardname, "Dashboard");

		} else {
			System.out.println(dashboardname + " dashboard already have");

		}

		return dash;
	}

//	add Report
	public Rpt_builder2_t SaveReport(String reportName, String description, Boolean isSql, String model) {

		Rpt_builder2_t report = null;
		Builder_entity_t entity_t = builderRepository.findByjobTypeAndName(reportName, "Report");

		if (entity_t == null) {
			System.out.println("now inserting Report");

			Rpt_builder2_t rp = new Rpt_builder2_t();
			rp.setReportName(reportName);
			rp.setDescription(description);
			rp.setIsSql(isSql);

			report = reBuilder2Service.Savedata(rp);
			Rpt_builder2_lines_t line = new Rpt_builder2_lines_t();

			line.setModel(model);
			line.setHeader_id(report.getId().toString());
			line.setRpt_builder2_t(rp);
			rLinesService.Savedata(line);

			savebuilderentity(reportName, "Report");

		} else {
			System.out.println(reportName + " Report already have");

		}

		return report;
	}

//	Add to api Registery

	public Api_registery_header SaveApiRegistery(String tableName) {

		Api_registery_header save = null;
		Builder_entity_t entity_t = builderRepository.findByjobTypeAndName(tableName, "Api_registery");

		if (entity_t == null) {
			System.out.println("now inserting apiregistery");

			Api_registery_header api_registery_header = new Api_registery_header();

			api_registery_header.setTable_name(tableName);

			save = api_registery_headerService.Savedata(api_registery_header);

			HashMap<String, String> hashMap = new HashMap<>();

			hashMap.put("GetAll", "/" + tableName + "/" + tableName);
			hashMap.put("GetById", "/" + tableName + "/" + tableName + "{Id}");
			hashMap.put("Post", "/" + tableName + "/" + tableName);
			hashMap.put("Put", "/" + tableName + "/" + tableName);

			Set<Entry<String, String>> entrySet = hashMap.entrySet();

			for (Entry<String, String> entry : entrySet) {

				String Method = entry.getKey();
				String url = entry.getValue();

				Api_registery_line registery_line = new Api_registery_line();

				registery_line.setMethod(Method);
				registery_line.setUrl(url);
				registery_line.setHeader_id(save.getId());

				api_registery_lineService.Savedata(registery_line);

			}

			savebuilderentity(tableName, "Api_registery");
		} else {
			System.out.println(tableName + " all method already have");

		}

		return save;
	}

	private void savebuilderentity(String Job_name, String jobType) {

		Builder_entity_t builder_entity_t = new Builder_entity_t();

		builder_entity_t.setJob_name(Job_name);
		builder_entity_t.setJob_type(jobType);
		builderRepository.save(builder_entity_t);

	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ResponseEntity<String> executeDump(Boolean execute) throws IOException {
		// Check if execution is allowed
		System.out.println(" dump executed start..");

		Builder_entity_t entity_t = builderRepository.findByjobTypeAndName("SqlDump", "Execute");

		List<AppUser> users = userServiceImpl.getAllUsers();
		if (entity_t != null || !users.isEmpty()) {
			execute = false;

		}
		if (!execute) {
			System.out.println("Dump Already Executed...\n");
			return ResponseEntity.ok("Service not executed due to 'execute' flag being false.");
		}

//		Path path = Paths.get(System.getProperty("user.dir")).resolve(filePath);

		// Check if file exists
//			if (!Files.exists(path)) {
//				return ResponseEntity.badRequest().body("File not found: " + filePath);
//			}
//
//			// File content ko read karo aur SQL commands extract karo
//			String sql = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);

		String sql = getSql();
		// SQL commands ko execute karo

		// Split statements by semicolon
//			List<String> sqlStatements = Arrays.stream(sql.split(";")).map(String::trim)
//					.filter(statement -> !statement.isEmpty()) // Empty statements ko remove karo
//					.filter(statement -> !statement.startsWith("USE")) // "USE db;" ko ignore karo
//					.filter(statement -> !statement.startsWith("--")) // Comments ko ignore karo
//					.filter(statement -> !statement.startsWith("/*")) // Special MySQL commands ko ignore karo
//					.collect(Collectors.toList());

		List<String> sqlStatements = Arrays.stream(sql.split("(?<=;)(?![^()]*\\))")) // Regular expression to split
																						// by semicolon not inside
																						// parentheses
				.map(String::trim).filter(statement -> !statement.isEmpty()) // Empty statements ko remove karo
				.filter(statement -> !statement.startsWith("--")) // Comments ko ignore karo
				.filter(statement -> !statement.startsWith("/*")) // Special MySQL commands ko ignore karo
				.collect(Collectors.toList());

		// Execute each statement
		for (String statement : sqlStatements) {
			try {
				jdbcTemplate.execute(statement);

				System.out.println(statement + " executed..");

			} catch (DataAccessException e) {
				System.out.println("DataAccessException error inside.." + e);

				// Specific SQL execution error ko catch karo aur log karo

			}
		}

		savebuilderentity("SqlDump", "Execute");

		System.out.println("Dump executed executed..");

		// File ko delete karo
//			Files.delete(path);

		System.out.println("File delete successfully...");

		return ResponseEntity.ok("Dump executed and file deleted successfully!");

	}

	public String getSql() {

		String sql = " \n" + "DROP TABLE IF EXISTS `accounts`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `accounts` (\n"
				+ "  `id` bigint NOT NULL,\n" + "  `companyname` varchar(255) DEFAULT NULL,\n"
				+ "  `email` varchar(255) DEFAULT NULL,\n" + "  `managing_work` varchar(255) DEFAULT NULL,\n"
				+ "  `mobile` bigint DEFAULT NULL,\n" + "  `name` varchar(255) DEFAULT NULL,\n"
				+ "  `pancard` varchar(255) DEFAULT NULL,\n" + "  `password` varchar(255) DEFAULT NULL,\n"
				+ "  `working` varchar(255) DEFAULT NULL,\n" + "  PRIMARY KEY (`id`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;\n"
				+ "INSERT INTO `accounts` VALUES (1,'test','test@gmail.com','w',123456789,'kk','kk','test','w');\n"
				+ "/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;\n" + "\n" + "DROP TABLE IF EXISTS `app_user_log`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `app_user_log` (\n"
				+ "  `log_id` bigint NOT NULL,\n" + "  `created_on` datetime DEFAULT NULL,\n"
				+ "  `generate_log` varchar(255) DEFAULT NULL,\n" + "  `log_file_name` varchar(255) DEFAULT NULL,\n"
				+ "  `log_level` varchar(255) DEFAULT NULL,\n" + "  `user_name` varchar(255) DEFAULT NULL,\n"
				+ "  PRIMARY KEY (`log_id`),\n" + "  UNIQUE KEY `UK_tl3or0c0pxxdvigxponlgee18` (`user_name`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `app_user_log` DISABLE KEYS */;\n"
				+ "INSERT INTO `app_user_log` VALUES (1,'2023-06-09 17:39:20','Y','sysadmin1686312560.log','info','sysadmin');\n"
				+ "/*!40000 ALTER TABLE `app_user_log` ENABLE KEYS */;\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `app_user_log_sequence`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `app_user_log_sequence` (\n"
				+ "  `next_val` bigint DEFAULT NULL\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n"
				+ "/*!40000 ALTER TABLE `app_user_log_sequence` DISABLE KEYS */;\n"
				+ "INSERT INTO `app_user_log_sequence` VALUES (2),(2),(2),(2),(2),(2),(2),(2),(2),(1);\n"
				+ "/*!40000 ALTER TABLE `app_user_log_sequence` ENABLE KEYS */;\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `logs`;\n" + "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `logs` (\n"
				+ "  `user_id` bigint NOT NULL,\n" + "  `dated` varchar(255) DEFAULT NULL,\n"
				+ "  `lavel` varchar(255) DEFAULT NULL,\n" + "  `logger` varchar(255) DEFAULT NULL,\n"
				+ "  `message` varchar(255) DEFAULT NULL,\n" + "  PRIMARY KEY (`user_id`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `role` DISABLE KEYS */;\n"
				+ "INSERT INTO `role` VALUES (1,'ADMIN','ROLE_ADMIN'),(2,'Developer','ROLE_Developer'),(3,'USER','ROLE_USER'),(5,'DEVEOPS','ROLE_DEVEOPS');\n"
				+ "/*!40000 ALTER TABLE `role` ENABLE KEYS */;\n" + "\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `sec_menu_det` DISABLE KEYS */;\n"
				+ "INSERT INTO `sec_menu_det` VALUES (1116,'2023-01-25 10:25:50','2023-01-25 10:25:50',3000,'security','lock',0,'Security','sec3000','Enable'),(1117,'2023-01-25 10:42:02','2023-02-04 15:34:25',3010,'usermaintance',NULL,1116,'User Maintance','U1000','Enable'),(1118,'2023-01-25 11:12:27','2023-02-04 15:34:36',3020,'usergrpmaintance',NULL,1116,'User Group Maintance','U2000','Enable'),(1523,'2023-02-04 11:15:57','2023-02-04 15:34:45',3030,'menumaintance',NULL,1116,'Menu Maintance','M3000','Enable'),(1524,'2023-02-04 11:16:52','2023-02-04 15:34:54',3040,'menuaccess',NULL,1116,'Menu Access Control','MA4000','Enable'),(1525,'2023-02-04 11:17:31','2023-02-04 15:35:06',3050,'systemparameters',NULL,1116,'System Parameters','SP5000','Enable'),(1526,'2023-02-04 11:18:04','2023-02-04 15:35:14',3060,'accesstype',NULL,1116,'Access Type','A6000','Enable'),(1528,'2023-02-04 13:31:48','2023-02-04 15:33:02',1010,'incident-new',NULL,1527,'Incident','I1000','Enable'),(1529,'2023-02-04 13:33:03','2023-02-04 15:33:12',1020,'incident-overview',NULL,1527,'Overview','O2000','Enable'),(1530,'2023-02-04 13:34:42','2023-02-04 15:33:21',1030,'sureboard2',NULL,1527,'Issueboard','I3000','Enable'),(1531,'2023-02-04 13:35:27','2023-02-04 15:33:36',1040,'change-request',NULL,1527,'Change Request','C4000','Enable'),(1532,'2023-02-04 13:36:01','2023-02-10 02:25:13',1050,'problem-creation',NULL,1527,'Problem Request','P5000','Enable'),(1534,'2023-02-04 13:56:22','2023-02-04 15:34:07',2010,'Sr_priority2_t',NULL,1533,'Priority','P1000','Enable'),(1535,'2023-02-04 17:12:27','2023-02-04 17:12:27',2020,'Sr_urgency_t',NULL,1533,'Urgency','Su2000','Enable'),(1536,'2023-02-04 17:13:22','2023-02-04 17:13:22',2030,'Sr_impact2_t',NULL,1533,'Impact','Sm3000','Enable'),(1537,'2023-02-04 17:14:01','2023-02-04 17:14:01',2040,'Sr_category2_t',NULL,1533,'Category','Sc4000','Enable'),(1538,'2023-02-04 17:14:31','2023-02-04 17:14:31',2050,'Sr_State_t',NULL,1533,'State','S5000','Enable'),(1539,'2023-02-04 17:15:12','2023-02-09 13:25:27',2070,'Sr_customer_t',NULL,1533,'Customer','c7000','Enable'),(1540,'2023-02-04 17:16:23','2023-02-14 13:15:27',2060,'Sr_Contact_type_t',NULL,1533,'Contact','C6000','Enable'),(1541,'2023-02-04 17:17:22','2023-02-04 17:17:22',2080,'Sr_handler_t',NULL,1533,'Handler','H8000','Enable'),(1543,'2023-02-08 15:07:14','2023-03-01 05:51:41',4010,'sequence',NULL,1542,'Define Sequence','SE1000','Enable'),(1544,'2023-02-16 10:14:33','2023-02-16 10:14:33',4020,'bugtracker',NULL,1542,'Bug Tracker','B1000','Enable'),(1545,'2023-02-16 11:00:25','2023-02-16 11:00:25',4030,'datamanagemennt',NULL,1542,'Data Management','M3000','Enable'),(1550,'2023-03-01 06:10:35','2023-03-01 06:10:35',2090,'projects',NULL,1533,'Projects','P2000','Enable'),(1551,'2023-03-01 07:14:27','2023-03-01 07:14:27',4040,'applysequence',NULL,1542,'Apply Sequence','As3000','Enable'),(1552,'2023-06-05 14:14:21','2023-06-05 14:14:21',2000,'dash','dashboard',0,'dashboard','dashboard','Enable'),(1553,'2023-06-05 14:16:48','2023-06-17 03:53:20',1000,'DashboardTesting',NULL,1552,'dashboard1','dashboard','Enable'),(1554,'2023-06-11 10:55:12','2023-06-11 10:55:12',2000,'Dashtest',NULL,1552,'das2','2000','Enable'),(1555,'2023-10-10 12:50:55','2024-01-19 14:33:06',1000,'project','flag',0,'project','project','Enable'),(1556,'2023-10-11 11:03:39','2023-10-11 11:03:39',4000,'superadmin','King',0,'Super Admin','Super Admin','Enable'),(1557,'2023-10-11 11:04:17','2023-10-11 11:04:17',4100,'extension',NULL,1556,'Code Extension','CodeExtension','Enable'),(1558,'2023-10-25 16:31:26','2023-10-25 16:31:26',5000,'test','tools',0,'Vault','Vault','Enable'),(1559,'2023-10-25 16:32:18','2023-10-25 16:32:18',5001,'Access_Point',NULL,1558,'Access Point','Access Point','Enable'),(1560,'2023-10-25 17:34:39','2023-10-25 17:34:39',6000,'crm','employee-group',0,'CRM','crm','Enable'),(1561,'2023-10-25 17:35:21','2023-10-25 17:35:21',6001,'Calls',NULL,1560,'Calls','Calls','Enable'),(1562,'2023-10-25 17:35:43','2023-10-25 17:35:43',6002,'Campaign',NULL,1560,'Campaign','Campaign','Enable'),(1563,'2023-10-25 17:35:57','2023-10-25 17:35:57',6003,'Company',NULL,1560,'Company','Company','Enable'),(1564,'2023-10-25 17:36:16','2023-10-25 17:36:16',6004,'Contact',NULL,1560,'Contact','Contact','Enable'),(1565,'2023-10-25 17:36:32','2023-10-25 17:36:32',6005,'Dealer',NULL,1560,'Dealer','Dealer','Enable'),(1566,'2023-10-25 17:36:48','2023-10-25 17:36:48',6006,'Deals',NULL,1560,'Deals','Deals','Enable'),(1567,'2023-10-25 17:37:00','2023-10-25 17:37:00',6007,'Documents',NULL,1560,'Documents','Documents','Enable'),(1568,'2023-10-25 17:37:25','2023-10-25 17:37:25',6008,'Leads',NULL,1560,'Leads','Leads','Enable'),(1569,'2023-10-25 17:37:41','2023-10-25 17:37:41',6009,'Meetings',NULL,1560,'Meetings','Meetings','Enable'),(1570,'2023-10-25 17:39:36','2023-10-25 17:39:36',6010,'Product',NULL,1560,'Product','Product','Enable'),(1571,'2023-10-25 17:39:51','2023-10-25 17:39:51',6011,'Tasks',NULL,1560,'Tasks','Tasks','Enable'),(1572,'2023-11-01 18:43:22','2023-11-03 11:42:38',4000,'SequenceGenerator',NULL,1116,'Document Sequence','Document Sequrnce','Enable'),(1574,'2023-12-04 11:53:00','2023-12-04 18:49:58',1000,'Teacher_Registration',NULL,1573,'Teacher Registration','Teacher','Enable'),(1575,'2023-12-04 11:53:15','2023-12-04 18:50:25',1000,'Student_registration',NULL,1573,'Student Registration','Student','Enable'),(1576,'2023-12-04 18:50:53','2023-12-04 18:50:53',3000,'Courses_name',NULL,1573,'course','course','Enable'),(1577,'2024-01-27 15:39:15','2024-03-20 19:46:19',7000,'Transactions','King',0,'Transactions','Transactions','Enable'),(1579,'2024-02-01 16:26:28','2024-02-07 16:03:10',4300,'apiregistery',NULL,1556,'Api Registery','Api Registery','Enable'),(1580,'2024-02-02 10:05:03','2024-02-02 10:05:03',4200,'tokenregistery',NULL,1556,'Token Registery','Token Registery','Enable'),(1582,'2024-02-03 10:25:51','2024-02-03 10:25:51',8000,'test','test',0,'test','test','Enable'),(1588,'2024-02-07 16:03:53','2024-02-07 16:03:53',4400,'survey-form',NULL,1556,'Survey Form','SurveyForm','Enable'),(1593,'2024-02-24 15:02:22','2024-02-24 15:02:22',4500,'datamanage','data-cluster',0,'Data Management','DataManagement','Enable'),(1594,'2024-02-24 15:03:01','2024-02-24 15:03:01',4501,'datamanagement',NULL,1593,'Data Management','DataManagement','Enable'),(1595,'2024-02-24 15:03:34','2024-02-24 15:03:34',4502,'validationrule',NULL,1593,'Validaiton Rule','ValidaitonRule','Enable'),(1596,'2024-02-24 15:03:58','2024-02-24 15:03:58',4503,'mappingrule',NULL,1593,'Mapping Rule','MappingRule','Enable'),(1601,'2024-03-20 19:46:01','2024-03-20 19:46:37',9000,'Masters','data-cluster',0,'Masters','Masters','Enable');\n"
				+ "/*!40000 ALTER TABLE `sec_menu_det` ENABLE KEYS */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `sec_user_group` DISABLE KEYS */;\n"
				+ "INSERT INTO `sec_user_group` VALUES (1,NULL,NULL,'add',30,'sysadmin','E',NULL,'2023-03-01 05:54:10'),(41,NULL,'2023-02-28 13:05:54','check',20,'users','Disable',NULL,'2023-02-28 13:30:14');\n"
				+ "/*!40000 ALTER TABLE `sec_user_group` ENABLE KEYS */;\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `sec_user_group_id`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `sec_user_group_id` (\n"
				+ "  `next_val` bigint DEFAULT NULL\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `sec_user_group_id` DISABLE KEYS */;\n"
				+ "INSERT INTO `sec_user_group_id` VALUES (59),(59),(40),(40),(40),(40),(40),(40);\n"
				+ "/*!40000 ALTER TABLE `sec_user_group_id` ENABLE KEYS */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `sys_accounts` DISABLE KEYS */;\n"
				+ "INSERT INTO `sys_accounts` VALUES (1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL);\n"
				+ "/*!40000 ALTER TABLE `sys_accounts` ENABLE KEYS */;\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `sec_users` DISABLE KEYS */;\n"
				+ "INSERT INTO `sec_users` (`user_id`, `is_complete`, `about`, `accesstype`, `active`, `change_passw`, `checknumber`, `country`, `createby`, `createdate`, `customer_id`, `days_mth`, `dep_string`, `email`, `expiry_date`, `first_login`, `full_name`, `is_blocked`, `lang_code`, `last_pwd_changed_date`, `mob_no`, `no_days_mth`, `notification`, `password1`, `password2`, `password3`, `password4`, `photo`, `photo_name`, `provider`, `pwd_changed_cnt`, `random_no`, `short_name`, `status`, `title`, `updateby`, `updatedate`, `user_passw`, `user_name`, `usr_grp_id`, `working`, `account_id`, `department_code`, `position_code`, `usr_grp`) VALUES (10007307, true, NULL, NULL, true, 'test3', NULL, NULL, NULL, '2024-09-03 19:32:38', NULL, NULL, NULL, 'sysadmin', NULL, NULL, 'sysadmin', false, NULL, '2024-09-03 19:32:38', '1234567890', NULL, NULL, 'admin123', NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, NULL, NULL, NULL, NULL, NULL, '$2b$10$8iFnL/cKTTmclSD1BZh8UeP0ZKKEzZ2hbTsrRcgy3kMinDRdxN7xe', 'sysadmin', NULL, NULL, 1, NULL, NULL, 1);\n"
				+ "/*!40000 ALTER TABLE `sec_users` ENABLE KEYS */;\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `sec_user_sessions`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `sec_user_sessions` (\n"
				+ "  `client_ip` varchar(255) NOT NULL,\n" + "  `session_id` varchar(255) NOT NULL,\n"
				+ "  `user_id` bigint NOT NULL,\n" + "  `last_access_date` datetime DEFAULT NULL,\n"
				+ "  `logintime` datetime DEFAULT NULL,\n" + "  `logouttime` datetime DEFAULT NULL,\n"
				+ "  `macid` varchar(255) DEFAULT NULL,\n" + "  PRIMARY KEY (`client_ip`,`session_id`,`user_id`),\n"
				+ "  KEY `FKp9jm02b501ugvjvfhas5sskfq` (`user_id`),\n"
				+ "  CONSTRAINT `FKp9jm02b501ugvjvfhas5sskfq` FOREIGN KEY (`user_id`) REFERENCES `sec_users` (`user_id`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `sec_user_sessions` DISABLE KEYS */;\n"
				+ "/*!40000 ALTER TABLE `sec_user_sessions` ENABLE KEYS */;\n" + "\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `sec_grp_menu_access`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `sec_grp_menu_access` (\n"
				+ "  `menu_item_id` bigint NOT NULL,\n" + "  `usr_grp` bigint NOT NULL,\n"
				+ "  `createby` varchar(255) DEFAULT NULL,\n" + "  `created_at` datetime DEFAULT NULL,\n"
				+ "  `isdisable` varchar(255) DEFAULT NULL,\n" + "  `item_seq` bigint DEFAULT NULL,\n"
				+ "  `m_create` varchar(255) DEFAULT NULL,\n" + "  `m_delete` varchar(255) DEFAULT NULL,\n"
				+ "  `m_edit` varchar(255) DEFAULT NULL,\n" + "  `m_query` varchar(255) DEFAULT NULL,\n"
				+ "  `m_visible` varchar(255) DEFAULT NULL,\n"
				+ "  `main_menu_action_name` varchar(255) DEFAULT NULL,\n"
				+ "  `main_menu_icon_name` varchar(255) DEFAULT NULL,\n" + "  `menu_id` bigint DEFAULT NULL,\n"
				+ "  `menu_item_desc` varchar(255) DEFAULT NULL,\n" + "  `mexport` varchar(255) DEFAULT NULL,\n"
				+ "  `module_name` varchar(255) DEFAULT NULL,\n" + "  `status` varchar(255) DEFAULT NULL,\n"
				+ "  `updateby` varchar(255) DEFAULT NULL,\n" + "  `updated_at` datetime DEFAULT NULL,\n"
				+ "  PRIMARY KEY (`menu_item_id`,`usr_grp`),\n" + "  KEY `FKtj8mtsrhc4m4acbrvjnnyvglm` (`usr_grp`),\n"
				+ "  CONSTRAINT `FKmkev6w9umgp6fg2afatibhq1x` FOREIGN KEY (`menu_item_id`) REFERENCES `sec_menu_det` (`menu_item_id`),\n"
				+ "  CONSTRAINT `FKtj8mtsrhc4m4acbrvjnnyvglm` FOREIGN KEY (`usr_grp`) REFERENCES `sec_user_group` (`usr_grp`) ON DELETE CASCADE\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `sec_grp_menu_access` DISABLE KEYS */;\n"
				+ "INSERT INTO `sec_grp_menu_access` VALUES (1116,41,NULL,'2024-01-17 18:56:05','true',3000,'true','true','true','true','true','security','lock',0,'Security','true','sec3000','Enable',NULL,'2024-01-17 18:56:05'),(1117,41,NULL,'2024-01-17 18:56:07','true',3010,'true','true','true','true','true','usermaintance',NULL,1116,'User Maintance','true','U1000','Enable',NULL,'2024-01-17 18:56:07'),(1118,41,NULL,'2024-01-17 18:56:11','true',3020,'true','true','true','true','true','usergrpmaintance',NULL,1116,'User Group Maintance','true','U2000','Enable',NULL,'2024-01-17 18:56:11'),(1523,41,NULL,'2024-01-17 18:56:14','true',3030,'true','true','true','true','true','menumaintance',NULL,1116,'Menu Maintance','true','M3000','Enable',NULL,'2024-01-17 18:56:14'),(1524,41,NULL,'2024-01-17 18:56:16','true',3040,'true','true','true','true','true','menuaccess',NULL,1116,'Menu Access Control','true','MA4000','Enable',NULL,'2024-01-17 18:56:16'),(1525,41,NULL,'2024-01-17 18:56:18','true',3050,'true','true','true','true','true','systemparameters',NULL,1116,'System Parameters','true','SP5000','Enable',NULL,'2024-01-17 18:56:18'),(1526,41,NULL,'2024-01-17 18:56:19','true',3060,'true','true','true','true','true','accesstype',NULL,1116,'Access Type','true','A6000','Enable',NULL,'2024-01-17 18:56:19'),(1572,41,NULL,'2024-01-17 18:56:21','true',4000,'true','true','true','true','true','SequenceGenerator',NULL,1116,'Document Sequence','true','Document Sequrnce','Enable',NULL,'2024-01-17 18:56:21'),(1577,1,NULL,'2024-01-27 15:40:46','true',7000,'true','true','true','true','true','Transactions','King',0,'Transactions','true','Transactions','Enable',NULL,'2024-01-27 15:40:46'),(1601,1,NULL,'2024-03-20 20:06:50','true',9000,'true','true','true','true','true','Masters','data-cluster',0,'Masters','true','Masters','Enable',NULL,'2024-03-20 20:06:50');\n"
				+ "/*!40000 ALTER TABLE `sec_grp_menu_access` ENABLE KEYS */;\n" + "\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `sec_users_sequencs`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `sec_users_sequencs` (\n"
				+ "  `next_val` bigint DEFAULT NULL\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `sec_users_sequencs` DISABLE KEYS */;\n"
				+ "INSERT INTO `sec_users_sequencs` VALUES (10007321),(10007300),(10007300),(10007300),(10007300),(10007300),(10007300),(10007300);\n"
				+ "/*!40000 ALTER TABLE `sec_users_sequencs` ENABLE KEYS */;\n" + "\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `sec_workspace`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `sec_workspace` (\n"
				+ "  `id` int NOT NULL AUTO_INCREMENT,\n" + "  `account_id` bigint DEFAULT NULL,\n"
				+ "  `created_at` datetime NOT NULL,\n" + "  `created_by` bigint DEFAULT NULL,\n"
				+ "  `updated_at` datetime NOT NULL,\n" + "  `updated_by` bigint DEFAULT NULL,\n"
				+ "  `is_active` varchar(255) DEFAULT NULL,\n" + "  `description` varchar(255) DEFAULT NULL,\n"
				+ "  `is_default` varchar(255) DEFAULT NULL,\n" + "  `name` varchar(255) DEFAULT NULL,\n"
				+ "  `owner_id` varchar(255) DEFAULT NULL,\n" + "  `project_id` int DEFAULT NULL,\n"
				+ "  PRIMARY KEY (`id`)\n" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `sec_workspace_users`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `sec_workspace_users` (\n"
				+ "  `id` bigint NOT NULL AUTO_INCREMENT,\n" + "  `account_id` bigint DEFAULT NULL,\n"
				+ "  `created_at` datetime NOT NULL,\n" + "  `created_by` bigint DEFAULT NULL,\n"
				+ "  `updated_at` datetime NOT NULL,\n" + "  `updated_by` bigint DEFAULT NULL,\n"
				+ "  `project_id` int DEFAULT NULL,\n" + "  `user_id` bigint DEFAULT NULL,\n"
				+ "  `user_name` varchar(255) DEFAULT NULL,\n" + "  `user_role` varchar(255) DEFAULT NULL,\n"
				+ "  `worksapce_id` int NOT NULL,\n" + "  PRIMARY KEY (`id`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `secuser_roles` DISABLE KEYS */;\n"
				+ "INSERT INTO `secuser_roles` VALUES (10007307,1);\n"
				+ "/*!40000 ALTER TABLE `secuser_roles` ENABLE KEYS */;\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `sys_accounts_users`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `sys_accounts_users` (\n"
				+ "  `sys_accounts_id` bigint NOT NULL,\n" + "  `users_user_id` bigint NOT NULL,\n"
				+ "  UNIQUE KEY `UK_8dxppqkque5ehofdy83bntgks` (`users_user_id`),\n"
				+ "  KEY `FKs9o1t4hyiyq2y330p0d2evf2a` (`sys_accounts_id`),\n"
				+ "  CONSTRAINT `FKdkc60wy8v55ylspegueinngjx` FOREIGN KEY (`users_user_id`) REFERENCES `sec_users` (`user_id`),\n"
				+ "  CONSTRAINT `FKs9o1t4hyiyq2y330p0d2evf2a` FOREIGN KEY (`sys_accounts_id`) REFERENCES `sys_accounts` (`id`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `sys_accounts_users` DISABLE KEYS */;\n"
				+ "/*!40000 ALTER TABLE `sys_accounts_users` ENABLE KEYS */;\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `user_list`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `user_list` (\n"
				+ "  `id` bigint NOT NULL,\n" + "  `about` varchar(255) DEFAULT NULL,\n"
				+ "  `account_id` varchar(255) DEFAULT NULL,\n" + "  `address1` varchar(255) DEFAULT NULL,\n"
				+ "  `address2` varchar(255) DEFAULT NULL,\n" + "  `checknumber` varchar(255) DEFAULT NULL,\n"
				+ "  `company` varchar(255) DEFAULT NULL,\n" + "  `country` varchar(255) DEFAULT NULL,\n"
				+ "  `created_at` varchar(255) DEFAULT NULL,\n" + "  `created_by` varchar(255) DEFAULT NULL,\n"
				+ "  `default_customer_id` varchar(255) DEFAULT NULL,\n" + "  `department` varchar(255) DEFAULT NULL,\n"
				+ "  `dob` varchar(255) DEFAULT NULL,\n" + "  `email` varchar(255) DEFAULT NULL,\n"
				+ "  `enable_beta_testing` bit(1) NOT NULL,\n" + "  `enable_renewal` bit(1) NOT NULL,\n"
				+ "  `firstname` varchar(255) DEFAULT NULL,\n" + "  `fullname` varchar(255) DEFAULT NULL,\n"
				+ "  `gender` varchar(255) DEFAULT NULL,\n" + "  `is_active` bit(1) NOT NULL,\n"
				+ "  `is_blocked` bit(1) NOT NULL,\n" + "  `lastname` varchar(255) DEFAULT NULL,\n"
				+ "  `managing_work` varchar(255) DEFAULT NULL,\n" + "  `menu_group` int NOT NULL,\n"
				+ "  `name` varchar(255) DEFAULT NULL,\n" + "  `other_roles` varchar(255) DEFAULT NULL,\n"
				+ "  `pancard` varchar(255) DEFAULT NULL,\n" + "  `password` varchar(255) DEFAULT NULL,\n"
				+ "  `phone` varchar(255) DEFAULT NULL,\n" + "  `photos` varchar(255) DEFAULT NULL,\n"
				+ "  `postal` varchar(255) DEFAULT NULL,\n" + "  `role` varchar(255) DEFAULT NULL,\n"
				+ "  `secret_answer` varchar(255) DEFAULT NULL,\n" + "  `secret_question` varchar(255) DEFAULT NULL,\n"
				+ "  `security_provider_id` varchar(255) DEFAULT NULL,\n" + "  `status` varchar(255) DEFAULT NULL,\n"
				+ "  `updated_at` varchar(255) DEFAULT NULL,\n" + "  `updated_by` varchar(255) DEFAULT NULL,\n"
				+ "  `user_id` varchar(255) DEFAULT NULL,\n" + "  `username` varchar(255) DEFAULT NULL,\n"
				+ "  `working` varchar(255) DEFAULT NULL,\n" + "  PRIMARY KEY (`id`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `user_list` DISABLE KEYS */;\n"
				+ "/*!40000 ALTER TABLE `user_list` ENABLE KEYS */;\n" + "\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `userloginhist`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `userloginhist` (\n"
				+ "  `create_by` varchar(255) DEFAULT NULL,\n" + "  `create_date` datetime DEFAULT NULL,\n"
				+ "  `expiry_reminder` bigint DEFAULT NULL,\n" + "  `last_login_date` datetime DEFAULT NULL,\n"
				+ "  `last_password_chg_date` datetime DEFAULT NULL,\n"
				+ "  `last_password_fail_no` bigint DEFAULT NULL,\n" + "  `update_by` varchar(255) DEFAULT NULL,\n"
				+ "  `update_date` datetime DEFAULT NULL,\n" + "  `user_id` bigint NOT NULL,\n"
				+ "  PRIMARY KEY (`user_id`),\n"
				+ "  CONSTRAINT `FK2a2xifc15js82mjl20honhdfr` FOREIGN KEY (`user_id`) REFERENCES `sec_users` (`user_id`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `userpasswlog`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `userpasswlog` (\n"
				+ "  `create_date` datetime NOT NULL,\n" + "  `user_id` bigint NOT NULL,\n"
				+ "  `user_passw` varchar(255) NOT NULL,\n" + "  `create_by` varchar(255) DEFAULT NULL,\n"
				+ "  `update_by` varchar(255) DEFAULT NULL,\n" + "  `update_date` datetime DEFAULT NULL,\n"
				+ "  PRIMARY KEY (`create_date`,`user_id`,`user_passw`),\n"
				+ "  KEY `FKpd759n25auh4bw5ri0xikikia` (`user_id`),\n"
				+ "  CONSTRAINT `FKpd759n25auh4bw5ri0xikikia` FOREIGN KEY (`user_id`) REFERENCES `sec_users` (`user_id`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `userpasswlog` DISABLE KEYS */;\n"
				+ "/*!40000 ALTER TABLE `userpasswlog` ENABLE KEYS */;\n" + "\n" + "\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `dashboard`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `dashboard` (\n"
				+ "  `id` int NOT NULL AUTO_INCREMENT,\n" + "  `account_id` bigint DEFAULT NULL,\n"
				+ "  `created_at` datetime NOT NULL,\n" + "  `created_by` bigint DEFAULT NULL,\n"
				+ "  `updated_at` datetime NOT NULL,\n" + "  `updated_by` bigint DEFAULT NULL,\n"
				+ "  `extn1` varchar(255) DEFAULT NULL,\n" + "  `extn10` varchar(255) DEFAULT NULL,\n"
				+ "  `extn11` varchar(255) DEFAULT NULL,\n" + "  `extn12` varchar(255) DEFAULT NULL,\n"
				+ "  `extn13` varchar(255) DEFAULT NULL,\n" + "  `extn14` varchar(255) DEFAULT NULL,\n"
				+ "  `extn15` varchar(255) DEFAULT NULL,\n" + "  `extn2` varchar(255) DEFAULT NULL,\n"
				+ "  `extn3` varchar(255) DEFAULT NULL,\n" + "  `extn4` varchar(255) DEFAULT NULL,\n"
				+ "  `extn5` varchar(255) DEFAULT NULL,\n" + "  `extn6` varchar(255) DEFAULT NULL,\n"
				+ "  `extn7` varchar(255) DEFAULT NULL,\n" + "  `extn8` varchar(255) DEFAULT NULL,\n"
				+ "  `extn9` varchar(255) DEFAULT NULL,\n" + "  `isdashboard` bit(1) NOT NULL,\n"
				+ "  `model` varchar(5000) DEFAULT NULL,\n" + "  `name` varchar(255) DEFAULT NULL,\n"
				+ "  PRIMARY KEY (`id`)\n"
				+ ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `dashboard` DISABLE KEYS */;\n"
				+ "INSERT INTO `dashboard` VALUES (1,NULL,'2023-12-04 12:11:04',NULL,'2023-12-04 13:05:56',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,_binary '','[{\\\"charttitle\\\":\\\"index1\\\",\\\"type\\\":\\\"Index\\\",\\\"cols\\\":5,\\\"rows\\\":5,\\\"x\\\":0,\\\"y\\\":0,\\\"chartid\\\":3,\\\"component\\\":\\\"Index\\\",\\\"name\\\":\\\"Index\\\",\\\"slices\\\":false,\\\"donut\\\":false,\\\"chartcolor\\\":false,\\\"chartlegend\\\":false,\\\"showlabel\\\":false,\\\"Read Only\\\":false,\\\"selectedIcon\\\":\\\"IconData(U+0EE2A)\\\",\\\"charturl\\\":\\\"\\\",\\\"chartparameter\\\":\\\"\\\",\\\"datasource\\\":\\\"Default\\\"},{\\\"charttitle\\\":\\\"Index2\\\",\\\"type\\\":\\\"Index\\\",\\\"cols\\\":5,\\\"rows\\\":5,\\\"x\\\":0,\\\"y\\\":0,\\\"chartid\\\":4,\\\"component\\\":\\\"Index\\\",\\\"name\\\":\\\"Index\\\"},{\\\"charttitle\\\":\\\"Index3\\\",\\\"type\\\":\\\"Index\\\",\\\"cols\\\":5,\\\"rows\\\":5,\\\"x\\\":0,\\\"y\\\":0,\\\"chartid\\\":5,\\\"component\\\":\\\"Index\\\",\\\"name\\\":\\\"Index\\\"},{\\\"charttitle\\\":\\\"Doughnut Chart\\\",\\\"type\\\":\\\"Doughnut Chart\\\",\\\"cols\\\":5,\\\"rows\\\":5,\\\"x\\\":0,\\\"y\\\":0,\\\"chartid\\\":6,\\\"component\\\":\\\"Doughnut Chart\\\",\\\"name\\\":\\\"Doughnut Chart\\\",\\\"slices\\\":false,\\\"donut\\\":false,\\\"chartcolor\\\":false,\\\"chartlegend\\\":false,\\\"showlabel\\\":false,\\\"Read Only\\\":false,\\\"charturl\\\":\\\"http://43.205.154.152:30179/entityBuilder/Gaurav_testing/3\\\",\\\"chartparameter\\\":\\\"\\\",\\\"datasource\\\":\\\"Default\\\",\\\"selectedIcon\\\":\\\"IconData(U+0EE29)\\\"},{\\\"cols\\\": 4, \\\"rows\\\": 5, \\\"x\\\": 0, \\\"y\\\": 0, \\\"chartid\\\": 1,  \\\"name\\\": \\\"Line Chart\\\", \\\"fieldName\\\": null,  \\\"showlabel\\\": true,   \\\"chartcolor\\\": null,  \\\"chartlegend\\\": true, \\\"charturl\\\": \\\"http://43.205.154.152:30179/entityBuilder/Gaurav_testing\\\",\\\"xAxis\\\": \\\"name\\\",\\\"donut\\\": null, \\\"chartparameter\\\": null, \\\"datastore\\\": null,\\\"datasource\\\": null,\\\"id\\\": null,\\\"slices\\\": null,\\\"yAxis\\\": [\\\"pincode\\\"],\\\"charttitle\\\": \\\"Live Details\\\"}]','myfirstdashboard');\n"
				+ "/*!40000 ALTER TABLE `dashboard` ENABLE KEYS */;\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `dashboard_builder_t`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `dashboard_builder_t` (\n"
				+ "  `id` bigint NOT NULL AUTO_INCREMENT,\n" + "  `dashboardname` varchar(255) DEFAULT NULL,\n"
				+ "  PRIMARY KEY (`id`)\n" + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `dashboard_builder_t` DISABLE KEYS */;\n"
				+ "/*!40000 ALTER TABLE `dashboard_builder_t` ENABLE KEYS */;\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `dashboard_schedule_t`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `dashboard_schedule_t` (\n"
				+ "  `id` bigint NOT NULL AUTO_INCREMENT,\n" + "  `attachment` varchar(255) DEFAULT NULL,\n"
				+ "  `cc` varchar(255) DEFAULT NULL,\n" + "  `cron` varchar(255) DEFAULT NULL,\n"
				+ "  `end_time` datetime DEFAULT NULL,\n" + "  `every` varchar(255) DEFAULT NULL,\n"
				+ "  `gateway` varchar(255) DEFAULT NULL,\n" + "  `gatewaydone` varchar(255) DEFAULT NULL,\n"
				+ "  `replacement_string` varchar(255) DEFAULT NULL,\n" + "  `send_to` varchar(255) DEFAULT NULL,\n"
				+ "  `start_time` datetime DEFAULT NULL,\n" + "  `template` varchar(255) DEFAULT NULL,\n"
				+ "  `type` varchar(255) DEFAULT NULL,\n" + "  PRIMARY KEY (`id`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `dashboard_schedule_t` DISABLE KEYS */;\n"
				+ "/*!40000 ALTER TABLE `dashboard_schedule_t` ENABLE KEYS */;\n" + "\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `dashboardaxis`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `dashboardaxis` (\n"
				+ "  `id` bigint NOT NULL AUTO_INCREMENT,\n" + "  `april` varchar(255) DEFAULT NULL,\n"
				+ "  `feb` varchar(255) DEFAULT NULL,\n" + "  `jan` varchar(255) DEFAULT NULL,\n"
				+ "  `march` varchar(255) DEFAULT NULL,\n" + "  `may` varchar(255) DEFAULT NULL,\n"
				+ "  PRIMARY KEY (`id`)\n"
				+ ") ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `dashboardaxis` DISABLE KEYS */;\n"
				+ "INSERT INTO `dashboardaxis` VALUES (1,'5','10','25','3',NULL),(3,'5000','15000','10000','30000',NULL),(4,'5000','15000','10000','30000',NULL),(5,'5000','15000','5','30000',NULL),(6,'5000','15000','10000','30000','2000'),(7,'23','20','25','30','20'),(8,'23','20','25','30','99'),(9,'23','20','25','30','20');\n"
				+ "/*!40000 ALTER TABLE `dashboardaxis` ENABLE KEYS */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `system_paramaters` DISABLE KEYS */;\n"
				+ "INSERT INTO `system_paramaters` VALUES (1,'test1',0,NULL,NULL,NULL,0,NULL,NULL,NULL,0,_binary '',NULL,0,0,0,NULL,NULL,NULL,0,NULL,0,0,0,NULL,NULL,NULL,NULL,0,0);\n"
				+ "/*!40000 ALTER TABLE `system_paramaters` ENABLE KEYS */;\n" + "\n" + "\n"
				+ "DROP TABLE IF EXISTS `sys_param_upload`;\n"
				+ "/*!40101 SET @saved_cs_client     = @@character_set_client */;\n"
				+ "/*!50503 SET character_set_client = utf8mb4 */;\n" + "CREATE TABLE `sys_param_upload` (\n"
				+ "  `attachment_id` int NOT NULL,\n" + "  `attachment` longblob,\n"
				+ "  `attachment_filename` varchar(255) DEFAULT NULL,\n"
				+ "  `attachment_type` varchar(255) DEFAULT NULL,\n" + "  `cancel_status` varchar(255) NOT NULL,\n"
				+ "  `external_flag` varchar(255) DEFAULT NULL,\n" + "  `updated_by` varchar(255) DEFAULT NULL,\n"
				+ "  `sys_param_entity_id` int DEFAULT NULL,\n" + "  PRIMARY KEY (`attachment_id`),\n"
				+ "  KEY `FKl1h7bcgpr1y10ydiqw849vq99` (`sys_param_entity_id`),\n"
				+ "  CONSTRAINT `FKl1h7bcgpr1y10ydiqw849vq99` FOREIGN KEY (`sys_param_entity_id`) REFERENCES `system_paramaters` (`id`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;\n"
				+ "/*!40101 SET character_set_client = @saved_cs_client */;\n" + "\n" + "\n" + "\n"
				+ "/*!40000 ALTER TABLE `sys_param_upload` DISABLE KEYS */;\n"
				+ "/*!40000 ALTER TABLE `sys_param_upload` ENABLE KEYS */;\n" + "\n" + "\n"
				+ "-- Dump completed on 2024-01-27 15:45:01";
		return sql;

	}

}