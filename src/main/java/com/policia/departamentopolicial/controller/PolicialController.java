package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.PolicialRequestDTO;
import com.policia.departamentopolicial.dto.PolicialResponseDTO;
import com.policia.departamentopolicial.entity.Policial;
import com.policia.departamentopolicial.service.PolicialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policiais")
public class PolicialController {

    private final PolicialService service;

    public PolicialController(PolicialService service) {
        this.service = service;
    }

    @GetMapping
    public List<PolicialResponseDTO> findAll() {
        return service.getPoliciais();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policial> findById(@PathVariable Integer id) {
        Policial p = service.getPolicialById(id);
        return ResponseEntity.ok(p);
    }

    @PostMapping
    public ResponseEntity<PolicialResponseDTO> create(@RequestBody PolicialRequestDTO dto) {
        PolicialResponseDTO saved = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PolicialResponseDTO> update(@PathVariable Integer id, @RequestBody PolicialRequestDTO dto) {
        PolicialResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
