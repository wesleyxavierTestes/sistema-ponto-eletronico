package com.sistemapontoeletronico.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class DateUtils {
    private DateUtils() {}

    public static long GetMilisegundos(LocalDateTime datalocal) {
        return GetMilisegundos(datalocal, 0);
    }

    public static long GetMilisegundos(LocalDateTime datalocal, long minutos) {
        return ZonedDateTime.of(datalocal, ZoneId.systemDefault())
                .toInstant().toEpochMilli() + minutos;
    }

    public static long GetMilisegundos(long minuto) {
        int milisegundosMinuto = 60000;
        return minuto * milisegundosMinuto;
    }
}
