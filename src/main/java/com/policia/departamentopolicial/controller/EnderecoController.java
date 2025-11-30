package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.EnderecoRequestDTO;
import com.policia.departamentopolicial.dto.EnderecoResponseDTO;
import com.policia.departamentopolicial.entity.Endereco;
import com.policia.departamentopolicial.service.EnderecoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {

    private final EnderecoService service;

    public EnderecoController(EnderecoService service) {
        this.service = service;
    }

    @GetMapping
    public List<EnderecoResponseDTO> findAll() {
        return service.getEnderecos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> findById(@PathVariable Integer id) {
        Endereco e = service.getEnderecoById(id);
        return ResponseEntity.ok(e);
    }

    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> create(@RequestBody EnderecoRequestDTO dto) {
        EnderecoResponseDTO saved = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> update(@PathVariable Integer id, @RequestBody EnderecoRequestDTO dto) {
        EnderecoResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
