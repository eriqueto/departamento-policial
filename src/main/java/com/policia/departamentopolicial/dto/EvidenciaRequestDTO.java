package com.policia.departamentopolicial.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EvidenciaRequestDTO {
    @Positive(message = "O ID da Evidência deve ser um valor positivo.")
    private Integer id;

    @NotNull(message = "O ID do Caso é obrigatório.")
    @Positive(message = "O ID do Caso deve ser um valor positivo.")
    private Integer casoId;

    @NotBlank(message = "A Descrição da evidência é obrigatória.")
    @Size(min = 10, max = 500, message = "A Descrição deve ter entre 10 e 500 caracteres.")
    private String descricao;

    @NotBlank(message = "A Localização de Coleta é obrigatória.")
    @Size(max = 100, message = "A Localização deve ter no máximo 100 caracteres.")
    private String localizacao;

    @NotNull(message = "A Data de Coleta é obrigatória.")
    @PastOrPresent(message = "A Data de Coleta não pode ser futura.")
    private LocalDate dataColeta;
}