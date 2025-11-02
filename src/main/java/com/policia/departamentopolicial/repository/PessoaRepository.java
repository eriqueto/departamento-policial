package com.policia.departamentopolicial.repository;

import com.policia.departamentopolicial.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, String> {
}

