package listeners;

import base.BaseTest;
import base.TestUtilities;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.*;

import java.io.ByteArrayInputStream;
import java.util.List;

public class TestListener extends TestUtilities implements ITestListener, IInvokedMethodListener {

    Logger log;
    String testName;
    protected ThreadLocal<String> testMethodName= new ThreadLocal<String>();
    protected String jsonPath="src/main/resources/Selenoid_properties.json";

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod() && testResult.isSuccess()) {
            Object x = testResult.getInstance();
            BaseTest currentCase = (BaseTest) x;
            WebDriver driver = currentCase.getDriver();
            log.info(testMethodName.get()+"is Success and screenshot taken ");
            Allure.addAttachment(testMethodName.get(), new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
            String URL_FILE_TO_VIDEO= "http://"+ip_selenoid("src/main/resources/ip_output.txt")+":4444/video/"+((RemoteWebDriver)driver).getSessionId()+".mp4";
            Allure.addAttachment("url for Success video","text/uri-list",URL_FILE_TO_VIDEO);

        }
        if (method.isTestMethod() && !testResult.isSuccess()) {
            Object x = testResult.getInstance();
            BaseTest currentCase = (BaseTest) x;
            WebDriver driver = currentCase.getDriver();
            log.info(testMethodName.get()+"is Faild and screenshot taken ");
            Allure.addAttachment(testMethodName.get(), new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
            String URL_FILE_TO_VIDEO= "http://"+ip_selenoid("src/main/resources/ip_output.txt")+":4444/video/"+((RemoteWebDriver)driver).getSessionId()+".mp4";
            Allure.addAttachment("url for faild video","text/uri-list",URL_FILE_TO_VIDEO);



        }
    }
    @Override
    public void onTestStart(ITestResult iTestResult) {
        testMethodName.set(iTestResult.getMethod().getMethodName());
        log.info("[Starting " + testMethodName.get() + "]");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        log.info("[Test " + testMethodName.get() + " passed]");
        Object testClass = iTestResult.getInstance();
//        WebDriver driver = (WebDriver) iTestResult.getAttribute("WebDriver");
//        Allure.addAttachment(testMethodName, new ByteArrayInputStream(((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES)));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        log.info("[Test " + testMethodName.get() + " failed]");
        Object testClass = iTestResult.getInstance();
//        WebDriver driver = (WebDriver) iTestResult.getAttribute("WebDriver");
//            Allure.addAttachment(testMethodName, new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {
        this.testName = iTestContext.getSuite().getName();
        this.log = LogManager.getLogger(testName);
        log.info("[TEST " + testName + " STARTED]");
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        log.info("[ALL " + testName + " FINISHED]");
    }
}
