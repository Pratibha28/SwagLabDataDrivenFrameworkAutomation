package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import actiondriver.Action;

public class OrderConfrimationPage extends BaseClass {
	Action action = new Action();

	public OrderConfrimationPage() {

		PageFactory.initElements(getDriver(), this);
	}

	@FindBy(xpath = "//span[@class='title']")
	WebElement title;
	@FindBy(xpath = "//h2[@class='complete-header']")
	WebElement confirmMessage;

	@FindBy(name = "back-to-products")
	WebElement homeButton;

	public Boolean validateOrderConfimation() {

		return action.isDisplayed(getDriver(), title);
	}

	public String validateCOnfimationMessage() {
		String confirmMsg = confirmMessage.getText();
		return confirmMsg;
	}

	public Boolean validateHomeButton() {
		action.click(homeButton, "BackHome");
		Boolean flag = action.isDisplayed(getDriver(), title);
		return flag;
	}

}
