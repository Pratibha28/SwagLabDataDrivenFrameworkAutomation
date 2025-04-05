package tests;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import actiondriver.Action;
import dataprovider.DataProviders;
import pages.BaseClass;
import pages.CartPage;
import pages.LandingPage;
import pages.ProductDetailPage;
import pages.ProductPage;

public class CartPageTest extends BaseClass {
	LandingPage landingPage;
	ProductPage productPage;
	ProductDetailPage productDetailPage;
	CartPage cartPage;
	Action action= new Action();

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
	public void validateCartPage(HashMap<String, String>hashMap) throws InterruptedException {

		 landingPage = new LandingPage(driver);
		productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
				productPage);
		productDetailPage = productPage.clickOnProductTitle();

		cartPage = productDetailPage.clickOnCart();

		Boolean cartresult = cartPage.validateCartPage();
		Assert.assertTrue(cartresult);

	}

	// verify add to cart product & product present into the cart title matches

	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void validateCartPageProductTitle(HashMap<String, String>hashMap) throws InterruptedException {
		 landingPage = new LandingPage(driver);
		productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
				productPage);
		productDetailPage = productPage.clickOnProductTitle();

		String productTitleProductDetail = productDetailPage.addTocartProductfromDetail();
		String productPriceProductDetail = productDetailPage.productDetailProductPrice();
		cartPage = productDetailPage.clickOnCart();

		String productTitleCart = cartPage.validate_Product_Title();
		
		String productPriceCart= cartPage.validate_Product_Price();

		Assert.assertEquals(productTitleProductDetail, productTitleCart);
	}

	//Verify Remove button functionality on Cart Page
	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void validateRemoveProduct(HashMap<String, String>hashMap) throws InterruptedException {
		
		 landingPage= new LandingPage(driver);
		 productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
					productPage);
		 
		 
		 productDetailPage= productPage.clickOnProductTitle();
		productDetailPage.addTocartProductfromDetail();
		cartPage = productDetailPage.clickOnCart();
		
		
		cartPage.validateProductRemoveFromCart();
		 
		

		System.out.println(cartPage.cartproductTitle());
		Assert.assertFalse(cartPage.cartproductTitle());
	}
	
	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void validateContinueShippingButton(HashMap<String, String>hashMap) throws InterruptedException {
		
		landingPage= new LandingPage(driver);
		productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
				productPage);
		
		productDetailPage= productPage.clickOnProductTitle();
		productDetailPage.addTocartProductfromDetail();
		cartPage = productDetailPage.clickOnCart();
		Thread.sleep(3000);
		String title =cartPage.validateContinueShipping();
		Assert.assertEquals("Products",title);
		
	}
	
}
