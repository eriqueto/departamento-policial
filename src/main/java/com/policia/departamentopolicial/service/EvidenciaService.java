package com.policia.departamentopolicial.service;

import com.policia.departamentopolicial.dto.EvidenciaRequestDTO;
import com.policia.departamentopolicial.dto.EvidenciaResponseDTO;
import com.policia.departamentopolicial.entity.Evidencia;
import com.policia.departamentopolicial.entity.Caso;
import com.policia.departamentopolicial.repository.EvidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvidenciaService {

    @Autowired
    private EvidenciaRepository evidenciaRepository;

    @Autowired
    private CasoService casoService;

    public Evidencia getEvidenciaById(int id) {
        return evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evidência não encontrada"));
    }

    public List<EvidenciaResponseDTO> getEvidencias() {
        return evidenciaRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional // Adicionado
    public EvidenciaResponseDTO create(EvidenciaRequestDTO dto) {
        Evidencia novaEvidencia = new Evidencia();
        updateEntityFromDTO(novaEvidencia, dto);

        Evidencia evidenciaSalva = evidenciaRepository.save(novaEvidencia);
        return convertToResponseDTO(evidenciaSalva);
    }

    @Transactional // Adicionado
    public EvidenciaResponseDTO update(int id, EvidenciaRequestDTO dto) {
        Evidencia evidenciaExistente = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evidência não encontrada"));

        updateEntityFromDTO(evidenciaExistente, dto);

        Evidencia evidenciaSalva = evidenciaRepository.save(evidenciaExistente);
        return convertToResponseDTO(evidenciaSalva);
    }

    public void delete(int id) {
        if (!evidenciaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evidência não encontrada");
        }
        evidenciaRepository.deleteById(id);
    }

    private EvidenciaResponseDTO convertToResponseDTO(Evidencia evidencia) {
        EvidenciaResponseDTO dto = new EvidenciaResponseDTO();
        dto.setId(evidencia.getId());
        dto.setDescricao(evidencia.getDescricao());
        dto.setLocalizacao(evidencia.getLocalizacao());
        dto.setDataColeta(evidencia.getDataColeta());

        if (evidencia.getCaso() != null) {
            dto.setCasoId(evidencia.getCaso().getId());
        }

        return dto;
    }

    private void updateEntityFromDTO(Evidencia evidencia, EvidenciaRequestDTO dto) {
        evidencia.setDescricao(dto.getDescricao());
        evidencia.setLocalizacao(dto.getLocalizacao());
        evidencia.setDataColeta(dto.getDataColeta());

        Caso caso = casoService.getCasoById(dto.getCasoId());
        evidencia.setCaso(caso);
    }
}