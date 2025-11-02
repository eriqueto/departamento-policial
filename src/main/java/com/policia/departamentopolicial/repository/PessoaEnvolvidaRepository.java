package com.policia.departamentopolicial.repository;

import com.policia.departamentopolicial.entity.PessoaEnvolvida;
import com.policia.departamentopolicial.entity.PessoaEnvolvidaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaEnvolvidaRepository extends JpaRepository<PessoaEnvolvida, PessoaEnvolvidaId> {
}

