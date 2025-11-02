package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.entity.Caso;
import com.policia.departamentopolicial.repository.CasoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/casos")
public class CasoController {

    private final CasoRepository repository;

    public CasoController(CasoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Caso> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caso> findById(@PathVariable Integer id) {
        Optional<Caso> c = repository.findById(id);
        return c.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Caso> create(@RequestBody Caso caso) {
        Caso saved = repository.save(caso);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Caso> update(@PathVariable Integer id, @RequestBody Caso caso) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        caso.setId(id);
        Caso saved = repository.save(caso);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

