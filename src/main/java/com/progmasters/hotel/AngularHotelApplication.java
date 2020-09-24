package com.progmasters.hotel;

import com.progmasters.hotel.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class AngularHotelApplication {
    //----- GENERATE DEFAULT ADMIN, USER, HOTELOWNER ------

    private final AccountService accountService;

    @Autowired
    public AngularHotelApplication(AccountService accountService) {
        this.accountService = accountService;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        accountService.checkAdmin();
        accountService.checkUser();
        accountService.checkHotelOwner();
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }


    public static void main(String[] args) {
        SpringApplication.run(AngularHotelApplication.class, args);
    }

}
