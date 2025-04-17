package pages;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import util.Log;

public class BaseClass {
	public static ExtentReports extent;
	public static ExtentTest test;

//	Write a Test Script to automate www.saucedemo.com using Page Object Model
//	● Create Maven Project
//	● Use TestNG
//	● Create Repository in Class File

	public static Properties prop;
	// Declare ThreadLocal Driver
	public static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();

	@BeforeSuite
	public void loadConfig() throws IOException {
		prop = new Properties();
		FileInputStream fis = new FileInputStream("G:\\eclipse workplace\\SwagLabs\\Resources\\global.properties");
		prop.load(fis);
		
		//DOMConfigurator.configure("log4j.xml");
		org.apache.logging.log4j.Logger testLogger = org.apache.logging.log4j.LogManager.getLogger(BaseClass.class);
		testLogger.info("Log4j2 initialized - config loaded!");
        Log.clearLogs();

	}

	public static WebDriver getDriver() {
		// Get Driver from threadLocalmap
		return driver.get();
	}

	public void launchApp(String BrowserType) {

		if (BrowserType.equalsIgnoreCase("chrome")) {
			// ChromeOptions setup to disable password alerts
			driver.set(new ChromeDriver());
		} else if (BrowserType.equalsIgnoreCase("firefox")) {
			driver.set(new FirefoxDriver());
		}
		// Maximize the screen
		getDriver().manage().window().maximize();
		// Delete all the cookies
		getDriver().manage().deleteAllCookies();
		// Implicit TimeOuts
		getDriver().manage().timeouts()
				.implicitlyWait(Duration.ofSeconds(Integer.parseInt(prop.getProperty("implicitWait"))));

		String url = prop.getProperty("url");
		

		// Launching the URL
		getDriver().get(prop.getProperty("url"));

	}

	

}