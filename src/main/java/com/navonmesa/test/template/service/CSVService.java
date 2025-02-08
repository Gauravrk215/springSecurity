package com.realnet.template.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;

@Service
public class CSVService {

	@Value("${projectPath}")
	private String projectpath;
	@Autowired
	private EntityManager entityManager;

	public String generateCSV(String tableName) throws IOException {
		// Get column names dynamically
		String columnQuery = "SELECT column_name FROM information_schema.columns WHERE table_name = :tableName";
		Query query = entityManager.createNativeQuery(columnQuery);
		query.setParameter("tableName", tableName);

		List<String> columnNames = query.getResultList();

		// Query the table data
//		String dataQuery = "SELECT * FROM " + tableName; // for all record
		String dataQuery = "SELECT * FROM " + tableName + " LIMIT 1"; // for only one record

		Query dataQueryExec = entityManager.createNativeQuery(dataQuery);
		List<Object[]> resultList = dataQueryExec.getResultList();

		// Write to CSV
		String pathString = projectpath;
		String filename = "file" + System.currentTimeMillis() + ".csv";
		String filePath = pathString + File.separator + filename; // Update this path as needed
		try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
			writer.writeNext(columnNames.toArray(new String[0]));

			for (Object[] row : resultList) {
				String[] rowArray = new String[row.length];
				for (int i = 0; i < row.length; i++) {
					rowArray[i] = row[i] != null ? row[i].toString() : "";
				}
				writer.writeNext(rowArray);
			}
		}

		return filename;
	}
}
