package com.policia.departamentopolicial.dto;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaRequestDTO {
    @NotBlank(message = "O CPF é obrigatório.")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos (apenas números).")
    @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    private String cpf;

    @NotBlank(message = "Nome não pode estar em branco")
    @Size(min = 3, max = 100)
    private String nome;

    private String sexo;

    @NotNull(message = "A Data de Nascimento é obrigatória.")
    @Past(message = "A Data de Nascimento deve ser anterior à data atual.")
    private LocalDate dataNascimento;

    @Pattern(regexp = "^$|^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$",
            message = "O Telefone deve estar no formato válido.")
    private String telefone;

    @Valid
    private EnderecoRequestDTO endereco;

    private Integer idEndereco;;
}