package com.progmasters.hotel.validator;

import com.progmasters.hotel.domain.Account;
import com.progmasters.hotel.dto.RegistrationDetails;
import com.progmasters.hotel.repository.AccountRepository;
import com.progmasters.hotel.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegistrationValidator implements Validator {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public boolean supports(Class<?> clazz) {
        return RegistrationDetails.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegistrationDetails registrationDetails = (RegistrationDetails) target;

        Account registerByEmail = accountRepository.findByEmail(registrationDetails.getEmail());
        if (registerByEmail != null) {
            errors.rejectValue("email", "registration.email.already.taken");
        }

        String email = registrationDetails.getEmail();
        String regex = "[a-zA-Z0-9\\_\\-\\.]+@[a-zA-Z]+(\\.[a-zA-Z]+){1,4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            errors.rejectValue("email", "registration.email.invalid");
        }

        if (registrationDetails.getPassword().trim().length() < 1) {
            errors.rejectValue("password", "registration.password.notGiven");
        }

        if (registrationDetails.getFirstname().trim().length() < 1) {
            errors.rejectValue("firstname", "registration.firstName.notGiven");
        }

        if (registrationDetails.getLastname().trim().length() < 1) {
            errors.rejectValue("lastname", "registration.lastName.notGiven");
        }

        if (registrationDetails.getAddress().trim().length() < 1) {
            errors.rejectValue("address", "registration.address.notGiven");
        }
    }
}
