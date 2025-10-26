package com.policia.departamentopolicial.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class PessoaEnvolvidaId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_caso")
    private Integer idCaso;

    @Column(name = "cpf_pessoa", length = 11)
    private String cpfPessoa;
}