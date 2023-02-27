package com.example.telegrambot2023.Jsoup;

import com.example.telegrambot2023.dto.TelegramResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class Parsing {

    private final JsoupService jsoupService;



    public TelegramResponseType convert(String languageCode, String word, String from, String to) throws IOException {

        try {
             return jsoupService.generateData(word, from, to);

        } catch (Exception exception) {
            String response = "";
            switch (languageCode) {
                case "az" -> {
                    response = "Heç bir nəticə tapılmadı";
                }
                case "ru" -> {
                    response = "Pезультатов не найдено";
                }
                case "tr" -> {
                    response = "Sonuç bulunamadı";
                }
                default -> {
                    response = "Could not find any result";
                }
            }
            return TelegramResponseType.builder()
                    .fromLanguage(response)
                    .toLanguage("")
                    .build();
        }
    }
}



