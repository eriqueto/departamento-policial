package com.policia.departamentopolicial.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaEnvolvidaResponseDTO {
    private Integer casoId;
    private String pessoaCpf;
    private String tipoEnvolvimento;
}
