package com.algostyle.authentification.service;


import com.algostyle.authentification.model.User;
import com.algostyle.authentification.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException("Utilisateur non trouvé")
        );
    }


    public User updateUser(User updatedUser){
        User currentUser = getCurrentUser();

        // Mettre à jour seulement les champs modifiables
        currentUser.setNom(updatedUser.getNom());
        currentUser.setPrenom(updatedUser.getPrenom());

        return userRepository.save(currentUser);
    }
}
