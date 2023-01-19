package com.example.telegrambot2023.dto;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */


import lombok.Data;

import java.util.ArrayList;

@Data
public class Translation{
    public int id;
    public String text;
    public String lang;
    public int correctness;
    public Object script;
    public ArrayList<Object> transcriptions;
    public ArrayList<Object> audios;
    public boolean isDirect;
    public String lang_name;
    public String dir;
    public String lang_tag;
}

