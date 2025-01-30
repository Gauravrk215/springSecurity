package com.realnet.Rpt_builder2.Controllers;
import java.io.IOException;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.realnet.Rpt_builder2.Entity.Rpt_builder2_t;
import com.realnet.Rpt_builder2.Services.Rpt_builder2_Service;
import com.realnet.Rpt_builder2_lines.Entity.Rpt_builder2_lines_t;
import com.realnet.Rpt_builder2_lines.Repository.Rpt_builder2_lines_Repository;
@RestController
@RequestMapping("/Rpt_builder2/html")
public class ReportHtmlController {
	@Autowired
	private Rpt_builder2_Service builder2_Service;
	@Autowired
	private Rpt_builder2_lines_Repository line_repo;
//	@GetMapping(value = "/build_report2/{id}")
//	public ResponseEntity<?> build_wireframe2(@PathVariable Long id) throws IOException {
//		Rpt_builder2_t reportList = builder2_Service.getdetailsbyId(id);
//		String std_param = reportList.getStd_param_html();
//
//		StringBuilder htmlFile = new StringBuilder();
//
//		// Start building the HTML code
//		htmlFile.append("<div class=\"clr-row\">\n");
//
//		// Remove the outer square brackets and split the std_param string to get
//		// individual parameter values
//		String[] params = std_param.substring(1, std_param.length() - 1).split("\",\"");
//
//		for (int i = 0; i < params.length; i++) {
//			String param = params[i].replaceAll("\"", ""); // Remove quotation marks from parameter value
//
//			// Create the HTML code for each parameter, including the actual parameter value
//			htmlFile.append("    <div class=\"clr-col-md-6 clr-col-sm-12\">\n")
//					.append("        <label for=\"description\">").append(param).append("</label>\n")
//					.append("        <input type=\"text\" class=\"clr-input\" placeholder=\"Enter ").append(param)
//					.append("\">\n").append("    </div>\n");
//		}
//
//		htmlFile.append("</div>");
//
//		// Return the HTML code as a response
//		return ResponseEntity.ok(htmlFile.toString());
//	}
	@GetMapping(value = "/build_report2/{rptbuilderid}")
	public ResponseEntity<?> build_wireframe2(@PathVariable Long rptbuilderid) throws IOException {
		Rpt_builder2_lines_t rptlines = line_repo.getRpt_builder2_lines(rptbuilderid);
		String model = rptlines.getModel();
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(model).getAsJsonObject();
		String std_param = jsonObject.get("std_param_html").getAsString();
		StringBuilder htmlFile = new StringBuilder();
		// Start building the HTML code
		htmlFile.append("<div class=\"clr-row\">\n");
		// Remove the outer square brackets and split the std_param string to get
		// individual parameter values
		String[] params = std_param.substring(1, std_param.length() - 1).split("\",\"");
		for (int i = 0; i < params.length; i++) {
			String param = params[i].replaceAll("\"", ""); // Remove quotation marks from parameter value
			// Create the HTML code for each parameter, including the actual parameter value
			htmlFile.append("    <div class=\"clr-col-md-6 clr-col-sm-12\">\n")
					.append("        <label for=\"description\">").append(param).append("</label>\n")
					.append("        <input type=\"text\" class=\"clr-input\" placeholder=\"Enter ").append(param)
					.append("\">\n").append("    </div>\n");
		}
		htmlFile.append("</div>");
		// Return the HTML code as a response
		return ResponseEntity.ok(htmlFile.toString());
	}
}







