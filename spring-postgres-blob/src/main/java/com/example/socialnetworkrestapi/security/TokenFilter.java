package com.example.socialnetworkrestapi.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final JwtCore jwtCore;
    private final UserDetailsService userDetailsService;

    @Autowired
    public TokenFilter(JwtCore jwtCore, UserDetailsService userDetailsService) {
        this.jwtCore = jwtCore;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String userName = null;
        UserDetails userDetails;
        UsernamePasswordAuthenticationToken auth;
        try{
            String headerAuth = request.getHeader("Authorization");
            if(headerAuth != null && headerAuth.startsWith("Bearer ")){
                jwt = headerAuth.substring(7);
            }
            if(jwt != null){
              try{
                  userName = jwtCore.getNameFromJwt(jwt);
              } catch (ExpiredJwtException e){
                  System.out.println("EXCEPTION  " + e.getMessage());
              }
              if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                  userDetails = userDetailsService.loadUserByUsername(userName);
                  auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                  SecurityContextHolder.getContext().setAuthentication(auth);
              }
            }
        } catch (Exception e){
            System.out.println("EXCEPTION  " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
