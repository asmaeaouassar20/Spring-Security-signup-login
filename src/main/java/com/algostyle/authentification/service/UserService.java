package com.algostyle.authentification.service;


import com.algostyle.authentification.model.User;
import com.algostyle.authentification.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service    // indique que c'est un service Spring
@AllArgsConstructor // Générer un constructeur avec tous les champs (Lombok)
public class UserService implements UserDetailsService {

    // Service d'authentification personnalisé

    @Autowired  // Injection du repository pour accéder aux utilisateurs
    private UserRepository userRepository;

    // Implémentation de l'interface UserDetailsService


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Charge un utilisateur par son username

        Optional<User> userOpt =  userRepository.findByUsername(username);     // recherche dans la base de données

        if(userOpt.isPresent()){   // Si l'utilisateur trouvé
            var user=userOpt.get();
            return org.springframework.security.core.userdetails.User.builder() // Construire un UserDetails Spring Security
                    .username(user.getUsername())   // Nom d'utilisateur
                    .password(user.getPassword())   // Mot de passe (déjà encodé)
                    .build();
        }else{
            throw  new UsernameNotFoundException(username); // erreur si non trouvé
        }
    }
}
