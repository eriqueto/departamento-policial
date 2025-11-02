package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.entity.Evidencia;
import com.policia.departamentopolicial.repository.EvidenciaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/evidencias")
public class EvidenciaController {

    private final EvidenciaRepository repository;

    public EvidenciaController(EvidenciaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Evidencia> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evidencia> findById(@PathVariable Integer id) {
        Optional<Evidencia> e = repository.findById(id);
        return e.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Evidencia> create(@RequestBody Evidencia evidencia) {
        Evidencia saved = repository.save(evidencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evidencia> update(@PathVariable Integer id, @RequestBody Evidencia evidencia) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        evidencia.setId(id);
        Evidencia saved = repository.save(evidencia);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

