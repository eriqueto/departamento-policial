package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.entity.Relatorio;
import com.policia.departamentopolicial.repository.RelatorioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioRepository repository;

    public RelatorioController(RelatorioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Relatorio> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Relatorio> findById(@PathVariable Integer id) {
        Optional<Relatorio> r = repository.findById(id);
        return r.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Relatorio> create(@RequestBody Relatorio relatorio) {
        Relatorio saved = repository.save(relatorio);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Relatorio> update(@PathVariable Integer id, @RequestBody Relatorio relatorio) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        relatorio.setId(id);
        Relatorio saved = repository.save(relatorio);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

