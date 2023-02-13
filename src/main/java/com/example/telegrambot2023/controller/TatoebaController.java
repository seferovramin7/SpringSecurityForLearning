package com.example.telegrambot2023.controller;

import com.example.telegrambot2023.service.TatoebaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class TatoebaController {

    @Autowired
    TatoebaService service;

    @GetMapping(value = "/translate/example")
    public String getExample(@RequestParam("word") String word, @RequestParam("from") String from
            , @RequestParam("to") String to) throws IOException {
        return service.gtExample(word, from, to);

    }


}
