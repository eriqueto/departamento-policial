package com.policia.departamentopolicial.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaRequestDTO {
    private String cpf;

    @NotBlank(message = "Nome não pode estar em branco")
    @Size(min = 3, max = 100)
    private String nome;

    private String sexo;

    private LocalDate dataNascimento;

    private String telefone;

}