package com.example.telegrambot2023.Jsoup;

import com.example.telegrambot2023.dto.TatoebaData;
import com.example.telegrambot2023.dto.TelegramResponseType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class Parsing {


    public TelegramResponseType convert(String languageCode, String word, String from, String to) throws IOException {

        try {
            Document document = Jsoup.connect("https://tatoeba.org/en/sentences/search?from=" + from + "&query=" + word + "&to=" + to).get();
            Elements div = document.select("div");
            List<String> strings = div.eachAttr("ng-init");
            int limit = strings.size();
            int random = (int) (Math.random() * limit);
            String attr = strings.get(random);
            String s = attr.split("vm.init\\(\\[], ")[1];
            String s1 = s.split("\t")[0];
            String finalText = s1.split(", \\[\\{")[0];

            TatoebaData student = new ObjectMapper().readValue(finalText, TatoebaData.class);
            String original = student.getText();
            String translation;
            if (student.getTranslations().get(0).size() > 0) {
                translation = student.getTranslations().get(0).get(0).getText();
            } else {
                translation = student.getTranslations().get(1).get(0).getText();
            }


            return TelegramResponseType.builder()
                    .fromLanguage(original)
                    .toLanguage(translation)
                    .build();

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



