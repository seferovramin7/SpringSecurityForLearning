package com.example.telegrambot2023.editing;

import com.example.telegrambot2023.dto.TatoebaData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Parsing {


    public void convert(String word) throws IOException {
        Document document = Jsoup.connect("https://tatoeba.org/en/sentences/search?from=eng&query=" + word + "&to=tur").get();

        Elements div = document.select("div");
        String text = div.attr("ng-init");
        String s = text.split("vm.init\\(\\[], ")[1];
        String s1 = s.split("\t")[0];
        String finalText = s1.split(", \\[\\{")[0];

        TatoebaData student = new ObjectMapper().readValue(finalText, TatoebaData.class);
        System.out.println(word);
        System.out.println("~~~~~~~~~~~~~~~");
        System.out.println(student.getText());
    }


}
