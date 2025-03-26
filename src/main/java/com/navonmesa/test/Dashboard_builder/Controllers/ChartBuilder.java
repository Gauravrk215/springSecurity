package com.realnet.Dashboard_builder.Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.realnet.Payment.Paytm.PaytmPayment;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@RequestMapping("/chart")
@RestController
public class ChartBuilder {

	public List<Map<String, Object>> getAllDataFromTable(String tableName) {
		List<Map<String, Object>> tableData = new ArrayList<>();

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			// Establish a database connection
			connection = DriverManager.getConnection(
					"jdbc:mysql://realnet.cdtynkxfiu2h.ap-south-1.rds.amazonaws.com:3306/suresetu", "cnsdev",
					"cnsdev1234");

			// Create a SQL statement
			statement = connection.createStatement();

			// Execute the query to retrieve all data from the table
			String query = "SELECT * FROM " + tableName;
			resultSet = statement.executeQuery(query);

			// Retrieve the column names from the result set
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			List<String> columnNames = new ArrayList<>();
			for (int i = 1; i <= columnCount; i++) {
				columnNames.add(metaData.getColumnName(i));
			}

			// Iterate over the result set and store each row in a map
			while (resultSet.next()) {
				Map<String, Object> rowData = new HashMap<>();
				for (String columnName : columnNames) {
					Object value = resultSet.getObject(columnName);
					rowData.put(columnName, value);
				}
				tableData.add(rowData);
			}
		} catch (SQLException e) {
			log.error(e.getLocalizedMessage());
		} finally {
			// Close the resources
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					log.error(e.getLocalizedMessage());
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					log.error(e.getLocalizedMessage());
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					log.error(e.getLocalizedMessage());
				}
			}
		}

		return tableData;
	}

//...........................22.07.2023.............................//

//	@GetMapping(value = "/getdashjson/{job_type}")
//	public ResponseEntity<?> jsonretun(@RequestParam String tableName, @PathVariable String job_type,
//			@RequestParam String xAxis, @RequestParam String yAxis) throws IOException {
//
//		List<Map<String, Object>> tableData = getAllDataFromTable(tableName); // Retrieve all data from the table
//
//		List<Object> yAxisValues = new ArrayList<>();
//		List<String> xAxisValues = new ArrayList<>();
//
//		for (Map<String, Object> row : tableData) {
//			for (Entry<String, Object> entry : row.entrySet()) {
//				String key = entry.getKey();
//				Object value = entry.getValue();
//
//				if (key.equalsIgnoreCase(xAxis)) {
//					xAxisValues.add(value.toString());
//				} else if (key.equalsIgnoreCase(yAxis)) {
//					yAxisValues.add(value);
//				}
//			}
//		}
//
//		StringBuilder jsonmap = new StringBuilder();
//
//		if (job_type.equalsIgnoreCase("Bar Chart")) {
//			jsonmap.append("[\n");
//		} else if (job_type.equalsIgnoreCase("Line Chart")) {
//			jsonmap.append("  {\r\n" + "   \"chartData\": [\r\n" + "    { \"data\": [");
//		} else if (job_type.equalsIgnoreCase("Doughnut Chart")) {
//			jsonmap.append("{\"chartData\": [[");
//		}
//
//		for (int i = 0; i < xAxisValues.size(); i++) {
//			String xValue = xAxisValues.get(i);
//			Object yValue = yAxisValues.get(i);
//
//			if (job_type.equalsIgnoreCase("Bar Chart")) {
//				jsonmap.append("{\"name\": \"" + xValue + "\", \"progress\":\"" + yValue + "\"},\n");
//			} else if (job_type.equalsIgnoreCase("Line Chart")) {
//				jsonmap.append(yValue + ",");
//			} else if (job_type.equalsIgnoreCase("Doughnut Chart")) {
//				jsonmap.append(yValue + ",");
//			}
//		}
//
//		if (!xAxisValues.isEmpty() && !yAxisValues.isEmpty()) {
//			jsonmap.deleteCharAt(jsonmap.lastIndexOf(","));
//		}
//
//		if (job_type.equalsIgnoreCase("Bar Chart")) {
//			jsonmap.append("]");
//		} else if (job_type.equalsIgnoreCase("Line Chart")) {
//			jsonmap.append("], \"label\": \"" + yAxis + "\" }\r\n" + "   ],\r\n" + "   \"chartLabels\": [ ");
//		} else if (job_type.equalsIgnoreCase("Doughnut Chart")) {
//			jsonmap.append("]],\r\n" + " \"chartLabels\": [");
//		}
//
//		for (String xValue : xAxisValues) {
//			jsonmap.append("\"" + xValue + "\",");
//		}
//
//		if (!xAxisValues.isEmpty()) {
//			jsonmap.deleteCharAt(jsonmap.lastIndexOf(","));
//		}
//
//		if (job_type.equalsIgnoreCase("Line Chart")) {
//			jsonmap.append("] \n }\n");
//		} else if (job_type.equalsIgnoreCase("Doughnut Chart")) {
//			jsonmap.append("]\n" + "}");
//		}
//
//		return new ResponseEntity<>(jsonmap.toString(), HttpStatus.CREATED);
//	}

	@GetMapping(value = "/getdashjson/{job_type}")
	public ResponseEntity<?> jsonretun2(@RequestParam String tableName, @PathVariable String job_type,
			@RequestParam(required = false) String xAxis, @RequestParam(required = false) List<String> yAxes)
			throws IOException {

		StringBuilder jsonmap = new StringBuilder();

		if (job_type.equalsIgnoreCase("Grid View")) {
			List<Map<String, Object>> allData = getAllDataFromApi(tableName);

			jsonmap.append("[\n");

			for (Map<String, Object> row : allData) {
				jsonmap.append("{\n");

				int colCount = 0;
				for (String yAxis : yAxes) {
					String key = yAxis;
					Object value = row.get(key);

					jsonmap.append("\"" + key + "\": \"" + value + "\"");

					colCount++;
					if (colCount < yAxes.size()) {
						jsonmap.append(", ");
					}
					jsonmap.append("\n");
				}

				jsonmap.append("}");

				if (!row.equals(allData.get(allData.size() - 1))) {
					jsonmap.append(", ");
				}
			}

			jsonmap.append("\n]\n");

			return new ResponseEntity<>(jsonmap.toString(), HttpStatus.CREATED);
		}

		if (job_type.equalsIgnoreCase("Todo List") && yAxes != null && !yAxes.isEmpty()) {
			List<Map<String, Object>> allData = getAllDataFromApi(tableName);

			String listName = yAxes.get(0); // Assuming the first column in yAxes to be the list name
			List<Object> listData = new ArrayList<>();

			for (Map<String, Object> row : allData) {
				Object value = row.get(listName);

				if (value != null) {
					listData.add(value);
				}
			}

			Map<String, Object> response = new HashMap<>();
			response.put("listName", listName);
			response.put("List", listData);

			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}

		List<Map<String, Object>> tableData = getAllDataFromApi(tableName); // Retrieve all data from the table

		List<List<Object>> yAxisValuesList = new ArrayList<>();
		List<String> xAxisValues = new ArrayList<>();

		// Initialize a list for each y-axis parameter
		for (int i = 0; i < yAxes.size(); i++) {
			yAxisValuesList.add(new ArrayList<>());
		}

		for (Map<String, Object> row : tableData) {
			for (Entry<String, Object> entry : row.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				if (key.equalsIgnoreCase(xAxis)) {
					xAxisValues.add(value.toString());
				} else {
					int yIndex = yAxes.indexOf(key);
					if (yIndex >= 0) {
						yAxisValuesList.get(yIndex).add(value);
					}
				}
			}
		}

		if (job_type.equalsIgnoreCase("Bar Chart")) {
			jsonmap.append("{\n \"barChartData\": [\n");

			for (int j = 0; j < yAxes.size(); j++) {
				String yAxis = yAxes.get(j);

				jsonmap.append("{");
				jsonmap.append("\"data\": [");

				for (int i = 0; i < xAxisValues.size(); i++) {
					Object yValue = yAxisValuesList.get(j).get(i);
					jsonmap.append(yValue);

					if (i < xAxisValues.size() - 1) {
						jsonmap.append(",");
					}
				}

				jsonmap.append("],");
				jsonmap.append("\"label\": \"" + yAxis + "\"");
				jsonmap.append("}");

				if (j < yAxes.size() - 1) {
					jsonmap.append(",");
				}
			}

			jsonmap.append("],\n \"barChartLabels\": [ ");

			for (String xValue : xAxisValues) {
				jsonmap.append("\"" + xValue + "\",");
			}

			if (!xAxisValues.isEmpty()) {
				jsonmap.deleteCharAt(jsonmap.lastIndexOf(","));
			}

			jsonmap.append("] \n }\n");
		}

		else if (job_type.equalsIgnoreCase("Line Chart")) {
			jsonmap.append("{\n \"chartData\": [\n");

			for (int j = 0; j < yAxes.size(); j++) {
				String yAxis = yAxes.get(j);

				jsonmap.append("{");
				jsonmap.append("\"data\": [");

				for (int i = 0; i < xAxisValues.size(); i++) {
					Object yValue = yAxisValuesList.get(j).get(i);
					jsonmap.append(yValue);

					if (i < xAxisValues.size() - 1) {
						jsonmap.append(",");
					}
				}

				jsonmap.append("],");
				jsonmap.append("\"label\": \"" + yAxis + "\"");
				jsonmap.append("}");

				if (j < yAxes.size() - 1) {
					jsonmap.append(",");
				}
			}

			jsonmap.append("],\n \"chartLabels\": [ ");

			for (String xValue : xAxisValues) {
				jsonmap.append("\"" + xValue + "\",");
			}

			if (!xAxisValues.isEmpty()) {
				jsonmap.deleteCharAt(jsonmap.lastIndexOf(","));
			}

			jsonmap.append("] \n }\n");
		} else if (job_type.equalsIgnoreCase("Doughnut Chart")) {
			jsonmap.append("{\"chartData\": [\n");

			for (int j = 0; j < yAxes.size(); j++) {
				String yAxis = yAxes.get(j);

				jsonmap.append("[");

				for (int i = 0; i < xAxisValues.size(); i++) {
					Object yValue = yAxisValuesList.get(j).get(i);
					jsonmap.append(yValue);

					if (i < xAxisValues.size() - 1) {
						jsonmap.append(",");
					}
				}

				jsonmap.append("]");

				if (j < yAxes.size() - 1) {
					jsonmap.append(",");
				}
			}

			jsonmap.append("],\n \"chartLabels\": [");

			for (String xValue : xAxisValues) {
				jsonmap.append("\"" + xValue + "\",");
			}

			if (!xAxisValues.isEmpty()) {
				jsonmap.deleteCharAt(jsonmap.lastIndexOf(","));
			}

			jsonmap.append("]\n}");
		}

		else if (job_type.equalsIgnoreCase("Radar Chart")) {
			jsonmap.append("{\n \"radarChartData\": [\n");

			for (int j = 0; j < yAxes.size(); j++) {
				String yAxis = yAxes.get(j);

				jsonmap.append("{");
				jsonmap.append("\"data\": [");

				for (int i = 0; i < xAxisValues.size(); i++) {
					Object yValue = yAxisValuesList.get(j).get(i);
					jsonmap.append(yValue);

					if (i < xAxisValues.size() - 1) {
						jsonmap.append(",");
					}
				}

				jsonmap.append("],");
				jsonmap.append("\"label\": \"" + yAxis + "\"");
				jsonmap.append("}");

				if (j < yAxes.size() - 1) {
					jsonmap.append(",");
				}
			}

			jsonmap.append("],\n \"radarChartLabels\": [ ");

			for (String xValue : xAxisValues) {
				jsonmap.append("\"" + xValue + "\",");
			}

			if (!xAxisValues.isEmpty()) {
				jsonmap.deleteCharAt(jsonmap.lastIndexOf(","));
			}

			jsonmap.append("] \n }\n");
		}

		else if (job_type.equalsIgnoreCase("PolarArea Chart")) {
			jsonmap.append("{\n \"polarAreaChartData\": [\n");

			for (int j = 0; j < yAxes.size(); j++) {
				String yAxis = yAxes.get(j);

				jsonmap.append("{");
				jsonmap.append("\"data\": [");

				for (int i = 0; i < xAxisValues.size(); i++) {
					Object yValue = yAxisValuesList.get(j).get(i);
					jsonmap.append(yValue);

					if (i < xAxisValues.size() - 1) {
						jsonmap.append(",");
					}
				}

				jsonmap.append("],");
				jsonmap.append("\"label\": \"" + yAxis + "\"");
				jsonmap.append("}");

				if (j < yAxes.size() - 1) {
					jsonmap.append(",");
				}
			}

			jsonmap.append("],\n \"polarAreaChartLabels\": [ ");

			for (String xValue : xAxisValues) {
				jsonmap.append("\"" + xValue + "\",");
			}

			if (!xAxisValues.isEmpty()) {
				jsonmap.deleteCharAt(jsonmap.lastIndexOf(","));
			}

			jsonmap.append("] \n }\n");
		}

		if (job_type.equalsIgnoreCase("Pie Chart")) {
			jsonmap.append("{\n \"pieChartData\": [");

			for (int i = 0; i < yAxisValuesList.get(0).size(); i++) { // Assuming "mark" is the first item in yAxes
				jsonmap.append(yAxisValuesList.get(0).get(i)); // Use the y-axis values

				if (i < yAxisValuesList.get(0).size() - 1) {
					jsonmap.append(",");
				}
			}

			jsonmap.append("],\n \"pieChartLabels\": [ ");

			for (int i = 0; i < xAxisValues.size(); i++) { // Assuming "name" is the x-axis
				jsonmap.append("\"" + xAxisValues.get(i) + "\""); // Use the x-axis values

				if (i < xAxisValues.size() - 1) {
					jsonmap.append(",");
				}
			}

			jsonmap.append("] \n }\n");
		}

		else if (job_type.equalsIgnoreCase("Bubble Chart")) {
			jsonmap.append("{\n \"bubbleChartData\": [\n");

			for (int j = 0; j < yAxes.size(); j++) {
				String yAxis = yAxes.get(j);

				jsonmap.append("{");
				jsonmap.append("\"data\": [");

				for (int i = 0; i < xAxisValues.size(); i++) {
					Object xValue = xAxisValues.get(i);
					Object yValue = yAxisValuesList.get(j).get(i);
					int radius = 5 + (i % 3) * 3; // Adjust the radius as needed

					jsonmap.append("{ \"x\": " + xValue + ", \"y\": " + yValue + ", \"r\": " + radius + "}");

					if (i < xAxisValues.size() - 1) {
						jsonmap.append(",");
					}
				}

				jsonmap.append("],");
				jsonmap.append("\"label\": \"" + yAxis + "\"");
				jsonmap.append("}");

				if (j < yAxes.size() - 1) {
					jsonmap.append(",");
				}
			}

			jsonmap.append("],\n \"bubbleChartLabels\": [ ");

			for (String label : xAxisValues) {
				jsonmap.append("\"" + label + "\",");
			}

			if (!xAxisValues.isEmpty()) {
				jsonmap.deleteCharAt(jsonmap.lastIndexOf(","));
			}

			jsonmap.append("] \n }\n");
		} else if (job_type.equalsIgnoreCase("Scatter Chart")) {
			jsonmap.append("{\n \"scatterChartData\": [\n");

			for (int j = 0; j < yAxes.size(); j++) {
				String yAxis = yAxes.get(j);

				jsonmap.append("{");
				jsonmap.append("\"data\": [");

				for (int i = 0; i < xAxisValues.size(); i++) {
					Object xValue = xAxisValues.get(i);
					Object yValue = yAxisValuesList.get(j).get(i);

					jsonmap.append("{ \"x\": " + xValue + ", \"y\": " + yValue + "}");

					if (i < xAxisValues.size() - 1) {
						jsonmap.append(",");
					}
				}

				jsonmap.append("],");
				jsonmap.append("\"label\": \"" + yAxis + "\"");
				jsonmap.append("}");

				if (j < yAxes.size() - 1) {
					jsonmap.append(",");
				}
			}

			jsonmap.append("],\n \"scatterChartLabels\": [ ");

			for (String label : xAxisValues) {
				jsonmap.append("\"" + label + "\",");
			}

			if (!xAxisValues.isEmpty()) {
				jsonmap.deleteCharAt(jsonmap.lastIndexOf(","));
			}

			jsonmap.append("] \n }\n");
		}

		else if (job_type.equalsIgnoreCase("Dynamic Chart")) {
			jsonmap.append("{\n \"dynamicChartData\": [\n");

			for (int j = 0; j < yAxes.size(); j++) {
				String yAxis = yAxes.get(j);

				jsonmap.append("{");
				jsonmap.append("\"data\": [");

				for (int i = 0; i < xAxisValues.size(); i++) {
					Object yValue = yAxisValuesList.get(j).get(i);
					jsonmap.append(yValue);

					if (i < xAxisValues.size() - 1) {
						jsonmap.append(",");
					}
				}

				jsonmap.append("],");
				jsonmap.append("\"label\": \"" + yAxis + "\"");
				jsonmap.append("}");

				if (j < yAxes.size() - 1) {
					jsonmap.append(",");
				}
			}

			jsonmap.append("],\n \"dynamicChartLabels\": [ ");

			for (String xValue : xAxisValues) {
				jsonmap.append("\"" + xValue + "\",");
			}

			if (!xAxisValues.isEmpty()) {
				jsonmap.deleteCharAt(jsonmap.lastIndexOf(","));
			}

			jsonmap.append("] \n }\n");
		}

		return new ResponseEntity<>(jsonmap.toString(), HttpStatus.CREATED);
	}

//	@GetMapping("/getKey")
	public List<Map<String, Object>> getAllKeyFromApi(String apiUrl) {
		List<Map<String, Object>> apiData = new ArrayList<>();

		try {
			// Make a GET request using the provided URL
			ResponseEntity<Object> responseEntity = GET(apiUrl);

			// Convert the response to a List<Map<String, Object>>
			if (responseEntity.getBody() instanceof List) {
				// If the response is a list, assume it's a list of maps
				apiData = (List<Map<String, Object>>) responseEntity.getBody();
			} else {
				// If the response is not a list, assume it's a single map
				Map<String, Object> singleMap = new HashMap<>();
				singleMap.put("data", responseEntity.getBody());
				apiData.add(singleMap);
			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}

		return apiData;
	}

	@GetMapping("/getAllKeys")
	public Set<String> getAllKeys(@RequestParam String apiUrl) {
		List<Map<String, Object>> apiData = getAllKeyFromApi(apiUrl);
		return getAllKeys(apiData);
	}

	private Set<String> getAllKeys(List<Map<String, Object>> apiData) {
		Set<String> allKeys = new HashSet<>();

		for (Map<String, Object> data : apiData) {
			allKeys.addAll(data.keySet());
		}

		return allKeys;
	}

	public List<Map<String, Object>> getAllDataFromApi(String apiUrl) {
		List<Map<String, Object>> apiData = new ArrayList<>();

		try {
			// Make a GET request using the provided URL
			ResponseEntity<Object> responseEntity = GET(apiUrl);

			// Convert the response to a List<Map<String, Object>>
			if (responseEntity.getBody() instanceof List) {
				// If the response is a list, assume it's a list of maps
				apiData = (List<Map<String, Object>>) responseEntity.getBody();
			} else {
				// If the response is not a list, assume it's a single map
				Map<String, Object> singleMap = new HashMap<>();
				singleMap.put("data", responseEntity.getBody());
				apiData.add(singleMap);
			}

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());

		}

		return apiData;
	}

	public ResponseEntity<Object> GET(String get) {
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Object> u = restTemplate.getForEntity(get, Object.class);

		return u;

	}
}
