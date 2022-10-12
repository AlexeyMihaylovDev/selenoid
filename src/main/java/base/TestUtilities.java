package base;

import excelData.JSONReader;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestUtilities extends BaseTest {






	protected void openUrl(String url) {
		log.info("get new site by url:  "+url);
		getDriver().get(url);
	}
	protected String  getURL() {
		 return getDriver().getCurrentUrl();
	}

	protected  void openNewTab(String url) {
		getDriver().findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
		ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
		getDriver().switchTo().window(tabs.get(0));
		getDriver().get(url);
	}
	// STATIC SLEEP
	protected void sleep(long millis) {
		try {
			log.info("sleeping a : "+millis+" millisecond");
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected JSONReader dJson(String JsonPath) {
		JSONReader readJ = new JSONReader(JsonPath);
		return readJ;
	}



	/** Switch to iFrame using it's locator */
	protected void switchToFrame(WebElement element) {
		log.info("swich to frame :"+" "+element);
		getDriver().switchTo().frame(element);
	}

	/** Todays date in yyyyMMdd format */
	private static String getTodaysDate() {
		return (new SimpleDateFormat("yyyyMMdd").format(new Date()));
	}

	/** Current time in HHmmssSSS */
	private String getSystemTime() {
		return (new SimpleDateFormat("HHmmssSSS").format(new Date()));
	}

	/** Get logs from browser console */
	protected List<LogEntry> getBrowserLogs() {
		LogEntries log = getDriver().manage().logs().get("browser");
		List<LogEntry> logList = log.getAll();
		return logList;
	}


	public  boolean ElementsIsDisplayed(WebElement element) {
		try {
			element.isDisplayed();
			log.info("The element is Displayed");
			return true;
		} catch (NoSuchElementException ex) {
			log.warn("The element  is not Displayed");
			return false;

		}
	}
	public  void swichWindow() {
		for (String winHandle : getDriver().getWindowHandles()) {
			getDriver().switchTo().window(winHandle);
		}
	}
	public  boolean waitForElement(WebElement element) {
		 boolean isElementPresent;
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(),Duration.ofSeconds(30));
			wait.until(ExpectedConditions.visibilityOf(element));
			isElementPresent = element.isDisplayed();
			sleep(200);
			return isElementPresent;
		} catch (Exception e) {
			isElementPresent = false;
			System.out.println(e.getMessage());
			return isElementPresent;
		}
	}
	public  boolean waitelementTovisibilityOf(WebElement element) {
		 boolean isElementPresent;
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(),Duration.ofSeconds(30));

			wait.until(ExpectedConditions.visibilityOf(element));
			isElementPresent = element.isDisplayed();
			sleep(200);
			return isElementPresent;
		} catch (Exception e) {
			isElementPresent = false;
			System.out.println(e.getMessage());
			return isElementPresent;
		}
	}


	protected  String Replace(String element) {
		String str = element;
		str = str.replaceAll("[^0-9.]", "");
		return str;
	}
	protected  String replaceAllSpaces(String element) {
		String str = element;
		str = str.replaceAll(" ", "");
		return str;
	}

	protected void click_text_link(String text) {
		List<WebElement> allLinks = getDriver().findElements(By.tagName("a"));
		for (WebElement specificlink : allLinks) {
			if (specificlink.getText().equals(text)) {
				specificlink.click();
				break;
			}
		}
	}
	protected  double ReplaceAndDouble(WebElement element) {
		String str = element.getText();
		str = str.replaceAll("[^0-9.]", "");
		double num = Double.valueOf(str);
		return num;
	}

	protected void scroolToElement(WebElement element){
		((JavascriptExecutor)getDriver()).executeScript("arguments[0].scrollIntoView();", element);

	}


	protected boolean waitElementDisplayed(WebElement el) {
		if (el == null)
			return false;

		for (int i = 0; i < 2; i++) { // 2 tries to make tap
			try {
				el.isDisplayed();
				return true;
			} catch (Exception e) {
				log.error("click(): FAILED\n" + e.getMessage());
			}
			log.error("waitElement(): RETRY '" + i + "' ----------------");
		}
		return false;
	}

	protected boolean check_equals_string(List list,String text){
		if (list ==null)
			for (int y = 0; y < list.size(); y++) {
				try {
					list.get(y).equals(text);
					return true;
				} catch (Exception e) {
					log.error("text not found" + e.getMessage());
				}
				log.error("search text: RETRY '" + y + "' ----------------");
			}
			return false;
		}
	protected  String extract_NumbersOnly(String element) {
		String str = element;
		str = str.replaceAll("[^0-9.]", "");
		return str;
	}

	protected  String extract_LettersOnly(String element) {
		String str = element;
		str = str.replaceAll("[^a-zA-Z ]", "");
		return str;
	}

}

