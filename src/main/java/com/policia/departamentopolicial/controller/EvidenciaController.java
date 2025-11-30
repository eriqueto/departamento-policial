package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.EvidenciaRequestDTO;
import com.policia.departamentopolicial.dto.EvidenciaResponseDTO;
import com.policia.departamentopolicial.entity.Evidencia;
import com.policia.departamentopolicial.service.EvidenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evidencias")
public class EvidenciaController {

    private final EvidenciaService service;

    public EvidenciaController(EvidenciaService service) {
        this.service = service;
    }

    @GetMapping
    public List<EvidenciaResponseDTO> findAll() {
        return service.getEvidencias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evidencia> findById(@PathVariable Integer id) {
        Evidencia e = service.getEvidenciaById(id);
        return ResponseEntity.ok(e);
    }

    @PostMapping
    public ResponseEntity<EvidenciaResponseDTO> create(@RequestBody EvidenciaRequestDTO dto) {
        EvidenciaResponseDTO saved = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvidenciaResponseDTO> update(@PathVariable Integer id, @RequestBody EvidenciaRequestDTO dto) {
        EvidenciaResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
