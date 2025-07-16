package com.algostyle.authentification.security;


import com.algostyle.authentification.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration  // Classe de configuration Spring

@EnableWebSecurity      // Activer la sécurité web Spring
public class SecurityConfig {
    @Autowired
    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }


    // déclarer un service comme fournisseur de détails utilisateur
    @Bean
    public UserDetailsService userDetailsService(){
        return userService;
    }

    // Configurer le mécanisme d'authentification
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);    // Service pour charger les utilisateurs
        provider.setPasswordEncoder(passwordEncoder()); // Encodeur pour vérifier les mots de passe
        return provider;
    }


    // Définir l'algorithme de hachage des mots de passe (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    // Configuration principale de la sécurité
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)  // désactiver CSRF (à activer en production)

                // Configuration du formulaire de login
                .formLogin(httpForm->{
                    httpForm.loginPage("/req/login").permitAll();   // Page de login personnalisée
                    httpForm.defaultSuccessUrl("/index"); // redirection après login réussi
                })

                // Gestion des autorisations
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/req/**","/css/**","/js/**").permitAll(); // URLs publiques
                    registry.anyRequest().authenticated();  // Toutes autres URLs nécessitent une authentification
                })

                .logout(logout-> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .build();
    }

}
