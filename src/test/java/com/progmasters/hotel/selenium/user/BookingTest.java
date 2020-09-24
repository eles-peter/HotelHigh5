package com.progmasters.hotel.selenium.user;

import com.progmasters.hotel.selenium.hotel.AddNewRoomTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingTest {
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
        loginUser();
        throwToBooking();
    }

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

    public void throwToBooking() {
        waitTime();
        String clickToTitle = driver.findElement(By.cssSelector("body > app-root > div > app-home > div > div > div:nth-child(1) > div.card.text-center.bg-light.mb-3 > div > form > div:nth-child(5) > button.btn.btn-outline-primary.btn-block")).getAttribute("innerHTML");
        assertEquals(clickToTitle, " Megnézem a teljes listát ");
        driver.findElement(By.cssSelector("body > app-root > div > app-home > div > div > div:nth-child(1) > div.card.text-center.bg-light.mb-3 > div > form > div:nth-child(5) > button.btn.btn-outline-primary.btn-block")).click();
        waitTime();

        driver.findElement(By.cssSelector("#numberOfGuests")).sendKeys("1");
        waitTime();
        driver.findElement(By.cssSelector("#bookingDateRange > div > input")).click();
        driver.findElement(By.cssSelector("body > div.flatpickr-calendar.rangeMode.animate.open.arrowTop > div.flatpickr-innerContainer > div > div.flatpickr-days > div > span:nth-child(12)")).click();
        waitTime();
        driver.findElement(By.cssSelector("body > div.flatpickr-calendar.rangeMode.animate.open.arrowTop > div.flatpickr-innerContainer > div > div.flatpickr-days > div > span:nth-child(14)")).click();
        waitTime();
        driver.findElement(By.cssSelector("body > app-root > div > app-hotel-list > div > form > div > div > div.form-row > div:nth-child(3) > div > button.btn.btn-primary.mr-4")).click();
        waitTime();
        driver.findElement(By.cssSelector("body > app-root > div > app-hotel-list > div > div:nth-child(2) > div > div.col-md-8 > div > div.card-title > div > div.col-md-9 > h4")).click();
        waitTime();
        jumpDown();
        driver.findElement(By.cssSelector("#numberOfGuests")).sendKeys("1");
        waitTime();
        driver.findElement(By.cssSelector("#bookingDateRange > div > input")).click();
        waitTime();
        driver.findElement(By.cssSelector("body > div.flatpickr-calendar.rangeMode.animate.open.arrowTop > div.flatpickr-innerContainer > div > div.flatpickr-days > div > span:nth-child(12)")).click();
        waitTime();
        driver.findElement(By.cssSelector("body > div.flatpickr-calendar.rangeMode.animate.open.arrowTop > div.flatpickr-innerContainer > div > div.flatpickr-days > div > span:nth-child(14)")).click();
        waitTime();
        driver.findElements(By.cssSelector("body > app-root > div > app-hotel-detail > div > form > div:nth-child(3) > div > div > div.col-md-8 > div > div > label > input")).get(0).isSelected();
        driver.findElements(By.cssSelector("body > app-root > div > app-hotel-detail > div > form > div:nth-child(3) > div > div > div.col-md-8 > div > div > label > input")).get(0).click();
        waitTime();
        waitTime();
        driver.findElement(By.cssSelector("body > app-root > div > app-hotel-detail > div > form > div.card.bg-light.mb-3 > div > div > div:nth-child(3) > div > button")).click();
        waitTime();
        driver.findElements(By.cssSelector("#mat-dialog-1 > app-booking-form-dialog > div > div > div.modal-body > form > div:nth-child(2) > label > input")).get(0).isSelected();
        driver.findElements(By.cssSelector("#mat-dialog-1 > app-booking-form-dialog > div > div > div.modal-body > form > div:nth-child(2) > label > input")).get(0).click();
        waitTime();
        driver.findElement(By.cssSelector("#mat-dialog-1 > app-booking-form-dialog > div > div > div.modal-footer > button.btn.btn-primary.mr-4")).click();
        waitTime();
        waitTime();
        waitTime();
        waitTime();
        driver.findElement(By.cssSelector("#mat-dialog-1 > app-booking-form-dialog > div > div > div.modal-footer > button")).click();
        waitTime();
        waitTime();
        driver.findElement(By.cssSelector("#user-bookings")).click();
        waitTime();
        waitTime();
        waitTime();
        clickToTitle = driver.findElement(By.cssSelector("#actual > actual-user-bookings > div:nth-child(2) > div > div.col-md-4 > div > div > button")).getAttribute("innerHTML");
        assertEquals(clickToTitle, " Foglalás törlése ");
        driver.findElement(By.cssSelector("#actual > actual-user-bookings > div:nth-child(2) > div > div.col-md-4 > div > div > button")).click();
        waitTime();
        waitTime();
        clickToTitle = driver.findElement(By.cssSelector("#mat-dialog-2 > app-popup > div > div.modal-footer > button.btn.btn-primary")).getAttribute("innerHTML");
        assertEquals(clickToTitle, "Oké");
        driver.findElement(By.cssSelector("#mat-dialog-2 > app-popup > div > div.modal-footer > button.btn.btn-primary")).click();
        waitTime();
        waitTime();
        waitTime();
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
        js.executeScript("window.scrollBy(0,500)");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
