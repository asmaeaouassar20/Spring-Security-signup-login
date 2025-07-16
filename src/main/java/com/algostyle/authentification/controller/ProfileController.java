package com.algostyle.authentification.controller;


import com.algostyle.authentification.model.User;
import com.algostyle.authentification.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/profile") // Toutes les routes commencent par /profile
public class ProfileController {
    @Autowired  // Injection du service utilisateur
    private ProfileService profileService;


    // Affichage de la page du profile (GET /profile)
    @GetMapping
    public String showProfile(Model model, Principal principal){
        User currentUser = profileService.getCurrentUser(); // Récupérer l'utilisateur
        model.addAttribute("user",currentUser); // Envoie l'utilisateur à la vue
        return "profile";   // Cela affiche profile.html
    }

    // Afficher le formulaire de modification (GET /profile/update)
    @GetMapping("/update")
    public String showUpdateForm(Model model){
        User currentUser = profileService.getCurrentUser();
        model.addAttribute("user", currentUser);
        return "update-profile";    // Cela affiche update-profile.html
    }



    // Traite la soumission du formulaire (POST  /profile/update)
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User user,  // Reçoit les données du formulaire
                                RedirectAttributes redirectAttributes) {    // Pour les messages flash

        try{
            profileService.updateUser(user);  // Mettre à jour l'utilisateur
            redirectAttributes.addFlashAttribute("success","Profil mis à jour !");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","Erreur"+ e.getMessage());
        }
        return "redirect:/profile"; // rediriger vers la page profil
    }


}
