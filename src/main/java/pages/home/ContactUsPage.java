package pages.home;

import base.BasePageObject;
import io.qameta.allure.Step;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class ContactUsPage extends BasePageObject {
    public ContactUsPage(WebDriver driver, Logger log) {
        super(driver, log);
    }

    @FindBy(xpath = "//div[contains(text(),'איך משחזרים קוד סודי לכרטיס האשראי?')]")
    private WebElement question;
    @FindBy(xpath = "//div[contains(text(),'איך משחזרים קוד סודי לכרטיס האשראי?')]/..//div[@class='answer']")
    private WebElement answer;


    @Step("לחיצה על שאלה")
    public ContactUsPage click_question(){
        Assert.assertTrue(m_click(question), "The click of question is faild");
        return this;
    }

    @Step("הצגת טקסט של התשובה")
    public String  get_answer_text(){
        Assert.assertTrue(getTextElement(answer), "The click of question is faild");
        return getText(answer);
    }

}


