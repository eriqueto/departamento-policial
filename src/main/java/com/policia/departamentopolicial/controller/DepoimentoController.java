package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.DepoimentoRequestDTO;
import com.policia.departamentopolicial.dto.DepoimentoResponseDTO;
import com.policia.departamentopolicial.entity.Depoimento;
import com.policia.departamentopolicial.service.DepoimentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depoimentos")
public class DepoimentoController {

    private final DepoimentoService service;

    public DepoimentoController(DepoimentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<DepoimentoResponseDTO> findAll() {
        return service.getDepoimentos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Depoimento> findById(@PathVariable Integer id) {
        Depoimento d = service.getDepoimentoById(id);
        return ResponseEntity.ok(d);
    }

    @PostMapping
    public ResponseEntity<DepoimentoResponseDTO> create(@RequestBody DepoimentoRequestDTO dto) {
        DepoimentoResponseDTO saved = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepoimentoResponseDTO> update(@PathVariable Integer id, @RequestBody DepoimentoRequestDTO dto) {
        DepoimentoResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
