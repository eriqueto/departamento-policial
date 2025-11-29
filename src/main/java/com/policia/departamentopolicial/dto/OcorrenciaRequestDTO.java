package com.policia.departamentopolicial.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class OcorrenciaRequestDTO {
    @Positive(message = "O Número do Boletim deve ser um valor positivo.")
    private Integer numBoletim;

    @NotNull(message = "A Data e Hora de Registro são obrigatórias.")
    private LocalDateTime dataHoraRegistro;

    @NotBlank(message = "O CPF do Declarante é obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos (apenas números).")
    @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    private String declaranteCpf;

    @NotNull(message = "O ID do Policial de Registro é obrigatório.")
    @Positive(message = "O ID do Policial de Registro deve ser um valor positivo.")
    private Integer policialRegistroId;

    @NotBlank(message = "A Descrição da ocorrência é obrigatória.")
    @Size(min = 10, max = 1000, message = "A Descrição deve ter entre 10 e 1000 caracteres.")
    private String descricao;
}
