package com.sistemapontoeletronico.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Identity irá utilizar o auto incremente do próprio database
    private long id;

    private LocalDateTime dataCadastro;
}