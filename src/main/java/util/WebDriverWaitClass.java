package util;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pages.BaseClass;

public class WebDriverWaitClass extends BaseClass {
	WebDriver driver;
    WebDriverWait wait;
    
	public WebDriverWaitClass(WebDriver driver) {
		this.driver= driver;
		wait= new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	public void validateExpectedURL(String url) {
		wait.until(ExpectedConditions.urlToBe(url));
		
	}
	
	public Boolean   visibilityOfElementLocated(By element) {
		
		  return wait.until(ExpectedConditions.visibilityOfElementLocated(element)).isDisplayed();
	}
	
}
