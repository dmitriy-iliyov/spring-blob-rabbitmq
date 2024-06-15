package com.example.socialnetworkrestapi.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping("")
    public String homePage(){
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser")
            return "redirect:/user/login";
        else
            return "home_page";
    }
}
