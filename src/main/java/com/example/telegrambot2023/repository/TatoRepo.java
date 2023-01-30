package com.example.telegrambot2023.repository;

import com.example.telegrambot2023.entity.TatoebaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TatoRepo extends CrudRepository <TatoebaEntity,Long>{
}
