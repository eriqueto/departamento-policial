package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.CasoRequestDTO;
import com.policia.departamentopolicial.dto.CasoResponseDTO;
import com.policia.departamentopolicial.entity.Caso;
import com.policia.departamentopolicial.service.CasoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/casos")
public class CasoController {

    private final CasoService service;

    public CasoController(CasoService service) {
        this.service = service;
    }

    @GetMapping
    public List<CasoResponseDTO> findAll() {
        return service.getCasos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caso> findById(@PathVariable Integer id) {
        Caso c = service.getCasoById(id);
        return ResponseEntity.ok(c);
    }

    @PostMapping
    public ResponseEntity<CasoResponseDTO> create(@RequestBody CasoRequestDTO dto) {
        CasoResponseDTO saved = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CasoResponseDTO> update(@PathVariable Integer id, @RequestBody CasoRequestDTO dto) {
        CasoResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
