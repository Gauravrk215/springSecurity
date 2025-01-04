package com.realnet.BulkUpload.Controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.realnet.BulkUpload.Entity.MappingRule;
import com.realnet.BulkUpload.Services.MappingRuleService;

@RestController
@RequestMapping("/MappingRule")
public class MappingRuleController {
	@Autowired
	private MappingRuleService Service;

	@PostMapping("/MappingRule")
	public MappingRule Savedata(@RequestBody MappingRule data) {
		MappingRule save = Service.Savedata(data);
		return save;
	}

	@GetMapping("/MappingRule")
	public List<MappingRule> getdetails() {
		List<MappingRule> get = Service.getdetails();
		return get;
	}

	@GetMapping("/MappingRule/{id}")
	public MappingRule getdetailsbyId(@PathVariable Long id) {
		MappingRule get = Service.getdetailsbyId(id);
		return get;

	}

	@DeleteMapping("/MappingRule/{id}")
	public void delete_by_id(@PathVariable Long id) {
		Service.delete_by_id(id);

	}

	@PutMapping("/MappingRule/{id}")
	public MappingRule update(@RequestBody MappingRule data, @PathVariable Long id) {
		MappingRule update = Service.update(data, id);
		return update;
	}

	@GetMapping("/getHeaders")
    public ResponseEntity<?> getFileHeadersForSheet(
            @RequestParam("file") MultipartFile excelFile,
            @RequestParam("sheetName") String sheetName) {
        try (Workbook workbook = WorkbookFactory.create(excelFile.getInputStream())) {
            List<String> sheetHeaders = new ArrayList<>();
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                return ResponseEntity.notFound().build(); // Sheet not found
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow != null) {
                for (Cell cell : headerRow) {
                    String header = cell.getStringCellValue();
                    sheetHeaders.add(header);
                }
            }

            return ResponseEntity.ok(sheetHeaders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Excel file.");
        }
    }

//	@GetMapping("/getHeadersMapping")
//	public ResponseEntity<?> getAllSheetHeadersForMapping(@RequestParam("file") MultipartFile excelFile,@RequestParam("sheetName") String sheetName) {
//		try (Workbook workbook = WorkbookFactory.create(excelFile.getInputStream())) {
//			Map<String, List<Map<String, String>>> sheetHeadersMap = new LinkedHashMap<>();
//
//			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
//				Sheet sheet = workbook.getSheet(sheetName);
//				List<Map<String, String>> sheetHeaders = new ArrayList<>();
//
//				Row headerRow = sheet.getRow(0);
//
//				if (headerRow != null) {
//					for (Cell cell : headerRow) {
//						String header = cell.getStringCellValue();
//						Map<String, String> headerObject = new LinkedHashMap<>();
//						headerObject.put("headerName", header);
//						headerObject.put("value", ""); // Empty value
//						sheetHeaders.add(headerObject);
//					}
//				}
//
//				sheetHeadersMap.put(sheet.getSheetName(), sheetHeaders);
//			}
//
//			return ResponseEntity.ok(sheetHeadersMap);
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Excel file.");
//		}
//	}

}