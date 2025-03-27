//package com.realnet.Dashboard_builder.Controllers;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.time.Instant;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
//import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
//import org.openqa.selenium.By;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.Rectangle;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.realnet.Dashboard1.Entity.Dashbord_Header;
//import com.realnet.Dashboard1.Repository.HeaderRepository;
//
//@Service
//public class PdfService {
//
//	@Autowired
//	private HeaderRepository headerRepository;
//
////	public void generatePdf(String dashboardName) throws DocumentException {
////		String pdfPath = "src/main/resources/" + dashboardName + ".pdf";
////
////		System.setProperty("webdriver.chrome.driver",
////				"C:\\Users\\Gyanadipta Pahi\\Desktop\\SureSetuLast\\suresetu-mohasin205\\backend\\src\\main\\resources\\chromedriver\\chromedriver.exe");
////
////		WebDriver driver = new ChromeDriver();
////
////		// driver.get("http://localhost:19004/token/dashboard2/getdashboardbyname/" +
////		// dashboardName);
////		driver.get("http://localhost:4200/#/cns-portal/dashboardrunner/dashrunner/" + dashboardName);
////
////		try {
////			
////			String username="sysadmin";
////			String password="test3";
////
////			// Find the username and password fields and login button
////			WebElement usernameField = driver.findElement(By.id("login_username"));
////			WebElement passwordField = driver.findElement(By.id("login_password"));
////			WebElement loginButton = driver.findElement(By.id("login_Buttom"));
////
////			// Enter the login credentials
////			usernameField.sendKeys(username);
////			passwordField.sendKeys(password);
////
////			// Click the login button
////			loginButton.click();
////
////			Document document = new Document();
////			FileOutputStream outputStream = new FileOutputStream(pdfPath);
////			PdfWriter.getInstance(document, outputStream);
////			document.open();
////
////			// Find the dashboard container element in your Angular app
////			// WebElement dashboardContainer =
////			// driver.findElement(By.id("contentContainer"));
////
////			WebDriverWait wait = new WebDriverWait(driver, 10); // Wait up to 10 seconds
////			WebElement dashboardContainer = wait
////					.until(ExpectedConditions.presenceOfElementLocated(By.id("contentContainer")));
////
////			// Capture the screenshot of the dashboard using Selenium
////			byte[] dashboardImageBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
////			Image dashboardImage = Image.getInstance(dashboardImageBytes);
////
////			// Scale the image to fit the PDF page
////			Rectangle pageSize = document.getPageSize();
////			dashboardImage.scaleToFit(pageSize.getWidth(), pageSize.getHeight());
////			dashboardImage.setAlignment(Image.MIDDLE);
////
////			// Add the dashboard image to the PDF document
////			document.add(dashboardImage);
////
////			document.close();
////			driver.quit();
////
////		} catch (Exception e) {
////			throw new DocumentException("Failed to generate PDF: " + e.getMessage());
////		}
////	}
//
////	public String generatePdf2(String dashboardName) throws IOException {
////		Dashbord_Header dashbord_Header = headerRepository.findByDashboardName(dashboardName);
////		Integer id = dashbord_Header.getId();
////
////		System.out.println("id is .. " + id);
////
////		long unixTimestamp = Instant.now().getEpochSecond();
////
////		String pdfFileName = dashboardName + "_" + unixTimestamp + ".pdf";
////		String pdfPath = "/data/images/" + pdfFileName;
////		String chromiumPath = "/usr/bin/chromium"; // Replace with the actual path to your Chromium executable
////
////		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver"); // Replace with the correct path
////
////		ChromeOptions options = new ChromeOptions();
////		options.setBinary(chromiumPath);
////		options.addArguments("--headless");
////		options.addArguments("--disable-gpu");
////		options.addArguments("--no-sandbox");
////		options.addArguments("--remote-debugging-address=0.0.0.0");
////		options.addArguments("--remote-debugging-port=9222");
////
////		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
////		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
////
////		try {
////			WebDriver driver = new RemoteWebDriver(capabilities);
////		//	by me
////		//	WebDriverWait wait = new WebDriverWait(driver, 10);
////			driver.get("http://43.205.154.152:30182/#/cns-portal/dashboardrunner/dashrunner/" + id);
////
////			// Replace these login steps with your actual login logic
////			String username = "sysadmin";
////			String password = "test3";
////			WebElement usernameField = driver.findElement(By.id("login_username"));
////			WebElement passwordField = driver.findElement(By.id("login_password"));
////			WebElement loginButton = driver.findElement(By.id("login_Buttom"));
////
////			usernameField.sendKeys(username);
////			passwordField.sendKeys(password);
////			loginButton.click();
////
////			Thread.sleep(15000); // Wait for the page to load (you can adjust the wait time as needed)
////
////			// Capture the screenshot of the dashboard
////			File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
////			FileUtils.copyFile(screenshotFile, new File(pdfPath + ".png"));
////
////			driver.quit();
////
////			// Convert the screenshot to PDF using Apache PDFBox
////			PDDocument document = new PDDocument();
////			PDPage pdfPage = new PDPage();
////			document.addPage(pdfPage);
////			PDPageContentStream contentStream = new PDPageContentStream(document, pdfPage);
////			PDImageXObject imageXObject = PDImageXObject.createFromFile(pdfPath + ".png", document);
////			contentStream.drawImage(imageXObject, 50, 600);
////			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
////			contentStream.beginText();
////			contentStream.newLineAtOffset(100, 700);
////			contentStream.showText("Your PDF content here"); // Replace with any additional content you want to add
////			contentStream.endText();
////			contentStream.close();
////
////			document.save(pdfPath);
////			document.close();
////
////			// Clean up the temporary screenshot image
////			new File(pdfPath + ".png").delete();
////		} catch (IOException | InterruptedException e) {
////			throw new IOException("Failed to generate PDF: " + e.getMessage());
////		}
////
////		return pdfFileName;
////	}
//
//	public String generatePdf(String dashboardName) throws DocumentException {
//
//		Dashbord_Header dashbord_Header = headerRepository.findByDashboardName(dashboardName);
//		Integer id = dashbord_Header.getId();
//
//		System.out.println("id is .. " + id);
//
//		long unixTimestamp = Instant.now().getEpochSecond();
//
//		String pdfFileName = dashboardName + "_" + unixTimestamp + ".pdf";
//
////		String pdfPath = "src/main/resources/images/" + pdfFileName;
//		String pdfPath = "/data/images/" + pdfFileName;
//
//		System.setProperty("webdriver.chrome.driver",
//				// "C:\\Users\\Gyanadipta Pahi\\Desktop\\SureSetNew\\suresetu-mohasin205\\backend\\src\\main\\resources\\chromedriver\\chromedriver.exe");
//				"/usr/local/bin/chromedriver");
//
//		System.out.println("test1 ....");
////		WebDriver driver = new ChromeDriver();
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--headless");
//		options.addArguments("--disable-gpu");
//		options.addArguments("--no-sandbox");
//		options.addArguments("--remote-debugging-address=0.0.0.0");
//		options.addArguments("--remote-debugging-port=9222");
//		WebDriver driver = new ChromeDriver(options);
//
//		driver.get("http://43.205.154.152:30182/#/cns-portal/dashboardrunner/dashrunner/" + id);
//
//		try {
//			System.out.println("test2 ....");
//
//			String username = "sysadmin";
//			String password = "test3";
//
//			// Find the username and password fields and login button
//			WebElement usernameField = driver.findElement(By.id("login_username"));
//			WebElement passwordField = driver.findElement(By.id("login_password"));
//			WebElement loginButton = driver.findElement(By.id("login_Buttom"));
//
//			// Enter the login credentials
//			usernameField.sendKeys(username);
//			passwordField.sendKeys(password);
//
//			// Click the login button
//			loginButton.click();
//			
//			Thread.sleep(10000);
//
//			Document document = new Document();
//			FileOutputStream outputStream = new FileOutputStream(pdfPath);
//			PdfWriter.getInstance(document, outputStream);
//			document.open();
//
//			System.out.println("test3 ....");
//
////			WebDriverWait wait = new WebDriverWait(driver, 10); // Wait up to 10 seconds
////			WebElement dashboardContainer = wait
////					.until(ExpectedConditions.presenceOfElementLocated(By.id("contentContainer")));
////
////			// Capture the screenshot of the dashboard using Selenium
////			byte[] dashboardImageBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
////			Image dashboardImage = Image.getInstance(dashboardImageBytes);
////
////			// Scale the image to fit the PDF page
////			Rectangle pageSize = document.getPageSize();
////			dashboardImage.scaleToFit(pageSize.getWidth(), pageSize.getHeight());
////			dashboardImage.setAlignment(Image.MIDDLE);
////
////			// Add the dashboard image to the PDF document
////			document.add(dashboardImage);
////
////			document.close();
////			driver.quit();
//			
//			
//			
//			WebDriverWait wait = new WebDriverWait(driver, 10);
//		    WebElement dashboardContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("contentContainer")));
//
//		    // Capture screenshot of the specific element
//		    File screenshotFile = dashboardContainer.getScreenshotAs(OutputType.FILE);
//
//		    // Convert the screenshot file into an image
//		    Image dashboardImage = Image.getInstance(screenshotFile.getAbsolutePath());
//
//		    // Scale and align the image as needed
//		    Rectangle pageSize = document.getPageSize();
//		    dashboardImage.scaleToFit(pageSize.getWidth(), pageSize.getHeight());
//		    dashboardImage.setAlignment(Image.MIDDLE);
//
//		    // Add the dashboard image to the PDF document
//		    document.add(dashboardImage);
//
//		    document.close();
//		    driver.quit();
//
//		} catch (Exception e) {
//			throw new DocumentException("Failed to generate PDF: " + e.getMessage());
//		}
//
//		return pdfFileName;
//	}
//}
