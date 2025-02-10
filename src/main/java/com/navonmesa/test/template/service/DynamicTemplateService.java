package com.realnet.template.service;

import org.apache.poi.ss.usermodel.*;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class DynamicTemplateService {

//    public List<JSONObject> processTemplate(MultipartFile file) throws IOException {
//        BufferedReader br;
//        InputStream is = file.getInputStream();
//        br = new BufferedReader(new InputStreamReader(is));
//
//        Workbook workbook = WorkbookFactory.create(is);
//
//        DataFormatter dataFormatter = new DataFormatter();
//
//        Sheet sheet = workbook.getSheetAt(0);
//
//        int cols = sheet.getRow(0).getLastCellNum();
//        int firstRowNum = sheet.getFirstRowNum();
//
//        List<String> keys = new ArrayList<>();
//        List<JSONObject> jsonList = new ArrayList<>();
//
//        for (Row row : sheet) {
//            if (row.getRowNum() == firstRowNum) {
//                for (int i = 0; i < cols; i++) {
//                    String value = dataFormatter.formatCellValue(row.getCell(i));
//                    keys.add(value);
//                }
//            } else {
//                JSONObject object = new JSONObject();
//
//                for (int i = 0; i < cols; i++) {
//                    String key = keys.get(i);
//                    String value = dataFormatter.formatCellValue(row.getCell(i));
//                    object.put(key, value);
//                }
//
//                jsonList.add(object);
//            }
//        }
//
//        workbook.close();
//
//        return jsonList;
//    }
//	String fromSheet=dh;
//	String fromacolumn=
	
	
	public List<JSONObject> processTemplate(MultipartFile file) throws IOException {
	    InputStream is = file.getInputStream();
	    Workbook workbook = WorkbookFactory.create(is);
	    Sheet sheet = workbook.getSheetAt(0);
	    DataFormatter dataFormatter = new DataFormatter();

	    List<String> keys = new ArrayList<>();
	    List<JSONObject> jsonList = new ArrayList<>();

	    for (Row row : sheet) {
	        if (row.getRowNum() == 0) {
	            // Read the header row and populate the keys list
	            for (Cell cell : row) {
	                String value = dataFormatter.formatCellValue(cell);
	                keys.add(value);
	            }
	        } else {
	            // Skip the row if all cells are empty
	            boolean allCellsEmpty = true;
	            JSONObject object = new JSONObject();

	            for (int i = 0; i < keys.size(); i++) {
	                String key = keys.get(i);
	                Cell cell = row.getCell(i);
	                String value = dataFormatter.formatCellValue(cell);

	                object.put(key, value);

	                if (!value.isEmpty()) {
	                    allCellsEmpty = false;
	                }
	            }

	            if (!allCellsEmpty) {
	                jsonList.add(object);
	            }
	        }
	    }

	    workbook.close();
	    return jsonList;
	}

}
