package com.policia.departamentopolicial.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class DepoimentoResponseDTO {
    private Integer id;
    private Integer casoId;
    private String pessoaCpf;
    private LocalDateTime dataHoraDepoimento;
    private String conteudoDepoimento;
}
