package pages.home;

import base.BasePageObject;
import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;


public class HomePage extends BasePageObject {


    public HomePage(WebDriver driver, Logger log) {
        super(driver, log);
    }


    @FindBy(css = ".header-contacts--mail")
    private WebElement contact_us;


    @Step("לחיצה על יצירת קשר")
    public ContactUsPage click_contact_us(){
        Assert.assertTrue(m_click(contact_us), "The click of contact_us is faild");
        return new ContactUsPage(driver,log);
    }

}
