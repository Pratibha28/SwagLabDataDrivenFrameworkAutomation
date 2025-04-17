package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import actiondriver.Action;

public class AddressPage extends BaseClass {

	Action action = new Action();

	public AddressPage() {

		
		PageFactory.initElements(getDriver(), this);
	}

	@FindBy(xpath = "//span[text()='Checkout: Your Information']")
	WebElement title;

	@FindBy(name = "firstName")
	WebElement firstName;

	@FindBy(name = "lastName")
	WebElement latName;

	@FindBy(name = "postalCode")
	WebElement postalCode;

	@FindBy(name = "continue")
	WebElement Continue;

	@FindBy(xpath = "//div[@class='error-message-container error']/h3")
	WebElement errorMessage;

	public Boolean validateAddressPage() {
		return title.isDisplayed();
	}

	public String validateAddressEmptyField(String firstname, String lastname, String postalcode) {
		action.type(firstName, firstname);
		action.type(latName, lastname);
		action.type(postalCode, postalcode);
		action.click(Continue, "Continue");

		action.visibilityOfElement(getDriver(), errorMessage, 5);
		String errormessage = errorMessage.getText();
		System.out.println(errormessage);
		return errormessage;
	}

	public OrderSummaryPage clickOnContinue(String firstname, String lastname, String postalcode) {

		action.type(firstName, firstname);
		action.type(latName, lastname);
		action.type(postalCode, postalcode);
		action.click(Continue, "Continue");

		return new OrderSummaryPage();

	}

}
