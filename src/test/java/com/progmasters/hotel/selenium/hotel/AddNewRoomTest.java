package com.progmasters.hotel.selenium.hotel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddNewRoomTest {
    private WebDriver driver;

    @BeforeEach
    public void startBrowser() {
        ClassLoader classLoader = AddNewRoomTest.class.getClassLoader();
        System.setProperty("webdriver.chrome.driver", classLoader.getResource("win/chromedriver.exe").getFile());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        driver = new ChromeDriver(options);
    }

    @Test
    public void AddNewRoomWithHotelOwnerTest() {
        loginHotelOwner();
        fillTheForm();
    }

    public void loginHotelOwner() {
        driver.get("http://localhost:4200");
        waitTime();
        String loginTitle = driver.findElement(By.cssSelector("#navbar > ul > li:nth-child(2) > a")).getAttribute("innerHTML");
        assertEquals(loginTitle, "Bejelentkezés");
        driver.findElement(By.cssSelector("#navbar > ul > li:nth-child(2) > a")).click();
        waitTime();

        driver.findElement(By.id("email")).sendKeys("hotel.team.five.h@gmail.com");
        waitTime();

        driver.findElement(By.id("password")).sendKeys("Hotelowner");
        waitTime();

        loginTitle = driver.findElement(By.cssSelector("#loginMember > div.text-center > div > button")).getAttribute("innerHTML");
        assertEquals("Belépés ", loginTitle);
        waitTime();
        driver.findElement(By.cssSelector("#loginMember > div.text-center > div > button")).click();
        waitTime();

    }

    public void fillTheForm() {
        waitTime();
        jumpDown();
        waitTime();
        String loginTitle = driver.findElement(By.cssSelector("body > app-root > div > app-hotel-detail > div > div.ng-star-inserted > button")).getAttribute("innerHTML");
        assertEquals(loginTitle, " Új szoba hozzáadása ");
        driver.findElement(By.cssSelector("body > app-root > div > app-hotel-detail > div > div.ng-star-inserted > button")).click();
        waitTime();

        driver.findElement(By.id("roomName")).sendKeys("Fürdőszobás Superior szoba");
        waitTime();
        new Select(driver.findElement(By.cssSelector("#roomType"))).selectByVisibleText("két ágyas szoba");
        waitTime();
        driver.findElement(By.id("numberOfBeds")).sendKeys("5");
        waitTime();
        driver.findElement(By.id("roomArea")).sendKeys("42");
        waitTime();
        driver.findElement(By.id("pricePerNight")).sendKeys("23500");
        waitTime();
        driver.findElement(By.id("description")).sendKeys("Déli fekvésű szoba, folyóra néző ablakokkal");
        waitTime();
        driver.findElement(By.id("roomImageUrl")).sendKeys("https://res.cloudinary.com/doaywchwk/image/upload/v1584022262/oxdtudhgev1nvrbiqxyd.jpg");
        waitTime();

        for (int i = 1; i < 10; i += 2) {
            driver.findElements(By.cssSelector("input[name='roomFeatures']")).get(i).isSelected();
            driver.findElements(By.cssSelector("input[name='roomFeatures']")).get(i).click();
        }
        jumpDown();
        waitTime();
        String listPageTitle = driver.findElement(By.cssSelector("body > app-root > div > app-room-form > div > form > button")).getAttribute("innerHTML");
        assertEquals(listPageTitle, "Szoba mentése");
        waitTime();
        driver.findElement(By.cssSelector("body > app-root > div > app-room-form > div > form > button")).click();
        waitTime();

        jumpDown();
        jumpDown();
        String deleteRoomButton = driver.findElement(By.cssSelector("body > app-root > div > app-hotel-detail > div > form > div:nth-child(5) > div > div > div.col-md-8 > div > div > div:nth-child(2) > button.btn.btn-danger.btn-sm")).getAttribute("innerHTML");
        assertEquals(deleteRoomButton, "törlése");
        waitTime();
        driver.findElement(By.cssSelector("body > app-root > div > app-hotel-detail > div > form > div:nth-child(5) > div > div > div.col-md-8 > div > div > div:nth-child(2) > button.btn.btn-danger.btn-sm")).click();
        waitTime();
        waitTime();
        waitTime();
    }

    public void waitTime() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void jumpDown() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,600)");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
