package driver;

import base.TestUtilities;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BrowserDriverFactory  extends TestUtilities {


	protected String browser;
	protected String numServer;
	protected String browser_ver;
	List<String> list = new ArrayList<>();
	protected WebDriver driver;
	protected static final String  BROWSERNAME  = "browserName";
	protected static final String  BROWSERVERSION  = "browserVersion";
	protected static final String  ENABLE_VNC  = "enableVNC";
	protected static final String  ENABLE_VIDEO  = "enableVideo";
	protected static final String SELENOIDPROP="src/main/resources/Selenoid_properties.json";
	protected static final String DISABLENOTOFOCATIONS="--disable-notifications";
	protected static final String DISABLEGPU="--disable-gpu";
	protected static final String DEVICENAME="deviceName";
	protected static final String DISABLEDEVSHM="--disable-dev-shm-usage";
	protected static final String NOSANDBOX="--no-sandbox";
	protected static final String SHMSIZE="--shm-size=2g";

	public BrowserDriverFactory(String browser, Logger log, String numServer,String browser_ver) {
		this.browser = browser;
		this.log = log;
		this.numServer=numServer;
		this.browser_ver=browser_ver;
	}

	public WebDriver createDriver() {
		ChromeOptions chromeOptions = new ChromeOptions();
		FirefoxOptions opt =new FirefoxOptions();
		Map<String, Object> mobileEmulation = new HashMap<>();
		Map<String, Object> deviceMetrics = new HashMap<>();
		// Create driver
		log.info("Create driver: " + browser+" " +browser_ver);


		switch (browser) {
			case "chrome":
				chromeOptions.addArguments(DISABLENOTOFOCATIONS);
				chromeOptions.setAcceptInsecureCerts(true);
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver(chromeOptions);
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
				driver.manage().window().maximize();
				break;
			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver(opt);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.manage().window().maximize();
				break;
			case "selenoid":
				chromeOptions.setCapability(BROWSERNAME, dJson(SELENOIDPROP).readData("browserName"));
//				chromeOptions.setCapability(BROWSERVERSION, browser_ver);
				chromeOptions.setAcceptInsecureCerts(true);
				chromeOptions.setCapability(ENABLE_VNC, true);
				chromeOptions.setCapability(ENABLE_VIDEO, true);
				chromeOptions.addArguments(DISABLENOTOFOCATIONS);
				chromeOptions.addArguments(DISABLEGPU);
				chromeOptions.addArguments(DISABLEDEVSHM);
				chromeOptions.addArguments(NOSANDBOX);
				chromeOptions.addArguments(SHMSIZE);
				chromeOptions.setCapability("sessionTimeout","10m");

				try {
					driver = new RemoteWebDriver(URI.create("http://"+ip_selenoid("src/main/resources/ip_output.txt")+":4444/wd/hub/").toURL(),
							chromeOptions);
				} catch (MalformedURLException e) {
					log.error("create driver is faild");

				}
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
				driver.manage().window().setSize(new Dimension(1920,1080));
				break;

			case "iPhone X":
				WebDriverManager.chromedriver().setup();
				mobileEmulation.put(DEVICENAME, "iPhone X");
				chromeOptions.setAcceptInsecureCerts(true);
				chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
				chromeOptions.addArguments(DISABLENOTOFOCATIONS);
				driver = new ChromeDriver(chromeOptions);
				driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
				break;
			case "Pixel 2":
				WebDriverManager.chromedriver().setup();
				chromeOptions.setAcceptInsecureCerts(true);
				mobileEmulation.put(DEVICENAME, "Nexus 7");
				chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
				chromeOptions.addArguments(DISABLENOTOFOCATIONS);
				driver = new ChromeDriver(chromeOptions);
				driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

				break;
			case "iPhone_selenoid":
				chromeOptions.setCapability(BROWSERNAME, dJson(SELENOIDPROP).readData("browserName"));
				chromeOptions.setCapability(BROWSERVERSION, browser_ver);
				chromeOptions.setAcceptInsecureCerts(true);
				chromeOptions.setCapability(ENABLE_VNC, true);
				chromeOptions.setCapability(ENABLE_VIDEO, true);
				chromeOptions.addArguments(DISABLENOTOFOCATIONS);
				chromeOptions.addArguments(DISABLEGPU);
				chromeOptions.addArguments(DISABLEDEVSHM);
				chromeOptions.addArguments(NOSANDBOX);
				chromeOptions.addArguments(SHMSIZE);
				chromeOptions.setCapability("sessionTimeout","10m");
				deviceMetrics.put("width", 428);
				deviceMetrics.put("height", 926);
				deviceMetrics.put("pixelRatio", 3.0);
				mobileEmulation.put("userAgent", "Mozilla/5.0 (iPhone; CPU iPhone OS 15_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.3 Mobile/15E148 Safari/604.1");
				mobileEmulation.put("deviceMetrics", deviceMetrics);
				chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

				switch (numServer){
					case "1":
						log.info("create driver on 192.168.99.171 server");
						list.add("www.mitchatnim.co.il:192.168.99.171");
						list.add("mitchatnim.co.il:192.168.99.171");
						list.add("mb.mitchatnim.co.il:192.168.99.171");
						list.add("preview.mitchatnim.co.il:192.168.99.171");
						chromeOptions.setCapability("hostsEntries",list);
						break;
					case "2":
						log.info("create driver on 192.168.99.172 server");
						list.add("www.mitchatnim.co.il:192.168.99.172");
						list.add("mitchatnim.co.il:192.168.99.172");
						list.add("mb.mitchatnim.co.il:192.168.99.172");
						list.add("preview.mitchatnim.co.il:192.168.99.172");
						chromeOptions.setCapability("hostsEntries",list);
						break;
				}
				try {
					driver = new RemoteWebDriver(URI.create(dJson(SELENOIDPROP).readData("selenoid_work_ip")).toURL(), chromeOptions);
				} catch (MalformedURLException e) {
					log.error("create driver is faild");

				}
				driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
				break;

			case "Samsung_Galaxy_S21_selenoid":
				chromeOptions.setCapability(BROWSERNAME, dJson(SELENOIDPROP).readData("browserName"));
				chromeOptions.setCapability(BROWSERVERSION, browser_ver);
				chromeOptions.setAcceptInsecureCerts(true);
				chromeOptions.setCapability(ENABLE_VNC, true);
				chromeOptions.setCapability(ENABLE_VIDEO, true);
				chromeOptions.addArguments(DISABLENOTOFOCATIONS);
				chromeOptions.addArguments(DISABLEGPU);
				chromeOptions.addArguments(DISABLEDEVSHM);
				chromeOptions.addArguments(NOSANDBOX);
				chromeOptions.addArguments(SHMSIZE);
				chromeOptions.setCapability("sessionTimeout","12m");
				deviceMetrics.put("width", 360);
				deviceMetrics.put("height", 740);
				deviceMetrics.put("pixelRatio", 3.0);
				mobileEmulation.put("deviceMetrics", deviceMetrics);
				mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 12; SAMSUNG SM-G991B) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/16.0 Chrome/100.0.4896.60 Mobile Safari/537.36"); //Samsung Galaxy S21
				chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
				switch (numServer){
					case "1":
						log.info("create driver on 192.168.99.171 server");
						list.add("www.mitchatnim.co.il:192.168.99.171");
						list.add("mitchatnim.co.il:192.168.99.171");
						list.add("mb.mitchatnim.co.il:192.168.99.171");
						list.add("preview.mitchatnim.co.il:192.168.99.171");
						chromeOptions.setCapability("hostsEntries",list);
						break;
					case "2":
						log.info("create driver on 192.168.99.172 server");
						list.add("www.mitchatnim.co.il:192.168.99.172");
						list.add("mitchatnim.co.il:192.168.99.172");
						list.add("mb.mitchatnim.co.il:192.168.99.172");
						list.add("preview.mitchatnim.co.il:192.168.99.172");
						chromeOptions.setCapability("hostsEntries",list);
						break;
									}
				try {
					driver = new RemoteWebDriver(
							URI.create(dJson(SELENOIDPROP).readData("selenoid_work_ip")).toURL(),
							chromeOptions
					);
				} catch (MalformedURLException e) {
					log.error("create driver is faild");
				}
				driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
				break;
			default:
				// Do nothing
		}

		return driver;
	}


}



