//package com.realnet.Dashboard_builder.Controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/pdf")
//public class PdfController {
//
//    private final PdfService pdfService;
//
//    @Autowired
//    public PdfController(PdfService pdfService) {
//        this.pdfService = pdfService;
//    }
//
//    @GetMapping("/generate/{dashboardName}")
//    public ResponseEntity<String> generatePdf(@PathVariable String dashboardName) {
//        try {
//            pdfService.generatePdf(dashboardName);
//            return new ResponseEntity<>("PDF generated successfully!", HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}