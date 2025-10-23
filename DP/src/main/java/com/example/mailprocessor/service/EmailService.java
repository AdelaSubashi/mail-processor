package com.example.mailprocessor.service;

import com.example.mailprocessor.entity.Email;
import com.example.mailprocessor.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailRepository emailRepository;

    public void sendEmail(String to, String subject, String text) {
        // 1. Dërgimi i email-it
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("stest3642@gmail.com"); //  emaili test
        mailSender.send(message);

        // 2. Ruajtja në databazë
        Email email = new Email();
        email.setRecipient(to);
        email.setSubject(subject);
        email.setContent(text);
        email.setSentAt(LocalDateTime.now());

        emailRepository.save(email);
    }
}

