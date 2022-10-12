package listeners;

import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;

import java.util.List;

public class SuiteAlterer implements IAlterSuiteListener {

    @Override
    public void alter(List<XmlSuite> tests) {
        int count = Integer.parseInt(System.getProperty("threadcount", "1"));
        int count_provider = Integer.parseInt(System.getProperty("threadcountProvider", "1"));
        XmlSuite test= tests.get(0);
        test.setThreadCount(count);
        test.setDataProviderThreadCount(count_provider);
    }


}
