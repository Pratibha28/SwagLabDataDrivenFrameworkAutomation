package tests;

import java.io.IOException;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import dataprovider.DataProviders;
import pages.BaseClass;
import pages.LandingPage;
import pages.ProductPage;
import util.Log;

public class LoginPageTest extends BaseClass {
	LandingPage LandingPage;
	ProductPage productPage;

	@Parameters("browser")
	@BeforeMethod
	public void setup(String browser) {

		launchApp(browser);
	}

	@AfterMethod
	public void tearDown() {
		getDriver().quit();
	}

	@Test
	public void verifyLogging() {
		Log.info("######## This is a test log from LoginTest ########");
	}

	@Test(enabled = true)
	public void verifyLogin() throws IOException, InterruptedException {

		LandingPage = new LandingPage();

		productPage = LandingPage.loginApplication(prop.getProperty("username"), prop.getProperty("password"),
				productPage);
		String actualURL = LandingPage.getCurrentURL();
		String expectedURL = "https://www.saucedemo.com/inventory.html";
		Log.info("Validate URl");
		Assert.assertEquals(actualURL, expectedURL);

	}

	// Login with valid password
	@Test(dataProvider = "credentials", dataProviderClass = DataProviders.class)
	public void verifyLoginnew(HashMap<String, String> hashMapValue) throws IOException, InterruptedException {

		LandingPage = new LandingPage();
		productPage = LandingPage.loginApplication(hashMapValue.get("Username"), hashMapValue.get("Password"),
				productPage);
		String actualURL = LandingPage.getCurrentURL();
		String expectedURL = "https://www.saucedemo.com/inventory.html";
		Log.info("Validate URl");

		Assert.assertEquals(actualURL, expectedURL);

	}

	// login with the invalid username & password
	@Test(dataProvider = "InvalidCredentials", dataProviderClass = DataProviders.class)
	public void verifyLoginInvalidData(HashMap<String, String> hashMapValue) throws InterruptedException {

		LandingPage = new LandingPage();

		String errorMessage = LandingPage.loginWithInvalidCredential(hashMapValue.get("Username"),
				hashMapValue.get("Password"));
		Assert.assertEquals(errorMessage, hashMapValue.get("errorMessage"));
	}

}
