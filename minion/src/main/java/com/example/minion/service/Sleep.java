package com.example.minion.service;

import lombok.SneakyThrows;

public class Sleep {
    @SneakyThrows
    public static void sleepSeconds(int seconds){
        Thread.sleep(seconds * 1000L);
    }
}
