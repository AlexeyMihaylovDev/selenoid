package web.contact;

import base.TestUtilities;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.home.ContactUsPage;
import pages.home.HomePage;

public class CategoryTest extends TestUtilities {




    @Epic("Home page")
    @Feature("Contact Bank")
    @Test(testName = "Category", description = "לחיצה על שאלה איך משחזרים קוד סודי לכרטיס האשראי?")
    public void Click_conatct_us() {
        HomePage home = new HomePage(getDriver(), log);
        ContactUsPage contact = new ContactUsPage(getDriver(),log);
        home.click_contact_us()
                .click_question();
        Assert.assertEquals(contact.get_answer_text(),"באפליקציה\n" +
                "לוחצים על האייקון של שלושת הפסים מצד ימין > כל הפעולות > כרטיסי אשראי > קוד סודי לכרטיס אשראי\n" +
                "באתר\n" +
                "לוחצים על הפלוס הסגול > כרטיסי אשראי > שחזור קוד סודי\n" +
                "לשחזור קוד סודי באתר >>\n" +
                "להסבר מפורט יותר >>");




    }


}
