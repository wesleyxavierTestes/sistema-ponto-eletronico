package com.sistemapontoeletronico.utils;

import java.time.*;

import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;

import lombok.NonNull;

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

    public static LocalDateTime IniciDesteDia() {
        return LocalDateTime.of(
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth(),
                0, 0,0
        );
    }

    public static LocalDateTime IniciProximoDia() {
        return LocalDateTime.of(
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth() + 1,
                0, 0, 0
        );
    }

    public static LocalDateTime IniciDesteMes() {
        return LocalDateTime.of(
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                0, 0, 0, 0
        );
    }

    public static LocalDateTime IniciProximoMes() {
        return LocalDateTime.of(
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth().plus(1),
                0, 0, 0, 0
        );
    }

    public static LocalTime ContarTotalHoras(RelogioPonto entrada, RelogioPonto saida) {        
        long miliA = GetMilisegundos(ConfigurarExpedienteDia(entrada.getPonto()));
        long miliB = GetMilisegundos(ConfigurarExpedienteDia(saida.getPonto()));

        long totalHoras = miliB - miliA;
        LocalDateTime totalDateTime = GetLocalDateTime(totalHoras);

        return LocalTime.of(totalDateTime.getHour(), totalDateTime.getMinute(), totalDateTime.getSecond());
    }

    public static LocalDateTime InicioDestaSemana() {
        return LocalDateTime.of(
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth() - 7,
                0, 0,0)
                        .with(DayOfWeek.SUNDAY);
    }

    public static LocalDateTime InicioProximaSemana() {
        return LocalDateTime.of(
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth(),
                0, 0, 0)
                        .with(DayOfWeek.SUNDAY);
    }

	public static LocalDateTime ConfigurarExpedienteDia(
        @NonNull LocalDateTime inicioExpediente) {
		return LocalDateTime.of(
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonth(),
                LocalDateTime.now().getDayOfMonth(),
                
                inicioExpediente.getHour(), 
                inicioExpediente.getMinute(),
                inicioExpediente.getSecond());
	}

	public static LocalDateTime ZerarHoras(LocalDateTime inicioIntervalo) {
		return LocalDateTime.of(
            inicioIntervalo.getYear(),
            inicioIntervalo.getMonth(),
            inicioIntervalo.getDayOfMonth(),
                0, 0,0
        );
	}

	public static LocalDateTime AddDays(LocalDateTime inicioIntervalo, int i) {
		return null;
	}
}
