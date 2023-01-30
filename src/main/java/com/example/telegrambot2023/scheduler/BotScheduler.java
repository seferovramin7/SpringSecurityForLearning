package com.example.telegrambot2023.scheduler;

import com.example.telegrambot2023.dto.TelegramResponseType;
import com.example.telegrambot2023.Jsoup.Parsing;
import com.example.telegrambot2023.service.TatoebaService;
import com.example.telegrambot2023.telegram.send.SendMessageResponseDTO;
import com.example.telegrambot2023.telegram.send.text.SendMessageDTO;
import com.example.telegrambot2023.telegram.update.TelegramResponseDTO;
import com.example.telegrambot2023.telegram.update.TelegramUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class BotScheduler {
    @Value("${telegram.api.base-url}")
    String api;

    @Value("${telegram.api.token}")
    String token;
    private Long offset = null;


    @Autowired
    Parsing parsing;

    @Autowired
    TatoebaService tatoebaService;


    @Scheduled(fixedRate = 3000)
    public TelegramResponseType GetUpdates() throws IOException {
        String url = api + "/bot" + token + "/getUpdates";
        if (offset != null)
            url = url + "?offset=" + offset;

        RestTemplate restTemplate = new RestTemplate();
        TelegramResponseDTO forObject = restTemplate.getForObject(url, TelegramResponseDTO.class);
        if (forObject.getResult().size() > 0) {
            if (forObject.getResult().get(0) != null) {
                TelegramUpdateDTO telegramUpdateDTO = forObject.getResult().get(0);
                offset = telegramUpdateDTO.getUpdateId() + 1;
                tatoebaService.saveTextToDb(telegramUpdateDTO);
                String text = telegramUpdateDTO.getMessageDTO().getText();
                TelegramResponseType convert = parsing.convert(text);
                Long id = telegramUpdateDTO.getMessageDTO().getChat().getId();
                sendMessage(convert, id);


            }
        }
        return null;
    }

    public void sendMessage(TelegramResponseType text, Long id) throws IOException {
        String url1 = api + "/bot" + token + "/sendMessage";


        SendMessageDTO dto = SendMessageDTO.builder()
                .chatId(id)
                .text(text.toString())
                .build();


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(url1, dto, SendMessageResponseDTO.class);

    }

}
