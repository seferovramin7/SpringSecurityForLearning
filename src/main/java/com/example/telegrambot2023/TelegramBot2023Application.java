package com.example.telegrambot2023;

import com.example.telegrambot2023.scheduler.BotService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelegramBot2023Application {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBot2023Application.class, args);
    }

}
