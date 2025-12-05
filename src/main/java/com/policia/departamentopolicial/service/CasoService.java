package com.policia.departamentopolicial.service;

import com.policia.departamentopolicial.dto.CasoRequestDTO;
import com.policia.departamentopolicial.dto.CasoResponseDTO;
import com.policia.departamentopolicial.entity.Caso;
import com.policia.departamentopolicial.entity.Ocorrencia;
import com.policia.departamentopolicial.entity.Policial;
import com.policia.departamentopolicial.repository.CasoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CasoService {

    @Autowired
    private CasoRepository casoRepository;

    @Autowired
    private OcorrenciaService ocorrenciaService;

    @Autowired
    private PolicialService policialService;

    public Caso getCasoById(int id) {
        return casoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caso não encontrado"));
    }

    public List<CasoResponseDTO> getCasos() {
        return casoRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional // Adicionado
    public CasoResponseDTO create(CasoRequestDTO dto) {
        // Validação de datas
        validateDates(dto);

        // ID é automático
        Caso novoCaso = new Caso();
        updateEntityFromDTO(novoCaso, dto);

        Caso casoSalvo = casoRepository.save(novoCaso);
        return convertToResponseDTO(casoSalvo);
    }

    @Transactional // Adicionado
    public CasoResponseDTO update(int id, CasoRequestDTO dto) {
        Caso casoExistente = casoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caso não encontrado"));

        validateDates(dto);
        updateEntityFromDTO(casoExistente, dto);

        Caso casoSalvo = casoRepository.save(casoExistente);
        return convertToResponseDTO(casoSalvo);
    }

    public void delete(int id) {
        if (!casoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Caso não encontrado");
        }
        casoRepository.deleteById(id);
    }

    private void validateDates(CasoRequestDTO dto) {
        if (dto.getDataFechamento() != null && dto.getDataAbertura() != null &&
                dto.getDataFechamento().isBefore(dto.getDataAbertura())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A Data de Fechamento não pode ser anterior à Data de Abertura.");
        }
    }

    private CasoResponseDTO convertToResponseDTO(Caso caso) {
        CasoResponseDTO dto = new CasoResponseDTO();
        dto.setId(caso.getId());
        dto.setStatusCaso(caso.getStatusCaso());
        dto.setDataAbertura(caso.getDataAbertura());
        dto.setDataFechamento(caso.getDataFechamento());

        if (caso.getOcorrencia() != null) {
            dto.setOcorrenciaNumBoletim(caso.getOcorrencia().getNumBoletim());
        }

        if (caso.getPolicialResponsavel() != null) {
            dto.setPolicialResponsavelId(caso.getPolicialResponsavel().getNumDistintivo());
        }

        return dto;
    }

    private void updateEntityFromDTO(Caso caso, CasoRequestDTO dto) {
        // Não mexemos no ID aqui (automático)

        caso.setStatusCaso(dto.getStatusCaso());
        caso.setDataAbertura(dto.getDataAbertura());
        caso.setDataFechamento(dto.getDataFechamento());

        // Busca e associa a Ocorrência (OneToOne)
        Ocorrencia ocorrencia = ocorrenciaService.getOcorrenciaById(dto.getOcorrenciaNumBoletim());
        caso.setOcorrencia(ocorrencia);

        // Busca e associa o Policial (ManyToOne)
        Policial policialResponsavel = policialService.getPolicialById(dto.getPolicialResponsavelId());
        caso.setPolicialResponsavel(policialResponsavel);
    }
}