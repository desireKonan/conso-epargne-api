package org.marketplace_lea.common.common.service.notification;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class MailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String adminMail;

    @Value("${conso.admin.mail}")
    private String mail;

    public MailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }


    /// Methodes publiques.
    @Async("notificationTaskExecutor")
    public void sendRegistrationEmail(Map<String, Object> data) {
        Context context = new Context();
        context.setVariable("firstName", data.get("firstName"));
        context.setVariable("password", data.get("password"));
        String to = data.get("email").toString();
        String emailContent = templateEngine.process("emails/welcome", context);

        sendEmail(adminMail, to, "Bienvenue dans notre famille Conso-Epargne!", emailContent);
        log.info("[MailService.sendRegistrationEmail] send registration Email to {}", to);
    }

    @Async("notificationTaskExecutor")
    public void sendRegistrationEmail(RegistrationEmailData data) {
        Context context = new Context();
        context.setVariable("firstName", data.fullname());
        context.setVariable("password", data.password());
        String to = data.email();
        String emailContent = templateEngine.process("emails/welcome", context);

        sendEmail(adminMail, to, "Bienvenue dans notre famille Conso-Epargne!", emailContent);
        log.info("[MailService.sendRegistrationEmail] send registration Email to {}", to);
    }

    @Async("notificationTaskExecutor")
    public void sendErrorMailToAdmin(Map<String, Object> data) {
        Context context = new Context();
        data.forEach(context::setVariable);
        String object = Optional.ofNullable(String.valueOf(data.get("objet")))
                .orElse("Erreur système Conso-Epargne");

        String emailContent = templateEngine.process("emails/admin", context);
        sendEmail(adminMail, mail, object, emailContent);
        log.info("[MailService.sendRegistrationEmail] send email to {}", adminMail);
    }

    @Async("notificationTaskExecutor")
    public void sendResetPasswordEmail(Map<String, Object> data) {
        Context context = new Context();
        context.setVariable("password", data.get("password"));
        String to = data.get("email").toString();

        String emailContent = templateEngine.process("emails/reset-password", context);

        sendEmail(adminMail, to, "Changez votre mot de passe!", emailContent);
        log.info("[MailService.sendRegistrationEmail] send reset password email to {}", to);
    }

    @Async("notificationTaskExecutor")
    public void sendDeleteAccount(Map<String, Object> data) {
        Context context = new Context();
        context.setVariable("password", data.get("password"));
        String to = data.get("email").toString();

        String emailContent = templateEngine.process("emails/reset-password", context);
        sendEmail(adminMail, to, "Supprimer votre compte conso-épargne!", emailContent);
        log.info("[MailService.sendDeleteAccount] send deleted account email to {}", to);
    }



    /// Méthodes privées (partagées).
    private void sendEmail(String from, String to, String subject, String content) {
        try {
            jakarta.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom(from, "Conso-Epargne");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            log.error("Error while sending email to {}: {}", to, ex.getMessage(), ex);
        }
    }
}
