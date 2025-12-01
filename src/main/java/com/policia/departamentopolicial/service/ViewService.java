package com.policia.departamentopolicial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ViewService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getCasosAtivosDetails() {
        String sql = "SELECT * FROM vw_casos_ativos_detalhes";

        return jdbcTemplate.queryForList(sql);
    }
}