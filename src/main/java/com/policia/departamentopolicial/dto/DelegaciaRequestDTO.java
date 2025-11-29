package com.policia.departamentopolicial.dto;

import com.policia.departamentopolicial.entity.Endereco;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DelegaciaRequestDTO {
    @Positive()
    private Integer id;

    @NotBlank(message = "Nome não pode estar em branco")
    @Size(min = 3, max = 100)
    private String nome;

    @NotBlank(message = "Telefone não pode estar em branco")
    @Pattern(regexp = "^\\\\d{5}-?\\\\d{4}$", message = "Insira um telefone no formato XXXXX-XXXX")
    private String telefone;
    private Endereco endereco;
}
