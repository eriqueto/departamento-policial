package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.entity.Depoimento;
import com.policia.departamentopolicial.repository.DepoimentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/depoimentos")
public class DepoimentoController {

    private final DepoimentoRepository repository;

    public DepoimentoController(DepoimentoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Depoimento> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Depoimento> findById(@PathVariable Integer id) {
        Optional<Depoimento> d = repository.findById(id);
        return d.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Depoimento> create(@RequestBody Depoimento depoimento) {
        Depoimento saved = repository.save(depoimento);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Depoimento> update(@PathVariable Integer id, @RequestBody Depoimento depoimento) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        depoimento.setId(id);
        Depoimento saved = repository.save(depoimento);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

