package com.example.telegrambot2023.dto;

import lombok.Builder;

@Builder
public class JsoupResponseType {
    String text;

    String translation;

    @Override
    public String toString() {
        return text + '\n'
                + translation;
    }
}
