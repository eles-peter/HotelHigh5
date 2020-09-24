package com.progmasters.hotel.selenium.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {
    private WebDriver driver;

    @BeforeEach
    public void startBrowser() {
        ClassLoader classLoader = LoginTest.class.getClassLoader();
        System.setProperty("webdriver.chrome.driver", classLoader.getResource("win/chromedriver.exe").getFile());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        driver = new ChromeDriver(options);
    }

    @Test
    public void loginUserWithWrongPassword() {
        driver.get("http://localhost:4200");
        waitTime();
        String loginTitle = driver.findElement(By.cssSelector("#navbar > ul > li:nth-child(2) > a")).getAttribute("innerHTML");
        assertEquals(loginTitle, "Bejelentkezés");
        driver.findElement(By.cssSelector("#navbar > ul > li:nth-child(2) > a")).click();
        waitTime();

        driver.findElement(By.id("email")).sendKeys("hotel.team.five.u@gmail.com");
        waitTime();

        driver.findElement(By.id("password")).sendKeys("UserTest");
        waitTime();

        loginTitle = driver.findElement(By.cssSelector("#loginMember > div.text-center > div > button")).getAttribute("innerHTML");
        assertEquals("Belépés ", loginTitle);
        waitTime();
        driver.findElement(By.cssSelector("#loginMember > div.text-center > div > button")).click();
        waitTime();
        waitTime();
    }

    @Test
    public void loginUser() {
        driver.get("http://localhost:4200");
        waitTime();
        String loginTitle = driver.findElement(By.cssSelector("#navbar > ul > li:nth-child(2) > a")).getAttribute("innerHTML");
        assertEquals(loginTitle, "Bejelentkezés");
        driver.findElement(By.cssSelector("#navbar > ul > li:nth-child(2) > a")).click();
        waitTime();

        driver.findElement(By.id("email")).sendKeys("hotel.team.five.u@gmail.com");
        waitTime();

        driver.findElement(By.id("password")).sendKeys("User");
        waitTime();

        loginTitle = driver.findElement(By.cssSelector("#loginMember > div.text-center > div > button")).getAttribute("innerHTML");
        assertEquals("Belépés ", loginTitle);
        waitTime();
        driver.findElement(By.cssSelector("#loginMember > div.text-center > div > button")).click();
        waitTime();
        waitTime();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    public void waitTime() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
