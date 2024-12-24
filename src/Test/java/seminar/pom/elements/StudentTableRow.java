package seminar.pom.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class StudentTableRow {
    private final WebElement root;

    public StudentTableRow(WebElement root) {
        this.root = root;
    }

    public String getStatus() {
        return root.findElement(By.xpath("./td[3]")).getText();
    }

    public void clickTrashIcon() {
        root.findElement(By.xpath("./td/button[text()='delete']")).click();
    }

    public void clickRestoreIcon() {
        root.findElement(By.xpath("./td/button[text()='restore']")).click();
    }
}
