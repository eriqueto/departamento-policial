package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.PessoaRequestDTO;
import com.policia.departamentopolicial.dto.PessoaResponseDTO;
import com.policia.departamentopolicial.service.PessoaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    private final PessoaService service;

    public PessoaController(PessoaService service) {
        this.service = service;
    }

    @GetMapping
    public List<PessoaResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<PessoaResponseDTO> findById(@PathVariable String cpf) {
        PessoaResponseDTO p = service.findById(cpf);
        return ResponseEntity.ok(p);
    }

    @PostMapping
    public ResponseEntity<PessoaResponseDTO> create(@RequestBody PessoaRequestDTO dto) {
        PessoaResponseDTO saved = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<PessoaResponseDTO> update(@PathVariable String cpf, @RequestBody PessoaRequestDTO dto) {
        PessoaResponseDTO updated = service.update(cpf, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        service.delete(cpf);
        return ResponseEntity.noContent().build();
    }
}
