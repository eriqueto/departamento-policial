package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.entity.Delegacia;
import com.policia.departamentopolicial.repository.DelegaciaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/delegacias")
public class DelegaciaController {

    private final DelegaciaRepository repository;

    public DelegaciaController(DelegaciaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Delegacia> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delegacia> findById(@PathVariable Integer id) {
        Optional<Delegacia> d = repository.findById(id);
        return d.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Delegacia> create(@RequestBody Delegacia delegacia) {
        Delegacia saved = repository.save(delegacia);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Delegacia> update(@PathVariable Integer id, @RequestBody Delegacia delegacia) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        delegacia.setId(id);
        Delegacia saved = repository.save(delegacia);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

