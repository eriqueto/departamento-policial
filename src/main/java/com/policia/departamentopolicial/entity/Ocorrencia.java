package com.policia.departamentopolicial.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ocorrencia")
@Getter
@Setter
@NoArgsConstructor
public class Ocorrencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_boletim")
    private Integer numBoletim;

    @Column(name = "data_hora_registro")
    private java.time.LocalDateTime dataHoraRegistro;

    @ManyToOne
    @JoinColumn(name = "cpf_declarante", nullable = false)
    private Pessoa declarante;

    @ManyToOne
    @JoinColumn(name = "policial_registro", nullable = false)
    private Policial policialRegistro;

    @Lob
    @Column(name = "descricao")
    private String descricao;
}