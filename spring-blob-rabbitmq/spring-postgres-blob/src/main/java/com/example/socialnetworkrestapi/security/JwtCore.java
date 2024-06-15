package com.example.socialnetworkrestapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtCore {

    @Value("${socialnetworkrestapi.app.secret}")
    private String secret;

    @Value("${socialnetworkrestapi.app.lifetime}")
    private int lifetime;

    public String generateToken(Authentication authentication) {
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("id", userDetails.getId());
        claims.put("roles",
                userDetails
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + lifetime))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    public String getTokenFromHttpHeader(String header){
        String jwt = null;
        try{
            if(header != null && header.startsWith("Bearer "))
                jwt = header.substring(7);
            if (jwt != null)
                return jwt;
        } catch (Exception e){
            System.out.println("EXCEPTION  " + e.getMessage());
        }
        return null;
    }

    public Long getIdFromJwt(String token){
        try {
            Claims userData = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            return userData.get("id", Long.class);
        } catch (Exception e){
            System.out.println("EXCEPTION  " + e.getMessage());
        }
        return null;
    }

    public String getNameFromJwt(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
