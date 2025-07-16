package com.algostyle.authentification.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  // Déclarer cette classe comme contrôleur Spring MVC
public class AuthController {

    @GetMapping("/req/login")  // Gérer les requêtes GET vers /req/login
    public String login(){
        return "login"; // Renvoie la vue login.html/thymeleaf
    }


    @GetMapping("/req/signup")  // Gérer les requêtes GEt vers /req/signup
    public String signup(){
        return "signup";    // renvoie la vue signup.html/thymeleaf
    }


    @GetMapping("index")  // Gérer les requêtes GET vers /index
    public String home(){
        return "index"; // Renvoie la vue index.html/thymeleaf
    }

}
