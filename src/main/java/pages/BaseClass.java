package pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

public class BaseClass {

    public static Properties prop;
    // ThreadLocal driver to support parallel runs later
    public static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();

    @Parameters("env")
    @BeforeSuite
    public void loadConfig(String envFromTestNG) throws IOException {
        String mavenEnv = System.getProperty("env");
        String finalEnv;

        if (envFromTestNG != null && !envFromTestNG.isEmpty()) {
            finalEnv = envFromTestNG;
        } else if (mavenEnv != null && !mavenEnv.isEmpty()) {
            finalEnv = mavenEnv;
        } else {
            finalEnv = "qa";
        }

        System.out.println("✅ Environment Loaded: " + finalEnv);
        prop = util.ConfigReader.loadProperties(finalEnv);
    }

    public static RemoteWebDriver getDriver() {
        return driver.get();
    }

    /**
     * Launch application.
     * Browser name expected from TestNG param (browser) or Maven -Dbrowser=...
     */
    public void launchApp(String browserType) {
        // system props override config file
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

        // Normalize seleniumGridUrl if user forgot protocol
        if (!seleniumGridUrl.isEmpty() && !(seleniumGridUrl.startsWith("http://") || seleniumGridUrl.startsWith("https://"))) {
            seleniumGridUrl = "http://" + seleniumGridUrl;
        }

        // Detect OS to avoid Linux-only flags on Windows
        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindows = os.contains("win");

        if (runOnGrid) {
            if (seleniumGridUrl.isEmpty()) {
                throw new IllegalStateException("seleniumGridUrl must be provided for grid runs (system prop or properties file)");
            }
            System.out.println("🔌 Running on Grid: " + seleniumGridUrl + " , browser=" + browserType + " , headless=" + headless);

            try {
                MutableCapabilities caps;
                if ("chrome".equalsIgnoreCase(browserType)) {
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (headless) {
                        // try new headless flag else fallback to classic if needed
                        chromeOptions.addArguments("--headless=new");
                    }
                    // add recommended args; avoid linux-only args on Windows
                    if (!isWindows) {
                        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                    }
                    chromeOptions.addArguments("--disable-gpu"); // harmless on all OS
                    caps = chromeOptions;
                    caps.setCapability("browserName", "chrome");
                } else if ("firefox".equalsIgnoreCase(browserType)) {
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (headless) firefoxOptions.addArguments("-headless");
                    caps = firefoxOptions;
                    caps.setCapability("browserName", "firefox");
                } else {
                    throw new IllegalArgumentException("Unsupported browser: " + browserType);
                }

                // Create remote driver (will throw ConnectException if server not reachable)
                driver.set(new RemoteWebDriver(new URL(seleniumGridUrl), caps));
            } catch (MalformedURLException mue) {
                throw new IllegalArgumentException("seleniumGridUrl is malformed: " + seleniumGridUrl, mue);
            }
        } else {
            System.out.println("🖥️ Running locally: browser=" + browserType + " , headless=" + headless);
            if (browserType.equalsIgnoreCase("chrome")) {
                // Optional: use WebDriverManager to avoid chromedriver mismatch. Uncomment if dependency added.
                // io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();

                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new"); // prefer new headless; fallback if not supported
                }
                // On Windows avoid linux-only args
                if (!isWindows) {
                    options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                }
                options.addArguments("--disable-gpu");
                driver.set(new ChromeDriver(options));
            } else if (browserType.equalsIgnoreCase("firefox")) {
                // io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("-headless");
                driver.set(new FirefoxDriver(options));
            } else {
                throw new IllegalArgumentException("Unsupported browser: " + browserType);
            }
        }

        // Common setup (null-check driver)
        if (getDriver() == null) {
            throw new IllegalStateException("Driver was not created. Check Selenium server / driver setup.");
        }

        try {
            getDriver().manage().window().maximize();
        } catch (Exception e) {
            // maximize may fail in headless or certain environments — ignore safely
            System.out.println("Warning: window maximize failed: " + e.getMessage());
        }

        getDriver().manage().deleteAllCookies();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));

        if (baseURL == null || baseURL.isEmpty()) {
            throw new IllegalStateException("baseURL must be provided either via -DbaseURL or in properties file");
        }
        System.out.println("➡️ Navigating to: " + baseURL);
        getDriver().get(baseURL);
    }

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
