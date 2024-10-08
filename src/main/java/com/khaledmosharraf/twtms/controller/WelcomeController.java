package com.khaledmosharraf.twtms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("username")
public class WelcomeController {
    Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping({"/","/home"})
    public String ShowLangdingPage(Model model){
        model.addAttribute("pageTitle","Welcome Page");
        model.addAttribute("username", getLoggedUsername());
        return "teacherPanel/home";
    }
    @GetMapping("/test")
    public String test(Model model){
        return "adminPanel/report/template";
    }

    private String getLoggedUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
