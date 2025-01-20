package com.realnet.rb.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.realnet.CredentialDatabase.Service.SurevaultService;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.rb.entity.Rn_Rb_Where_Param;
import com.realnet.rb.entity.Rn_rb_Column;
import com.realnet.rb.entity.Rn_rb_Tables;
import com.realnet.rb.entity.Rn_reportDTO;
import com.realnet.rb.entity.Rn_report_builder;
import com.realnet.rb.repository.Rn_column_Repository;
import com.realnet.rb.repository.Rn_rb_where_param_repository;
import com.realnet.rb.repository.Rn_report_builder_repository;
import com.realnet.rb.repository.Rn_tables_Repository;
import com.realnet.users.entity1.AppUser;
import com.realnet.users.service1.AppUserServiceImpl;
import com.realnet.utils.Constant;

@Service
public class Rn_report_builder_serviceIpml implements Rn_report_builder_service{
	@Autowired
	private Rn_report_builder_repository rn_report_builder_repository;
	
	@Autowired
	private AppUserServiceImpl userService;
	
	@Autowired
	private Rn_column_Repository columnRepo;
	
	@Autowired
	private Rn_tables_Repository tableRepo;
	
	@Autowired
	private Rn_rb_where_param_repository whereRepo;
	
//	@Value("${spring.datasource.username}")
//	private String userName;
//	
//	@Value("${spring.datasource.password}")
//	private String password;
//	
//	@Value("${spring.datasource.url}")
//	private String url;
	
	@Autowired
	private SurevaultService databaseCredentialsService;

	
	@Override
	public Page<Rn_report_builder> getAll(Pageable page) {
		return rn_report_builder_repository.findAll(page);
	}
	
	@Override
	public Rn_report_builder save(Rn_report_builder rn_report_builder) {
		Rn_report_builder savereport = rn_report_builder_repository.save(rn_report_builder);
		return savereport;
	}
	
	@Override
	public Rn_report_builder getById(int  id) {
		Rn_report_builder bcf_extractor = rn_report_builder_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		return bcf_extractor;
	}
	
	@Override
	public List<Rn_report_builder> getByReportId(int  id) {
		List<Rn_report_builder> bcf_extractor = rn_report_builder_repository.getByReportId(id);
		return bcf_extractor;
	}

	@Override
	public boolean saveReport(Rn_reportDTO report, int moduleId) {
		AppUser user = userService.getLoggedInUser();
		Long userId = user.getUserId();
		//Long accountId = user.getSys_account().getId();

		String report_name = report.getReport_name();
		String desc = report.getDescription();
		String tags=report.getReport_tags();
		
		
		Rn_report_builder reportBuilder=new Rn_report_builder();
		reportBuilder.setReport_name(report_name);
		reportBuilder.setDescription(desc);
		reportBuilder.setReport_tags(tags);
		reportBuilder.setModule_id(moduleId);
		save(reportBuilder);
		return true;
	}

	@Override
	public Rn_report_builder saveReport(Rn_report_builder report, int moduleId) {
		AppUser user = userService.getLoggedInUser();
		Long userId = user.getUserId();
		//Long accountId = user.getSys_account().getId();

		String report_name = report.getReport_name();
		String desc = report.getDescription();
		String tags=report.getReport_tags();
		
		
		Rn_report_builder reportBuilder=new Rn_report_builder();
		reportBuilder.setReport_name(report_name);
		reportBuilder.setDescription(desc);
		reportBuilder.setReport_tags(tags);
		reportBuilder.setModule_id(moduleId);
		reportBuilder.setIs_build("N");
		reportBuilder.setIs_updated("N");
	
		
		return save(reportBuilder); 
	}
	
	
	@Override
	public Rn_report_builder updateById(int id, String date_string) {
		
		Rn_report_builder rn_builder = rn_report_builder_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		rn_builder.setDate_string(date_string);
		
		final Rn_report_builder updatedProject = rn_report_builder_repository.save(rn_builder);
		return updatedProject;
	}
	
	@Override
	public Rn_report_builder updateByIdAdhoc(int id, String param_string) {
		
		Rn_report_builder rn_builder = rn_report_builder_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		rn_builder.setAdd_param_string(param_string);
		
		final Rn_report_builder updatedProject = rn_report_builder_repository.save(rn_builder);
		return updatedProject;
	}
	
	@Override
	public Rn_report_builder updateByIdGridHeaders(int id, String param_string) {
		
		Rn_report_builder rn_builder = rn_report_builder_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		rn_builder.setGrid_headers(param_string);
		rn_builder.setGrid_values(param_string);
		
		final Rn_report_builder updatedProject = rn_report_builder_repository.save(rn_builder);
		return updatedProject;
	}
	
	@Override
	public Rn_report_builder updateByIdQuery(int id, Rn_report_builder param_string) {
		
		Rn_report_builder rn_builder = rn_report_builder_repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
		
		StringBuilder sb=new StringBuilder();
		StringBuilder sb2=new StringBuilder();
		StringBuilder sb3=new StringBuilder();
		
		//get tables 
		List<Rn_rb_Tables> tableList=tableRepo.getTablesByReport(id);
		for (int i = 0; i < tableList.size(); i++) {
			String table_name=tableList.get(i).getTable_name();
		   	String table_allias_name=tableList.get(i).getTable_allias_name();
		   	if(i ==0)
		   	{
		   	   sb.append(table_name+" "+table_allias_name);
		   	}else{
		   	   sb.append(","+table_name+" "+table_allias_name);
		   	}
		}
		
		//get columns
		List<Rn_rb_Column> columnList=columnRepo.getColumnByReport(id);
		for (int i = 0; i < columnList.size(); i++) {
			   String column_name=columnList.get(i).getColumn_name();
			   String table_allies_name=columnList.get(i).getTable_allies_name();
			   String column_allias_name=columnList.get(i).getColumn_allias_name();
			   String asc_desc=columnList.get(i).getAsc_desc();
			   String function=columnList.get(i).getFunctions();
			   if(function != null && !function.isEmpty())
				{
				   sb2.append(function+"("+table_allies_name+"."+column_name+")"+column_allias_name);

				}else
				{
				  if(i ==0)
				  {
				      sb2.append(column_name+" "+column_allias_name);
				  }else{
					  sb2.append(","+column_name+" "+column_allias_name);
				  }
				}
			
		}
		//where condition
		List<Rn_Rb_Where_Param> whereList=whereRepo.getWhereByReport(id);
		for (int i = 0; i < whereList.size(); i++) {
			String explecity=whereList.get(i).getExplecity();
	   		String table_column_name1=whereList.get(i).getWhere_coloumn1_tbl_alias_name();
   			String column1=whereList.get(i).getWhere_coloumn();
   			String condition=whereList.get(i).getWhere_condition();
   			String switch_control=whereList.get(i).getSwitch_control();
   			String table_column_name2=whereList.get(i).getWhere_coloumn2_tbl_alias_name();
   			String column2=whereList.get(i).getWhere_coloumn2_tbl_alias_name();
   			
   			if(explecity!=null) {
   				sb3.append(" and "+table_column_name1+"."+column1+"="+table_column_name2+"."+column2);
   			}
   			else {
   				sb3.append("");
   			}
   			
			
		}
			
			String sql6="select "+sb2+" from "+sb+" where 1=1 "+sb3+"";

		
		
		//sb.append("select book.author author_1,gb_records_t.name name_2,book.title title_3 from book book,gb_records_t gb_records_t where 1=1 ");
		//String master_query=sb.toString();
		
		rn_builder.setMaster_select(sql6);
		final Rn_report_builder updatedProject = rn_report_builder_repository.save(rn_builder);
		return updatedProject;
	}
	
	
	
//	@Override
//	public List<String> getQueryData(String query) {
//		String sql_query = query;
//		List<String> list=new ArrayList<String>(); 
//		try (
//				Connection con =DriverManager.getConnection(url,userName,password);
//				
//			  //Statement stmt = con.createStatement();
//			  PreparedStatement ps = con.prepareStatement(query);) {
//		      ResultSet rs = ps.executeQuery();
//		      java.sql.ResultSetMetaData rsm = rs.getMetaData();
//		      System.out.println("matadata::"+rsm);
//			  int rscount = rsm.getColumnCount();
//		      while (rs.next()) {
//					for (int i = 1; i <= rscount; i++) {
//						String data= rs.getString(i); 
//						list.add(data);
//					}
//				}
//		      
//		    } catch (SQLException e) {
//		      e.printStackTrace();
//		    }
//		return list;
//	}
	
	
	@Override
	public List<Map<String, Object>> getQueryData(String query) {
		String sql_query = query;
		List<Map<String, Object>> list = new ArrayList<>(); 
		String url=null; 
		String userName=null;
		String password=null;
		try {
			userName = databaseCredentialsService.getSurevaultCredentials("databaseuserName");
			url = databaseCredentialsService.getSurevaultCredentials("databaseUrl");
			password = databaseCredentialsService.getSurevaultCredentials("databasePassword");
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (
				
			//	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				
				
			  Connection con =DriverManager.getConnection(url,userName,password);
				
			  //Statement stmt = con.createStatement();
			  PreparedStatement ps = con.prepareStatement(query);) {
		      ResultSet rs = ps.executeQuery();
		      java.sql.ResultSetMetaData rsm = rs.getMetaData();
		      System.out.println("matadata::"+rsm);
			  int rscount = rsm.getColumnCount();
		      while (rs.next()) {
		    	  Map<String, Object> row = new HashMap<>();
					for (int i = 1; i <= rscount; i++) {
						//String data= rs.getString(i); 
						//list.add(data);
						String colName = rsm.getColumnName(i);
		    	        Object colVal = rs.getObject(i);
		    	        row.put(colName,colVal);
					}
					list.add(row);
					
				}
		      
		    } catch (SQLException e) {
		      e.printStackTrace();
		    }
		return list;
	}

	@Override
	public Rn_report_builder saveReportservice(Rn_report_builder report, int moduleId) {
		AppUser user = userService.getLoggedInUser();
		Long userId = user.getUserId();
		//Long accountId = user.getSys_account().getId();

		String report_name = report.getReport_name();
		String desc = report.getDescription();
		String tags=report.getReport_tags();
		String servicename=report.getServicename();
		
		
		Rn_report_builder reportBuilder=new Rn_report_builder();
		reportBuilder.setReport_name(report_name);
		reportBuilder.setDescription(desc);
		reportBuilder.setReport_tags(tags);
		reportBuilder.setModule_id(moduleId);
		reportBuilder.setIs_build("N");
		reportBuilder.setIs_updated("N");
		reportBuilder.setServicename(servicename);
	
		
		return save(reportBuilder); 
	}

	@Override
	public Rn_report_builder updatereport(int reportid,Rn_report_builder reportdata) {
		
		Rn_report_builder oldrn_builder = rn_report_builder_repository.findById(reportid)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + reportid));
			
		oldrn_builder.setReport_name(reportdata.getReport_name());
		oldrn_builder.setReport_tags(reportdata.getReport_tags());
		oldrn_builder.setDescription(reportdata.getDescription());
		oldrn_builder.setServicename(reportdata.getServicename());
		
		
		Rn_report_builder newreport = rn_report_builder_repository.save(oldrn_builder);
		
		
		return newreport;
		
		
	}
	
	
	
	
	
	
	
	
	
//	@Override
//	public List<String> getQueryData(String query) {
//		List<String> queryResult = rn_report_builder_repository.getQueryData(query)
//				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :" + id));
//		return queryResult;
//	}
	
}
