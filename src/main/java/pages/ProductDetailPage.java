package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import actiondriver.Action;

public class ProductDetailPage {
	WebDriver driver;
	Action action = new Action();

	public ProductDetailPage(WebDriver driver) {

		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//button[text()='Add to cart']")
	WebElement addToCart;

	@FindBy(xpath = "//button[text()='Remove']")
	WebElement remove;

	@FindBy(id = "back-to-products")
	WebElement backToProduct;

	@FindBy(xpath = "//div[@class='inventory_details_name large_size']")
	WebElement productDetail;
	
	@FindBy(name="back-to-products")
	WebElement backToProducts;
	
	@FindBy(xpath="//span[@class='title']")
	WebElement products;

	
	@FindBy(xpath="//a[@class='shopping_cart_link']")
	WebElement cart;
	
	@FindBy(xpath="//div[@class='inventory_details_name large_size']")
	WebElement productDetailTitle;
	
	@FindBy(xpath="//div[@class='inventory_details_price']")
	WebElement productDetailPrice;
	
	
	public Boolean validateProductdetail() {
		return action.isDisplayed(driver, productDetail);
	}

	public String  addTocartProductfromDetail() {

		if (action.isDisplayed(driver, addToCart)) {
			action.click(addToCart, "addtocart");
			
		}
            return productDetailTitle.getText();
	}

	public String productDetailProductPrice() {
		addTocartProductfromDetail();
		
		
		return productDetailPrice.getText();
		
	}
	 
	
	public Boolean validateAddToCart() {

		return action.isDisplayed(driver, remove);

	}

	public void removeProductFromCart() {

		if (action.isDisplayed(driver, remove)) {
			action.click(remove, "remove");

		}
	}

	public Boolean validateRemove() {

		return action.isDisplayed(driver, addToCart);

	}
	
	public void BackToProductListing() {
		
		action.click(backToProducts, "Back To Productd");
		
	}
	public Boolean validateBackToProducts() {

		return action.isDisplayed(driver, products);

	}
	
	public CartPage clickOnCart() {
		action.click(cart, "Cart");
		return new CartPage(driver);
		
	}

}
