package pages;

import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import actiondriver.Action;

public class ProductPage extends BaseClass {
	Action action = new Action();

	public ProductPage(WebDriver driver) {
		this.driver = driver;

		PageFactory.initElements(driver, this);

	}
	
	@FindBy(xpath="//a[@class='shopping_cart_link']")
	WebElement cart;

	@FindBy(xpath = "//select[@class='product_sort_container']")
	WebElement select;

	@FindBy(xpath = "//div[@class='inventory_item_name ']")
	List<WebElement> productListTitle;

	@FindBy(xpath = "//div[@class='inventory_item_desc']")
	List<WebElement> productListDesc;

	@FindBy(xpath = "//div[@class='inventory_item_price']")
	List<WebElement> productListPrice;

	@FindBy(xpath = "//button[text()='Add to cart']")
	WebElement addToCart;

	@FindBy(xpath = "//button[text()='Remove']")
	WebElement remove;

	@FindBy(xpath="//div[@class='inventory_list']/div[1]/div[2]/div[1]/a")
	WebElement productTitle;
	public List<WebElement> VerifyallProductTitle() {
		// productSTitle = null;

		return productListTitle;
	}

	public List<WebElement> getAllProductDesc() {

		return productListDesc;
	}

	public List<WebElement> getAllProductprice() {

		return productListPrice;
	}

	public List<WebElement> VerifyDecOrderProductTitle() throws InterruptedException {
		System.out.println("in method");

		action.selectByIndex(select, 1);
		Thread.sleep(5000);
		VerifyallProductTitle();

		return productListTitle;

	}

	public List<WebElement> verifyProductPriceAscOrder() {
		action.selectByIndex(select, 2);
		getAllProductprice();
		return productListPrice;
	}
 
	public List<WebElement> verifyProductPriceDscOrder() {
		action.selectByIndex(select, 3);
		getAllProductprice();

		return productListPrice;
	}
	
	

	public void addTocartProductfromList() {

		for (int i = 0; i < productListTitle.size(); i++) {
			if (productListTitle.get(i).getText().equalsIgnoreCase(prop.getProperty("productname"))) {

				if (action.isDisplayed(driver, addToCart)) {
					action.click(addToCart, "addtocart");

				}
			}
		}
	}

	
	public void addTocartMultipleProductfromList() {

		for (int i = 0; i < productListTitle.size(); i++) {
			if (productListTitle.get(i).getText().contains(prop.getProperty("ProductsNameContain"))) {

				if (action.isDisplayed(driver, addToCart)) {
					action.click(addToCart, "addtocart");

				}
			}
		}
	}

	
	public ProductDetailPage clickOnProductTitlenew() {
		
		action.click(productTitle, "ProductTitle");
		return new ProductDetailPage(driver);
	}
	
	
public ProductDetailPage clickOnProductTitle() {
		
		for (int i = 0; i < productListTitle.size(); i++) {
			if (productListTitle.get(i).getText().equalsIgnoreCase(prop.getProperty("productname"))) {

				if (action.isDisplayed(driver, addToCart)) {
					System.out.println("i am here##############");
					System.out.println(productListTitle.get(i));

					System.out.println(productListTitle.get(i).getText());

					action.click(productListTitle.get(i), "productTitle");

				}
			}
		}
		
		return new ProductDetailPage(driver);
	}
	
	
	
	public Boolean validateAddToCart() {

		return action.isDisplayed(driver, remove);

	}

	public void removeProductFromList() {

		for (int i = 0; i < productListTitle.size(); i++) {
			if (productListTitle.get(i).getText().equalsIgnoreCase(prop.getProperty("productname"))) {

				if (action.isDisplayed(driver, remove)) {
					action.click(remove, "remove");

				}
			}
		}

	}

	public Boolean validateRemove() {

		return action.isDisplayed(driver, addToCart);

	}
	public CartPage clickOnCart() {
		action.click(cart, "Cart");
		return new CartPage(driver);
		
	}
}