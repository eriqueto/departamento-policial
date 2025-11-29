package com.policia.departamentopolicial.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicialResponseDTO {
    private Integer numDistintivo;
    private String pessoaCpf;
    private String cargo;
    private Integer delegaciaId;
}
