package pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;


public class BaseClass {

//	Write a Test Script to automate www.saucedemo.com using Page Object Model
//	● Create Maven Project
//	● Use TestNG
//	● Create Repository in Class File

	public static Properties prop;
	// Declare ThreadLocal Driver
	public static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
	
	/**
     * Load environment-specific properties into util.ConfigReader.
     * Priority for 'env' selection:
     * 1) TestNG parameter (envFromTestNG)
     * 2) Maven/System property (-Denv=...)
     * 3) default "qa"
     */
     @Parameters("env")
	@BeforeSuite
	public void loadConfig(String envFromTestNG) throws IOException {
    	 String mavenEnv = System.getProperty("env");
    	
 	    String finalEnv;

 	    if (envFromTestNG != null && !envFromTestNG.isEmpty()) {
 	        finalEnv = envFromTestNG;       // 🔑 Priority 1: TestNG XML param
 	    } else if (mavenEnv != null && !mavenEnv.isEmpty()) {
 	        finalEnv = mavenEnv;            // 🔑 Priority 2: Maven -Denv or surefire
 	    } else {
 	        finalEnv = "qa";                // 🔑 Priority 3: fallback
 	    }

 	    System.out.println("✅ Environment Loaded: " + finalEnv);

		prop = new Properties();
		prop = util.ConfigReader.loadProperties(finalEnv);

	}

	public static RemoteWebDriver getDriver() {
		// Get Driver from threadLocalmap
		return driver.get();
	}


    /**
     * Launch application. Browser name expected from TestNG param or Maven -Dbrowser=...
     * This method gives precedence to System properties for:
     * - runOnGrid
     * - headless
     * - implicitWait
     * - seleniumGridUrl
     * - baseURL
     *
     * If system props are absent, values are read from loaded properties file.
     * @throws MalformedURLException 
     */

	public void launchApp(String browserType) {
		
		
		 // Helper to resolve property: system prop > config file > default
        String sysRunOnGrid = System.getProperty("runOnGrid");
        String sysHeadless = System.getProperty("headless");
        String sysImplicitWait = System.getProperty("implicitWait");
        String sysSeleniumGridUrl = System.getProperty("seleniumGridUrl");
        String sysBaseURL = System.getProperty("baseURL");
        
        String propRunOnGrid = util.ConfigReader.getProperties("runOnGrid");
        String propHeadless = util.ConfigReader.getProperties("headless");
        String propImplicitWait = util.ConfigReader.getProperties("implicitWait");
        String propSeleniumGridUrl = util.ConfigReader.getProperties("seleniumGridUrl");
        String propBaseURL = util.ConfigReader.getProperties("baseURL");

        
        boolean runOnGrid = Boolean.parseBoolean(firstNonNull(sysRunOnGrid, propRunOnGrid, "false"));
        boolean headless = Boolean.parseBoolean(firstNonNull(sysHeadless, propHeadless, "true"));
        long implicitWaitSeconds = Long.parseLong(firstNonNull(sysImplicitWait, propImplicitWait, "10"));
        String seleniumGridUrl = firstNonNull(sysSeleniumGridUrl, propSeleniumGridUrl, "");
        String baseURL = firstNonNull(sysBaseURL, propBaseURL, "");
        
        if (runOnGrid) {
            if (seleniumGridUrl.isEmpty()) {
                throw new IllegalStateException("seleniumGridUrl must be provided (system prop or properties file) for grid runs");
            }

            DesiredCapabilities caps = new DesiredCapabilities();
            
            if ("chrome".equalsIgnoreCase(browserType)) {
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                caps.merge(chromeOptions);
                caps.setBrowserName("chrome");
                
            }  else if ("firefox".equalsIgnoreCase(browserType)) {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("-headless");
                caps.merge(firefoxOptions);
                caps.setBrowserName("firefox");
            } else {
                throw new IllegalArgumentException("Unsupported browser: " + browserType);
            }
            
            try {
				driver.set(new RemoteWebDriver(new URL(seleniumGridUrl), caps));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }else {
        	// Local execution
	    if (browserType.equalsIgnoreCase("chrome")) {
	       // WebDriverManager.chromedriver().setup();
	        ChromeOptions options= new ChromeOptions();
	        options.addArguments("--headless");
	        driver.set(new ChromeDriver(options));
	    } else if (browserType.equalsIgnoreCase("firefox")) {
	    	
	    	FirefoxOptions options= new FirefoxOptions();
	    	options.addArguments("--headless");
	        //WebDriverManager.firefoxdriver().setup();
	        driver.set(new FirefoxDriver(options));
	    }

	    // Common setup
        getDriver().manage().window().maximize();
        getDriver().manage().deleteAllCookies();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));

        if (baseURL == null || baseURL.isEmpty()) {
            throw new IllegalStateException("baseURL must be provided either via -DbaseURL or in properties file");
        }
        getDriver().get(baseURL);
	}}
	
	// helper: return first non-null and non-empty string among a, b, else defaultVal
    private String firstNonNull(String a, String b, String defaultVal) {
        if (a != null && !a.isEmpty()) return a;
        if (b != null && !b.isEmpty()) return b;
        return defaultVal;
    }
    
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            if (getDriver() != null) {
                getDriver().quit();
            }
        } catch (Exception e) {
            System.err.println("Error while quitting driver: " + e.getMessage());
        } finally {
            driver.remove();
        }
    }
}