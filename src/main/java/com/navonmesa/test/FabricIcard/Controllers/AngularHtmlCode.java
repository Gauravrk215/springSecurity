package com.realnet.FabricIcard.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class AngularHtmlCode {

	 @GetMapping("/convert")
	    public String convertAngularToHtml(@RequestParam String angularCode) {
	        // Perform conversion logic here (replace Angular-specific syntax with HTML)
	        String htmlCode = convertAngularToHtmlCode(angularCode);
	        return htmlCode;
	    }
	 
	 
//	 private String convertAngularToHtmlCode(String angularCode) {
//		    // Replace ng-container with a div
//		    String htmlCode = angularCode.replace("<ng-container", "<div");
//
//		    // Replace ngIf with standard HTML attributes
//		    htmlCode = htmlCode.replace("*ngIf=", "ng-if=");
//
//		    // Remove Angular-specific directives (ngFor, ngIf) and Angular-specific syntax ([...], {{...}})
//		    htmlCode = htmlCode.replaceAll("\\[.*?\\]", ""); // Remove [property]="value"
//		    htmlCode = htmlCode.replaceAll("\\{\\{.*?\\}\\}", ""); // Remove {{expression}}
//
//		    // Replace Angular-specific structural directives with standard HTML
//		    htmlCode = htmlCode.replace("*ngFor", "ng-repeat"); // Replace *ngFor with ng-repeat
//
//		    // Add more replacements as needed for other Angular-specific syntax
//
//		    return htmlCode;
//		}

	 private String convertAngularToHtmlCode(String angularCode) {
		    // Replace ng-container with a div
		    String htmlCode = angularCode.replace("<ng-container", "<div");

		    // Replace ng-repeat with ng-for
		    htmlCode = htmlCode.replace("ng-repeat", "ng-for");

		    // Replace ngIf with standard HTML attributes
		    htmlCode = htmlCode.replace("*ngIf=", "ng-if=");

		    // Remove Angular-specific structural directives (ngFor, ngIf) and Angular-specific syntax ([...], {{...}})
		    htmlCode = htmlCode.replaceAll("\\[.*?\\]", ""); // Remove [property]="value"
		    htmlCode = htmlCode.replaceAll("\\{\\{.*?\\}\\}", ""); // Remove {{expression}}

		    // Remove Angular-specific directives
		    htmlCode = htmlCode.replaceAll("ng-for", ""); // Remove ng-for
		    htmlCode = htmlCode.replaceAll("ng-if", ""); // Remove ng-if

		    // Add more replacements as needed for other Angular-specific syntax

		    return htmlCode;
		}

}
