package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int nowCount=0;
    private int maxCount=1;
    public static Logger log;


    @Override
    public boolean retry(ITestResult iTestResult) {
        log = LogManager.getLogger();

        if (nowCount<maxCount) {
            nowCount++;
            return true; //пока истина перезапускаем
        }
        log.error("TEST FAILED TWICE "); // пишем в лог или делаем скриншот
        nowCount=0;
        return false;
    }
}