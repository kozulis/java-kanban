package ru.yandex.praktikum.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

public class GsonConverter {
    public static Gson getGsonConverter() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
//                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}