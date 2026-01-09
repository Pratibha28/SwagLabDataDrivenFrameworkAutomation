package util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import actiondriver.Action;
import pages.BaseClass;

public class ListenersClass implements ITestListener, IAnnotationTransformer {

    Action action = new Action();
    ExtentReports extent = ExtentReporterNG.getReportObject();

    // ThreadLocal for parallel execution
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // 🔹 helper to fetch browser + machine details
    private String getBrowserAndMachineInfo() {
        try {
            RemoteWebDriver driver = (RemoteWebDriver) BaseClass.getDriver();
            Capabilities caps = driver.getCapabilities();

            String browser = caps.getBrowserName();
            String version = caps.getBrowserVersion();
            String os = System.getProperty("os.name");
            String osVersion = System.getProperty("os.version");
            String javaVersion = System.getProperty("java.version");

            return String.format("Browser: %s %s | OS: %s %s | Java: %s",
                    browser, version, os, osVersion, javaVersion);

        } catch (Exception e) {
            return "Browser/Machine details not available";
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Only create test node if not already created for this method
        if (extentTest.get() == null) {
            String testName = result.getMethod().getMethodName();
            String description = result.getMethod().getDescription();

            ExtentTest test;
            if (description != null && !description.isEmpty()) {
                test = extent.createTest(testName, description);
            } else {
                test = extent.createTest(testName);
            }
            extentTest.set(test);
        }
    }
    @Override
    public void onTestSuccess(ITestResult result) {
        String description = result.getMethod().getDescription();
        extentTest.get().log(Status.PASS,
                (description != null ? description : result.getName()) + " - PASSED ✅");
        extentTest.get().info(getBrowserAndMachineInfo()); // log browser + machine details
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            String description = result.getMethod().getDescription();
            int retryCount = result.getMethod().getCurrentInvocationCount();

            if (retryCount > 0) {
                extentTest.get().log(Status.WARNING,
                        "Retry #" + retryCount + " failed ❌ for: " +
                                (description != null ? description : result.getName()));
            } else {
                extentTest.get().log(Status.FAIL,
                        (description != null ? description : result.getName()) + " - FAILED ❌");
            }

            // Log exception in block
            extentTest.get().fail(
                MarkupHelper.createCodeBlock(result.getThrowable().toString())
            );

            // Browser + machine info
            extentTest.get().info(getBrowserAndMachineInfo());

            // Screenshot
            String imgPath = action.screenShot(BaseClass.getDriver(), result.getName());
            extentTest.get().fail(MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());
            extentTest.get().info("Click to view Screenshot: " +
                "<a href='" + imgPath + "' target='_blank'>Open Screenshot</a>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    @Override
    public void onTestSkipped(ITestResult result) {
        if (result.getMethod().getRetryAnalyzer(result) instanceof Retry) {
            Retry retry = (Retry) result.getMethod().getRetryAnalyzer(result);
            if (retry.isRetryAvailable()) {
                return; // skip fired due to retry, ignore it
            }
        }

        // Real skip
        String description = result.getMethod().getDescription();
        extentTest.get().log(Status.SKIP,
                (description != null ? description : result.getName()) + " - SKIPPED ⚠️");
        extentTest.get().info(getBrowserAndMachineInfo());
    }

    
    @Override
    public void onStart(ITestContext context) {
        // Optional: log suite start
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(Retry.class);
    }
}
