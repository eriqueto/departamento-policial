package com.policia.departamentopolicial.dto;

import com.policia.departamentopolicial.entity.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DelegaciaResponseDTO {
    private Integer id;
    private String nome;
    private String telefone;
    private Endereco endereco;
}
