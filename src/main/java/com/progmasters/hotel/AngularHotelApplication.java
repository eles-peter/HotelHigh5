package com.progmasters.hotel;

import com.progmasters.hotel.service.AccountService;
import com.progmasters.hotel.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final AccountService accountService;
    private final DataService dataService;
    private static final Logger logger = LoggerFactory.getLogger(AngularHotelApplication.class);
    private static boolean rebuildDataBase = false;

    @Autowired
    public AngularHotelApplication(AccountService accountService, DataService dataService) {
        this.accountService = accountService;
        this.dataService = dataService;
    }

    //----- GENERATE DEFAULT ADMIN, USER, HOTELOWNER ------
    @EventListener
    public void seed(ContextRefreshedEvent event) {
        accountService.checkAdmin();
        accountService.checkUser();
        accountService.checkHotelOwner();
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        if (rebuildDataBase) {
            dataService.restoreDatabaseFromBackupWithJDBC();
            logger.info("DataBase is restored from {}", dataService.getDumpSqlFile());
        }
    }

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equals("rebuildDataBase"))
                rebuildDataBase = true;
        }
        SpringApplication.run(AngularHotelApplication.class, args);
    }

}
