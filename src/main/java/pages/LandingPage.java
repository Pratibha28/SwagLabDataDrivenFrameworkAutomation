package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import actiondriver.Action;
import util.Log;
import util.WebDriverWaitClass;

public class LandingPage extends BaseClass {
	
	Action action = new Action();
	@FindBy(name = "user-name")
	WebElement usernameField;

	@FindBy(name = "password")
	WebElement passwordField;

	@FindBy(name = "login-button")
	WebElement loginButton;

	@FindBy(xpath = "//div[@class='login-box']//h3")
	WebElement errorMessage;

	public LandingPage() {
		
		PageFactory.initElements(getDriver(), this);

	}

	public ProductPage loginApplication(String username, String password, ProductPage productPage)
			throws InterruptedException {
		Log.startTestCase("LoginTest");
		// driver.findElement(By.name("user-name"));
		Log.info("Enter username & password and login into the  account");
		action.type(usernameField, username);
		action.type(passwordField, password);
		action.click(loginButton, "loginButton");
		Log.endTestCase("Test End");
		Thread.sleep(2000);
		productPage = new ProductPage();
		return productPage;
	}

	public String loginWithInvalidCredential(String username, String password) {
		Log.startTestCase("LoginTest");

		action.type(usernameField, username);
		action.type(passwordField, password);
		action.click(loginButton, "loginButton");
		action.visibilityOfElement(getDriver(), errorMessage, 7);
		String message = errorMessage.getText();
		Log.endTestCase("End Test");
		return message;
      
	}

	public String getCurrentURL() {

		String url = action.getCurrentURL(getDriver());
		return url;
	}

}
