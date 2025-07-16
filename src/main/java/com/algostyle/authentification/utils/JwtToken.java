package com.algostyle.authentification.utils;


import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component  // Classe gérée par Spring comme composant
public class JwtToken {

    // Configuration des clés et de validité
    private final static SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);    // Clé secrète pour  signer les tokens
    private final static long EXPIRATION_TIME = 86400000;  // 24h en millisecondes

    // Générer un JWT token
    public static String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)  // Email comme sujet
                .setIssuedAt(new Date())    // Date de création
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // Date d'expiration
                .signWith(SECRET_KEY,SignatureAlgorithm.HS256)  // Signature
                .compact(); // Générer le token
    }


    // Vérifier la validité du token
    public boolean validateToken(String token){
        return !isTokenExpired(token);  // Vérifier seulement l'expiration
    }


    // Extraire l'email du token
    public String extractEmail(String token){
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Clé de vérification
                .build();
        return jwtParser.parseClaimsJws(token)
                .getBody()
                .getSubject();  // retourner l'email
    }


    // vérifier si le token a expiré
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date()); // Comparer avec la date actuelle
    }




    // Extraire la date s'expiration du token
    private Date extractExpiration(String token){
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build();
        return jwtParser.parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
