package com.eazybytes.eazyschool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping(value = {"","/","/home"})
    public String displayHomePage() {
        return "home.html";
    }

    /*@RequestMapping("/contact")
    public String contact() {
        return "contact.html";
    }

    @RequestMapping("/courses")
    public String courses() {
        return "courses.html";
    }*/

}
