package com.example.telegrambot2023.controller;

import com.example.telegrambot2023.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin()
public class TatoebaController {

    @Autowired
    ApiService service;

    @RequestMapping(value = "/translate/example")
    public String getExample(@RequestParam("word") String word, @RequestParam("from") String from
            , @RequestParam("to") String to) throws IOException {
        return service.gtExample(word, from, to);

    }


}
