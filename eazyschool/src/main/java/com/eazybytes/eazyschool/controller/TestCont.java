package com.eazybytes.eazyschool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestCont {

    @GetMapping("/yup")
    public String hello(){
        return "hello";
    }
}
