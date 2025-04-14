package tests;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import dataprovider.DataProviders;
import pages.AddressPage;
import pages.BaseClass;
import pages.CartPage;
import pages.LandingPage;
import pages.OrderSummaryPage;
import pages.ProductDetailPage;
import pages.ProductPage;

public class AddressPageTest extends BaseClass {
	
	LandingPage landingPage;
	ProductPage productPage;
	ProductDetailPage productDetailPage;
	CartPage cartPage;
	AddressPage addressPage;
	OrderSummaryPage orderSummaryPage;

	@Parameters("browser")
	@BeforeMethod
	public void setup(String browser) {
		launchApp(browser);
	}

	@AfterMethod
	public void tearDown() {

		
	        getDriver().quit();
	    
	    extent.flush();	}

	@Test(dataProvider = "credentials", dataProviderClass = DataProviders.class)
	public void validateAddressPage(HashMap<String, String> hashMap) throws InterruptedException {

		landingPage = new LandingPage();
		test.info("Launching the app and logging in");
		try {
			productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"), productPage);
			test.pass("Login successful");
		} catch (InterruptedException e) {
			e.printStackTrace();
			test.fail("Login failed due to exception: " + e.getMessage());
		} 
		test.info("Navigating to Product Detail Page");
		productDetailPage = productPage.clickOnProductTitle();

		test.info("Adding product to cart");
		productDetailPage.addTocartProductfromDetail();
		cartPage = productDetailPage.clickOnCart();
		addressPage = cartPage.clickOnChekout();
		Thread.sleep(2000);

		Boolean flag = addressPage.validateAddressPage();
		Assert.assertTrue(flag);
	}

	@Test(dataProvider = "credentials", dataProviderClass = DataProviders.class)
	public void validateAddressEmptyData(HashMap<String, String> hashMap) throws InterruptedException {

		landingPage = new LandingPage();
		try {
			productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"), productPage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		productDetailPage = productPage.clickOnProductTitle();
		productDetailPage.addTocartProductfromDetail();
		cartPage = productDetailPage.clickOnCart();
		addressPage = cartPage.clickOnChekout();
		String error = addressPage.validateAddressEmptyField("", "", "");

		Assert.assertEquals(error, prop.getProperty("firstNameError"));
		Thread.sleep(1000);
		String lastFieldError = addressPage.validateAddressEmptyField(prop.getProperty("first"), "", "");
		Assert.assertEquals(lastFieldError, prop.getProperty("lastNameError"));

		String postalFieldError = addressPage.validateAddressEmptyField(prop.getProperty("first"), "last", "");
		Assert.assertEquals(postalFieldError, prop.getProperty("postalFieldError"));

	}

	@Test(dataProvider = "credentials", dataProviderClass = DataProviders.class)
	public void validateAddressContinueButton(HashMap<String, String> hashMap) {
		landingPage = new LandingPage();
		try {
			productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"), productPage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		productDetailPage = productPage.clickOnProductTitle();
		productDetailPage.addTocartProductfromDetail();
		cartPage = productDetailPage.clickOnCart();
		addressPage = cartPage.clickOnChekout();
		orderSummaryPage = addressPage.clickOnContinue(prop.getProperty("first"), prop.getProperty("last"),
				prop.getProperty("postal"));
		Boolean result = orderSummaryPage.validateOrderSummaryPage();
		Assert.assertTrue(result);
		test.pass("Navigated to Order Summary Page successfully");

	}

}
