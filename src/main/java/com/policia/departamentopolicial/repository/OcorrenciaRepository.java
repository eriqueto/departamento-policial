package com.policia.departamentopolicial.repository;

import com.policia.departamentopolicial.entity.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Integer> {
}

