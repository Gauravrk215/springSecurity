package com.realnet.rb.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.realnet.rb.entity.Rn_rb_Tables;
import com.realnet.rb.entity.Rn_report_builder;
import com.realnet.rb.repository.Rn_report_builder_repository;
import com.realnet.rb.repository.Rn_tables_Repository;

@Service
public class Rn_rb_tables_serviceImpl implements Rn_rb_tables_service {

	@Autowired
	private Rn_report_builder_repository rn_table_repository;

	@Autowired
	private Rn_tables_Repository rn_repo;

	@Value("${spring.datasource.username}")
	private String userName;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${spring.datasource.url}")
	private String url;

	@Override
	public Rn_report_builder save(Rn_report_builder rn_tables) {
		Rn_report_builder savedTables = rn_table_repository.save(rn_tables);
		return savedTables;
	}

	@Override
	public List<Rn_rb_Tables> save(List<Rn_rb_Tables> rn_tables) {
		List<Rn_rb_Tables> savedTables = rn_repo.saveAll(rn_tables);
		return savedTables;
	}

	@Override
	public List<String> getListOfTables() {

		String url = null;
		String userName = null;
		String password = null;

		String query = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'realnet_CNSBE'";
		List<String> list = new ArrayList<String>();

		try (Connection con = DriverManager.getConnection(url, userName, password);

				Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("table list is" + rs);
			while (rs.next()) {
				String coffeeName = rs.getString("table_name");
				list.add(coffeeName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<String> getListOfColumns(int id) {
		// Connection con = null;
		// Connection con=null;

		String url = null;
		String userName = null;
		String password = null;

		String query = "SELECT table_allias_name FROM rn_rb_tables_t WHERE report_id=" + id + "";
		List<String> list = new ArrayList<String>();
		try (Connection con = DriverManager.getConnection(url, userName, password);

				Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String coffeeName = rs.getString("table_allias_name");
				list.add(coffeeName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<String> getColumnList(int id) {

		String url = null;
		String userName = null;
		String password = null;

		String query = "SELECT column_name FROM rn_rb_column_t WHERE report_id=" + id + "";
		List<String> list = new ArrayList<String>();
		try (Connection con = DriverManager.getConnection(url, userName, password);

				Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String coffeeName = rs.getString("column_name");
				list.add(coffeeName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<String> getColumnAliasList(String name) {

		String url = null;
		String userName = null;
		String password = null;

		String query = "SELECT column_name FROM information_schema.columns WHERE TABLE_SCHEMA='realnet_CNSBE' and table_name = '"
				+ name + "' ";
		List<String> list = new ArrayList<String>();
		try (Connection con = DriverManager.getConnection(url, userName, password);

				Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String coffeeName = rs.getString("column_name");
				list.add(name + "." + coffeeName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// get all column
	@Override
	public List<String> getColumnAliasList1(String table_schema, String tABLE_NAME) {

		String url = null;
		String userName = null;
		String password = null;

		String query = "SELECT column_name FROM information_schema.columns WHERE TABLE_SCHEMA='" + table_schema
				+ "' and table_name = '" + tABLE_NAME + "' ";
		List<String> list = new ArrayList<String>();
		try (Connection con = DriverManager.getConnection(url, userName, password);

				Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String coffeeName = rs.getString("column_name");
				list.add(coffeeName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// get all table list from databse
	@Override
	public List<String> getListOftable(String table_schema) {

		String url = null;
		String userName = null;
		String password = null;

		String query = "SELECT table_name FROM information_schema.tables WHERE table_schema='" + table_schema + "' ";

		List<String> list = new ArrayList<String>();
		try (Connection con = DriverManager.getConnection(url, userName, password);

				Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String coffeeName = rs.getString("table_name");
				list.add(coffeeName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<String> getListOftables() {

		String url = null;
		String userName = null;
		String password = null;

		String query = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'realnet_CNSBE'";
		List<String> list = new ArrayList<String>();
		try (Connection con = DriverManager.getConnection(url, userName, password);

				Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("table list is" + rs);
			while (rs.next()) {
				String coffeeName = rs.getString("table_name");
				list.add(coffeeName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<String> getdatabaseList() {

		String url = null;
		String userName = null;
		String password = null;

		String query = "select schema_name from information_schema.schemata";
		List<String> list = new ArrayList<String>();
		try (Connection con = DriverManager.getConnection(url, userName, password);

				Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("table list is" + rs);
			while (rs.next()) {
				String coffeeName = rs.getString("table_name");
				list.add(coffeeName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<String> getColumnAliasList2(String tableNames) {

		String url = null;
		String userName = null;
		String password = null;

		List<String> list = new ArrayList<>();
		String[] tableArray = tableNames.split(",");
		int tableIndex = 1; // Initialize the table index
		for (int i = 0; i < tableArray.length; i++) {
			String tableName = tableArray[i].trim();
			String tableAlias = generateTableAlias(tableIndex);
			String query = "SELECT CONCAT('" + tableAlias
					+ "', column_name) AS full_column_name FROM information_schema.columns WHERE table_name = '"
					+ tableName + "'";
			try (Connection con = DriverManager.getConnection(url, userName, password);
					Statement stmt = con.createStatement()) {
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					String columnName = rs.getString("full_column_name");
					list.add(columnName);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			tableIndex++;
		}
		return list;
	}

	// Method to generate the table alias based on the table index
	private String generateTableAlias(int tableIndex) {
		StringBuilder sb = new StringBuilder();
		// Calculate the suffix based on the table index
		int suffixIndex = (tableIndex - 1) / 26; // e.g., 0 for table 1-26, 1 for table 27-52, etc.
		// Add prefix to the table alias
		sb.append((char) ('a' + (tableIndex - 1) % 26));
		// Add suffix to the table alias
		for (int i = 0; i < suffixIndex; i++) {
			sb.append('a');
		}
		sb.append('.');
		return sb.toString();
	}

	@Override
	public List<Integer> createdatabase(String table_schema) throws JsonProcessingException {

		String query = "CREATE SCHEMA " + table_schema + ";";

		List<Integer> list = new ArrayList<Integer>();
		try (Connection con = DriverManager.getConnection(url, userName, password); // conn.str

				Statement stmt = con.createStatement()) {
			int rs = stmt.executeUpdate(query);

			list.add(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return list;

	}

	@Override
	public List<String> getallcolwithalltable(String table_schema, String str) {
		ArrayList<String> tables = new ArrayList<>();

		String liString = "," + str;
		int i = 0;
		do {

			int lastIndexOf = liString.lastIndexOf(",");

			String substring = liString.substring(lastIndexOf + 1);
			tables.add(substring);

			System.out.println(substring);

			liString = liString.substring(0, lastIndexOf);

			System.out.println("step " + i + " = " + liString);
			i++;

		} while (liString.contains(","));

		ArrayList<String> arrayList = new ArrayList<>();
		for (String TABLE_NAME : tables) {
			List<String> list = getColumnAliasList1(table_schema, TABLE_NAME);
			if (!list.isEmpty()) {
				list.forEach(l -> arrayList.add(l));
			}

		}
		return arrayList;
	}

}
