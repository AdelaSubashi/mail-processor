package com.example.mailprocessor.listener;

import com.example.mailprocessor.entity.EmailReceived;
import com.example.mailprocessor.repository.received.EmailReceivedRepository;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

import org.jsoup.Jsoup;
import jakarta.mail.Part;
import jakarta.mail.Multipart;
import jakarta.mail.BodyPart;

@Service
public class EmailScheduler {

    @Autowired
    private EmailReceivedRepository emailReceivedRepository;

    @Scheduled(fixedDelay = 10000) // kontrollon çdo 10 sekonda
    public void checkNewEmails() {
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            Session session = Session.getInstance(props);

            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", "your_email@gmail.com", "your_app_password");

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();

            for (Message message : messages) {
                if (!emailReceivedRepository.existsBySubject(message.getSubject())) {
                    EmailReceived email = new EmailReceived();
                    email.setSender(message.getFrom()[0].toString());
                    email.setSubject(message.getSubject());
                    email.setContent(extractText(message));
                    email.setReceivedAt(convertDateToLocalDateTime(message.getReceivedDate()));
                    emailReceivedRepository.save(email);
                    System.out.println("📥 Email i RI u ruajt te received_emails!");
                }
            }

            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LocalDateTime convertDateToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    //
    private String extractText(Part part) throws Exception {
        if (part.isMimeType("text/plain")) {
            return part.getContent().toString();
        }
        if (part.isMimeType("text/html")) {
            String html = part.getContent().toString();
            return Jsoup.parse(html).text(); // tekst i pastër për front
        }
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart bp = mp.getBodyPart(i);
                String text = extractText(bp);
                if (text != null && !text.isBlank()) {
                    if (sb.length() > 0) sb.append("\n");
                    sb.append(text);
                }
            }
            return sb.toString();
        }
        return "";
    }

}


