package com.example.telegrambot2023.controller;

import com.example.telegrambot2023.service.TatoebaService;
import com.example.telegrambot2023.telegram.update.TelegramUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TatoebaController {

    @Autowired
    TatoebaService service;


    public void savingInRepository(@RequestBody TelegramUpdateDTO dto){
        service.saveTextToDb(dto);
    }
}
