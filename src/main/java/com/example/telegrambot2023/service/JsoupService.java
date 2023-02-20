package com.example.telegrambot2023.service;

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
public class JsoupService {

    public TelegramResponseType generateData(String word, String from, String to) throws IOException {
        Document document = Jsoup.connect("https://tatoeba.org/en/sentences/search?from=" + from + "&query=" + word + "&to=" + to).get();
        Elements div = document.select("div");
        List<String> strings = div.eachAttr("ng-init");
        int limit = strings.size();
        int random = (int) (Math.random() * limit);
        String attr = strings.get(random);
        String s = attr.split("vm.init\\(\\[], ")[1];
        String s1 = s.split("\t")[0];
        String finalText = s1.split(", \\[\\{")[0];
        TatoebaData data = new ObjectMapper().readValue(finalText, TatoebaData.class);
        String original = data.getText();
        String translation;
        if (data.getTranslations().get(0).size() > 0) {
            translation = data.getTranslations().get(0).get(0).getText();
        } else {
            translation = data.getTranslations().get(1).get(0).getText();
        }
        return TelegramResponseType.builder()
                .fromLanguage(original)
                .toLanguage(translation)
                .build();
    }
}
