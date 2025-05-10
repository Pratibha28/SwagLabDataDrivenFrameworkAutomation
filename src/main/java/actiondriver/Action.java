package actiondriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import pages.BaseClass;

public class Action extends BaseClass {
	
	
	
	
	public boolean waitForElementToDisappear(By element){
	    
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));

	        wait.until((ExpectedConditions.invisibilityOfElementLocated(element)));
	        return true;
	    
	}
	

	public boolean type(WebElement ele, String text) {
		boolean flag = false;
		try {
			flag = ele.isDisplayed();
			ele.clear();
			ele.sendKeys(text);
			// logger.info("Entered text :"+text);
			flag = true;
		} catch (Exception e) {
			System.out.println("Location Not found");
			flag = false;
		} finally {
			if (flag) {
				//System.out.println("Successfully entered value");
			} else {
				System.out.println("Unable to enter value");
			}

		}
		return flag;
	}

	public boolean click(WebElement locator, String locatorName) {
		boolean flag = false;
		try {
			locator.click();
			flag = true;
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		} finally {
			if (flag) {
				//System.out.println("Able to click on \"" + locatorName + "\"");
			} else {
				System.out.println("Click Unable to click on \"" + locatorName + "\"");
			}
		}

	}

	public String getCurrentURL(WebDriver driver) {
		boolean flag = false;

		String text = driver.getCurrentUrl();
		if (flag) {
			System.out.println("Current URL is: \"" + text + "\"");
		}
		return text;
	}

	public boolean visibilityOfElement(WebDriver driver, WebElement element, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(element));
		
		return true;
	}

	public boolean selectByIndex(WebElement element, int index) {
		boolean flag = false;
		try {
			Select s = new Select(element);
			s.selectByIndex(index);
			flag = true;
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (flag) {
				System.out.println("Option selected by Index");
			} else {
				System.out.println("Option not selected by Index");
			}
		}
	}
	
	public boolean findElement(WebDriver driver, WebElement ele) {
		boolean flag = false;
		try {
			ele.isDisplayed();
			flag = true;
		} catch (Exception e) {
			// System.out.println("Location not found: "+locatorName);
			flag = false;
		} finally {
			if (flag) {
				System.out.println("Successfully Found element at");

			} else {
				System.out.println("Unable to locate element at");
			}
		}
		return flag;
	}

	public boolean isDisplayed(WebDriver driver, WebElement ele) {
		boolean flag = false;
		flag = findElement(driver, ele);
		
		try {
			if (flag) {
				flag = ele.isDisplayed();
				if (flag) {
					System.out.println("The element is Displayed");
				} else {
					System.out.println("The element is not Displayed");
				}
			}
			
		}catch(NoSuchElementException e){
		
		
			System.out.println("Not displayed ");
	
		}
		return flag;
	}
	
	
	public String screenShot(WebDriver driver, String filename) {
		
		String dateName= new SimpleDateFormat("ddMMyyyyhhmmss").format(new Date());
		TakesScreenshot takeScreenShot= (TakesScreenshot) driver;
		File source= takeScreenShot.getScreenshotAs(OutputType.FILE);
		String destination= System.getProperty("user.dir")+ "\\Screenshots\\" +filename+ "_" + dateName + ".png";
		
		try {
			FileUtils.copyFile(source, new File(destination));
		} catch (Exception e) {
			
			e.getMessage();
		}
		
		
		return destination;
		
	}
	
	
public boolean validateExpectedURL(String url) {
	  WebDriverWait wait= new WebDriverWait(getDriver(), Duration.ofSeconds(10));

		return wait.until(ExpectedConditions.urlToBe(url));
		
	}
	
	public Boolean   visibilityOfElementLocated(By element) {
		  WebDriverWait wait= new WebDriverWait(getDriver(), Duration.ofSeconds(10));

		  return wait.until(ExpectedConditions.visibilityOfElementLocated(element)).isDisplayed();
	}
	
	
	
	public List<WebElement> ListOfWebelement(By element){
		
		List<WebElement> list= getDriver().findElements(element);
		return null;
		
		
	}
	
	public void acceptAlertMethod() {
		if (getDriver() == null) {
	        System.out.println("Driver is null!");
	        return;
	    }
	    getDriver().switchTo().alert().accept();

		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}