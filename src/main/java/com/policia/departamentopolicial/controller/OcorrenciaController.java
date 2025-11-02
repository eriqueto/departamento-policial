package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.entity.Ocorrencia;
import com.policia.departamentopolicial.repository.OcorrenciaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaRepository repository;

    public OcorrenciaController(OcorrenciaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Ocorrencia> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ocorrencia> findById(@PathVariable Integer id) {
        Optional<Ocorrencia> o = repository.findById(id);
        return o.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ocorrencia> create(@RequestBody Ocorrencia ocorrencia) {
        Ocorrencia saved = repository.save(ocorrencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ocorrencia> update(@PathVariable Integer id, @RequestBody Ocorrencia ocorrencia) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        ocorrencia.setNumBoletim(id);
        Ocorrencia saved = repository.save(ocorrencia);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

