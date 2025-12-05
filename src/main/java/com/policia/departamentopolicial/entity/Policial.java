package com.policia.departamentopolicial.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "policial")
public class Policial implements Persistable<Integer> {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public Integer getId() {
        return numDistintivo;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    @PostLoad
    @PrePersist
    void markNotNew() {
        this.isNew = false;
    }
}
