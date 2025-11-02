package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.entity.Policial;
import com.policia.departamentopolicial.repository.PolicialRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/policiais")
public class PolicialController {

    private final PolicialRepository repository;

    public PolicialController(PolicialRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Policial> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policial> findById(@PathVariable Integer id) {
        Optional<Policial> p = repository.findById(id);
        return p.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Policial> create(@RequestBody Policial policial) {
        Policial saved = repository.save(policial);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Policial> update(@PathVariable Integer id, @RequestBody Policial policial) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        policial.setNumDistintivo(id);
        Policial saved = repository.save(policial);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

