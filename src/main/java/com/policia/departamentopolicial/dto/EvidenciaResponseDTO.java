package com.policia.departamentopolicial.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EvidenciaResponseDTO {
    private Integer id;
    private Integer casoId;
    private String descricao;
    private String localizacao;
    private LocalDate dataColeta;
}
