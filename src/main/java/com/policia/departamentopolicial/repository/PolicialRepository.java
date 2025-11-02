package com.policia.departamentopolicial.repository;

import com.policia.departamentopolicial.entity.Policial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicialRepository extends JpaRepository<Policial, Integer> {
}

