package com.policia.departamentopolicial.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatorioRequestDTO {

    @Positive(message = "O ID do Relatório deve ser um valor positivo.")
    private Integer id;

    @NotNull(message = "O ID do Caso é obrigatório.")
    @Positive(message = "O ID do Caso deve ser um valor positivo.")
    private Integer casoId;

    @NotNull(message = "O ID do Policial Emissor é obrigatório.")
    @Positive(message = "O ID do Policial Emissor deve ser um valor positivo.")
    private Integer policialEmissorId;

    @NotBlank(message = "O Conteúdo do Relatório é obrigatório.")
    @Size(min = 50, max = 10000, message = "O Conteúdo deve ter entre 50 e 10000 caracteres.")
    private String conteudo;
}