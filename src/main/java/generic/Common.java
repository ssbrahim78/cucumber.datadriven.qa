package generic;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Common {

    public static WebDriver driver;
    public static WebDriverWait wait;
    public String browserstack_username = "mhshahib1";
    public String browserstack_accesskey = "YA4xsqrMqFurrGduX1X9";
    public String saucelabs_username = "";
    public String saucelabs_accesskey = "";

    //Set Properties
    public static void setAppProperties(String MyKey,String MyValue,String propertiesFilePath) throws IOException {
        Properties prop = new Properties();
        InputStream ism = new FileInputStream(propertiesFilePath);
        prop.load(ism);
        FileOutputStream ism1 = new FileOutputStream(propertiesFilePath);
        if(prop.containsKey(MyKey)){
            prop.replace(MyKey,MyValue);
        }else{
            prop.put(MyKey,MyValue);
        }
        prop.store(ism1,null);
        ism1.close();
    }

//    public Properties loadProperties(String propertiesFilePath) throws IOException {
//        Properties prop = new Properties();
////        "src/test/resources/config.properties"
//        InputStream ism = new FileInputStream(propertiesFilePath);
//        prop.load(ism);
//        ism.close();
//        return prop;
//    }

//==========================================================================
public Properties loadProperties() throws IOException {
    Properties prop = new Properties();
    //InputStream ism = new FileInputStream("/secret.properties");
    InputStream ism = new FileInputStream("src/main/resources/config.properties");
    prop.load(ism);
    //    conect=  DriverManager.getConnection(String url,String os,);
    boolean useCloudEnv = Boolean.parseBoolean(prop.getProperty("UseCloudEnv"));
    String cloudEnvName = prop.getProperty("CloudEnvName");
    String os = prop.getProperty("Os");
    String os_version = prop.getProperty("Os_version");
    String browserName = prop.getProperty("BrowserName");
    String browserVersion = prop.getProperty("BrowserVersion");
    String url = prop.getProperty("Url");
    long implicitlyWaitTime=Long.parseLong(prop.getProperty("ImplicitlyWaitTime"));
    //long implicitlyWaitTime =implicitlywaitTime.longValue();
    // System.out.println(useCloudEnv + " and   " + cloudEnvName + " and " + url + " and  " + os + "  and " + os_version + "  " + browserName + " and " + browserVersion);
    setUp( useCloudEnv,  cloudEnvName,
            os,  os_version,  browserName,
            browserVersion,  url,  implicitlyWaitTime);
    ism.close();
    return prop;
}
    //===================================================================



    public void setUp(Boolean useCloudEnv, String cloudEnvName,
                      String os, String os_version, String browserName,
                      String browserVersion, String url, long implicitlyWaitTime) throws IOException {

        if (useCloudEnv == true) {
            if (cloudEnvName.equalsIgnoreCase("browserstack")) {
                getCloudDriver(cloudEnvName, browserstack_username, browserstack_accesskey, os, os_version, browserName, browserVersion);
            } else if (cloudEnvName.equalsIgnoreCase("saucelabs")) {
                getCloudDriver(cloudEnvName, saucelabs_username, saucelabs_accesskey, os, os_version, browserName, browserVersion);
            }
        } else {
            getLocalDriver(browserName);
        }
        driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.get(url);
    }

    public WebDriver getLocalDriver(String browserName) {

        if (browserName.equalsIgnoreCase("Chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();

        } else if (browserName.equalsIgnoreCase("Firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();

        } else if (browserName.equalsIgnoreCase("IE")) {
            WebDriverManager.iedriver().setup();
            driver = new InternetExplorerDriver();

        } else if (browserName.equalsIgnoreCase("Edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();

        } else if (browserName.equalsIgnoreCase("chrome-options")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);
        }
        return driver;
    }

    public WebDriver getCloudDriver(String envName, String envUsername, String envAccessKey, String os, String os_version, String browserName,
                                    String browserVersion) throws IOException {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("browser", browserName);
        cap.setCapability("browser_version", browserVersion);
        cap.setCapability("os", os);
        cap.setCapability("os_version", os_version);
        if (envName.equalsIgnoreCase("Saucelabs")) {
            //resolution for Saucelabs
            driver = new RemoteWebDriver(new URL("http://" + envUsername + ":" + envAccessKey +
                    "@ondemand.saucelabs.com:80/wd/hub"), cap);
        } else if (envName.equalsIgnoreCase("Browserstack")) {
            cap.setCapability("resolution", "1024x768");
            driver = new RemoteWebDriver(new URL("http://" + envUsername + ":" + envAccessKey +
                    "@hub-cloud.browserstack.com/wd/hub"), cap);
        }
        return driver;
    }
    //screenShot Method
    public void screenShot(Scenario scenario) throws IOException {
        if (scenario.isFailed()) {
            try {
                Object Timestamp = new SimpleDateFormat(" yy-MM-dd HH-mm-ss").format(new Date());
                TakesScreenshot shot = (TakesScreenshot) driver;
                File file = shot.getScreenshotAs(OutputType.FILE);
                File screensho_Destination = new File("./target/Screenshot/Screenshot" + scenario.getName() + Timestamp + ".png");
                FileUtils.copyFile(file, screensho_Destination);
            } catch (WebDriverException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                scenario.getName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // Helper methods
    public void clickOnElement(String locator) {
        try {
            driver.findElement(By.cssSelector(locator)).click();
        } catch (Exception ex) {
            try {
                driver.findElement(By.className(locator)).click();
            } catch (Exception ex2) {
                try {
                    driver.findElement(By.id(locator)).click();
                } catch (Exception ex3) {
                    driver.findElement(By.xpath(locator)).click();
                }
            }
        }
    }

    public void typeOnElement(String locator, String value) {
        try {
            driver.findElement(By.cssSelector(locator)).sendKeys(value);
        } catch (Exception ex) {
            driver.findElement(By.xpath(locator)).sendKeys(value);
        }
    }

    public static void typeOnElementNEnter(String locator, String value) {
        try {
            driver.findElement(By.cssSelector(locator)).sendKeys(value, Keys.ENTER);
        } catch (Exception ex1) {
            try {
                System.out.println("First Attempt was not successful");
                driver.findElement(By.name(locator)).sendKeys(value, Keys.ENTER);
            } catch (Exception ex2) {
                try {
                    System.out.println("Second Attempt was not successful");
                    driver.findElement(By.xpath(locator)).sendKeys(value, Keys.ENTER);
                } catch (Exception ex3) {
                    System.out.println("Third Attempt was not successful");
                    driver.findElement(By.id(locator)).sendKeys(value, Keys.ENTER);
                }
            }
        }
    }

    public static void typeOnElementNEnter(String locator, String value, WebDriver driver1) {
        try {
            driver1.findElement(By.cssSelector(locator)).sendKeys(value, Keys.ENTER);
        } catch (Exception ex1) {
            try {
                System.out.println("First Attempt was not successful");
                driver1.findElement(By.id(locator)).sendKeys(value, Keys.ENTER);
            } catch (Exception ex2) {
                try {
                    System.out.println("Second Attempt was not successful");
                    driver1.findElement(By.name(locator)).sendKeys(value, Keys.ENTER);
                } catch (Exception ex3) {
                    System.out.println("Third Attempt was not successful");
                    driver1.findElement(By.xpath(locator)).sendKeys(value, Keys.ENTER);
                }
            }
        }
    }

    public void clearField(String locator) {
        driver.findElement(By.id(locator)).clear();
    }

    public void navigateBack() {
        driver.navigate().back();
    }

    public static void clickOnElement(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            System.out.println("UNABLE TO CLICK ON ELEMENT\n");
            e.getMessage();
            e.printStackTrace();
        }
    }

    public void typeOnInputField(String locator, String value) {
        try {
            driver.findElement(By.cssSelector(locator)).sendKeys(value);
        } catch (Exception ex) {
            driver.findElement(By.id(locator)).sendKeys(value);
        }

    }

    public void clickByXpath(String locator) {
        driver.findElement(By.xpath(locator)).click();
    }

    public void typeByCss(String locator, String value) {
        driver.findElement(By.cssSelector(locator)).sendKeys(value);
    }

    public void typeByCssNEnter(String locator, String value) {
        driver.findElement(By.cssSelector(locator)).sendKeys(value, Keys.ENTER);
    }

    public void typeByXpath(String locator, String value) {
        driver.findElement(By.xpath(locator)).sendKeys(value);
    }

    public void takeEnterKeys(String locator) {
        driver.findElement(By.cssSelector(locator)).sendKeys(Keys.ENTER);
    }

    public void clearInputField(String locator) {
        driver.findElement(By.cssSelector(locator)).clear();
    }

    public List<WebElement> getListOfWebElementsById(String locator) {
        List<WebElement> list = new ArrayList<WebElement>();
        list = driver.findElements(By.id(locator));
        return list;
    }

    public static List<String> getTextFromWebElements(String locator) {
        List<WebElement> element = new ArrayList<WebElement>();
        List<String> text = new ArrayList<String>();
        element = driver.findElements(By.cssSelector(locator));
        for (WebElement web : element) {
            String st = web.getText();
            text.add(st);
        }
        return text;
    }

    public static List<String> getTextFromWebElements(String locator, WebDriver driver1) {
        List<WebElement> element = new ArrayList<WebElement>();
        List<String> text = new ArrayList<String>();
        element = driver1.findElements(By.cssSelector(locator));
        for (WebElement web : element) {
            String st = web.getText();
            text.add(st);
        }
        return text;
    }

    public static List<WebElement> getListOfWebElementsByCss(WebElement element, String locator) {
        List<WebElement> list = element.findElements(By.cssSelector(locator));
        return list;
    }

    public static List<WebElement> getListOfWebElementsByCss(String locator, WebDriver driver1) {
        List<WebElement> list = new ArrayList<WebElement>();
        list = driver1.findElements(By.cssSelector(locator));
        return list;
    }

    public List<WebElement> getListOfWebElementsByXpath(WebElement element, String locator) {
        List<WebElement> list = new ArrayList<WebElement>();
        list = element.findElements(By.xpath(locator));
        return list;
    }

    public List<WebElement> getListOfWebElementsByTagName(WebElement element, String tagName) {
        List<WebElement> list = new ArrayList<WebElement>();
        list = element.findElements(By.tagName(tagName));
        return list;
    }

    public String getCurrentPageUrl() {
        String url = driver.getCurrentUrl();
        return url;
    }

    public String getCurrentPageTitle() {
        String title = driver.getTitle();
        return title;
    }


    public void navigateForward() {
        driver.navigate().forward();
    }

    public String getTextByCss(String locator) {
        String st = driver.findElement(By.cssSelector(locator)).getText();
        return st;
    }

    public String getTextByXpath(String locator) {
        String st = driver.findElement(By.xpath(locator)).getText();
        return st;
    }

    public String getTextById(String locator) {
        return driver.findElement(By.id(locator)).getText();
    }

    public String getTextByName(String locator) {
        String st = driver.findElement(By.name(locator)).getText();
        return st;
    }

    public List<String> getListOfString(List<WebElement> list) {
        List<String> items = new ArrayList<String>();
        for (WebElement element : list) {
            items.add(element.getText());
        }
        return items;
    }

    public void selectOptionByVisibleText(WebElement element, String value) {
        Select select = new Select(element);
        select.selectByVisibleText(value);
    }

    public static void sleepFor(int sec) throws InterruptedException {
        Thread.sleep(sec * 1000);
    }

    public void mouseHoverByCSS(String locator) {
        try {
            WebElement element = driver.findElement(By.cssSelector(locator));
            Actions action = new Actions(driver);
            Actions hover = action.moveToElement(element);
        } catch (Exception ex) {
            System.out.println("First attempt has been done, This is second try");
            WebElement element = driver.findElement(By.cssSelector(locator));
            Actions action = new Actions(driver);
            action.moveToElement(element).perform();
        }
    }

    public static void mouseHover(WebElement element) {
        try {
            Actions hover = new Actions(driver);
            hover.moveToElement(element).perform();
        } catch (Exception ex) {
            driver.navigate().refresh();
            System.out.println("1st mouse-hover attempt failed - Attempting 2nd time");

            WebDriverWait wait = new WebDriverWait(driver, 10);
            Actions hover = new Actions(driver);

            wait.until(ExpectedConditions.visibilityOf(element));
            hover.moveToElement(element).perform();
        }
    }

    //handling Alert
    public void okAlert() {
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    public void cancelAlert() {
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
    }

    //iFrame Handle
    public void iframeHandle(WebElement element) {
        driver.switchTo().frame(element);
    }

    public void goBackToHomeWindow() {
        driver.switchTo().defaultContent();
    }

    //get Links
    public void getLinks(String locator) {
        driver.findElement(By.linkText(locator)).findElement(By.tagName("a")).getText();
    }

    //Synchronization
    public void waitUntilClickAble(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitUntilVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitUntilSelectable(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        boolean element = wait.until(ExpectedConditions.elementToBeSelected(locator));
    }

    public void upLoadFile(String locator, String path) {
        driver.findElement(By.cssSelector(locator)).sendKeys(path);
        /* path example to upload a file/image
           path= "C:\\Users\\rrt\\Pictures\\ds1.png";
         */
    }

    public void clearInput(String locator) {
        driver.findElement(By.cssSelector(locator)).clear();
    }

    public void keysInput(String locator) {
        driver.findElement(By.cssSelector(locator)).sendKeys(Keys.ENTER);
    }

    //Handling New Tabs
    public static WebDriver handleNewTab(WebDriver driver1) {
        String oldTab = driver1.getWindowHandle();
        List<String> newTabs = new ArrayList<String>(driver1.getWindowHandles());
        newTabs.remove(oldTab);
        driver1.switchTo().window(newTabs.get(0));
        return driver1;
    }

    public static boolean isPopUpWindowDisplayed(WebDriver driver1, String locator) {
        boolean value = driver1.findElement(By.cssSelector(locator)).isDisplayed();
        return value;
    }

    public void typeOnInputBox(String locator, String value) {
        try {
            driver.findElement(By.id(locator)).sendKeys(value, Keys.ENTER);
        } catch (Exception ex1) {
            System.out.println("ID locator didn't work");
        }
        try {
            driver.findElement(By.name(locator)).sendKeys(value, Keys.ENTER);
        } catch (Exception ex2) {
            System.out.println("Name locator didn't work");
        }
        try {
            driver.findElement(By.cssSelector(locator)).sendKeys(value, Keys.ENTER);
        } catch (Exception ex3) {
            System.out.println("CSS locator didn't work");
        }
    }


    // Custom-made Helper Methods for Amex.com
    public void brokenLink() throws IOException {
        //Step:1-->Get the list of all the links and images
        List<WebElement> linkslist = driver.findElements(By.tagName("a"));
        linkslist.addAll(driver.findElements(By.tagName("img")));

        System.out.println("Total number of links and images--------->>> " + linkslist.size());

        List<WebElement> activeLinks = new ArrayList<WebElement>();
        //Step:2-->Iterate linksList: exclude all links/images which does not have any href attribute
        for (int i = 0; i < linkslist.size(); i++) {
            //System.out.println(linkslist.get(i).getAttribute("href"));
            if (linkslist.get(i).getAttribute("href") != null && (!linkslist.get(i).getAttribute("href").contains("javascript") && (!linkslist.get(i).getAttribute("href").contains("mailto")))) {
                activeLinks.add(linkslist.get(i));
            }
        }
        System.out.println("Total number of active links and images-------->>> " + activeLinks.size());

        //Step:3--> Check the href url, with http connection api
        for (int j = 0; j < activeLinks.size(); j++) {

            HttpURLConnection connection = (HttpURLConnection) new URL(activeLinks.get(j).getAttribute("href")).openConnection();

            connection.connect();
            String response = connection.getResponseMessage();
            connection.disconnect();
            System.out.println(activeLinks.get(j).getAttribute("href") + "--------->>> " + response);
        }
    }

    public void inputValueInTextBoxByWebElement(WebElement webElement, String value) {

        webElement.sendKeys(value + Keys.ENTER);

    }

    public void clearInputBox(WebElement webElement) {
        webElement.clear();
    }

    public String getTextByWebElement(WebElement webElement) {
        String text = webElement.getText();
        return text;
    }


    public static void clickJScript(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    public void scrollToElementJScript(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
    }

    public static void mouseHoverJScript(WebElement element) {
        try {
            if (isElementPresent(element)) {
                String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
                ((JavascriptExecutor) driver).executeScript(mouseOverScript, element);
                System.out.println("Hover performed\n");
            } else {
                System.out.println("UNABLE TO HOVER OVER ELEMENT\n");
            }
        } catch (StaleElementReferenceException e) {
            System.out.println("ELEMENT WITH " + element
                    + " IS NOT ATTACHED TO THE PAGE DOCUMENT"
                    + e.getStackTrace());
        } catch (NoSuchElementException e) {
            System.out.println("ELEMENT " + element + " WAS NOT FOUND IN DOM"
                    + e.getStackTrace());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR OCCURED WHILE HOVERING\n"
                    + e.getStackTrace());
        }
    }

    public static boolean isElementPresent(WebElement element) {
        boolean flag = false;
        try {
            if (element.isDisplayed()
                    || element.isEnabled())
                flag = true;
        } catch (NoSuchElementException e) {
            flag = false;
        } catch (StaleElementReferenceException e) {
            flag = false;
        }
        return flag;
    }


    /**
     * Helper Methods To Use in Asserts
     *
     * @author Sami Sheikh
     */

    // Hover over dropdown and make sure it is visible (built-in page refresh)
    public void hoverOverDropdown(WebElement elementToHover) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(elementToHover));

        try {
            Thread.sleep(1000);
            mouseHover(elementToHover);
            System.out.println("Hovered over dropdown\n");
        } catch (InterruptedException e) {
            try {
                driver.navigate().refresh();
                System.out.println("Couldn't hover over dropdown --- Refreshing page\n");

                wait.until(ExpectedConditions.visibilityOf(elementToHover));
                Thread.sleep(1000);

                mouseHoverJScript(elementToHover);
            } catch (Exception e1) {
                e.getMessage();
            }
        }
    }

    // Gets text from List<WebElements> and compares against expected String array from Excel workbook
//    public static boolean compareWebElementListToExpectedStringArray(By by, String path, String sheetName) throws IOException {
//        List<WebElement> actualList = driver.findElements(by);
//        String[] expectedList = dataReader.fileReaderStringXSSF(path, sheetName);
//
//        String[] actual = new String[actualList.size()];
//
//        for (int j = 0; j<actualList.size(); j++) {
//            actual[j] = actualList.get(j).getText().replaceAll("&amp;", "&").replaceAll("’", "'").trim();
//            actual[j].replaceAll("&amp;", "&").replaceAll("’", "'").trim();
////            escapeHtml4(actual[j]);
////            escapeHtml3(actual[j]);
//        }
//
//        int falseCount = 0;
//        boolean flag = false;
//        for (int i = 0; i < expectedList.length; i++) {
//            if (actual[i].equalsIgnoreCase(expectedList[i])) {
//                flag = true;
//                System.out.println("ACTUAL STRING " + (i + 1) + ": " + actual[i]);
//                System.out.println("EXPECTED STRING " + (i + 1) + ": " + expectedList[i] + "\n");
//            } else {
//                System.out.println("***FAILED AT INDEX " + (i + 1) + "***\nEXPECTED STRING: " + expectedList[i] +
//                        "\nACTUAL STRING: " + actual[i] + "\n");
//                falseCount++;
//            }
//        }
//        if (falseCount > 0) {
//            flag = false;
//        }
//        return flag;
//    }
//
//    // Gets text from String[] and compares against expected String array from Excel workbook
//    public static boolean compareTextListToExpectedStringArray(String[] actualArray, String path, String sheetName) throws IOException {
//        String[] expectedList = dataReader.fileReaderStringXSSF(path, sheetName);
//
//        int falseCount = 0;
//        boolean flag = false;
//        for (int i = 0; i < expectedList.length; i++) {
//            if (actualArray[i].replaceAll("&amp;", "&").replaceAll("’", "'").trim().equalsIgnoreCase(expectedList[i])) {
//                flag = true;
//                System.out.println("ACTUAL " + (i + 1) + ": " + actualArray[i]);
//                System.out.println("EXPECTED " + (i + 1) + ": " + expectedList[i] + "\n");
//            } else {
//                System.out.println("FAILED AT INDEX " + (i + 1) + "\nEXPECTED STRING: " + expectedList[i] + "\nACTUAL STRING: "
//                        + actualArray[i]);
//                falseCount++;
//            }
//        }
//        if (falseCount > 0) {
//            flag = false;
//        }
//        return flag;
//    }
//
//    // Compares actual string against an expected string from Excel workbook
//    public static boolean compareTextToExpectedString(String actual, String path, String sheetName) throws IOException {
//        String[] expectedArray = dataReader.fileReaderStringXSSF(path, sheetName);
//        String expected = expectedArray[0];
//
//        boolean flag;
//        if (actual.replaceAll("&amp;", "&").replaceAll("’", "'").trim().equalsIgnoreCase(expected)) {
//            flag = true;
//            System.out.println("ACTUAL STRING: " + actual + "\nEXPECTED STRING: " + expected);
//        } else {
//            flag = false;
//            System.out.println("***DOES NOT MATCH***\nEXPECTED STRING: " + expected + "\nACTUAL STRING: " + actual);
//        }
//        return flag;
//    }
//
//    // Gets text from List<WebElements> and compares against expected String array from Excel workbook
//    public static boolean compareAttributeListToExpectedStringArray(By by, String attribute, String path, String sheetName) throws IOException {
//        List<WebElement> actualList = driver.findElements(by);
//        String[] expectedList = dataReader.fileReaderStringXSSF(path, sheetName);
//
//        String[] actual = new String[actualList.size()];
//
//        for (int j = 0; j<actualList.size(); j++) {
//            actual[j] = actualList.get(j).getAttribute(attribute).replaceAll("&amp;", "&").replaceAll("’", "'").replaceAll("<br>", "\n").trim();
//            actual[j].replaceAll("&amp;", "&").replaceAll("’", "'").replaceAll("<br>", "\n").trim();
////            escapeHtml4(actual[j]);
////            escapeHtml3(actual[j]);
//        }
//
//        int falseCount = 0;
//        boolean flag = false;
//        for (int i = 0; i < expectedList.length; i++) {
//            if (actual[i].equalsIgnoreCase(expectedList[i])) {
//                flag = true;
//                System.out.println("ACTUAL " + attribute.toUpperCase() + " " + (i + 1) + ": " + actual[i]);
//                System.out.println("EXPECTED " + attribute.toUpperCase() + " " + (i + 1) + ": " + expectedList[i] + "\n");
//            } else {
//                System.out.println("FAILED AT INDEX " + (i+1) + "\nEXPECTED " + attribute.toUpperCase() + ": " + expectedList[i] +
//                        "\nACTUAL " + attribute.toUpperCase() + ": " + actual[i] + "\n");
//                falseCount++;
//            }
//        }
//        if (falseCount > 0) {
//            flag = false;
//        }
//        return flag;
//    }
//
//    public static boolean compareListSizeToExpectedCount(By by, String path, String sheetName) throws IOException {
//        int[] expectedArray = dataReader.fileReaderIntegerXSSF(path, sheetName);
//        int expected = expectedArray[0];
//
//        List<WebElement> dropdownList = driver.findElements(by);
//        int actual = dropdownList.size();
//        System.out.println("Counted " + actual + " elements\n");
//
//        boolean flag;
//        if (actual == expected) {
//            flag = true;
//            System.out.println("ACTUAL COUNT: " + actual + "\nEXPECTED COUNT: " + expected);
//        } else {
//            flag = false;
//            System.out.println("***SIZE DOES NOT MATCH***\nACTUAL COUNT: " + actual + "\nEXPECTED COUNT: " + expected + "\n");
//        }
//        return flag;
//    }
//

    // Switches to newly opened tab, gets URL, closes new tab, switches back to parent tab
    public static String switchToTabAndGetURL() {
        java.util.Iterator<String> iter = driver.getWindowHandles().iterator();

        String parentWindow = iter.next();
        String childWindow = iter.next();

        driver.switchTo().window(childWindow);
        System.out.println("Switched to \"" + driver.getTitle() + "\" window");
        String actualURL = driver.getCurrentUrl();
        System.out.println(actualURL + "\n");
        driver.close();

        driver.switchTo().window(parentWindow);
        System.out.println("Switched back to \"" + driver.getTitle() + "\" window");
        System.out.println(driver.getCurrentUrl() + "\n");

        return actualURL;
    }

    // Switches to newly opened tab, gets URL and compares to expected URL in Excel workbook
//    public static boolean switchToTabAndCompareURL(String path, String sheetName) throws IOException {
//        java.util.Iterator<String> iter = driver.getWindowHandles().iterator();
//
//        String parentWindow = iter.next();
//        String childWindow = iter.next();
//
//        driver.switchTo().window(childWindow);
//        System.out.println("Switched to \"" + driver.getTitle() + "\" window");
//        String actualURL = driver.getCurrentUrl();
//        System.out.println(actualURL + "\n");
//        driver.close();
//
//        driver.switchTo().window(parentWindow);
//        System.out.println("Switched back to \"" + driver.getTitle() + "\" window");
//        System.out.println(driver.getCurrentUrl() + "\n");
//
//     //   boolean flag = compareTextToExpectedString(actualURL, path, sheetName);
//
//     //   return flag;
//    }

    // Loops through list of dropdown elements, clicks on each link individually, grabs each page URL, inserts into String[],
    // closes child page & switches back to parent page
    //
    // Compares String[] to expected URLs in Excel workbook
//    public static boolean clickLinksSwitchTabsCompareURLs(WebElement hoverElement, By by, String path, String sheetName) throws InterruptedException, IOException {
//        Wait fluentWait = new FluentWait(driver)
//                .withTimeout(Duration.ofSeconds(10))
//                .pollingEvery(Duration.ofSeconds(2))
//                .ignoring(Exception.class);
//
//        List<WebElement> list = driver.findElements(by);
//        int listSize = list.size();
//        String[] actualURLs = new String[listSize];
//
//        int i = 0;
//        for (WebElement element : list) {
//            if (hoverElement.isEnabled()) {
//                fluentWait.until(ExpectedConditions.elementToBeClickable(element));
//                element.sendKeys(Keys.CONTROL, Keys.ENTER);
//                Thread.sleep(800);
//                actualURLs[i] = switchToTabAndGetURL();
//            } else if (!(hoverElement.isEnabled())){
//                try {
//                    mouseHoverJScript(hoverElement);
//                } catch (Exception e){
//                    mouseHover(hoverElement);
//                }
//                fluentWait.until(ExpectedConditions.elementToBeClickable(element));
//                element.sendKeys(Keys.CONTROL, Keys.ENTER);
//                Thread.sleep(800);
//                actualURLs[i] = switchToTabAndGetURL();
//            }
//            i++;
//        }
//        boolean flag = compareTextListToExpectedStringArray(actualURLs, path, sheetName);
//
//        return flag;
//    }

    // navigatBackWindow
    public void navigatebackWindow() {
        driver.navigate().back();
    }


}
