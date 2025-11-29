package com.policia.departamentopolicial.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EnderecoRequestDTO {
    private int id;

    @NotBlank(message = "CEP não pode estar em branco")
    @Size(min = 9, max = 9)
    @Pattern(regexp = "^\\\\d{5}-?\\\\d{3}$", message = "Insira um CEP no formato XXXXX-XXX")
    private String cep;

    private String logradouro;
    private String complemento;
    private String bairro;
}
