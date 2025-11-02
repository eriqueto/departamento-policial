package com.policia.departamentopolicial.repository;

import com.policia.departamentopolicial.entity.Depoimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepoimentoRepository extends JpaRepository<Depoimento, Integer> {
}

