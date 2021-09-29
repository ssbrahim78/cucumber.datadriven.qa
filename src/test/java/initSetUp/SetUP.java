package initSetUp;

import generic.Common;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.support.PageFactory;
import webPages.AmazonHomePage;

import java.io.IOException;
import java.util.Properties;

public  class SetUP extends Common {
    public static AmazonHomePage amazonHomePage;
    // get parameters from Jenkins
//    String PropertiesFilePath = "src/test/resources/config.properties";
//    String useCloudEnvJ= System.getProperty("UseCloudEnv");
//    String cloudEnvNameJ= System.getProperty("CloudEnvName");
//    String osJ= System.getProperty("Os");
//    String os_versionJ = System.getProperty("Os_version");
//    String browserNameJ = System.getProperty("BrowserName");
//    String browserVersionJ = System.getProperty("BrowserVersion");
//    String urlJ = System.getProperty("Url");
//    String implicitlyWaitTimeJ=System.getProperty("ImplicitlyWaitTime");
//
//   // Read properties from propertie file
//    Properties prop = loadProperties(PropertiesFilePath);
//    Boolean useCloudEnv= Boolean.parseBoolean(prop.getProperty("UseCloudEnv")) ;
//    String cloudEnvName= prop.getProperty("CloudEnvName");
//    String os= prop.getProperty("Os");
//    String os_version = prop.getProperty("Os_version");
//    String browserName = prop.getProperty("BrowserName");
//    String browserVersion = prop.getProperty("BrowserVersion");
//    String url = prop.getProperty("Url");
//    int implicitlyWaitTime=Integer.parseInt(prop.getProperty("ImplicitlyWaitTime"));



   // jenkins
//    Boolean useCloudEnv= Boolean.parseBoolean(System.getProperty("UseCloudEnv"));
//    String cloudEnvName= System.getProperty("CloudEnvName");
//    String os= System.getProperty("Os");
//    String os_version = System.getProperty("Os_version");
//    String browserName = System.getProperty("BrowserName");
//    String browserVersion = System.getProperty("BrowserVersion");
//    String url = System.getProperty("Url");
//    int implicitlyWaitTime=Integer.parseInt(System.getProperty("ImplicitlyWaitTime"));

    public static void Init() {

        amazonHomePage = PageFactory.initElements(driver,AmazonHomePage.class);

    }

//    public static void main(String[] args) throws IOException {
//        System.out.println("karim");
//        Common com = new Common();
//
//
//        System.out.println(com.loadProperties().getProperty("Os_version"));
//    }

    @Before
    public void setUp_Init() throws IOException {
        // put the parameter in the prop file
//        setAppProperties("UseCloudEnv",useCloudEnvJ, PropertiesFilePath);
//        setAppProperties("UseCloudEnv",cloudEnvNameJ, PropertiesFilePath);
//        setAppProperties("UseCloudEnv",osJ, PropertiesFilePath);
//        setAppProperties("UseCloudEnv",os_versionJ, PropertiesFilePath);
//        setAppProperties("UseCloudEnv",browserNameJ, PropertiesFilePath);
//        setAppProperties("UseCloudEnv",browserVersionJ, PropertiesFilePath);
//        setAppProperties("UseCloudEnv",urlJ, PropertiesFilePath);
//        setAppProperties("UseCloudEnv",implicitlyWaitTimeJ, PropertiesFilePath);

        loadProperties();
        Init();
    }
    //ScreenShot method
    @After
    public void tearDown(Scenario scenario) throws IOException {
        screenShot(scenario);
        driver.quit();

    }
}
