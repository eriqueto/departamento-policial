package com.policia.departamentopolicial.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "evidencia")
@Getter
@Setter
@NoArgsConstructor
public class Evidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evidencia")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_caso")
    private Caso caso;

    @Lob
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "localizacao", length = 150)
    private String localizacao;

    @Column(name = "data_coleta")
    private java.time.LocalDate dataColeta;
}