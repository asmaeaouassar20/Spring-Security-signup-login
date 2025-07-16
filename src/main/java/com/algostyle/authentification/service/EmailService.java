package com.algostyle.authentification.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.mail.internet.MimeMessage;


@Service    // Service Spring gérer l'envoi d'eamils
public class EmailService {

    @Autowired  // Injection du service d'envoi d'eamils de Spring
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")   // Adresse email de l'expéditeur depuis les propriétés
    private String from;


    // Envoie un email de vérification
    public void sendVerificationEmail(String email, String verificationToken){
        String subject = "Email Verification";
        String path = "/req/signup/verify"; // Endpoint de vérification
        String message = "Click the button below to verify your email address : ";
        sendEmail(email,verificationToken,subject,path,message);
    }


    // Envoyer un email et réinitialisation de mot de passe
    public void sendForgotPasswordEmail(String email, String resetToken){
        String subject = "Password reset Request";
        String path = "/req/reset-password";  // endpoint de reset
        String message = "Click the button below to reset your password";
        sendEmail(email, resetToken, subject, path, message);
    }

    // Méthode privée pour l'envoi générique d'email
    private void sendEmail(String email, String token, String subject, String path, String message){
        try{
            // Construction de l'URL avec le token
            String actionUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(path)
                    .queryParam("token", token)
                    .toString();

            // Template HTML de l'email
            String content = """
                    <div>
                        <h2>%s</h2>
                        <p>%s</p>
                        <a href="%s">Proceed</a>
                        <p>Or copy and paste this link : </p>
                        <p>%s</p>
                        <p>Automated message</p>
                    </div>
                    
                    """.formatted(subject,message,actionUrl,actionUrl);

            // Création et configuration de l'email
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);    // Destinataire
            helper.setSubject(subject);     // Sujet
            helper.setFrom(from);   // Expéditeur
            helper.setText(content, true);
            mailSender.send(mimeMessage); // Envoi

        }catch (Exception e){
            System.out.println("Failed to send email : "+e.getMessage());
        }
    }

}
