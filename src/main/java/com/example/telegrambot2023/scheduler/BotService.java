package com.example.telegrambot2023.scheduler;

import com.example.telegrambot2023.editing.Parsing;
import com.example.telegrambot2023.telegram.update.TelegramResponseDTO;
import com.example.telegrambot2023.telegram.update.TelegramUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class BotService {
    private Long offset = null;


    @Autowired
    Parsing parsing;


    @Scheduled(fixedRate = 5000)
    public void sayHello() throws IOException {
        String url = "https://api.telegram.org/bot5949526867:AAEokbBVeToXAxSPw2VNgvMntLAaK4lQxMY/getUpdates";
        if(offset != null)
            url = url +"?offset=" + offset;

        RestTemplate restTemplate = new RestTemplate();
        TelegramResponseDTO forObject = restTemplate.getForObject(url, TelegramResponseDTO.class);
        if(forObject.getResult().size() > 0){
            if (forObject.getResult().get(0) != null) {
                TelegramUpdateDTO telegramUpdateDTO = forObject.getResult().get(0);
                offset = telegramUpdateDTO.getUpdateId() + 1;
                String text = telegramUpdateDTO.getMessageDTO().getText();
                parsing.convert(text);
            }else
                return;
        }else
            return;
    }

}
