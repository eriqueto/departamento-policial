package com.policia.departamentopolicial.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "relatorio")
@Getter
@Setter
@NoArgsConstructor
public class Relatorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_relatorio")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_caso")
    private Caso caso;

    @Lob
    @Column(name = "conteudo")
    private String conteudo;

    @ManyToOne
    @JoinColumn(name = "policial_emissor")
    private Policial policialEmissor;
}