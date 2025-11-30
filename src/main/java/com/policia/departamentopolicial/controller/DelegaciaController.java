package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.DelegaciaRequestDTO;
import com.policia.departamentopolicial.dto.DelegaciaResponseDTO;
import com.policia.departamentopolicial.entity.Delegacia;
import com.policia.departamentopolicial.service.DelegaciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delegacias")
public class DelegaciaController {

    private final DelegaciaService service;

    public DelegaciaController(DelegaciaService service) {
        this.service = service;
    }

    @GetMapping
    public List<DelegaciaResponseDTO> findAll() {
        return service.getDelegacias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delegacia> findById(@PathVariable Integer id) {
        Delegacia d = service.getDelegaciaById(id);
        return ResponseEntity.ok(d);
    }

    @PostMapping
    public ResponseEntity<DelegaciaResponseDTO> create(@RequestBody DelegaciaRequestDTO dto) {
        DelegaciaResponseDTO saved = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DelegaciaResponseDTO> update(@PathVariable Integer id, @RequestBody DelegaciaRequestDTO dto) {
        DelegaciaResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
