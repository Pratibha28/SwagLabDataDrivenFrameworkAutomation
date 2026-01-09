package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import actiondriver.Action;

public class CartPage extends BaseClass {
	
	Action action = new Action();

	public CartPage() {
		
		PageFactory.initElements(getDriver(), this);
	}

	@FindBy(xpath = "//span[@class='title']")
	WebElement title;

	@FindBy(xpath = "//a[@id='item_4_title_link']/div")
	WebElement cartProductTitle;

	@FindBy(xpath = "//div[@class='inventory_item_price']")
	WebElement cartProductPrice;

	@FindBy(xpath = "//button[text()='Remove']")
	WebElement removeButton;
	
	@FindBy(xpath= "//button[@id='continue-shopping']")
	WebElement continueShopping;
	
	@FindBy(xpath="//span[text()='Products']")
	WebElement productTitle;

	@FindBy (name="checkout")
	WebElement checkout;
	
	public Boolean validateCartPage() {
		
		
		

		return action.isDisplayed(getDriver(), title);

	}

	public String validate_Product_Title() {

		System.out.println(cartProductTitle.getText());

		return cartProductTitle.getText();
	}

	public String validate_Product_Price() {

		System.out.println(cartProductPrice.getText());

		return cartProductPrice.getText();
	}

	// Get cart product title is present or not
	public boolean cartproductTitle() {
		boolean flag = action.isDisplayed(getDriver(), cartProductTitle);
		return flag;
	}

	// Remove product From cart page
	public void validateProductRemoveFromCart() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		action.click(removeButton, "Remove");

	}
	
public String validateContinueShipping() {
	
	 action.click(continueShopping, "ContinueShpooing");
	  //action.click(continueShopping, "ContinueShopping");
	 try {
		Thread.sleep(2000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 String title= productTitle.getText();
	 System.out.println(title);
	 return title;
}

public AddressPage clickOnChekout() {
	action.click(checkout, "Checkout");
	
	return new AddressPage();
}


}
