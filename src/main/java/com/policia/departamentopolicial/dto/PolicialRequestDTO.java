package com.policia.departamentopolicial.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicialRequestDTO {

    @Positive(message = "Número do Distintivo deve ser maior que 0")
    private Integer numDistintivo;

    @NotNull(message = "CPF da Pessoa é obrigatório")
    private String pessoaCPF;

    private String cargo;

    @NotNull(message = "ID da Delegacia é obrigatório")
    @Positive(message = "ID da Delegacia deve ser maior que 0")
    private Integer delegaciaId;
}