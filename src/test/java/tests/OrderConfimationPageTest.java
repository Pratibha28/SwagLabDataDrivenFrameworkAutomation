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
import pages.OrderConfrimationPage;
import pages.OrderSummaryPage;
import pages.ProductDetailPage;
import pages.ProductPage;
import util.Retry;

public class OrderConfimationPageTest extends BaseClass{
	LandingPage landingPage;
	ProductPage productPage;
	ProductDetailPage productDetailPage;
	CartPage cartPage;
	OrderSummaryPage orderSummaryPage;
	AddressPage addressPage;
	OrderConfrimationPage orderConfirmationPage;

	
	@Parameters("browser")
	@BeforeMethod
	public void setup(String browser) {
		
		launchApp(browser);
		
	}
	
	@AfterMethod
	public void tearDown() {
		
		driver.quit();
	}
	
	
	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void validateOrderConfirmationPage(HashMap<String, String>hashMap) {
		landingPage= new LandingPage(driver);
		try {
			productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
					productPage);		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		productPage.addTocartMultipleProductfromList();
		cartPage = productPage.clickOnCart();
		addressPage = cartPage.clickOnChekout();
		orderSummaryPage = addressPage.clickOnContinue(prop.getProperty("first"), prop.getProperty("last"),
				prop.getProperty("postal"));
		
		orderConfirmationPage= orderSummaryPage.clickOnFinish();
		String confirmationMessage= orderConfirmationPage.validateCOnfimationMessage();
		System.out.println(confirmationMessage);
		Assert.assertEquals(prop.getProperty("message"), confirmationMessage);
		boolean flag= orderConfirmationPage.validateHomeButton();
		Assert.assertTrue(flag);
	}
}
