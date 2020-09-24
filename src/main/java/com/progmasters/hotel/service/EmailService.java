package com.progmasters.hotel.service;

import com.progmasters.hotel.domain.Account;
import com.progmasters.hotel.domain.ConfirmationToken;
import com.progmasters.hotel.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String MESSAGE_FROM;

    @Value("${spring.mail.url}")
    private String mailSenderAddress;

    private final JavaMailSender javaMailSender;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, ConfirmationTokenRepository confirmationTokenRepository) {
        this.javaMailSender = javaMailSender;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    //    @Async
    public void sendMail(String email, String emailSubject, String emailText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MESSAGE_FROM);
        message.setTo(email);
        message.setSubject(emailSubject);
        message.setText(emailText);
        javaMailSender.send(message);
    }

    @Async
    public void sendConfirmationMail(Account account, ConfirmationToken confirmationToken) {
        String emailSubject = "Sikeres Regisztráció!";
        String emailText = "Regisztrációd megerősítéséhez kérlek kattints a linkre: "
                + this.mailSenderAddress + "/login/" + confirmationToken.getConfirmationToken();
        sendMail(account.getEmail(), emailSubject, emailText);
    }

    @Async
    public void sendMailAtBooking(Account account) {
        String emailSubject = "Sikeres Foglalás!";
        String emailText = "Tisztelt " + account.getUsername() + "!\n\n" + "Foglalásod elmentettük, részletei megtalálhatóak a honlapon";
        sendMail(account.getEmail(), emailSubject, emailText);
    }

    @Async
    public void sendMailAtDeleteBooking(Account account) {
        String emailSubject = "Foglalás Törölve!";
        String emailText = "Tisztelt " + account.getUsername() + "!\n\n" + "Foglalásod töröltük az adatbázisból!";
        sendMail(account.getEmail(), emailSubject, emailText);
    }

    @Async
    public void sendMailAtBookingByHotelOwner(String email, String firstName, String lastName) {
        String emailSubject = "Sikeres Foglalás!";
        String emailText = "Tisztelt " + firstName + " " + lastName + "!\n\n" + "Foglalásod elmentettük, részletei megtalálhatóak a honlapon";
        sendMail(email, emailSubject, emailText);
    }

}
