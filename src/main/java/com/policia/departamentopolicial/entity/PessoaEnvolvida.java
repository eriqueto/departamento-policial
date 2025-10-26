package com.policia.departamentopolicial.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pessoa_envolvida")
@Getter
@Setter
@NoArgsConstructor
public class PessoaEnvolvida {

    @EmbeddedId
    private PessoaEnvolvidaId id;

    @ManyToOne
    @MapsId("idCaso")
    @JoinColumn(name = "id_caso")
    private Caso caso;

    @ManyToOne
    @MapsId("cpfPessoa")
    @JoinColumn(name = "cpf_pessoa")
    private Pessoa pessoa;

    @Column(name = "tipo_envolvimento", length = 25)
    private String tipoEnvolvimento;
}