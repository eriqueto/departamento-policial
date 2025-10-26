package com.policia.departamentopolicial.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "depoimento")
@Getter
@Setter
@NoArgsConstructor
public class Depoimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_depoimento")
    private Integer id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "id_caso", referencedColumnName = "id_caso", nullable = false),
            @JoinColumn(name = "cpf_pessoa", referencedColumnName = "cpf_pessoa", nullable = false)
    })
    private PessoaEnvolvida envolvido;

    @Column(name = "data_hora_depoimento")
    private java.time.LocalDateTime dataHoraDepoimento;

    @Lob
    @Column(name = "conteudo_depoimento")
    private String conteudoDepoimento;
}