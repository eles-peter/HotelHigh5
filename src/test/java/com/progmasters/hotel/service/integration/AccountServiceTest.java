package com.progmasters.hotel.service.integration;

import com.progmasters.hotel.dto.RegistrationDetails;
import com.progmasters.hotel.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
@AutoConfigureTestDatabase
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;


    @Test
    public void saveAccountTest() throws Exception {
        RegistrationDetails registrationDetails = new RegistrationDetails();

        registrationDetails.setEmail("x@z.com");
        registrationDetails.setPassword("Test");
        registrationDetails.setAddress("Budapest");
        registrationDetails.setLastname("Boka");
        registrationDetails.setFirstname("Tibor");

        accountService.saveUserRegistration(registrationDetails);
        String email = accountService.findAccountByEmail(registrationDetails.getEmail()).getEmail();
        //assertEquals("x@z.com", email);

    }


}
