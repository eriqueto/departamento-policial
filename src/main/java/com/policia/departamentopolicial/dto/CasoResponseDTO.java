package com.policia.departamentopolicial.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CasoResponseDTO {
    private Integer id;
    private Integer ocorrenciaNumBoletim;
    private Integer policialResponsavelId;
    private String statusCaso;
    private LocalDate dataAbertura;
    private LocalDate dataFechamento;
}
