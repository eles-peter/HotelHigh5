package com.progmasters.hotel.service;

import com.progmasters.hotel.domain.Account;
import com.progmasters.hotel.domain.ConfirmationToken;
import com.progmasters.hotel.domain.Role;
import com.progmasters.hotel.dto.AccountDetails;
import com.progmasters.hotel.dto.RegistrationDetails;
import com.progmasters.hotel.repository.AccountRepository;
import com.progmasters.hotel.repository.ConfirmationTokenRepository;
import com.progmasters.hotel.security.AuthenticatedLoginDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AccountService {

    private static final int TOKEN_EXPIRATION_CHECK_INTERVAL_MIL = (24 * 60 * 60 * 1000);
    private static final int TOKEN_EXPIRATION_IN_DAY = 1;

    private final BCryptPasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;

    @Autowired
    public AccountService(AccountRepository accountRepository, ConfirmationTokenRepository confirmationTokenRepository, EmailService emailService) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.accountRepository = accountRepository;
    }

    //----------REGISTRATION  -> SAVE A USER----------

    public void saveUserRegistration(RegistrationDetails registrationDetails) throws Exception {
        registrationDetails.setPassword(passwordEncoder.encode(registrationDetails.getPassword()));
        Account account = new Account(registrationDetails);
        account.setRole(Role.ROLE_USER);
        accountRepository.save(account);
        ConfirmationToken confirmationToken = new ConfirmationToken(account);
        confirmationTokenRepository.save(confirmationToken);
        emailService.sendConfirmationMail(account, confirmationToken);
    }

    public void saveHotelOwnerRegistration(RegistrationDetails registrationDetails) throws Exception {
        Account otherAccount = accountRepository.findByEmail(registrationDetails.getEmail());
        if (otherAccount == null) {
            registrationDetails.setPassword(passwordEncoder.encode(registrationDetails.getPassword()));
            Account account = new Account(registrationDetails);
            account.setRole(Role.ROLE_HOTELOWNER);
            accountRepository.save(account);
            ConfirmationToken confirmationToken = new ConfirmationToken(account);
            confirmationTokenRepository.save(confirmationToken);

            emailService.sendConfirmationMail(account, confirmationToken);

        } else {
            throw new Exception("Mail already taken!");
        }
    }

    public ConfirmationToken findToken(String confirmationToken) {
        return confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    }

    public void deleteToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.delete(confirmationToken);
    }

    //----------CHECK THE MAIL----------



    //----------CREATE DEFAULT ADMIN----------

    public void checkAdmin() {
        String adminMail = "hotel.team.five.a@gmail.com";
        Account adminAccount = accountRepository.findByEmail(adminMail);
        if (adminAccount == null) {
            Account newAdmin = new Account();
            newAdmin.setEmail(adminMail);
            newAdmin.setPassword(passwordEncoder.encode("Admin"));
            newAdmin.setRole(Role.ROLE_ADMIN);
            newAdmin.setEnabled(true);
            newAdmin.setUsername(newAdmin.getEmail());
            newAdmin.setAddress("Budapest, Baross utca");
            newAdmin.setFirstname("Sylvester");
            newAdmin.setLastname("Stallone");
            newAdmin.setRegistrationDate(LocalDateTime.now());
            accountRepository.save(newAdmin);
        } else if (adminAccount.getRole() != Role.ROLE_ADMIN) {
            adminAccount.setRole(Role.ROLE_ADMIN);
        }
    }

    //----------CREATE DEFAULT USER----------

    public void checkUser() {
        String userMail = "hotel.team.five.u@gmail.com";
        Account userAccount = accountRepository.findByEmail(userMail);
        if (userAccount == null) {
            Account newUser = new Account();
            newUser.setEmail(userMail);
            newUser.setPassword(passwordEncoder.encode("User"));
            newUser.setRole(Role.ROLE_USER);
            newUser.setEnabled(true);
            newUser.setUsername(newUser.getEmail());
            newUser.setAddress("Cemetery street");
            newUser.setFirstname("Bruce");
            newUser.setLastname("Lee");
            newUser.setRegistrationDate(LocalDateTime.now());
            accountRepository.save(newUser);
        } else if (userAccount.getRole() != Role.ROLE_USER) {
            userAccount.setRole(Role.ROLE_USER);
        }
    }

    //----------CREATE DEFAULT HOTELOWNER----------

    public void checkHotelOwner() {
        String hotelOwnerEmail = "hotel.team.five.h@gmail.com";
        Account hotelOwnerAccount = accountRepository.findByEmail(hotelOwnerEmail);
        if (hotelOwnerAccount == null) {
            Account newHotelOwner = new Account();
            newHotelOwner.setEmail(hotelOwnerEmail);
            newHotelOwner.setPassword(passwordEncoder.encode("Hotelowner"));
            newHotelOwner.setRole(Role.ROLE_HOTELOWNER);
            newHotelOwner.setEnabled(true);
            newHotelOwner.setUsername(newHotelOwner.getEmail());
            newHotelOwner.setAddress("God street");
            newHotelOwner.setFirstname("Chuck");
            newHotelOwner.setLastname("Norris");
            newHotelOwner.setRegistrationDate(LocalDateTime.now());
            newHotelOwner.setHotelId(1L);
            accountRepository.save(newHotelOwner);
        } else if (hotelOwnerAccount.getRole() != Role.ROLE_HOTELOWNER) {
            hotelOwnerAccount.setRole(Role.ROLE_HOTELOWNER);
        }
    }

    //----------DELETE UNUSED CONFIRMATION TOKEN----------

    @Scheduled(fixedDelay = TOKEN_EXPIRATION_CHECK_INTERVAL_MIL)
    public void deleteUnusedConfirmationToken() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(TOKEN_EXPIRATION_IN_DAY);
        Iterable<ConfirmationToken> confirmationTokens = this.confirmationTokenRepository.findAll();
        for (ConfirmationToken confirmationToken : confirmationTokens) {
            if (confirmationToken.getCreatedDate().isBefore(yesterday)) {
                this.accountRepository.delete(confirmationToken.getAccount());
                this.confirmationTokenRepository.delete(confirmationToken);
            }
        }
    }

    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public void saveConfirmedAccount(Account account) {
        this.accountRepository.save(account);
    }

    public Account findByUsername(String username) {
        return this.accountRepository.findByUsername(username);
    }

    public boolean accountIsActive(String email) {
        Account account = accountRepository.findByEmail(email);
        return account.isEnabled();
    }

    public AccountDetails getUserAccountByEmail(String email) {
        return new AccountDetails(findAccountByEmail(email));
    }

    public AccountDetails updateUserAccount(AccountDetails accountDetails, String username) {
        Account account = accountRepository.findByUsername(username);
        updateUserAccountWithDetails(accountDetails, account);
        return new AccountDetails(account);
    }

    public void updateUserAccountWithDetails(AccountDetails accountDetails, Account account) {
        account.setFirstname(accountDetails.getFirstname());
        account.setLastname(accountDetails.getLastname());
        account.setAddress(accountDetails.getAddress());
    }

    public AuthenticatedLoginDetails getAuthenticatedLoginDetails(UserDetails user) {
        String username = user.getUsername();
        Account userAccount = findByUsername(username);
        return new AuthenticatedLoginDetails(userAccount);
    }
}
