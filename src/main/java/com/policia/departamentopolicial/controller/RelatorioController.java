package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.RelatorioRequestDTO;
import com.policia.departamentopolicial.dto.RelatorioResponseDTO;
import com.policia.departamentopolicial.entity.Relatorio;
import com.policia.departamentopolicial.service.RelatorioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService service;

    public RelatorioController(RelatorioService service) {
        this.service = service;
    }

    @GetMapping
    public List<RelatorioResponseDTO> findAll() {
        return service.getRelatorios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Relatorio> findById(@PathVariable Integer id) {
        Relatorio r = service.getRelatorioById(id);
        return ResponseEntity.ok(r);
    }

    @PostMapping
    public ResponseEntity<RelatorioResponseDTO> create(@RequestBody RelatorioRequestDTO dto) {
        RelatorioResponseDTO saved = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RelatorioResponseDTO> update(@PathVariable Integer id, @RequestBody RelatorioRequestDTO dto) {
        RelatorioResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
