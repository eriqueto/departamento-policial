package com.policia.departamentopolicial.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "delegacia")
public class Delegacia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_delegacia")
    private Integer id;

    @Column(name = "nome", length = 100, nullable = false, unique = true)
    private String nome;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @OneToOne
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;


}