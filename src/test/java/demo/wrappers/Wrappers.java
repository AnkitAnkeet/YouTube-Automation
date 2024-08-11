package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Wrappers {
   
    
    
    //method to click or select any element from header area
     public static WebElement iconFromHeader(ChromeDriver driver,By locator){
        //defining parent element 
        WebElement headerArea = driver.findElement(By.xpath("//div[@id='container' and @class='style-scope ytd-masthead']"));
        return headerArea.findElement(locator);

     }

      //method to select any element from left area
     public static void selectFromLeft(ChromeDriver driver,String option) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Adjust timeout as needed

        try {
        //define all elements from leftbar
            List<WebElement> leftElements = driver.findElements(By.xpath("//div[@id='contentContainer' and @class='style-scope tp-yt-app-drawer']//a"));
            
            boolean optionSelected = false;
            for (WebElement leftElement : leftElements) {
                //check if the text or title matches the given option
                if (leftElement.getText().trim().equals(option) || leftElement.getAttribute("title").equals(option)) {
                    //wait until the element is clickable
                    wait.until(ExpectedConditions.elementToBeClickable(leftElement));
            
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].scrollIntoView(true);", leftElement);
                    leftElement.click();
                    Thread.sleep(4000);//to wait for corresponding page to load 
                    optionSelected = true;
                    break;
                }
            }
            if(!optionSelected){
                System.err.println("Option '" + option + "' not found in the left bar.");
            }
            
        } catch (Exception e) {
            System.err.println("Error selecting option from the left bar: " + e.getMessage());
        }
    }

     //method to print for element(s)
     public static void printText(ChromeDriver driver, By locator) {
        try {
            List<WebElement> printables = driver.findElements(locator);
            if (printables.isEmpty()) {
                System.out.println("No elements found with locator: " + locator.toString());
            } else {
                for (WebElement printable : printables) {
                    System.out.println(printable.getText());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("No elements found with locator: " + locator.toString());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
    //method to wait for any element which is only for to click
    public static void waitForElement(ChromeDriver driver, By locator) {
        try {
             WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
             wait.until(ExpectedConditions.elementToBeClickable(locator));
             System.out.println("Element is clickable: " + locator.toString());

        } catch (TimeoutException e) {
            System.out.println("Element with locator " + locator.toString() + " was not clickable after 10 seconds.");
        } catch (NoSuchElementException e) {
            System.out.println("No element found with locator: " + locator.toString());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }





//method to get element from any tab by using its positional index
 public static WebElement getElement(ChromeDriver driver, String section, int posnOfElement) throws InterruptedException {
    WebElement element = null;

    try {
        List<WebElement> sectionElements = driver.findElements(By.xpath("//span[contains(@class,'style-scope') and @id='title']"));
        List<WebElement> elements1 = new ArrayList<>();

        for (WebElement sectionElement : sectionElements) {
            if (sectionElement.getText().trim().equals(section)) {
                //locate the webelements for "News" section and put them in a list
                List<WebElement> elements2 = sectionElement.findElements(By.xpath(".//ancestor::div[@id='dismissible']//ytd-rich-item-renderer"));

                if (!elements2.isEmpty()) {
                    if (posnOfElement - 1 < elements2.size()) {
                        element = elements2.get(posnOfElement - 1);
                    } else {
                        System.out.println("Index " + (posnOfElement - 1) + " is out of bounds for elements2.");
                    }
                } else {
                    //right swipe to load all elements (movies/music) 
                    WebElement rightSwipeButton = sectionElement.findElement(By.xpath("./ancestor::div[@id='dismissible']//button[@aria-label='Next']"));

                    while (rightSwipeButton.isDisplayed()) {
                        rightSwipeButton.click();
                        Thread.sleep(1000); //to avoid rapid clicking
                    }
                    //locate the webelements of movies/music and put them in a list
                    elements1 = sectionElement.findElements(By.xpath("./ancestor::div[@id='dismissible']//div[@id='items']/*[@class='style-scope yt-horizontal-list-renderer']"));

                    if (posnOfElement - 1 < elements1.size()) {
                        element = elements1.get(posnOfElement - 1);
                    } else {
                        System.out.println("Index " + (posnOfElement - 1) + " is out of bounds for elements1.");
                    }
                }
            }
        }
    } catch (NoSuchElementException e) {
        System.out.println("Element not found: " + e.getMessage());
    } catch (IndexOutOfBoundsException e) {
        System.out.println("Index out of bounds: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("An unexpected error occurred: " + e.getMessage());
    }

    return element;
}



//method to get mark of movie 
public static String getMark(WebElement parentElement) {
    try {
        WebElement element = parentElement.findElement(By.xpath("./ytd-badge-supported-renderer//div[contains(@class,'simple')]"));
        String mark = element.getAttribute("aria-label").trim();
        return mark;
    } catch (NoSuchElementException e) {
        System.out.println("Element not found with the provided XPath.");
        return "Element not found";
    } catch (Exception e) {
        System.out.println("An unexpected error occurred: " + e.getMessage());
        return "Error occurred";
    }
}


//method to get the catagory detail of selected movie
public static String getCategory(WebElement parentElement) {
    try {
        WebElement element = parentElement.findElement(By.xpath("./a/span[text()]"));
        String category = element.getText().trim();
        return category;
    } catch (NoSuchElementException e) {
        System.out.println("Element not found with the provided XPath.");
        return "Element not found";
    } catch (Exception e) {
        System.out.println("An unexpected error occurred: " + e.getMessage());
        return "Error occurred";
    }
}




//method to count number from any music list
public static int countNumbers(WebElement elementToCount) {
        WebElement element = elementToCount.findElement(By.xpath(".//p[@id='video-count-text']"));
        String countStr = element.getText().trim();
        //extracting the numerics from the string and converting it into int 
        int count = (int) extractValue(countStr);
        return count;
  
}



        //method to print the content of webelement
        public static String printContent(WebElement element, By locator) {
            String content = "";
    
            try {
                WebElement element1 = element.findElement(locator);
                content = element1.getText().trim();
    
                if (content.isEmpty()) {
                    throw new Exception("Content is empty");
                }
            } catch (NoSuchElementException e) {
                System.out.println("Element not found: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
    
            return content;
        }


       //method to get any element from any section by using option index 
       public static WebElement getLastElement(ChromeDriver driver, int fromTab) throws InterruptedException {
        try {
            //locate all section elements
            List<WebElement> sectionElements = driver.findElements(By.xpath("//span[contains(@class,'style-scope') and @id='title']"));

            if (sectionElements.isEmpty()) {
                throw new NoSuchElementException("No section elements found with the specified XPath");
            }

            //check if the index is valid
            if (fromTab <= 0 || fromTab > sectionElements.size()) {
                throw new IndexOutOfBoundsException("Index out of bounds: " + fromTab);
            }

            WebElement sectionElement = sectionElements.get(fromTab - 1);
            WebElement rightSwipeButton = sectionElement.findElement(By.xpath("./ancestor::div[@id='dismissible']//button[@aria-label='Next']"));

            //swipe right until the button is no longer displayed to load all elements
            while (rightSwipeButton.isDisplayed()) {
                rightSwipeButton.click();
                Thread.sleep(1000); //to avoid rapid clicking 
                //checking for the rightSwipeButton in case it changes after click
                rightSwipeButton = sectionElement.findElement(By.xpath("./ancestor::div[@id='dismissible']//button[@aria-label='Next']"));
            }
            //locate the elements for "movies"/"music"
            List<WebElement> elements = sectionElement.findElements(By.xpath("./ancestor::div[@id='dismissible']//div[@id='items']/*[@class='style-scope yt-horizontal-list-renderer']"));

            if (elements.isEmpty()) {
                throw new NoSuchElementException("No elements found within the specified section");
            }

            //return the last element from the list
            WebElement lastElement = elements.get(elements.size() - 1);
            return lastElement;

        } catch (NoSuchElementException e) {
            System.out.println("No such element found: " + e.getMessage());
            throw e; // Rethrow to ensure the caller is aware of the failure
        } catch (StaleElementReferenceException e) {
            System.out.println("Stale element reference: " + e.getMessage());
            throw e; // Rethrow to ensure the caller is aware of the failure
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted: " + e.getMessage());
            throw e; // Rethrow to ensure the caller is aware of the failure
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            throw e; // Rethrow to ensure the caller is aware of the failure
        }
    }


    //method to count the like counts 
    public static int getLikesCount(ChromeDriver driver, int posnOfElement) {
        List<WebElement> likeElements = driver.findElements(By.xpath("//span[@id='vote-count-middle']"));
        
        WebElement likeElement = likeElements.get(posnOfElement - 1);
        
        int count = 0;
        
        String strOfLike = likeElement.getText().trim();
        
        if (strOfLike.isEmpty()) {
            count = 0; //if empty count should be zero
        } else {
            if (strOfLike.toLowerCase().contains("k")) {
                count = (int) extractValue(strOfLike) * 1000;
            } else if (strOfLike.toLowerCase().contains("m")) {
                count = (int) extractValue(strOfLike) * 1000000;
            } else {
                count = (int) extractValue(strOfLike);
            }
        }
        
        return count;
    }
    

    //method to extract the numerics
    public static double extractValue(String str) {
        StringBuilder numericPart = new StringBuilder();
        
        for (char ch : str.toCharArray()) {
            if (Character.isDigit(ch) || ch == '.') {
                numericPart.append(ch);
            }
        }
        
        return Double.parseDouble(numericPart.toString());
     }


     //method to search for elements
     public static void searchFor(ChromeDriver driver, String[] texts){
        for(String text:texts){
                iconFromHeader(driver, By.xpath(".//input")).clear();
                iconFromHeader(driver, By.xpath(".//input")).sendKeys(text);
                iconFromHeader(driver, By.xpath(".//*[@id='search-icon-legacy']")).click();
                
     }
    }
    
        
    
            
    
        
    } 

    


            




    

