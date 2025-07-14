package org.fai.study.projectsem4.service.Impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.fai.study.projectsem4.entity.DTOs.MailDTO;
import org.fai.study.projectsem4.service.interfaces.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Override
    public void sendEmail(MailDTO dataMailDto, String templateName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        Context context = new Context();
        context.setVariables(dataMailDto.getProps());

        String html = templateEngine.process(templateName, context);

        helper.setTo(dataMailDto.getTo());
        helper.setSubject(dataMailDto.getSubject());
        helper.setText(html, true);

        mailSender.send(message);
    }
}
