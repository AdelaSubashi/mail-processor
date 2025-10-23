package com.example.mailprocessor.service;

import com.example.mailprocessor.entity.EmailReceived;
import com.example.mailprocessor.repository.received.EmailReceivedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailReaderService {

    @Autowired
    private EmailReceivedRepository emailReceivedRepository;

    /**
     * Kthen listën e email-eve të pranuara të ruajtura në databazën received_emails
     */
    public List<EmailReceived> readEmails() {
        return emailReceivedRepository.findAll(Sort.by(Sort.Direction.DESC, "receivedAt"));
    };
}


