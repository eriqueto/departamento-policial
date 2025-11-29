package com.policia.departamentopolicial.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OcorrenciaResponseDTO {
    private Integer numBoletim;
    private LocalDateTime dataHoraRegistro;
    private String declaranteCpf;
    private Integer policialRegistroId;
    private String descricao;
}
