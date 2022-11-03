package base;

import driver.BrowserDriverFactory;
import driver.MobileDriverFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import listeners.TestListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.IInvokedMethod;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.Set;

@Listeners({TestListener.class})
public abstract class BaseTest {

	protected Logger log;
	protected ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();
	protected ThreadLocal<String> testMethodName = new ThreadLocal<String>();

	protected String testSuiteName;
	protected String testName;
	protected SoftAssert softAssert;



	public WebDriver getDriver() {
		return tdriver.get();
	}
	public String getTestMethodName() {
		return testMethodName.get();
	}

	@Parameters({"browser", "URL", "select_phone", "device", "appiumServer", "systemPort", "platformVersion", "deviceName", "platformName", "WdaLocalPort","numServer","browser_ver"})
	@BeforeMethod(alwaysRun = true)
	public void setUp(Method method,@Optional("chrome") String browser, String URL, ITestContext ctx, String select_phone, String device, String appiumServer, String systemPort, String platformVersion
			, String deviceName, String platformName, String WdaLocalPort,@Optional("51")String numServer,@Optional("107.0")String browser_ver)  {
		testName = ctx.getCurrentXmlTest().getName();
		testSuiteName = ctx.getSuite().getName();
		log = LogManager.getLogger(getTestMethodName());
		switch (select_phone) {
			case "emulators":
				for (int i=0; i < 2; i++){  // 2 tries to create driver
					try {
						BrowserDriverFactory emul_factory = new BrowserDriverFactory(browser, log, numServer,browser_ver);
						tdriver.set(emul_factory.createDriver());
						break;
					} catch (Exception e) {
						log.error("Create driver :FAILED\n" + e.getMessage());
						baseSleep(500);
					}
					log.error("Create driver : RETRY'" + i + "' ----------------");

				}
				break;
		}
		getDriver().get(URL);
		baseSleep(2000);
		softAssert = new SoftAssert();

	}

	@Parameters({"select_phone","platformName"})
	@BeforeMethod
	public void realdevices_actions(String select_phone,String platformName) {

		if(select_phone.equals("realDevices") && platformName.equals("android"))  {
			blockGeolocationChrome();
		}
	}





	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		try {
			log.info("close driver");
			getDriver().quit();
		} catch (Exception e) {
			log.error("Error quit the driver");
		}

	}



	protected void blockGeolocationChrome() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("Change to context: " + "NATIVE_APP");
		changeDriverContextTo("NATIVE_APP");
		WebElement element = getDriver().findElement(By.id("com.android.chrome:id/negative_button"));
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.android.chrome:id/negative_button")));
		element.click();
		changeDriverContextTo("CHROMIUM");

	}

	protected void changeDriverContextTo(String Con_name) {
		Set<String> allContext = ((SupportsContextSwitching) getDriver()).getContextHandles();
		for (String context : allContext) {
			if (context.equals(Con_name))
				((SupportsContextSwitching) getDriver()).context(context);
		}
	}


	// STATIC SLEEP
	protected void baseSleep(long millis) {
		try {
			log.info("sleeping a : " + millis + " millisecond");
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void check_lock_devices_IOS() {
		if (((IOSDriver) getDriver()).isDeviceLocked())
			((IOSDriver) getDriver()).unlockDevice();
	}

	public void check_lock_devices_Android() {
		if (((AndroidDriver) getDriver()).isDeviceLocked())
			((AndroidDriver) getDriver()).unlockDevice();
	}

}