package util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporterNG {
	public static ExtentReports extents;
	public static ExtentTest test;
	
	public static ExtentReports getReportObject()
	{
		System.out.println("in extent report");
		String path =System.getProperty("user.dir")+"//reports//index.html";

		
		ExtentSparkReporter reporter= new ExtentSparkReporter(path);
		reporter.config().setReportName("SwagLabs Automation Results");
		reporter.config().setDocumentTitle("Test Results");
		
	    extents= new ExtentReports();
		extents.attachReporter(reporter);
		extents.setSystemInfo("Tester", "Pratibha");
		
		return extents;
	}
	
	/*
	 * public static void endReport() { extents.flush(); }
	 */
	
}
