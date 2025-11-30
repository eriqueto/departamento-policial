package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.OcorrenciaRequestDTO;
import com.policia.departamentopolicial.dto.OcorrenciaResponseDTO;
import com.policia.departamentopolicial.entity.Ocorrencia;
import com.policia.departamentopolicial.service.OcorrenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaService service;

    public OcorrenciaController(OcorrenciaService service) {
        this.service = service;
    }

    @GetMapping
    public List<OcorrenciaResponseDTO> findAll() {
        return service.getOcorrencias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ocorrencia> findById(@PathVariable Integer id) {
        Ocorrencia o = service.getOcorrenciaById(id);
        return ResponseEntity.ok(o);
    }

    @PostMapping
    public ResponseEntity<OcorrenciaResponseDTO> create(@RequestBody OcorrenciaRequestDTO dto) {
        OcorrenciaResponseDTO saved = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OcorrenciaResponseDTO> update(@PathVariable Integer id, @RequestBody OcorrenciaRequestDTO dto) {
        OcorrenciaResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
