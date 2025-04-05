package pages;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class BaseClass {
	
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
	}

	


	public void launchApp(String BrowserType) {

		if (BrowserType.equalsIgnoreCase("chrome")) {
			driver= new ChromeDriver();

		} else if (BrowserType.equalsIgnoreCase("firefox")) {

			driver= new FirefoxDriver();

		}
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
		driver.manage().window().maximize();

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		String url= prop.getProperty("url");
		System.out.println(url);
		driver.get(url);
	}

	
	public static WebDriver getDriver() {
		// Get Driver from threadLocalmap
		return driver;
	}
	
}
