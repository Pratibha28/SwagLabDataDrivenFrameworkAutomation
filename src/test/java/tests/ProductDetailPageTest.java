package tests;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import dataprovider.DataProviders;
import pages.BaseClass;
import pages.LandingPage;
import pages.ProductDetailPage;
import pages.ProductPage;

public class ProductDetailPageTest extends BaseClass {

	LandingPage landingPage;
	ProductDetailPage productDetailPage;
	ProductPage productPage;

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
	public void validateProductDetail(HashMap<String, String>hashMap) throws InterruptedException {

		landingPage = new LandingPage(driver);
		productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
				productPage);
		productDetailPage = productPage.clickOnProductTitle();
		productDetailPage.validateProductdetail();
	}

	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void validateAddToCart(HashMap<String, String>hashMap) throws InterruptedException {
          
		
		
		landingPage = new LandingPage(driver);
		productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
				productPage);
		productDetailPage = productPage.clickOnProductTitle();

		productDetailPage.addTocartProductfromDetail();

		boolean result = productDetailPage.validateAddToCart();
		Assert.assertTrue(result);
		productDetailPage.removeProductFromCart();
		boolean resulst = productDetailPage.validateRemove();
		Assert.assertTrue(result);

	}

	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void validateBackToProductListing(HashMap<String, String>hashMap) throws InterruptedException {
		landingPage = new LandingPage(driver);
		productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
				productPage);
		productDetailPage = productPage.clickOnProductTitle();

		productDetailPage.BackToProductListing();
		Boolean productResult = productDetailPage.validateBackToProducts();
		Assert.assertTrue(productResult);
	}
}
