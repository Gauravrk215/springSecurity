package com.realnet.rb.Controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.fnd.entity.Error;
import com.realnet.fnd.entity.ErrorPojo;
import com.realnet.fnd.entity.Success;
import com.realnet.fnd.entity.SuccessPojo;
import com.realnet.rb.entity.RnTableListDto;
import com.realnet.rb.entity.Rn_Rb_Adhoc;
import com.realnet.rb.entity.Rn_Rb_Date_String;
import com.realnet.rb.entity.Rn_Rb_Std_Param;
import com.realnet.rb.entity.Rn_Rb_Where_Param;
import com.realnet.rb.entity.Rn_rb_Column;
import com.realnet.rb.entity.Rn_rb_Tables;
import com.realnet.rb.entity.Rn_report_builder;
import com.realnet.rb.response.Rn_adhocParamResponse;
import com.realnet.rb.response.Rn_columnResponse;
import com.realnet.rb.response.Rn_dateParamResponse;
import com.realnet.rb.response.Rn_report_builder_response;
import com.realnet.rb.response.Rn_stdParamResponse;
import com.realnet.rb.response.Rn_tableResponse;
import com.realnet.rb.response.Rn_whereParamResponse;
import com.realnet.rb.service.Rn_rb_adhoc_param_service;
import com.realnet.rb.service.Rn_rb_column_service;
import com.realnet.rb.service.Rn_rb_date_param_service;
import com.realnet.rb.service.Rn_rb_std_param_service;
import com.realnet.rb.service.Rn_rb_tables_service;
import com.realnet.rb.service.Rn_rb_where_param_service;
import com.realnet.rb.service.Rn_report_builder_service;
import com.realnet.utils.Constant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Rn_report_builder" })
public class Rn_report_builder_Controller {

	@Autowired
	private Rn_report_builder_service rn_report_builder_service;

	@Autowired
	private Rn_rb_tables_service rn_table_service;

	@Autowired
	private Rn_rb_column_service rn_column_service;

	@Autowired
	private Rn_rb_where_param_service where_param_service;

	@Autowired
	private Rn_rb_date_param_service date_param_service;

	@Autowired
	private Rn_rb_adhoc_param_service adhoc_param_service;

	@Autowired
	private Rn_rb_std_param_service std_param_service;

	@Value("${projectPath}")
	private String projectPath;

	// GET ALL Data for grid view
	@ApiOperation(value = "List of Reports", response = Rn_report_builder_response.class)
	@GetMapping("/report-builder")
	public Rn_report_builder_response getMenuGroups(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		// sorted data
		Pageable paging = PageRequest.of(page, size, Sort.by("createdAt").descending());
		Page<Rn_report_builder> result = rn_report_builder_service.getAll(paging);

		Rn_report_builder_response resp = new Rn_report_builder_response();
		resp.setPageStats(result, true);
		resp.setItems(result.getContent());
		return resp;
	}

	// Save report
//	@ApiOperation(value = "Save A New Report", response = Rn_report_builder.class)
//	@PostMapping("/report-builder")
//	public ResponseEntity<Rn_report_builder> createReport(
//
//			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
//
//			@Valid @RequestBody Rn_report_builder rn_report_builder) {
//		Rn_report_builder savedReport = rn_report_builder_service.save(rn_report_builder);
//		return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
//	}

	// GET data my module id
//	@ApiOperation(value = "List of Reports", response = CustomResponse.class)
//	@GetMapping("/report-builder-by-id")
//	public CustomResponse getWireframes(@RequestParam(value = "moduleId") Integer moduleId,
//			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
//			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
//		Rn_Module_Setup module = moduleService.getById(moduleId);
//		List<Rn_report_builder> headers = module.getRn_report_builder();
//		// sorted data
//		// System.out.println(headers);
//		Pageable paging = PageRequest.of(page, size, Sort.by(Constant.SORT_BY_CREATION_DATE).descending());
//		Page<Rn_report_builder> result = new PageImpl<>(headers, paging, headers.size());
//		CustomResponse resp = new CustomResponse();
//		resp.setPageStats(result, true);
//		resp.setItems(result.getContent());
//		return resp;
//	}

	@ApiOperation(value = "List of Reports", response = Rn_report_builder.class)
	@GetMapping("/report-data")
	public ResponseEntity<?> getFLFDetails(@RequestParam(value = "report_id") Integer report_id) {
		Rn_report_builder flf_header = rn_report_builder_service.getById(report_id);
		if (flf_header == null) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.FLF_API_TITLE);
			error.setMessage(Constant.NOT_FOUND_EXCEPTION);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		// else {
		return new ResponseEntity<Rn_report_builder>(flf_header, HttpStatus.OK);
		// }

	}

	/*--------- save report------------ */
//	@ApiOperation(value = "Add new Report")
//	@PostMapping(value = "/report-builder")
//	public ResponseEntity<?> saveReport(@RequestParam(value = "moduleId") Integer moduleId,
//			@Valid @RequestBody Rn_reportDTO report) throws IOException {
//		System.out.println("report controller start" + report.getReport_name());
//
//		Rn_reportDTO status = rn_report_builder_service.saveReport(report, moduleId);
//		if (status) {
//			SuccessPojo successPojo = new SuccessPojo();
//			Success success = new Success();
//			success.setTitle(Constant.REPORT_API_TITLE);
//			success.setMessage(Constant.REPORT_CREATED_SUCCESSFULLY);
//			successPojo.setSuccess(success);
//			
//			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
//		} else {
//			ErrorPojo errorPojo = new ErrorPojo();
//			Error error = new Error();
//			error.setTitle(Constant.REPORT_API_TITLE);
//			error.setMessage(Constant.REPORT_NOT_CREATED);
//			errorPojo.setError(error);
//			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
//		}
//	}

	@ApiOperation(value = "Add new Report")
	@PostMapping(value = "/report-builder")
	public ResponseEntity<?> saveReport(@RequestParam(value = "moduleId") Integer moduleId,
			@Valid @RequestBody Rn_report_builder report) throws IOException {
		System.out.println("report controller start" + report.getReport_name());

		System.out.println("service name" + report.getServicename());

		Rn_report_builder status = rn_report_builder_service.saveReport(report, moduleId);
		if (status.getId() != 0) {
			return ResponseEntity.status(HttpStatus.OK).body(status);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you have inserted wrong data");
		}
	}

	/*
	 * @ApiOperation(value = "Add A Tables", response = Rn_tableResponse.class)
	 * 
	 * @PostMapping("/add-tables") public ResponseEntity<Rn_report_builder>
	 * createDynamicTx(
	 * 
	 * @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String
	 * authToken,
	 * 
	 * @RequestParam(value = "reportId") Integer reportId, @Valid @RequestBody
	 * Rn_report_builder rn_tables) { Rn_rb_Tables tt = new Rn_rb_Tables();
	 * tt.setTables_id(reportId);
	 * 
	 * System.out.println("Report id::" + reportId);
	 * 
	 * for(Rn_report_builder rn_table:rn_tables) { rn_table.setReport_id(reportId);
	 * }
	 * 
	 * Rn_report_builder savedTables = rn_table_service.save(rn_tables); if
	 * (savedTables == null) { throw new
	 * ResourceNotFoundException("Tables Not Saved"); } return
	 * ResponseEntity.status(HttpStatus.CREATED).body(savedTables); }
	 */

	// Save tables
	@ApiOperation(value = "Add A Tables", response = Rn_tableResponse.class)
	@PostMapping("/add-alltables")
	public ResponseEntity<List<Rn_rb_Tables>> createTables(

			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,

			@RequestParam(value = "reportId") Integer reportId, @Valid @RequestBody List<Rn_rb_Tables> rn_tables) {

		System.out.println("Report id::" + reportId);

		for (Rn_rb_Tables rn_table : rn_tables) {
			System.out.println("table name::" + rn_table.getTable_name());
			System.out.println("table alias name::" + rn_table.getTable_allias_name());
			rn_table.setReport_id(reportId);
		}
		List<Rn_rb_Tables> savedTables = rn_table_service.save(rn_tables);
		if (savedTables == null) {
			throw new ResourceNotFoundException("Tables Not Saved");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(savedTables);
	}

	// Save Columns
	@ApiOperation(value = "Add A Columns", response = Rn_columnResponse.class)
	@PostMapping("/add-allcolumn")
	public ResponseEntity<List<Rn_rb_Column>> createColumn(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@RequestParam(value = "reportId") Integer reportId, @Valid @RequestBody List<Rn_rb_Column> rn_tables) {

		System.out.println("Report id::" + reportId);

		for (Rn_rb_Column rn_table : rn_tables) {
			rn_table.setReport_id(reportId);
		}
		List<Rn_rb_Column> savedTables = rn_column_service.save(rn_tables);
		if (savedTables == null) {
			throw new ResourceNotFoundException("Tables Not Saved");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(savedTables);
	}

	// save where param
	@ApiOperation(value = "Add A where param", response = Rn_whereParamResponse.class)
	@PostMapping("/add-where-param")
	public ResponseEntity<List<Rn_Rb_Where_Param>> createWhereParam(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@RequestParam(value = "reportId") Integer reportId, @Valid @RequestBody List<Rn_Rb_Where_Param> rn_tables) {

		System.out.println("Report id::" + reportId);

		for (Rn_Rb_Where_Param rn_table : rn_tables) {
			rn_table.setReport_id(reportId);
		}
		List<Rn_Rb_Where_Param> savedTables = where_param_service.save(rn_tables);
		if (savedTables == null) {
			throw new ResourceNotFoundException("Tables Not Saved");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(savedTables);
	}

	// save date param
	@ApiOperation(value = "Add A Date param", response = Rn_dateParamResponse.class)
	@PostMapping("/add-date-param")
	public ResponseEntity<List<Rn_Rb_Date_String>> createDateParam(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@RequestParam(value = "reportId") Integer reportId, @Valid @RequestBody List<Rn_Rb_Date_String> rn_tables) {

		System.out.println("Report id::" + reportId);
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (Rn_Rb_Date_String rn_table : rn_tables) {
			count++;
			rn_table.setReport_id(reportId);
			// sb.append("<option>"+rn_table.getCol_date_query()+"</option>");
			if (count == 1) {
				sb.append(rn_table.getCol_date_query());
			} else {
				sb.append("," + rn_table.getCol_date_query());
			}

		}
		String date_string = sb.toString();
		System.out.println("Date query:" + date_string);
		// update report table
		Rn_report_builder updatedFunction = rn_report_builder_service.updateById(reportId, date_string);

		List<Rn_Rb_Date_String> savedTables = date_param_service.save(rn_tables);
		if (savedTables == null) {
			throw new ResourceNotFoundException("Tables Not Saved");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(savedTables);
	}

	// save adhoc param
	@ApiOperation(value = "Add A Adhoc param", response = Rn_adhocParamResponse.class)
	@PostMapping("/add-adhoc-param")
	public ResponseEntity<List<Rn_Rb_Adhoc>> createAdhocParam(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@RequestParam(value = "reportId") Integer reportId, @Valid @RequestBody List<Rn_Rb_Adhoc> rn_tables) {

		System.out.println("Report id::" + reportId);
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (Rn_Rb_Adhoc rn_table : rn_tables) {
			count++;
			rn_table.setReport_id(reportId);
			// sb.append("<option>"+rn_table.getColumn_name()+"</option>");
			if (count == 1) {
				sb.append(rn_table.getColumn_name());
			} else {
				sb.append("," + rn_table.getColumn_name());
			}

		}
		String date_string = sb.toString();
		Rn_report_builder updatedFunction = rn_report_builder_service.updateByIdAdhoc(reportId, date_string);

		List<Rn_Rb_Adhoc> savedTables = adhoc_param_service.save(rn_tables);
		if (savedTables == null) {
			throw new ResourceNotFoundException("Tables Not Saved");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(savedTables);
	}

	// save std param
	@ApiOperation(value = "Add A std param", response = Rn_stdParamResponse.class)
	@PostMapping("/add-std-param")
	public ResponseEntity<List<Rn_Rb_Std_Param>> createStdParam(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@RequestParam(value = "reportId") Integer reportId, @Valid @RequestBody List<Rn_Rb_Std_Param> rn_tables) {

		System.out.println("Report id::" + reportId);
		StringBuilder sb = new StringBuilder();
		for (Rn_Rb_Std_Param rn_table : rn_tables) {
			rn_table.setReport_id(reportId);
			sb.append(rn_table.getCol_std_para_query() + ",");
		}
		String date_string = sb.toString();
		Rn_report_builder updatedFunction = rn_report_builder_service.updateByIdGridHeaders(reportId, date_string);

		List<Rn_Rb_Std_Param> savedTables = std_param_service.save(rn_tables);
		if (savedTables == null) {
			throw new ResourceNotFoundException("Tables Not Saved");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(savedTables);
	}

	// save std param
//				@ApiOperation(value = "Add A query", response = Rn_stdParamResponse.class)
//				@PostMapping("/add-master-query")
//				public ResponseEntity<Rn_report_builder> updateMasterQuery(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
//						@RequestParam(value = "reportId") Integer reportId) {
//
//					System.out.println("Report id::" + reportId);
//					StringBuilder sb=new StringBuilder();
//					sb.append("select * from table");
//					String date_string=sb.toString();
//					Rn_report_builder updatedQuery = rn_report_builder_service.updateByIdQuery(reportId, date_string);
//
//					//List<Rn_Rb_Std_Param> savedTables = std_param_service.save(rn_tables);
//					if (updatedQuery == null) {
//						throw new ResourceNotFoundException("Tables Not Saved");
//					}
//					return ResponseEntity.status(HttpStatus.CREATED).body(updatedQuery);
//				}

	@ApiOperation(value = "Update A Report", response = Rn_report_builder.class)
	@PutMapping("/add-master-query/{id}")
	public ResponseEntity<Rn_report_builder> updateFunction(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@PathVariable(value = "id") Integer id, @Valid @RequestBody Rn_report_builder rn_function_register) {
		System.out.println("report id::::" + id);
		Rn_report_builder updatedFunction = rn_report_builder_service.updateByIdQuery(id, rn_function_register);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedFunction);
	}

	// get list of tables available in database
	@ApiOperation(value = "Get Table List ", response = RnTableListDto.class)
	@GetMapping("/table-list")
	@ResponseBody
	public List<String> getTableList() {
		// List<Rn_Bcf_Technology_Stack> technologyStack =
		// technologyStackService.getAll();
		List<String> activeTechDTO = rn_table_service.getListOfTables();
		return activeTechDTO;
	}

	// get all table list available in realnet_cnsbe database
	@ApiOperation(value = "Get Table List ", response = RnTableListDto.class)
	@GetMapping("/Table_list")
	@ResponseBody
	public List<String> gettableList() {
		// List<Rn_Bcf_Technology_Stack> technologyStack =
		// technologyStackService.getAll();
		List<String> activeTechDTO = rn_table_service.getListOftables();
		return activeTechDTO;
	}

	// get list of tables available in database by id
	@ApiOperation(value = "Get Table List ", response = RnTableListDto.class)
	@GetMapping("/table-list/{id}")
	@ResponseBody
	public List<String> getTableListAlise(@PathVariable(value = "id") int id) {
		// List<Rn_Bcf_Technology_Stack> technologyStack =
		// technologyStackService.getAll();
		List<String> activeTechDTO = rn_table_service.getListOfColumns(id);
		return activeTechDTO;
	}

	// get list of column
	@ApiOperation(value = "Get Table List ", response = Rn_Rb_Where_Param.class)
	@GetMapping("/column-list/{id}")
	@ResponseBody
	public List<String> getColumnList(@PathVariable(value = "id") int id) {
		// List<Rn_Bcf_Technology_Stack> technologyStack =
		// technologyStackService.getAll();
		List<String> activeTechDTO = rn_table_service.getColumnList(id);
		return activeTechDTO;
	}

	@ApiOperation(value = "Get Column alias List ", response = Rn_Rb_Where_Param.class)
	@GetMapping("/column_alias_list/{id}")
	@ResponseBody
	public List<String> getColumnAliasList(@PathVariable(value = "id") String id) {
		System.out.println("Table alis name:" + id);
		List<String> activeTechDTO = rn_table_service.getColumnAliasList(id);
		return activeTechDTO;
	}

	@ApiOperation(value = "Get A Query", response = Rn_report_builder.class)
	@GetMapping("/master-query/{id}")
	public ResponseEntity<?> getProjectDetails(@PathVariable(value = "id") int id) {
		Rn_report_builder bcf_tech_stack = rn_report_builder_service.getById(id);
		// Map<String, Rn_Project_Setup> extractorMap =
		// Collections.singletonMap("extractior", bcf_extractor);
		// return new ResponseEntity<Map<String, Rn_Project_Setup>>(extractorMap,
		// HttpStatus.OK);
		// return ResponseEntity.ok().body(bcf_extractor);
		return new ResponseEntity<Rn_report_builder>(bcf_tech_stack, HttpStatus.OK);

	}

	@ApiOperation(value = "Get A Report", response = Rn_report_builder.class)
	@GetMapping("/edit-report/{id}")
	public ResponseEntity<?> getReport(@PathVariable(value = "id") int id) {
		Rn_report_builder reportList = rn_report_builder_service.getById(id); // Map<String, Rn_Project_Setup>
																				// extractorMap =
		return new ResponseEntity<Rn_report_builder>(reportList, HttpStatus.OK);

	}

//				@ApiOperation(value = "Get Table List ")
//				@GetMapping("/master-query-data")
//				@ResponseBody
//				public List<String> getQueryData(@RequestParam(value = "sql_query") String query) {
//					 //List<Rn_Bcf_Technology_Stack> technologyStack = technologyStackService.getAll();
//					System.out.println("Calling controleer");
//					MasterResponse resp=new MasterResponse();
//					List<String> activeTechDTO = rn_report_builder_service.getQueryData(query);
//				    resp.setItems(activeTechDTO);
//					
//					return activeTechDTO;
//				}

	@ApiOperation(value = "Get Table List ")
	@GetMapping("/master-query-data")
	@ResponseBody
	public List<Map<String, Object>> getQueryData(@RequestParam(value = "sql_query") String query) {
		// List<Rn_Bcf_Technology_Stack> technologyStack =
		// technologyStackService.getAll();
		System.out.println("Calling controleer");
		List<Map<String, Object>> activeTechDTO = rn_report_builder_service.getQueryData(query);
		return activeTechDTO;
	}

	@GetMapping(value = "/build_report")
	public ResponseEntity<?> build_wireframe(@RequestParam("id") Integer id) throws IOException {
		System.out.println("Calling build controller");
		Rn_report_builder reportList = rn_report_builder_service.getById(id);
		System.out.println(reportList);
		String report_name = reportList.getReport_name();
		String date_string = reportList.getDate_string();
		String add_param_string = reportList.getAdd_param_string();
		String std_param = reportList.getStd_param_view();

		StringBuilder tsFile = new StringBuilder();
		StringBuilder htmlFile = new StringBuilder();
		StringBuilder cssFile = new StringBuilder();

		tsFile.append("import { Component, OnInit } from '@angular/core';\r\n"
				+ "import { ActivatedRoute, Router } from '@angular/router';\r\n"
				+ "import { FormBuilder, FormGroup, FormControl } from '@angular/forms';\r\n"
				+ "import { ValidationError } from 'src/app/models/ValidationError';\r\n"
				+ "import { QueryRunnerService } from 'src/app/services/api/query-runner.service';\r\n"
				+ "import { ReportBuilderService } from 'src/app/services/api/report-builder.service';\r\n" + "\r\n"
				+ "@Component({\r\n" + "  selector: '" + report_name + "',\r\n" + "  templateUrl: './" + report_name
				+ ".component.html',\r\n" + "  styleUrls: ['./" + report_name + ".component.scss']\r\n" + "})\r\n"
				+ "export class " + report_name + " implements OnInit {\r\n" + "\r\n"
				+ "  public entryForm: FormGroup;\r\n" + "  submitted = false;\r\n" + "  basic: boolean = false;\r\n"
				+ "  fieldErrors: ValidationError[] = [];\r\n" + "  report_id: number;\r\n" + "  columns: any[];\r\n"
				+ "  rows:any[];\r\n" + "  \r\n" + "  constructor(private _fb: FormBuilder,\r\n"
				+ "    private router: Router,\r\n" + "    private route: ActivatedRoute,\r\n"
				+ "    private queryRunnerService: QueryRunnerService,\r\n"
				+ "    private reportBuilderService: ReportBuilderService) { }\r\n" + "    \r\n"
				+ "    date_list = [\r\n" + "      \r\n" + "    ];\r\n" + "    adhoc_list = [\r\n" + "      \r\n"
				+ "    ];\r\n" + "  ngOnInit(): void {\r\n" + "    this.route.queryParams.subscribe(params => {\r\n"
				+ "      this.report_id = +params['report_id'];\r\n" + "    });\r\n"
				+ "    console.log(\"Report id:\",this.report_id);\r\n" + "    \r\n"
				+ "    this.getById(this.report_id);\r\n" + "    this.entryForm = this._fb.group({\r\n"
				+ "      date_para:[null],\r\n" + "      from_date:[null],\r\n" + "      adhoc_para:[null],\r\n"
				+ "      condition:[null],\r\n" + "      value_1:[null],\r\n" + "      value_2:[null],\r\n"
				+ "      para_pane: [null]\r\n" + "    });\r\n" + " }\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "  onSubmit(){\r\n" + "    console.log(\"Para value:\",this.entryForm.value.para_pane);\r\n"
				+ "    var condition=this.entryForm.value.para_pane;\r\n" + "    \r\n"
				+ "    this.queryRunnerService.getById(this.report_id).subscribe((data) => \r\n" + "    {\r\n"
				+ "      var dates = [];\r\n" + "      var adhoc_param = [];\r\n"
				+ "      console.log(\"Complete query with contion::\",data.master_select+condition);\r\n"
				+ "      \r\n"
				+ "        this.reportBuilderService.getMasterData(data.master_select+condition).subscribe((data) => {\r\n"
				+ "            this.rows = data;\r\n" + "            var j;\r\n" + "            var cart = [];\r\n"
				+ "            \r\n" + "            \r\n" + "          for(var i = 0; i < data.length; i++) \r\n"
				+ "          { \r\n" + "            var columnsIn = data[i]; \r\n" + "            if(i==1)\r\n"
				+ "            {\r\n" + "                for(var key in columnsIn)\r\n" + "                { \r\n"
				+ "                j={prop:key , name: key};\r\n" + "                cart.push(j)\r\n"
				+ "                  \r\n" + "                } \r\n" + "            }\r\n" + "          }\r\n"
				+ "          this.columns = cart;\r\n" + "          \r\n" + "        });\r\n" + "     });\r\n" + "\r\n"
				+ "  }\r\n" + "\r\n" + "  getById(id: number) {\r\n"
				+ "    console.log(\"Report Id under getby id method::\",id);\r\n" + "    \r\n"
				+ "    this.queryRunnerService.getById(id).subscribe((data) => \r\n" + "    {\r\n"
				+ "      var dates = [];\r\n" + "      var adhoc_param = [];\r\n"
				+ "        this.reportBuilderService.getMasterData(data.master_select).subscribe((data) => {\r\n"
				+ "            this.rows = data;\r\n" + "            var j;\r\n" + "            var cart = [];\r\n"
				+ "            \r\n" + "            \r\n" + "          for(var i = 0; i < data.length; i++) \r\n"
				+ "          { \r\n" + "            var columnsIn = data[i]; \r\n" + "            if(i==1)\r\n"
				+ "            {\r\n" + "                for(var key in columnsIn)\r\n" + "                { \r\n"
				+ "                j={prop:key , name: key};\r\n" + "                cart.push(j)\r\n"
				+ "                  \r\n" + "                } \r\n" + "            }\r\n" + "          }\r\n"
				+ "          this.columns = cart;\r\n" + "          \r\n" + "        });\r\n" + "\r\n"
				+ "        //for date list param\r\n" + "        var str = data.date_string;\r\n"
				+ "        var myarray = str.split(',');\r\n" + "        for(var i = 0; i < myarray.length; i++)\r\n"
				+ "        {\r\n" + "          console.log(myarray[i]);\r\n"
				+ "         // this.date_list[myarray[i]+\",\"];\r\n" + "          dates.push(myarray[i])\r\n"
				+ "        }\r\n" + "        this.date_list=dates\r\n" + "\r\n" + "        //for adhoc param\r\n"
				+ "        var str2 = data.add_param_string;\r\n" + "        var adhoc = str2.split(',');\r\n"
				+ "        for(var i = 0; i < adhoc.length; i++)\r\n" + "        {\r\n"
				+ "          adhoc_param.push(adhoc[i])\r\n" + "        }\r\n"
				+ "        this.adhoc_list=adhoc_param\r\n" + "        \r\n" + "    });\r\n" + "\r\n" + "\r\n"
				+ "  }\r\n" + "\r\n" + "      dateColumn:string;\r\n" + "      addDateParam(name:string){\r\n"
				+ "        this.dateColumn=name;\r\n" + "     }\r\n" + "   \r\n" + "     v :string;\r\n"
				+ "     onDateChange(date:string){\r\n" + "       this.v=\" and \"+this.dateColumn+\"=\"+date;\r\n"
				+ "     }\r\n" + "\r\n" + "     onAddDate() {\r\n"
				+ "      (<FormControl> this.entryForm.controls['para_pane']).setValue(this.v);\r\n" + "      }\r\n"
				+ "\r\n" + "      finalCondition:string;\r\n" + "      adhocColumn:string;\r\n"
				+ "      addAdhocParam(name:string){\r\n" + "        console.log(name);\r\n"
				+ "        this.adhocColumn=name;\r\n" + "      }\r\n" + "\r\n" + "      condition:string;\r\n"
				+ "      addCondition(name:string){\r\n" + "        if(name==\"EQUAL\")\r\n"
				+ "        console.log(name);\r\n" + "        this.condition=name;\r\n" + "      }\r\n" + "\r\n"
				+ "      valu1:string;\r\n" + "      addValue1(name:string){\r\n" + "        console.log(name);\r\n"
				+ "        this.valu1=name;\r\n" + "      }\r\n" + "\r\n" + "      valu2:string;\r\n"
				+ "      addValue2(name:string){\r\n" + "        \r\n" + "        this.valu2=name;\r\n" + "      }");

		StringBuilder stdVar = new StringBuilder();
		StringBuilder stdIfCondition = new StringBuilder();
		StringBuilder stdValConcat = new StringBuilder();
		StringBuilder stdHtmlFields = new StringBuilder();

		if (std_param != null) {
			String[] stdParaList = std_param.split(",");

			for (int i = 0; i < stdParaList.length; i++) {
				System.out.println("Std variable:" + stdParaList[i]);
				int j = i + 1;
				tsFile.append("\nstd" + j + ":string;\r\n" + "     \n addStd" + j + "(name:string){\r\n"
						+ "      \n  \r\n" + "       \n this.std" + j + "=\" and " + stdParaList[i] + "=\"+name;\r\n"
						+ "      \n}");
				stdVar.append("\n  strString" + j + ":string;");
				stdIfCondition.append("if(this.std" + j + "!==null || this.std" + j + "!==undefined){\r\n"
						+ "          this.strString" + j + "=this.v+this.std" + j + ";\r\n" + "        }else{\r\n"
						+ "          this.strString" + j + "=this.v;\r\n" + "        }");
				if (i == 0) {
					stdValConcat.append("this.strString" + j + "");
				} else {
					stdValConcat.append("+this.strString" + j + "");
				}

				stdHtmlFields.append("\n\n<div class=\"clr-col-md-4 clr-col-sm-12\">\r\n"
						+ "                           \n <input id=\"name" + j + "\" type=\"text\" (change)=\"addStd"
						+ j + "($event.target.value)\" formControlName=\"name" + j + "\" placeholder=\""
						+ stdParaList[i] + "\"\r\n" + "                            class=\"clr-input\">\r\n"
						+ "                          \n</div>");

			}
		}

		tsFile.append(stdVar);
		if (std_param != null) {
			tsFile.append("      onAddstdPara() {\r\n" + stdIfCondition
					+ "        (<FormControl> this.entryForm.controls['para_pane']).setValue(" + stdValConcat + ");\r\n"
					+ "        }");
		}

		tsFile.append("stdParamCmplt:string;\r\n" + "      onAddAdhoc() {\r\n" + "        console.log(this.v);\r\n"
				+ "        \r\n");
		if (std_param != null) {
			tsFile.append("        this.stdParamCmplt=" + stdValConcat + ";\r\n");
		}
		tsFile.append("        this.finalCondition=this.v+\" and \"+this.adhocColumn+this.condition+this.valu1;\r\n"
				+ "        if(this.condition===\"BETWEEN\"){\r\n"
				+ "          this.finalCondition=this.v+this.stdParamCmplt+\" and \"+this.adhocColumn+\" \"+this.condition+\" \"+this.valu1+\" AND \"+this.valu2;\r\n"
				+ "         }else{\r\n"
				+ "          this.finalCondition=this.v+this.stdParamCmplt+\" and \"+this.adhocColumn+this.condition+this.valu1;\r\n"
				+ "        }\r\n" + "        \r\n"
				+ "        (<FormControl> this.entryForm.controls['para_pane']).setValue(this.finalCondition);\r\n"
				+ "        }\r\n" + "\r\n" + "      \r\n" + "\r\n" + "\r\n" + "  \r\n" + "}");

		htmlFile.append("<div class=\"section\">\r\n" + "            <p> Query Parameters </p>\r\n" + "    </div>\r\n"
				+ "    <br>\r\n" + "    <form [formGroup]=\"entryForm\" (ngSubmit)=\"onSubmit()\"> \r\n");

		if (date_string != null && date_string != "null") {
			htmlFile.append("     <!-- Date Parameter -->\r\n"
					+ "            <label for=\"to_tech_stack\">Date Parameter</label>\r\n"
					+ "             <div class=\"clr-row\">\r\n"
					+ "                           <div class=\"clr-col-md-4 clr-col-sm-12\">\r\n"
					+ "                                        <select id=\"date_para\" name=\"date_para\" formControlName=\"date_para\" (change)=\"addDateParam($event.target.value)\" selected=\"null\" class=\"clr-dropdown\">\r\n"
					+ "                                          <option value=\"null\">select</option>\r\n"
					+ "                                          <option *ngFor=\"let ts of date_list\" [value]=\"ts\">{{ ts }}\r\n"
					+ "                                          </option>\r\n"
					+ "                                        </select>\r\n"
					+ "                                      </div>\r\n"
					+ "                           <div class=\"clr-col-md-4 clr-col-sm-12\">\r\n"
					+ "                                \r\n"
					+ "                                        <input  placeholder=\"Pick a date\" formControlName=\"from_date\" \r\n"
					+ "                                        (change)=\"onDateChange($event.target.value)\" name=\"from_date\" id=\"from_date\"/>\r\n"
					+ "                                \r\n" + "                            </div>\r\n"
					+ "                              \r\n" + "            </div>\r\n"
					+ "            <div style=\"padding: 2px 2px; font-size: 15px; border-radius: 10px;\" type=\"submit\" >\r\n"
					+ "                    <button   (click)=\"onAddDate()\" class=\"btn btn-primary\">Add</button>\r\n"
					+ "                    <button   class=\"btn btn-primary\">filter</button>\r\n"
					+ "            </div>");
		}

		if (std_param != null) {
			htmlFile.append("<label for=\"to_tech_stack\">Standard Parameters</label>\r\n"
					+ "            <div class=\"clr-row\">\r\n" + stdHtmlFields + "            </div>\r\n"
					+ "            <div style=\"padding: 2px 2px; font-size: 15px; border-radius: 10px;\" type=\"submit\" >\r\n"
					+ "                    <button  (click)=\"onAddstdPara()\"  class=\"btn btn-primary\">Add</button>\r\n"
					+ "                    <button   class=\"btn btn-primary\">filter</button>\r\n"
					+ "            </div>");
		}

		if (add_param_string != null) {
			htmlFile.append(" <!-- Add Parameter -->\r\n"
					+ "            <label for=\"to_tech_stack\">Add Parameters</label>\r\n"
					+ "            <div class=\"clr-row\">\r\n"
					+ "                    <div class=\"clr-col-md-4 clr-col-sm-12\">\r\n"
					+ "                       <select id=\"adhoc_para\"  (change)=\"addAdhocParam($event.target.value)\" formControlName=\"adhoc_para\" selected=\"null\" class=\"clr-dropdown\">\r\n"
					+ "                                <option value=\"null\">select</option>\r\n"
					+ "                                <option *ngFor=\"let ts of adhoc_list\" [value]=\"ts\">{{ ts }}\r\n"
					+ "                                </option>\r\n" + "                        </select>\r\n"
					+ "                       \r\n" + "                    </div>\r\n" + "\r\n"
					+ "                    <div class=\"clr-col-md-4 clr-col-sm-12\">\r\n"
					+ "                            <select id=\"condition\"  (change)=\"addCondition($event.target.value)\" selected=\"null\" formControlName=\"condition\" class=\"clr-dropdown\">\r\n"
					+ "                                    <option value=\"null\">--Select Condition--</option>\r\n"
					+ "                                    <option value=\"=\" >EQUALS</option>\r\n"
					+ "				    <option value=\">\" >GREATER</option>\r\n"
					+ "				    <option value=\"<\">LESS</option>\r\n"
					+ "				   <option value=\"BETWEEN\">BETWEEN</option>\r\n"
					+ "                                </select>\r\n" + "                    </div>\r\n" + "\r\n"
					+ "                    <div class=\"clr-col-md-4 clr-col-sm-12\">\r\n"
					+ "                            <input id=\"value_1\" type=\"text\"  (change)=\"addValue1($event.target.value)\" placeholder=\"Value 1\"\r\n"
					+ "                            class=\"clr-input\">\r\n" + "                    </div>\r\n"
					+ "                         \r\n" + "           </div>\r\n"
					+ "           <div class=\"clr-row\">\r\n"
					+ "           <div class=\"clr-col-md-4 clr-col-sm-12\">\r\n"
					+ "                    <input id=\"value_2\" (change)=\"addValue2($event.target.value)\" formControlName=\"value_2\" type=\"text\"  placeholder=\"Value 2\"\r\n"
					+ "                    class=\"clr-input\">\r\n" + "            </div>\r\n" + "        </div>\r\n"
					+ "        <div style=\"padding: 2px 2px; font-size: 15px; border-radius: 10px;\" type=\"submit\" >\r\n"
					+ "            <button   (click)=\"onAddAdhoc()\"  class=\"btn btn-primary\">Add</button>\r\n"
					+ "            <button   class=\"btn btn-primary\">filter</button>\r\n" + "        </div>\r\n"
					+ "\r\n");
		}
		htmlFile.append("        <div class=\"clr-row\">\r\n"
				+ "                <div class=\"clr-col-md-4 clr-col-sm-12\">\r\n"
				+ "                    <label for=\"to_tech_stack\">Parameters Pane</label>\r\n"
				+ "                        <textarea id=\"para_pane\" formControlName=\"para_pane\" style=\"height: 150px;width: 600px;\" type=\"text\"  \r\n"
				+ "                        >\r\n" + "                        </textarea>\r\n"
				+ "                 </div>\r\n" + "        </div>\r\n"
				+ "        <div style=\"padding: 2px 2px; font-size: 15px; border-radius: 10px;\" type=\"submit\" >\r\n"
				+ "                <button class=\"btn btn-primary\">Run Report</button>\r\n" + "        </div>\r\n"
				+ "\r\n" + "    </form>\r\n" + "\r\n" + "\r\n" + "<div class=\"section\">\r\n"
				+ "        <p> Query Result </p>\r\n" + "  </div>\r\n" + "\r\n" + "  <div class=\"pad-16\">\r\n"
				+ "\r\n" + "    <ngx-datatable\r\n" + "        style='width:900px;'\r\n" + "        [rows]=\"rows\"\r\n"
				+ "        [columns]=\"columns\"\r\n" + "        \r\n" + "    >\r\n" + "    </ngx-datatable>\r\n"
				+ "</div>");

		cssFile.append("$bg-color: #dddddd;\r\n" + "\r\n" + ".button1::after {\r\n" + "  content: none;\r\n" + "}\r\n"
				+ ".button1:hover::after {\r\n" + "  content: \"ADD ROWS\";\r\n" + "}\r\n" + "\r\n" + ".section {\r\n"
				+ "  background-color: $bg-color;\r\n" + "  height: 40px;\r\n" + "}\r\n" + "\r\n" + ".section p {\r\n"
				+ "  //color: white;\r\n" + "  padding: 10px;\r\n" + "  font-size: 18px;\r\n" + "}\r\n" + "\r\n"
				+ ".clr-input {\r\n" + "  color: #212529;\r\n" + "  border: 1px solid #ced4da;\r\n"
				+ "  border-radius: 0.25rem;\r\n" + "  padding: 0.75rem 0.75rem;\r\n" + "  margin-top: 3px;\r\n"
				+ "  width: 100%;\r\n" + "  margin-bottom: 10px;\r\n" + "}\r\n" + "\r\n" + ".center {\r\n"
				+ "  text-align: center;\r\n" + "}\r\n" + "");

		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			// create component folder
			File file = new File(projectPath + "/webui/src/app/pages/" + report_name + "/");
			System.out.println("Ganesh File Path = " + file.getAbsolutePath());
			File dir = new File(file.getParent());
			if (!file.exists()) {
				file.mkdirs();
			}

			// create ts file
			File file2 = new File(
					projectPath + "/webui/src/app/pages/" + report_name + "/" + report_name + ".component.ts");
			File dir2 = new File(file.getParent());
			if (!dir2.exists()) {
				dir2.mkdirs();
			}
			if (!file2.exists()) {
				file2.createNewFile();
			}

			fw = new FileWriter(file2.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(tsFile.toString());
			bw.close();

			// create html file
			File file3 = new File(
					projectPath + "/webui/src/app/pages/" + report_name + "/" + report_name + ".component.html");
			File dir3 = new File(file.getParent());
			if (!dir3.exists()) {
				dir3.mkdirs();
			}
			if (!file3.exists()) {
				file3.createNewFile();
			}

			fw = new FileWriter(file3.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(htmlFile.toString());
			bw.close();

			// create css file
			File file4 = new File(
					projectPath + "/webui/src/app/pages/" + report_name + "/" + report_name + ".component.scss");
			File dir4 = new File(file.getParent());
			if (!dir4.exists()) {
				dir4.mkdirs();
			}
			if (!file4.exists()) {
				file4.createNewFile();
			}

			fw = new FileWriter(file4.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(cssFile.toString());
			bw.close();

			// update the rounting file
			System.out.println("In an static files loop");
			File fileTest = new File(projectPath + "/webui/src/app/app-routing.module.ts");
			Path path = Paths.get(projectPath + "/webui/src/app/app-routing.module.ts");
			StringBuilder code = new StringBuilder();
			List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			String importstr = "import { " + report_name + " } from './pages/" + report_name + "/" + report_name
					+ ".component';\n//add_import";
			String routestr = "{ path: '" + report_name + "'   , component:  " + report_name
					+ "},\n//add_routingreport";

			for (String line2 : lines) {
				if (line2.startsWith("//add_import")) {
					line2 = "import { " + report_name + " } from './pages/" + report_name + "/" + report_name
							+ ".component';\n//add_import";
				}

				if (line2.startsWith("//add_routingreport")) {
					line2 = "{ path: '" + report_name + "'   , component:  " + report_name + "},\n//add_routingreport";
				}

				code.append(line2 + "\n");
			}
			// System.out.println(code);
			bw = new BufferedWriter(new FileWriter(fileTest)); // replaced string
			bw.write(code.toString());
			bw.close();
			// update rout
//                        Path path1 = Paths.get(projectPath + "/webui/src/app/app-routing.module.ts");

			// update the module file
			File fileModule = new File(projectPath + "/webui/src/app/app.module.ts");
			Path pathModule = Paths.get(projectPath + "/webui/src/app/app.module.ts");
			StringBuilder codeModule = new StringBuilder();
			List<String> linesModule = Files.readAllLines(pathModule, StandardCharsets.UTF_8);
			for (String line3 : linesModule) {
				if (line3.startsWith("//add_module_import")) {
					line3 = "import { " + report_name + " } from './pages/" + report_name + "/" + report_name
							+ ".component';\n//add_module_import";
				}

				if (line3.startsWith("//add_module")) {
					line3 = "" + report_name + ",\n//add_module";
				}
				codeModule.append(line3 + "\n");
			}
			bw = new BufferedWriter(new FileWriter(fileModule)); // replaced string
			bw.write(codeModule.toString());
			bw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.FORM_BUILDER_API_TITLE);
			error.setMessage(Constant.FORM_BUILD_FAILURE);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.FORM_BUILDER_API_TITLE);
		success.setMessage(Constant.FORM_BUILD_SUCCESS);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
	}

}
