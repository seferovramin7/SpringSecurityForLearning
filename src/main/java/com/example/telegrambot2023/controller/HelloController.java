package com.example.telegrambot2023.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public String hello() {
        return "Hello";
    }
}
