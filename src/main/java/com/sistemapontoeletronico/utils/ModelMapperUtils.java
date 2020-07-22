package com.sistemapontoeletronico.utils;

import java.util.Objects;

import com.sistemapontoeletronico.domain.dto.BiometriaDto;
import com.sistemapontoeletronico.domain.dto.FuncionarioDto;
import com.sistemapontoeletronico.domain.dto.PreDefinicaoPontoDto;
import com.sistemapontoeletronico.domain.dto.RelogioPontoDto;
import com.sistemapontoeletronico.domain.entities.biometria.Biometria;
import com.sistemapontoeletronico.domain.entities.funcionario.Funcionario;
import com.sistemapontoeletronico.domain.entities.preDefinicaoPonto.PreDefinicaoPonto;
import com.sistemapontoeletronico.domain.entities.relogioPonto.RelogioPonto;

import org.modelmapper.ModelMapper;

/**
 * Mapeamento de classes DTO
 * Singleton
 */
public class ModelMapperUtils {

    private static ModelMapper modelMapperInstance;


    private ModelMapperUtils() {
        this.Mapping();
    }

    public static ModelMapper getInstance() {

        if (!Objects.nonNull(ModelMapperUtils.modelMapperInstance)) {
            ModelMapperUtils.modelMapperInstance = new ModelMapper();
        }

        return ModelMapperUtils.modelMapperInstance;
    }

    private void Mapping() {
        ModelMapperUtils.modelMapperInstance.createTypeMap(PreDefinicaoPontoDto.class, PreDefinicaoPonto.class);

        ModelMapperUtils.modelMapperInstance.createTypeMap(BiometriaDto.class, Biometria.class);
        ModelMapperUtils.modelMapperInstance.createTypeMap(FuncionarioDto.class, Funcionario.class);
        ModelMapperUtils.modelMapperInstance.createTypeMap(RelogioPontoDto.class, RelogioPonto.class);
    }

}