package com.example.telegrambot2023.dto;

import lombok.Data;

@Data
public class Audio{
    public int id;
    public String author;
    public String attribution_url;
    public Object license;
}