package com.waiter.javawaiter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/waiter")
public class HomePageController {

    @GetMapping
    public String getHomePage() {
        return "java-waiter: приложение для официантов и не только";
    }
}
