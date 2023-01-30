package com.example.telegrambot2023.dto;

import lombok.Builder;

@Builder
public class TelegramResponseType {
    String fromLanguage;
    String toLanguage;

    @Override
    public String toString() {
        return  fromLanguage + '\n'+
               toLanguage ;
    }
}
