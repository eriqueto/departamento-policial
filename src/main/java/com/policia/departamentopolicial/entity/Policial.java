package com.policia.departamentopolicial.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "policial")
public class Policial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_distintivo")
    private Integer numDistintivo;

    @OneToOne
    @JoinColumn(name = "cpf", referencedColumnName = "cpf")
    private Pessoa pessoa;

    @Column(name = "cargo", length = 50)
    private String cargo;

    @ManyToOne
    @JoinColumn(name = "id_delegacia")
    private Delegacia delegacia;
}
