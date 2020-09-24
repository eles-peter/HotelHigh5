package com.progmasters.hotel.controller;

import com.progmasters.hotel.dto.AccountDetails;
import com.progmasters.hotel.security.AuthenticatedLoginDetails;
import com.progmasters.hotel.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //----------GET A USER----------

    @GetMapping("/me")
    public ResponseEntity<AuthenticatedLoginDetails> getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();

        if (accountService.accountIsActive(user.getUsername())) {
            AuthenticatedLoginDetails authenticatedLoginDetails = accountService.getAuthenticatedLoginDetails(user);
            session.setAttribute("authenticatedLoginDetails", authenticatedLoginDetails);
            return new ResponseEntity<>(authenticatedLoginDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<AccountDetails> getUserAccount(@PathVariable String email) {
        return new ResponseEntity<>(accountService.getUserAccountByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/sessionCheck")
    public ResponseEntity<AuthenticatedLoginDetails> isSessionValid(HttpServletRequest request) {
        Object authenticatedLoginDetails = request.getSession().getAttribute("authenticatedLoginDetails");
        if (authenticatedLoginDetails instanceof AuthenticatedLoginDetails) {
            return new ResponseEntity<>((AuthenticatedLoginDetails) authenticatedLoginDetails, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //---------------update the user account-------------

    @PutMapping("/{username}")
    public ResponseEntity<AccountDetails> updateUserAccount(@RequestBody AccountDetails accountDetails, @PathVariable String username) {
        AccountDetails updateUserAccount = accountService.updateUserAccount(accountDetails, username);
        logger.info("account saved");
        return new ResponseEntity<>(updateUserAccount, HttpStatus.OK);
    }
}
