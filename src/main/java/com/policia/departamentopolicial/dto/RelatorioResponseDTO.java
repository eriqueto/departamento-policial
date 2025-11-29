package com.policia.departamentopolicial.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatorioResponseDTO {
    private Integer id;
    private Integer casoId;
    private Integer policialEmissorId;
    private String conteudo;
}
