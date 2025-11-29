package com.policia.departamentopolicial.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CasoRequestDTO {

    @Positive(message = "O ID do Caso deve ser um valor positivo.")
    private Integer id;

    @NotNull(message = "O número do Boletim de Ocorrência é obrigatório.")
    @Positive(message = "O número do Boletim deve ser um valor positivo.")
    private Integer ocorrenciaNumBoletim;

    @NotNull(message = "O ID do Policial Responsável é obrigatório.")
    @Positive(message = "O ID do Policial deve ser um valor positivo.")
    private Integer policialResponsavelId;

    @NotBlank(message = "O Status do Caso é obrigatório.")
    @Size(max = 30, message = "O Status do Caso deve ter no máximo 30 caracteres.")
    private String statusCaso;

    @NotNull(message = "A Data de Abertura é obrigatória.")
    @PastOrPresent(message = "A Data de Abertura não pode ser uma data futura.")
    private LocalDate dataAbertura;

    private LocalDate dataFechamento;
}