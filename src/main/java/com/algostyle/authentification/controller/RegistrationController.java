package com.algostyle.authentification.controller;


import com.algostyle.authentification.model.User;
import com.algostyle.authentification.repository.UserRepository;
import com.algostyle.authentification.service.EmailService;
import com.algostyle.authentification.utils.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController // Contrôleur Spring qui renvoie des réponses JSON/XML
public class RegistrationController {

    @Autowired  // Injection des dépendances Spring
    private UserRepository userRepository;  // Pour l'accès à la base de données

    @Autowired
    private PasswordEncoder passwordEncoder;  // Pour chiffrer les mots de passe

    @Autowired
    private EmailService emailService;  // Service d'envoi d'emails


    // Endpoint d'inscription
    @PostMapping(value="/req/signup", consumes = "application/json")
    public ResponseEntity<String> createUser(@RequestBody User user){

        // Vérifier si l'utilisateur existe déjà
        User existingUser = userRepository.findByEmail(user.getEmail());

        if(existingUser!=null){
            if(existingUser.isVerified()){
                return new ResponseEntity<>("Cet Utilisateur existe déjà et vérifié", HttpStatus.BAD_REQUEST);
            }else{
                // Régénérer  un token et renvoyer un email
                String verificationToken = JwtToken.generateToken(existingUser.getEmail());
                existingUser.setVerificationToken(verificationToken);
                userRepository.save(existingUser);
                emailService.sendVerificationEmail(existingUser.getEmail(), verificationToken);
                return  new ResponseEntity<>("Verification Email resent. Check your inbox", HttpStatus.OK);
            }
        }


        // Nouvel Utilisateur
        user.setPassword(passwordEncoder.encode(user.getPassword()));   // chiffrer le mot de passe
        String verificationToken = JwtToken.generateToken(user.getEmail());
        user.setVerificationToken(verificationToken);
        userRepository.save(user); // Sauvegarde en base
        emailService.sendVerificationEmail(user.getEmail(), verificationToken); // Envoi email

        return  new ResponseEntity<>("Inscription réussie! Veuillez vérifier votre adresse e-mail.",HttpStatus.OK);
    }

}
