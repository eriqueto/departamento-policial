package com.policia.departamentopolicial.repository;

import com.policia.departamentopolicial.entity.Caso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CasoRepository extends JpaRepository<Caso, Integer> {
}

