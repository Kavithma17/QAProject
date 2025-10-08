package com.example.QAProject.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskUiTest {

    private WebDriver driver;

    @BeforeAll
    void setup() {
//        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");

        // ✅ Add this line to fix your issue
        options.addArguments("--user-data-dir=/tmp/chrome-" + System.currentTimeMillis());
        driver = new ChromeDriver(options);
    }

    @AfterAll
    void teardown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void testLoginWithValidCredentials() {
        driver.get("http://localhost:8080/login.html");

        // Enter username
        WebElement usernameInput = driver.findElement(By.id("username"));
        usernameInput.sendKeys("admin");

        // Enter password
        WebElement passwordInput = driver.findElement(By.id("password"));
        passwordInput.sendKeys("admin");

        // Click the login button
        WebElement loginButton = driver.findElement(By.id("loginBtn"));
        loginButton.click();

        // Wait until redirected to task.html
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(webDriver -> webDriver.getCurrentUrl().contains("task.html"));

        // Assert that URL changed to task.html
        assertTrue(driver.getCurrentUrl().contains("task.html"), "User should be redirected to task.html after login");
    }




    @Test
    void testAddTaskUi() throws InterruptedException {
        driver.get("http://localhost:8080/task.html"); // task page
        driver.findElement(By.id("taskTitle")).sendKeys("Selenium Task");
        driver.findElement(By.id("taskDesc")).sendKeys("Automated UI test");
        driver.findElement(By.tagName("button")).click();

        // wait a bit for JS to render
        Thread.sleep(1000);

        String taskText = driver.findElement(By.id("taskList")).getText();
        assertTrue(taskText.contains("Selenium Task"), "Task list should contain the new task");
    }
}
