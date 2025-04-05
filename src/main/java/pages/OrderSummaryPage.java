package pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import actiondriver.Action;

public class OrderSummaryPage extends BaseClass {
	WebDriver driver;
	Action actions = new Action();

	public OrderSummaryPage(WebDriver driver) {

		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//span[@class='title']")
	WebElement title;
	@FindBy(xpath="//div[@class='inventory_item_price']")
	WebElement unitPrice;
	
	@FindBy(xpath="//div[@class='summary_total_label']")
	WebElement totalPrice;
	
	@FindBy(xpath="//div[@class='summary_tax_label']")
	WebElement tax;
	
	@FindBy(xpath="//div[@class='header_secondary_container']/span")
	WebElement cancel;
	@FindBy(name="finish")
	WebElement finish;

	@FindBy(xpath="//div[@class='item_pricebar']")
	List<WebElement> productsPrice;
	
	public Boolean validateOrderSummaryPage() {

		return actions.isDisplayed(driver, title);
	}
	
	public double getUnitPrice() {
		
		String unitPrice1= unitPrice.getText();
		System.out.println(unitPrice1);
		String unit= unitPrice1.replaceAll("[$]","");
		System.out.println(unit);

		double finalUnitPrice= Double.parseDouble(unit);
		System.out.println(finalUnitPrice);

		return finalUnitPrice;
	}
	
	public double  getAllProductsPrice() {
		double totalPrice=0.0;
		
		for (int i = 0; i < productsPrice.size(); i++) {
			String unitPrice1= productsPrice.get(i).getText();
			String unit= unitPrice1.replaceAll("[$]","");
			double finalUnitPrice= Double.parseDouble(unit);
			totalPrice= totalPrice+finalUnitPrice;
		}
		System.out.println(totalPrice);
		return totalPrice;
	}
	
	public double getTotalPrice() {
		
		String totalPrice1= totalPrice.getText();
		String unit= totalPrice1.replaceAll("[$:a-zA-Z]","");
		System.out.println(unit);
		double finalTotalPrice= Double.parseDouble(unit);
		System.out.println(finalTotalPrice);
		return finalTotalPrice;
		}
 	
	
	public double getOrderTax() {
		String totalTax1= tax.getText();
		System.out.println(totalTax1);
		String unit= totalTax1.replaceAll("[$:a-zA-Z]","");
		System.out.println(unit);

		double finalTotalTax= Double.parseDouble(unit);
		System.out.println(finalTotalTax);

		return finalTotalTax;
	}
	
	public boolean validateCancelButton() {
		
		return actions.isDisplayed(driver, cancel);
	}
	
	public OrderConfrimationPage clickOnFinish() {
		
		actions.click(finish, "Finish");
		
		return new OrderConfrimationPage(driver);
	}
}
