package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.logging.Level;


// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases { 
        static ChromeDriver driver;

      
       
        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);

                driver.manage().window().maximize();
                //launch the "www.YouTube.com" url
                driver.get("https://www.youtube.com");
                Assert.assertTrue(driver.getCurrentUrl().contains("youtube"),"Url is not successfully launched");
                System.out.println("Start Test: YouTube Automation");
        }

        @AfterTest
        public void endTest() {
                System.out.println("End Test: YouTube Automation:)");
                driver.close();
                driver.quit();

        }

        @Test(priority = 1)
        public static void testCase01() throws InterruptedException{
                
                System.out.println("Start TestCase: testCase01");
                //click on menu bar
                Wrappers.iconFromHeader(driver,By.xpath("./div/yt-icon-button[@id='guide-button']")).click();
                //click on "About"
                Wrappers.selectFromLeft(driver,"About");
                //print the useful message 
                if(driver.getCurrentUrl().toLowerCase().contains("about")){ 
                Wrappers.printText(driver,By.xpath("//p[@class='lb-font-display-3 lb-font-color-text-primary']"));
                }
                //go back to the home page
                driver.navigate().back();
                System.out.println("End TestCase: testCase01");
        }


         @Test(priority = 5)
        public static void testCase02() throws InterruptedException{
                System.out.println("Start TestCase: testCase02");
                //wait for the menu to be clickable incase
                Wrappers.waitForElement(driver, By.xpath("//div[@id='container' and @class='style-scope ytd-masthead']/div/yt-icon-button[@id='guide-button']"));
                //click on menu
                Wrappers.iconFromHeader(driver,By.xpath("./div/yt-icon-button[@id='guide-button']")).click();
                //click on the "Movies" element although in DOM it clearly has attribute title='Films' and text "Films" too
                Wrappers.selectFromLeft(driver,"Movies");  
                //check whether the last movie of "Top Selling" is marked "A" for mature
                SoftAssert softAssert = new SoftAssert();
                //softAssert.assertEquals(Wrappers.getMark(Wrappers.getElement(driver,"Top selling",16)),"A",  "the movie is not marked A for Mature"); 
                //check whether the last movie is either "Comedy" or "Animation"
                softAssert.assertTrue(Wrappers.getCategory(Wrappers.getElement(driver,"Top selling",16)).contains("Comedy") || Wrappers.getCategory(Wrappers.getElement(driver, "Top selling",16)).contains("Animation") || (Wrappers.getCategory(Wrappers.getElement(driver,"Top selling",16)).contains("Drama")),  "The category of the  movie is neither Animation nor Comedy nor Drama");
                softAssert.assertAll();
                System.out.println("End TestCase: testCase02");
        }


         @Test (priority = 2)
        public static void testCase03() throws InterruptedException {
                System.out.println("Start TestCase: testCase03");
                //wait for the menu to be clickable incase
                 Wrappers.waitForElement(driver,By.xpath("//div[@id='container' and @class='style-scope ytd-masthead']/div/yt-icon-button[@id='guide-button']"));
                //click on menu
                Wrappers.iconFromHeader(driver,By.xpath("./div/yt-icon-button[@id='guide-button']")).click();
                //click on Music
                Wrappers.selectFromLeft(driver,"Music");
                //print the Name of last playlist of the tab which comes first  
                System.out.println(Wrappers.printContent(Wrappers.getLastElement(driver,1), By.xpath(".//h3")) );
                //check whether the number of tracks listed is less than or equal to 50.
                SoftAssert softAssert = new SoftAssert();
                softAssert.assertTrue(Wrappers.countNumbers(Wrappers.getLastElement(driver,1))<=50,"The number of tracks is greater than 50");
                softAssert.assertAll();
                System.out.println("End TestCase: testCase03");
        }
         @Test(priority = 3)
        public static void testCase04() throws InterruptedException{
                System.out.println("Start TestCase: testCase04");
                //wait for the menu to be clickable incase
                Wrappers.waitForElement(driver,By.xpath("//div[@id='container' and @class='style-scope ytd-masthead']/div/yt-icon-button[@id='guide-button']"));
                //click on menu
                Wrappers.iconFromHeader(driver,By.xpath("./div/yt-icon-button[@id='guide-button']")).click();
                //go to news tab
                Wrappers.selectFromLeft(driver,"News");
                //print the title and body of first news from latest news section
                System.out.println(Wrappers.printContent(Wrappers.getElement(driver, "Latest news posts", 1),  By.xpath("./div/..")) );
                //print the title and body of first news from latest news section
                System.out.println(Wrappers.printContent(Wrappers.getElement(driver, "Latest news posts", 2),By.xpath("./div/.."  )) );
                //print the title and body of first news from latest news section
                System.out.println(Wrappers.printContent(Wrappers.getElement(driver, "Latest news posts", 3),By.xpath("./div/.."  )) );
                //the sum of the number of likes on 1st, second and third news
                System.out.println(Wrappers.getLikesCount(driver,1)+Wrappers.getLikesCount(driver,2)+Wrappers.getLikesCount(driver,3));
                System.out.println("End TestCase: testCase04");
        }     

}

                