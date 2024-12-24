package seminar.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(css = "form#login input[type='text']")
    private WebElement usernameField;

    @FindBy(css = "form#login input[type='password']")
    private WebElement passwordField;

    @FindBy(css = "form#login button")
    private WebElement loginButton;

    @FindBy(css = "div.error-block")
    private WebElement errorBlock;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get("https://test-stand.gb.ru/login");
    }

    public void login(String username, String password) {
        typeUsernameInField(username);
        typePasswordInField(password);
        clickLoginButton();
    }

    public void typeUsernameInField(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameField)).sendKeys(username);
    }

    public void typePasswordInField(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordField)).sendKeys(password);
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.visibilityOf(loginButton)).click();
    }

    public String getErrorBlockText() {

        return wait.until(ExpectedConditions.visibilityOf(errorBlock)).getText().replace("\n", " ");
    }
}
