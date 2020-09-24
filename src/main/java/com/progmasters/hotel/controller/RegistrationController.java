package com.progmasters.hotel.controller;

import com.progmasters.hotel.domain.Account;
import com.progmasters.hotel.domain.ConfirmationToken;
import com.progmasters.hotel.dto.RegistrationDetails;
import com.progmasters.hotel.service.AccountService;
import com.progmasters.hotel.validator.RegistrationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private final AccountService accountService;
    private final RegistrationValidator registrationValidator;

    @Value("${page.frontend.url")
    private String frontendUrl;

    @Value("${page.backend.url")
    private String backendUrl;

    @Autowired
    public RegistrationController(AccountService accountService, RegistrationValidator registrationValidator) {
        this.accountService = accountService;
        this.registrationValidator = registrationValidator;
    }

    @InitBinder("registrationDetails")
    protected void initRegistrationBinder(WebDataBinder binder) {
        binder.addValidators(registrationValidator);
    }

    //----------SEND MAIL AND CREATE USER AT REGISTRATION----------

    @PostMapping("/user")
    public ResponseEntity<Void> createNewUser(@RequestHeader @Valid RegistrationDetails registrationDetails) throws Exception {
        logger.info("New mail added");
        System.out.println(registrationDetails);

        accountService.saveUserRegistration(registrationDetails);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/hotelOwner")
    public ResponseEntity<Void> createNewHotelOwner(@RequestBody @Valid RegistrationDetails registrationDetails) throws Exception {
        logger.info("New mail added");
        accountService.saveHotelOwnerRegistration(registrationDetails);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    //TODO  folyamatban
    @PutMapping
    public ResponseEntity<Void> confirmAccount(@RequestBody String token) {
        ConfirmationToken confirmationToken = accountService.findToken(token);
        if (confirmationToken != null) {
            Account account = accountService.findAccountByEmail(confirmationToken.getAccount().getEmail());
            account.setEnabled(true);
            accountService.saveConfirmedAccount(account);
            accountService.deleteToken(confirmationToken);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
