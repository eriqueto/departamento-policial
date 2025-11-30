package com.policia.departamentopolicial.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaEnvolvidaRequestDTO {
    @NotNull(message = "O ID do Caso é obrigatório.")
    @Positive(message = "O ID do Caso deve ser um valor positivo.")
    private Integer casoId;

    @NotBlank(message = "O CPF da Pessoa é obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos (apenas números).")
    @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    private String pessoaCpf;

    @NotBlank(message = "O Tipo de Envolvimento é obrigatório.")
    @Size(max = 50, message = "O Tipo de Envolvimento deve ter no máximo 50 caracteres.")
    private String tipoEnvolvimento;
}