package com.example.telegrambot2023.scheduler;

import com.example.telegrambot2023.Jsoup.Parsing;
import com.example.telegrambot2023.dto.TelegramResponseType;
import com.example.telegrambot2023.enums.ChatStage;
import com.example.telegrambot2023.entity.TelegramEntity;
import com.example.telegrambot2023.repository.TatoRepo;
import com.example.telegrambot2023.service.ApiService;
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
    ApiService tatoebaService;

    @Autowired
    TatoRepo repository;


    @Scheduled(fixedRate = 3000)
    public void GetUpdates() throws IOException {
        String url = api + "/bot" + token + "/getUpdates";
        if (offset != null)
            url = url + "?offset=" + offset;

        RestTemplate restTemplate = new RestTemplate();
        TelegramResponseDTO forObject = restTemplate.getForObject(url, TelegramResponseDTO.class);
        if (forObject.getResult().size() > 0) {
            if (forObject.getResult().get(0) != null) {
                TelegramUpdateDTO telegramUpdateDTO = forObject.getResult().get(0);
                offset = telegramUpdateDTO.getUpdateId() + 1;

                String text = telegramUpdateDTO.getMessageDTO().getText();
                Long id = telegramUpdateDTO.getMessageDTO().getChat().getId();

                TelegramEntity chatId = repository.findByChatId(id);

                Boolean extracted = extracted(text, id, chatId);

                if (chatId != null && extracted) {
                    String chatStage = chatId.getChatStage();
                    if (chatStage.equals(ChatStage.FROM_LANG.name())) {
                        chatId.setFromLang(text);
                        String toLang = chatId.getToLang();
                        if (toLang == null ){
                            chatId.setChatStage(ChatStage.TO_LANG.name());
                            repository.save(chatId);
                            sendMessage("Zehmet olmasa hansı dilə tərcümə etmək istiyinizi seçin", id);
                        }else {
                            chatId.setChatStage(ChatStage.COMPLETED.name());
                            repository.save(chatId);
                            sendMessage("Dil seçiminiz bazaya uğurla yazıldı", id);
                        }


                    } else if (chatStage.equals(ChatStage.TO_LANG.name())) {
                        chatId.setToLang(text);
                        chatId.setChatStage(ChatStage.COMPLETED.name());
                        repository.save(chatId);
                        sendMessage("Seçimləriniz uğurla bazaya yazıldı", id);
                    } else if (chatStage.equals(ChatStage.COMPLETED.name())) {
                        String languageCode = telegramUpdateDTO.getMessageDTO().getFrom().getLanguageCode();
                        TelegramResponseType convert = parsing.convert(languageCode,text, chatId.getFromLang(), chatId.getToLang());
                        sendMessage(convert.toString(), id);
                    }
                }

            }
        }

    }

    private Boolean extracted(String text, Long id, TelegramEntity chatId) throws IOException {
        switch (text) {
            case "/start" -> {
                TelegramEntity entity = TelegramEntity.builder().chatId(id)
                        .chatStage(ChatStage.FROM_LANG.name()).build();
                if (chatId == null) {
                    repository.save(entity);
                } else {
                    repository.save(chatId);
                }
                sendMessage("Salam, tatoeba bota xoş gəldiniz", id);
                sendMessage("Zehmet olmasa hansı dildən tərcümə etmək istədiyinizi seçin", id);
                return false;
            }
            case "/fromlang" -> {
                TelegramEntity entity = TelegramEntity.builder().chatId(id)
                        .chatStage(ChatStage.FROM_LANG.name()).build();
                if (chatId == null) {
                    repository.save(entity);
                } else {
                    chatId.setChatStage(ChatStage.FROM_LANG.name());
                    repository.save(chatId);
                }
                sendMessage("Zehmet olmasa hansı dildən tərcümə etmək istədiyinizi seçin", id);
                return false;

            }
            case "/tolang" -> {
                TelegramEntity entity = TelegramEntity.builder().chatId(id)
                        .chatStage(ChatStage.TO_LANG.name()).build();
                if (chatId == null) {
                    repository.save(entity);
                } else {
                    chatId.setChatStage(ChatStage.TO_LANG.name());
                    repository.save(chatId);
                }
                sendMessage("Zehmet olmasa hansı dilə tərcümə etmək istiyinizi seçin", id);
                return false;


            }
        }
        return true;

    }

    public void sendMessage(String text, Long id) throws IOException {
        String url1 = api + "/bot" + token + "/sendMessage";


        SendMessageDTO dto = SendMessageDTO.builder()
                .chatId(id)
                .text(text)
                .build();


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(url1, dto, SendMessageResponseDTO.class);

    }

}
