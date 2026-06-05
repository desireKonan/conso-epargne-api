package org.marketplace_lea.common.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;

@Slf4j
@Service
public class MailSenderService {
    @Value("${spring.mail.username}")
    private String mail;

    @Value("${conso.admin.mail}")
    private String adminMail;

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    public MailSenderService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }


    public void sendSms(String senderId, String message, String recipient) {
        try {
            // Créer un objet Map pour stocker les données JSON
            Map<String, String> jsonMap = new HashMap<>();
            jsonMap.put("sender_id", senderId);
            jsonMap.put("message", message);
            jsonMap.put("recipient", recipient);

            // Convertir en JSON avec ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonPayload = objectMapper.writeValueAsString(jsonMap);

            // Créer une connexion HTTP avec l'API Yellika SMS
            URL url = new URL("https://panel.yellikasms.com/api/v3/sms/send");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer 515|V9pxoE9pEgDhkF7s4Y8GgnusRgBxArGwats21Hcp3aae307b");
            connection.getOutputStream().write(jsonPayload.getBytes());

            // Vérifier la réponse
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Message envoyé avec succès!");
            } else {
                System.out.println("Échec de l'envoi du message. Code de réponse: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRegistrationEmail(Map<String, Object> data) {
        Context context = new Context();
        context.setVariable("firstName", data.get("firstName"));
        context.setVariable("password", data.get("password"));

        String process = templateEngine.process("emails/welcome", context);
        Thread thread = new Thread(() -> {
            try {
                jakarta.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

                helper.setSubject("Bienvenue dans notre famille Conso-Epargne!");
                helper.setText(process, true);
                helper.setTo(data.get("email").toString());
                helper.setFrom(mail, "Conso-Epargne");
                javaMailSender.send(mimeMessage);
                log.info("Successfully send email to " + data.get("email").toString());
            } catch (MessagingException | UnsupportedEncodingException e) {
                log.error("Error while sending email", e);
            }
        });
        thread.start();
    }

    public void sendErrorMailToAdmin(Map<String, Object> data) {
        Context context = new Context();
        data.entrySet().forEach(entry -> context.setVariable(entry.getKey(), entry.getValue()));
        String process = templateEngine.process("emails/admin", context);
        String objet = data.get("objet") != null ? (String) data.get("objet") : "Erreur système consoepargne";
        Thread thread = new Thread(() -> {
            try {
                jakarta.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

                helper.setSubject(objet);
                helper.setText(process, true);
                helper.setTo(adminMail);
                helper.setFrom(mail, "Conso-Epargne");
                javaMailSender.send(mimeMessage);
                log.info("Successfully send email to admin: " + adminMail);
            } catch (MessagingException | UnsupportedEncodingException e) {
                log.error("Error while sending email", e);
            }
        });
        thread.start();
    }

    public void sendResetPasswordEmail(Map<String, Object> data) {
        Context context = new Context();
        context.setVariable("password", data.get("password"));

        String process = templateEngine.process("emails/reset-password", context);
        Thread thread = new Thread(() -> {
            try {
                jakarta.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

                helper.setSubject("Changez votre mot de passe!");
                helper.setText(process, true);
                helper.setTo(data.get("email").toString());
                helper.setFrom(mail, "Conso-Epargne");
                javaMailSender.send(mimeMessage);
                log.info("Successfully send email to " + data.get("email").toString());
            } catch (MessagingException | UnsupportedEncodingException e) {
                log.error("Error while sending email", e);
            }
        });
        thread.start();
    }

    public void sendDeleteAccount(Map<String, Object> data) {
        Context context = new Context();
        context.setVariable("password", data.get("password"));

        String process = templateEngine.process("emails/reset-password", context);
        Thread thread = new Thread(() -> {
            try {
                jakarta.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

                helper.setSubject("Supprimer votre compte conso-épargne!");
                helper.setText(process, true);
                helper.setTo(data.get("email").toString());
                helper.setFrom(mail, "Conso-Epargne");
                javaMailSender.send(mimeMessage);
                log.info("Successfully send email to {}", data.get("email").toString());
            } catch (MessagingException | UnsupportedEncodingException e) {
                log.error("Error while sending email", e);
            }
        });
        thread.start();
    }
}
