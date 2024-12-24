package seminar.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ModalWindow {
    private WebDriver driver;

    public ModalWindow(WebDriver driver) {
        this.driver = driver;
    }

    public void addStudent(String studentName) {
        WebElement input = driver.findElement(By.id("student-name-input"));
        input.sendKeys(studentName);
        driver.findElement(By.id("add-button")).click();
    }

    public void close() {
        driver.findElement(By.id("close-button")).click();
    }
}
