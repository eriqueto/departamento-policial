package com.policia.departamentopolicial.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "pessoa")
public class Pessoa {
    @Id
    @Column(name = "cpf", length = 11)
    private String cpf;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "sexo", length = 1)
    private String sexo;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;

    @Lob
    @Column(name = "foto_perfil")
    private byte[] fotoPerfil;
}