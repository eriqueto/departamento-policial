package com.policia.departamentopolicial.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DepoimentoRequestDTO {

    @Positive(message = "O ID do Depoimento deve ser um valor positivo.")
    private Integer id;

    @NotNull(message = "O ID do Caso do Envolvido é obrigatório.")
    @Positive(message = "O ID do Caso do Envolvido deve ser um valor positivo.")
    private Integer casoId;

    @NotBlank(message = "O CPF da Pessoa Envolvida é obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos (apenas números).")
    @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    private String pessoaCpf;

    @NotNull(message = "A Data e Hora do Depoimento são obrigatórias.")
    @PastOrPresent(message = "A Data e Hora do Depoimento não podem ser futuras.")
    private LocalDateTime dataHoraDepoimento;

    @NotBlank(message = "O Conteúdo do Depoimento é obrigatório.")
    @Size(min = 50, max = 5000, message = "O Conteúdo deve ter entre 50 e 5000 caracteres.")
    private String conteudoDepoimento;
}