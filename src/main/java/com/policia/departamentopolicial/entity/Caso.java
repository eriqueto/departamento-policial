package com.policia.departamentopolicial.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caso")
@Getter
@Setter
@NoArgsConstructor
public class Caso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_caso")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "num_boletim")
    private Ocorrencia ocorrencia;

    @Column(name = "status_caso", length = 30)
    private String statusCaso;

    @ManyToOne
    @JoinColumn(name = "policial_responsavel")
    private Policial policialResponsavel;

    @Column(name = "data_abertura")
    private java.time.LocalDate dataAbertura;

    @Column(name = "data_fechamento")
    private java.time.LocalDate dataFechamento;
}