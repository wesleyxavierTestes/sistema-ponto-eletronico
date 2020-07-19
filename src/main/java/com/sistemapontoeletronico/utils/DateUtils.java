package com.sistemapontoeletronico.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class DateUtils {
    private DateUtils() {}

    public static long GetMilisegundos(LocalDateTime datalocal) {
        return GetMilisegundos(datalocal, 0);
    }

    public static long GetMilisegundosPorSegundos(LocalDateTime datalocal, long segundos) {
        return GetMilisegundos(datalocal, GetMilisegundosPorSegundos(segundos));
    }
    public static long GetMilisegundos(LocalDateTime datalocal, long mili) {
        return ZonedDateTime.of(datalocal, ZoneId.systemDefault())
                .toInstant().toEpochMilli() + mili;
    }

    public static long GetMilisegundosPorSegundos(long segundos) {
        int totalMilisegundos = 1000;
        return segundos * totalMilisegundos;
    }

    public static long GetMilisegundosPorMinutos(long minutos) {
        return GetMilisegundosPorSegundos(minutos * 60);
    }

    public static LocalDateTime GetLocalDateTime(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return  date;
    }
}
