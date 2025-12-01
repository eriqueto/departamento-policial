package com.policia.departamentopolicial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProceduralService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void executeEncerrarCaso(Integer idCaso, String conteudoRelatorio) {
        String sql = "CALL encerrar_caso(?, ?)";

        try {
            jdbcTemplate.update(sql, idCaso, conteudoRelatorio);

        } catch (org.springframework.dao.DataAccessException e) {
            throw new RuntimeException("Erro ao executar procedure: " + e.getLocalizedMessage());
        }
    }
}