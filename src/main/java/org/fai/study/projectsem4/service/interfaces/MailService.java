package org.fai.study.projectsem4.service.interfaces;

import jakarta.mail.MessagingException;
import org.fai.study.projectsem4.entity.DTOs.MailDTO;

public interface MailService {
    void sendEmail(MailDTO dataMailDto, String templateName) throws MessagingException;
}
