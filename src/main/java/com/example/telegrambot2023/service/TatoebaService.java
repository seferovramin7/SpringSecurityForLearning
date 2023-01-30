package com.example.telegrambot2023.service;

import com.example.telegrambot2023.entity.TatoebaEntity;
import com.example.telegrambot2023.repository.TatoRepo;
import com.example.telegrambot2023.telegram.update.TelegramUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TatoebaService {

    @Autowired
    TatoRepo repository;


    public void saveTextToDb(TelegramUpdateDTO dto){
        TatoebaEntity entity = TatoebaEntity.builder()
                .text(dto.getMessageDTO().getText())
                .build();
       repository.save(entity);

    }

}
