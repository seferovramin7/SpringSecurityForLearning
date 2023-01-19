package com.example.telegrambot2023.telegram.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReplyKeyboardMarkupDTO implements ReplyKeyboard {

    @JsonProperty("keyboard")
    private KeyboardButtonDTO[][] keyboardButtonArray;

    @JsonProperty("one_time_keyboard")
    private Boolean oneTimeKeyboard;

}
