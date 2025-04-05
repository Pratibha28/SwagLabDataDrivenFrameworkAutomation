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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class BaseClass {
	public static ExtentReports extent;
    public static ExtentTest test;
	
//	Write a Test Script to automate www.saucedemo.com using Page Object Model
//	● Create Maven Project
//	● Use TestNG
//	● Create Repository in Class File

	public static Properties prop;
	// Declare ThreadLocal Driver
		public static WebDriver driver;

	@BeforeSuite
	public void loadConfig() throws IOException {

		prop = new Properties();
		FileInputStream fis = new FileInputStream("G:\\eclipse workplace\\SwagLabs\\Resources\\global.properties");
		prop.load(fis);
		ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
	}

	


	public void launchApp(String BrowserType) {

	    if (BrowserType.equalsIgnoreCase("chrome")) {
	        // ChromeOptions setup to disable password alerts
	        ChromeOptions options = new ChromeOptions();

	        // Disable Chrome password manager and security alerts
	        options.addArguments("--disable-password-manager-reauthentication");
	        options.addArguments("--disable-features=PasswordManagerRedesign");
	        options.addArguments("--disable-features=PasswordLeakDetection");
	        options.addArguments("--disable-save-password-bubble");

	        Map<String, Object> prefs = new HashMap<>();
	        prefs.put("credentials_enable_service", false);
	        prefs.put("profile.password_manager_enabled", false);
	        options.setExperimentalOption("prefs", prefs);

	        driver = new ChromeDriver(options);

	    } else if (BrowserType.equalsIgnoreCase("firefox")) {
	        driver = new FirefoxDriver();
	    }

	    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
	    driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

	    String url = prop.getProperty("url");
	    System.out.println(url);
	    driver.get(url);
	}

	public static WebDriver getDriver() {
		// Get Driver from threadLocalmap
		return driver;
	}
	
	@BeforeMethod
	public void createTest(Method method) {
	    test = extent.createTest(method.getName());
	}

		
}
