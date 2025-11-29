package com.policia.departamentopolicial.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoResponseDTO {
    private Integer id;
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
}
