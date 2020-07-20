package com.sistemapontoeletronico.utils;

import java.time.*;

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

    public static LocalDateTime IniciDoDia() {
        return LocalDateTime.of(
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth(),
                0, 0,0
        );
    }

    public static LocalDateTime FinalDoDia() {
        return LocalDateTime.of(
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth(),
                23, 59,59
        );
    }

    public static LocalDateTime InicioDaSemana() {

        return LocalDateTime.of(
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth() - 7,
                0, 0,0)
                        .with(DayOfWeek.SUNDAY);
    }
}
