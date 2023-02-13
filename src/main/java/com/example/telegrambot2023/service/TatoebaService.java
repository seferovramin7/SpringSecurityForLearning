package com.example.telegrambot2023.service;

import com.example.telegrambot2023.dto.TatoebaData;
import com.example.telegrambot2023.repository.TatoRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TatoebaService {

    @Autowired
    TatoRepo repository;

    public String gtExample(String word, String from, String to) throws IOException {
        Document document = Jsoup.connect("https://tatoeba.org/en/sentences/search?from=" + from + "&query=" + word + "&to=" + to).get();
        Elements div = document.select("div");
        String text = div.attr("ng-init");
        String s = text.split("vm.init\\(\\[], ")[1];
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
        return original.concat("\n" + translation);


    }

}
