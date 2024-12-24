package seminar.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import seminar.pom.elements.GroupTableRow;
import seminar.pom.elements.StudentTableRow;

public class MainPage {
    private final WebDriverWait wait;

    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private WebElement usernameLinkInNavBar;

    @FindBy(id = "create-btn")
    private WebElement createGroupButton;

    @FindBy(xpath = "//form//span[contains(text(), 'Group name')]/following-sibling::input")
    private WebElement groupNameField;

    @FindBy(css = "form div.submit button")
    private WebElement submitButtonModalWindow;

    public MainPage(WebDriver driver, WebDriverWait wait) {
        PageFactory.initElements(driver, this);
        this.wait = wait;
    }

    public WebElement waitAndGetGroupTitleByText(String title) {
        String xpath = String.format("//table[@aria-label='Tutors list']/tbody//td[text()='%s']", title);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public void createGroup(String groupName) {
        wait.until(ExpectedConditions.visibilityOf(createGroupButton)).click();
        wait.until(ExpectedConditions.visibilityOf(groupNameField)).sendKeys(groupName);
        submitButtonModalWindow.click();
        waitAndGetGroupTitleByText(groupName);
    }

    public String getUsernameLabelText() {
        return wait.until(ExpectedConditions.visibilityOf(usernameLinkInNavBar))
                .getText().replace("\n", " ");
    }

    public void closeCreateGroupModalWindow() {
        WebElement closeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button.modal-close")
        ));
        closeButton.click();
    }

    public String getStatusofGroupWithTitle(String groupName) {
        String xpath = String.format("//table[@aria-label='Tutors list']/tbody//td[text()='%s']/following-sibling::td[1]", groupName);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).getText();
    }

    public void clickTrashIconOnGroupWithTitle(String groupName) {
        String xpath = String.format("//table[@aria-label='Tutors list']/tbody//td[text()='%s']/following-sibling::td/button[text()='delete']", groupName);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
    }

    public void clickRestoreFromTrashIconOnGroupWithTitle(String groupName) {
        String xpath = String.format("//table[@aria-label='Tutors list']/tbody//td[text()='%s']/following-sibling::td/button[text()='restore_from_trash']", groupName);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
    }

    public GroupTableRow getGroupByTitle(String groupTitle) {
        String xpath = String.format("//table[@aria-label='Groups list']/tbody//td[text()='%s']/parent::tr", groupTitle);
        WebElement groupRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return new GroupTableRow(groupRow);
    }

    public StudentTableRow getStudentByName(String studentName) {
        String xpath = String.format("//table[@aria-label='Students list']/tbody//td[text()='%s']/parent::tr", studentName);
        WebElement studentRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return new StudentTableRow(studentRow);
    }
}
