package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import actiondriver.Action;
import util.WebDriverWaitClass;

public class LandingPage extends BaseClass {
	WebDriver driver;
	Action action = new Action();
	@FindBy(name = "user-name")
	WebElement usernameField;

	@FindBy(name = "password")
	WebElement passwordField;

	@FindBy(name = "login-button")
	WebElement loginButton;

	@FindBy(xpath = "//div[@class='login-box']//h3")
	WebElement errorMessage;

	public LandingPage(WebDriver driver) {
		System.out.println("in");
		this.driver = driver;
		PageFactory.initElements(driver, this);

	}

	public ProductPage loginApplication(String username, String password, ProductPage productPage)
			throws InterruptedException {
		// driver.findElement(By.name("user-name"));
		action.type(usernameField, username);
		action.type(passwordField, password);
		action.click(loginButton, "loginButton");
		
		Thread.sleep(2000);
		productPage = new ProductPage(driver);
		return productPage;
	}

	public String loginWithInvalidCredential(String username, String password) {

		action.type(usernameField, username);
		action.type(passwordField, password);
		action.click(loginButton, "loginButton");
		action.visibilityOfElement(driver, errorMessage, 7);
		String message = errorMessage.getText();
		return message;

	}

	public String getCurrentURL() {

		String url = action.getCurrentURL(driver);
		return url;
	}

}
