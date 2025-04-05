package tests;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

public class OrderSummaryPageTest extends BaseClass {
	LandingPage landingPage;
	ProductPage productPage;
	ProductDetailPage productDetailPage;
	CartPage cartPage;
	OrderSummaryPage orderSummaryPage;
	AddressPage addressPage;

	@Parameters("browser")
	@BeforeMethod
	public void setup(String browser) {
		launchApp(browser);
	}

	@AfterMethod
	public void teardown() {

		driver.quit();
	}

	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void validateOrderSummary(HashMap<String, String>hashMap) {

		landingPage = new LandingPage(driver);
		try {
			productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
					productPage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		productDetailPage = productPage.clickOnProductTitle();
		productDetailPage.addTocartProductfromDetail();
		cartPage = productDetailPage.clickOnCart();
		addressPage = cartPage.clickOnChekout();
		orderSummaryPage = addressPage.clickOnContinue(prop.getProperty("first"), prop.getProperty("last"),
				prop.getProperty("postal"));
		double productPrice = orderSummaryPage.getUnitPrice();
		double orderPrice = orderSummaryPage.getTotalPrice();
		double orderTaxPrice = orderSummaryPage.getOrderTax();
		Double expectedOrderPrice = productPrice + orderTaxPrice;
		System.out.println(expectedOrderPrice);
		Assert.assertEquals(expectedOrderPrice, orderPrice);

	}

	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void validateMultipleProductPrice(HashMap<String, String>hashMap) {

		landingPage = new LandingPage(driver);
		try {
			productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
					productPage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		productPage.addTocartMultipleProductfromList();
		cartPage = productPage.clickOnCart();
		addressPage = cartPage.clickOnChekout();
		orderSummaryPage = addressPage.clickOnContinue(prop.getProperty("first"), prop.getProperty("last"),
				prop.getProperty("postal"));
		double allProductPrice = orderSummaryPage.getAllProductsPrice();
		double orderPrice = orderSummaryPage.getTotalPrice();
		double orderTaxPrice = orderSummaryPage.getOrderTax();
		double expectedOrderPrice = allProductPrice + orderTaxPrice;
		BigDecimal bd = new BigDecimal(expectedOrderPrice).setScale(2, RoundingMode.HALF_UP);
		double newExpectedOrderPrice = bd.doubleValue();

		System.out.println(newExpectedOrderPrice);
		Assert.assertEquals(newExpectedOrderPrice, orderPrice);
		

	}
	
	
    @Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
    public void validateCancelButton(HashMap<String, String>hashMap) {
    	landingPage = new LandingPage(driver);
		try {
			productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
					productPage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		productPage.addTocartMultipleProductfromList();
		cartPage = productPage.clickOnCart();
		addressPage = cartPage.clickOnChekout();
		orderSummaryPage = addressPage.clickOnContinue(prop.getProperty("first"), prop.getProperty("last"),
				prop.getProperty("postal"));
		
		boolean flag= orderSummaryPage.validateCancelButton();
		Assert.assertTrue(flag);
    	
    }
}
