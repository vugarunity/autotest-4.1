package seminar;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Test {

    public static void main(String[] args) {
        String pathToChromeDriver = "src/main/resources/chromedriver";
        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);

//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");


        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.google.com");
        System.out.println(("Page title: " + driver.getTitle()));
        driver.quit();
    }
}
