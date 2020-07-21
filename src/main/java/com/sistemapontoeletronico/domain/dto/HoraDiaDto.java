package com.sistemapontoeletronico.domain.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoraDiaDto {

    @NonNull
    public String nome;

    public LocalTime totalHoras;
    
    @NonNull
    public List<LocalDateTime> pontos;
    
}