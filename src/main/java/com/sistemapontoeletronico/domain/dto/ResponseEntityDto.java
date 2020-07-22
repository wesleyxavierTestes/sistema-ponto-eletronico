package com.sistemapontoeletronico.domain.dto;

import com.sistemapontoeletronico.domain.entities.BaseEntity;

import lombok.Builder;

/**
 * Use para responder Http onde há Entidades
 * @param <T>
 */
@Builder
public class ResponseEntityDto {
    
    public boolean valido = false;
    public String erro;
    public Object objeto;

}