package com.policia.departamentopolicial.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaResponseDTO {

    private String cpf;
    private String nome;
    private String sexo;
    private LocalDate dataNascimento;
    private String telefone;

    // Também não incluímos a 'fotoPerfil' aqui!
}