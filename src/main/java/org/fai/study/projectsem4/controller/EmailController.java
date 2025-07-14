package org.fai.study.projectsem4.controller;

import jakarta.mail.MessagingException;
import org.fai.study.projectsem4.entity.DTOs.MailDTO;
import org.fai.study.projectsem4.service.interfaces.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class EmailController {
    @Autowired
    MailService mailService;
    @PostMapping("/email")
    public String sendEmail() throws MessagingException {
        MailDTO mailDTO = new MailDTO();
        mailDTO.setSubject("Email Test");
        mailDTO.setTo("caothanhdat21112003@gmail.com");
        mailDTO.setContent("123");
        mailService.sendEmail(mailDTO,"shipping-convert");
        return "Email Sent";

    }

}
