package seminar.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import seminar.pom.LoginPage;
import seminar.pom.MainPage;
import seminar.pom.ModalWindow;
import seminar.pom.elements.GroupTableRow;
import seminar.pom.elements.StudentTableRow;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeekBrainsTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private MainPage mainPage;

    private static final String USERNAME = "Student-20";
    private static final String PASSWORD = "30f1aee69f";

    @BeforeAll
    public static void setupClass() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
//        USERNAME = System.setProperty("geekbrains_username", System.getenv("geekbrains_username"));
//        PASSWORD = System.setProperty("geekbrains_password", System.getenv("geekbrains_password"));
    }


    @BeforeEach
    void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setVersion("128.0");

        URL url = new URL("http://localhost:4444/wd/hub");

        driver = new RemoteWebDriver(url, capabilities);

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();
        driver.get("https://test-stand.gb.ru/login");
        loginPage = new LoginPage(driver, wait);
        mainPage = new MainPage(driver, wait);
    }

    @Test
    public void testGeekBrainsStandLogin() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
    }

    @Test
    public void loginWithoutCredentialsShouldShowError() {
        loginPage.clickLoginButton();

        assertEquals("401 Invalid credentials.", loginPage.getErrorBlockText());
    }

    @Test
    public void testAddingGroupOnMainPage() throws IOException {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String groupTestName = "New Test Group" + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        assertTrue(mainPage.waitAndGetGroupTitleByText(groupTestName).isDisplayed());
        byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Files.write(
                Path.of("src/test/resources/screenshot_" + System.currentTimeMillis() + ".png"),
                screenshotBytes
        );
    }

    @Test
    void testArchiveGroupOnMainPage() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String groupTestName = "New Test Group" + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        mainPage.closeCreateGroupModalWindow();
        assertEquals("active", mainPage.getStatusofGroupWithTitle(groupTestName));
        mainPage.clickTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("inactive", mainPage.getStatusofGroupWithTitle(groupTestName));
        mainPage.clickRestoreFromTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("active", mainPage.getStatusofGroupWithTitle(groupTestName));
    }

    @Test
    public void studentCountShouldChangeWhenAddingAndRemovingStudents() {
        mainPage = new MainPage(driver, wait);

        GroupTableRow groupRow = mainPage.getGroupByTitle("Group name"); // Получаем строку таблицы по названию группы

        int initialStudentCount = groupRow.getStudentCount(); // Метод для получения количества студентов
        groupRow.clickAddStudentIcon(); // Метод клика по иконке добавления студента

        ModalWindow modal = new ModalWindow(driver);
        modal.addStudent("Student name");
        modal.close();

        int updatedStudentCount = groupRow.getStudentCount();
        assertThat(updatedStudentCount).isEqualTo(initialStudentCount + 1);
        groupRow.clickViewStudentsIcon();
        StudentTableRow studentRow = mainPage.getStudentByName("Student name");
        assertThat(studentRow.getStatus()).isEqualTo("Active");
        studentRow.clickTrashIcon();
        assertThat(studentRow.getStatus()).isEqualTo("Deleted");
        studentRow.clickRestoreIcon();
        assertThat(studentRow.getStatus()).isEqualTo("Active");
    }

    @AfterEach
    void tearDownTest() {
        driver.quit();
    }
}
