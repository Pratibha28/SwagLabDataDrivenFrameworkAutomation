package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.TestNG;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import dataprovider.DataProviders;
import pages.BaseClass;
import pages.LandingPage;
import pages.ProductPage;

public class ProductTest extends BaseClass {
	LandingPage landingPage;

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

	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void verifyAllProductTitle(HashMap<String, String>hashMap) throws IOException, InterruptedException {
		landingPage = new LandingPage();
		productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
				productPage);
		List<String> expectedProductTitle = new ArrayList<String>();
		String[] ProductListTitle = prop.getProperty("product").split(",");
		expectedProductTitle = Arrays.asList(ProductListTitle);

		List<WebElement> actualProductTitle = productPage.VerifyallProductTitle();
		System.out.println(actualProductTitle.size());
		for (int i = 0; i < actualProductTitle.size(); i++) {

			Assert.assertEquals(actualProductTitle.get(i).getText(), expectedProductTitle.get(i));
		}
	}

	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void verifyAllProductPrice(HashMap<String, String>hashMap) throws InterruptedException {
		landingPage = new LandingPage();
		productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
				productPage);
		List<String> expectedProductprice = new ArrayList<String>();
		String[] ProductListPrice = prop.getProperty("price").split(",");
		expectedProductprice = Arrays.asList(ProductListPrice);

		List<WebElement> actualProductprice = productPage.getAllProductprice();
		System.out.println(actualProductprice.size());
		for (int i = 0; i < actualProductprice.size(); i++) {

			Assert.assertEquals(actualProductprice.get(i).getText(), expectedProductprice.get(i));
		}
	}

	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void verifyDescOrderProduct(HashMap<String, String>hashMap) throws InterruptedException {
		landingPage = new LandingPage();
		productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
				productPage);
		List<WebElement> descProductTitle = productPage.VerifyDecOrderProductTitle();
		List<String> actuallist = new ArrayList();
		for (int i = 0; i < descProductTitle.size(); i++) {
			String data = descProductTitle.get(i).getText();
			actuallist.add(data);

		}

		Object templist = actuallist.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		for (int i = 0; i < descProductTitle.size(); i++) {
			System.out.println(actuallist.get(i));

		}

		Assert.assertEquals(templist, actuallist);

	}

	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void getProductPriceAscOrder(HashMap<String, String>hashMap) throws InterruptedException {
		landingPage = new LandingPage();
		productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
				productPage);		
		
		List<WebElement> allproductPrices = productPage.verifyProductPriceAscOrder();
		List actualList = new ArrayList();
		for (int i = 0; i < allproductPrices.size(); i++) {

			String data = allproductPrices.get(i).getText();

			String newstring = data.substring(1);

			double number = Double.parseDouble(newstring);

			actualList.add(number);
           System.out.println(actualList.get(i));
			
		}

		List templist = actualList;
		Collections.sort(templist);
		
		Assert.assertEquals(actualList, templist);
		

	}
	
	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void verifyProductPriceDscOrder(HashMap<String, String>hashMap) throws InterruptedException {
		
		landingPage= new LandingPage();
		productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
				productPage);		List<WebElement> dscProductPrice= productPage.verifyProductPriceDscOrder();
		List actuallist= new ArrayList();
		for (int i = 0; i < dscProductPrice.size(); i++) {
			String data= dscProductPrice.get(i).getText();
			String Price= data.substring(1);
			Double actualPrice= Double.parseDouble(Price);
			actuallist.add(actualPrice);
			
		}
		System.out.println("Highest price of the product is "+actuallist.get(0));
		System.out.println("Lowest  price of the product is "+actuallist.get(actuallist.size()-1));

		List tempList= actuallist;
		 
		Collections.reverse(tempList);
		Assert.assertEquals(actuallist, tempList);
	}

	
	@Test(dataProvider="credentials", dataProviderClass = DataProviders.class)
	public void addToCartProductFromListing(HashMap<String, String>hashMap) throws InterruptedException {
		
		landingPage= new LandingPage();
		productPage = landingPage.loginApplication(hashMap.get("Username"), hashMap.get("Password"),
				productPage);		productPage.addTocartProductfromList();
		Thread.sleep(2000);
		boolean result= productPage.validateAddToCart();
		Assert.assertTrue(result);
		productPage.removeProductFromList();
		boolean results = productPage.validateRemove();
		Assert.assertTrue(results);
		
	}
	
	
	
	
}
