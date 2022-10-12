package driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class MobileDriverFactory {
    protected AppiumDriver driver;
    protected  String device;
    protected  String appiumServer;
    protected  String systemPort;
    protected  String platformVersion;
    protected Logger log;
    protected  String deviceName;
    protected  String platformName;
    protected  String WdaLocalPort;

    public MobileDriverFactory(String device, String appiumServer, String systemPort, String platformVersion, Logger log, String deviceName, String platformName, String WdaLocalPort) {
        this.device = device;
        this.appiumServer = appiumServer.toLowerCase();
        this.systemPort = systemPort.toLowerCase();
        this.platformVersion = platformVersion.toLowerCase();
        this.deviceName = deviceName.toLowerCase();
        this.WdaLocalPort = WdaLocalPort.toLowerCase();
        this.platformName=platformName.toLowerCase();
        this.log = log;
    }

    public AppiumDriver createDriver() throws MalformedURLException {
        //Create appium driver
        log.info("Start driver whit this parametrs:" + "\n" + "IP this device: " + device + " \n " + "appiumServer: " + appiumServer + " \n " + "systemPort: " + systemPort + " \n " + "platformVersion: " + platformVersion + " \n " + "deviceName: " + deviceName + " \n " + "platformName: "  +platformName );
        DesiredCapabilities cap = new DesiredCapabilities();

        switch (platformName) {
            case "ios":
                cap.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
                cap.setCapability("unicodeKeyboard", true);
                cap.setCapability("resetKeyboard", true);
                cap.setCapability(MobileCapabilityType.NO_RESET, "true");
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
                cap.setCapability(IOSMobileCapabilityType.AUTO_DISMISS_ALERTS,"true");
                cap.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, "true");
                cap.setCapability(IOSMobileCapabilityType.LOCATION_SERVICES_AUTHORIZED, "true");
                cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
                cap.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
                cap.setCapability(MobileCapabilityType.UDID, device);
//                cap.setCapability("xcodeOrgId", "com.discountios1.WebDriverAgentRunner");
                cap.setCapability("xcodeSigningId", "iPhone Developer");
//                cap.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.infocell.zapMain");
                cap.setCapability(MobileCapabilityType.BROWSER_NAME,"Safari");
                cap.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, "true");
                cap.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, WdaLocalPort);
                URL url = new URL(appiumServer);
                driver = new IOSDriver(url, cap);
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                driver.manage().deleteAllCookies();
                Assert.assertNotNull(driver);
                break;

            case "android":
                ChromeOptions options = new ChromeOptions();
                cap.setCapability(AndroidMobileCapabilityType.PLATFORM_NAME, platformName);
                cap.setCapability("unicodeKeyboard", true);
                cap.setCapability("resetKeyboard", true);
                cap.setCapability(MobileCapabilityType.NO_RESET,"true");
                cap.setCapability(MobileCapabilityType.FULL_RESET,"false");
                cap.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS,"true");
                cap.setCapability("platformVersion", platformVersion);
                cap.setCapability("automationName", "UiAutomator2");
                cap.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT,systemPort);
                cap.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
                cap.setCapability(MobileCapabilityType.UDID, device);
                cap.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
//                cap.setCapability("chromedriverExecutable", "E:\\project\\zap_cars_mobile\\src\\main\\resources\\chromedriver.exe");
                options.setExperimentalOption("w3c", false);
//                options.addArguments("--disable-notifications");
                cap.setCapability(ChromeOptions.CAPABILITY, options);
                URL url1 = new URL(appiumServer);
                driver = new AndroidDriver(url1, cap);
                driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
                Assert.assertNotNull(driver);
                break;
        }
        return driver;
    }

    public void check_lock_devices_IOS() {
        if (((IOSDriver) driver).isDeviceLocked())
            ((IOSDriver) driver).unlockDevice();
    }
    public void check_lock_devices_Android() {
        if (((AndroidDriver) driver).isDeviceLocked())
            ((AndroidDriver) driver).unlockDevice();
    }



}
