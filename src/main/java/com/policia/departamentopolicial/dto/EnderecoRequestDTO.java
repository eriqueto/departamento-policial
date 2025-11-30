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
    private Integer id;

    @NotBlank(message = "CEP não pode estar em branco")
    @Size(min = 8, max = 10)
    @Pattern(regexp = "^\\d{8}(-\\d{3})?$", message = "Insira um CEP válido (8 dígitos numéricos).")
    private String cep;

    @NotBlank(message = "Logradouro não pode estar em branco")
    private String logradouro;

    private String complemento;

    @NotBlank(message = "Bairro não pode estar em branco")
    private String bairro;
}
