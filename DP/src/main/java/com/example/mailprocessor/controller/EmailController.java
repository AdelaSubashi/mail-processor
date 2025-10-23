package com.example.mailprocessor.controller;

import com.example.mailprocessor.entity.Email;
import com.example.mailprocessor.entity.EmailReceived;
import com.example.mailprocessor.repository.EmailRepository;
import com.example.mailprocessor.service.EmailReaderService;
import com.example.mailprocessor.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailReaderService emailReaderService;

    @Autowired
    private EmailRepository emailRepository;

    // GET: Shfaq formularin + email-et e lexuara
    @GetMapping("/send")
    public String showSendForm(Model model) {
        List<EmailReceived> inbox = emailReaderService.readEmails(); // Lexon email-et nga IMAP
        model.addAttribute("inbox", inbox);                  // I dërgon në view
        return "send-form";
    }

    @GetMapping("/inbox")
    public String showInbox(Model model) {
        List<EmailReceived> inbox = emailReaderService.readEmails();
        model.addAttribute("inbox", inbox);
        return "inbox"; // thymeleaf view me emrin inbox.html
    }

    @GetMapping("/sent")
    public String showSentEmails(Model model) {
        List<Email> sentEmails = emailRepository.findAll();  // merr të gjitha nga DB
        model.addAttribute("allSentMails", sentEmails);
        return "sent";
    }


    // POST: Dërgon email
    @PostMapping("/send")
    public String sendEmail(@RequestParam("to") String to,
                            @RequestParam("subject") String subject,
                            @RequestParam("text") String text,
                            RedirectAttributes redirectAttributes) {   //PRG (Post-Redirect-Get).
        try {
            emailService.sendEmail(to, subject, text);
            redirectAttributes.addFlashAttribute("message", "Email u dërgua me sukses!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Dërgimi i email-it dështoi: " + e.getMessage());
        }
        return "redirect:/send"; // *** REDIRECT te GET /send ***
    }
}
