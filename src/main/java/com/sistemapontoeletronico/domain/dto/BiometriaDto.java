package com.sistemapontoeletronico.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BiometriaDto {
    
    public String codigo;

    @JsonIgnore
    public FuncionarioDto funcionario;
}