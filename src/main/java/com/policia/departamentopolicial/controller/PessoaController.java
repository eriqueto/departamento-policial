package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.entity.Pessoa;
import com.policia.departamentopolicial.repository.PessoaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    private final PessoaRepository repository;

    public PessoaController(PessoaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Pessoa> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Pessoa> findById(@PathVariable String cpf) {
        Optional<Pessoa> p = repository.findById(cpf);
        return p.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pessoa> create(@RequestBody Pessoa pessoa) {
        if (pessoa.getCpf() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Pessoa saved = repository.save(pessoa);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<Pessoa> update(@PathVariable String cpf, @RequestBody Pessoa pessoa) {
        if (!repository.existsById(cpf)) {
            return ResponseEntity.notFound().build();
        }
        pessoa.setCpf(cpf);
        Pessoa saved = repository.save(pessoa);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        if (!repository.existsById(cpf)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(cpf);
        return ResponseEntity.noContent().build();
    }
}

