package com.policia.departamentopolicial.service;

import com.policia.departamentopolicial.dto.OcorrenciaRequestDTO;
import com.policia.departamentopolicial.dto.OcorrenciaResponseDTO;
import com.policia.departamentopolicial.entity.Ocorrencia;
import com.policia.departamentopolicial.entity.Pessoa;
import com.policia.departamentopolicial.entity.Policial;
import com.policia.departamentopolicial.repository.OcorrenciaRepository;
import com.policia.departamentopolicial.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OcorrenciaService {

    @Autowired
    private OcorrenciaRepository ocorrenciaRepository;

    @Autowired
    private PessoaService pessoaService; // Necessário para buscar o Declarante (Pessoa)

    @Autowired
    private PolicialService policialService; // Necessário para buscar o Policial de Registro
    @Autowired
    private PessoaRepository pessoaRepository;

    public Ocorrencia getOcorrenciaById(int id) {
        return ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ocorrência não encontrada"));
    }

    public List<OcorrenciaResponseDTO> getOcorrencias() {
        List<Ocorrencia> ocorrencias = ocorrenciaRepository.findAll();
        return ocorrencias.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OcorrenciaResponseDTO create(OcorrenciaRequestDTO dto) {
        Ocorrencia novaOcorrencia = new Ocorrencia();
        updateEntityFromDTO(novaOcorrencia, dto);

        Ocorrencia ocorrenciaSalva = ocorrenciaRepository.save(novaOcorrencia);
        return convertToResponseDTO(ocorrenciaSalva);
    }

    @Transactional
    public OcorrenciaResponseDTO update(int id, OcorrenciaRequestDTO dto) {
        Ocorrencia ocorrenciaExistente = ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ocorrência não encontrada"));

        updateEntityFromDTO(ocorrenciaExistente, dto);

        Ocorrencia ocorrenciaSalva = ocorrenciaRepository.save(ocorrenciaExistente);
        return convertToResponseDTO(ocorrenciaSalva);
    }

    public void delete(int id) {
        if (!ocorrenciaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ocorrência não encontrada");
        }
        ocorrenciaRepository.deleteById(id);
    }

    private OcorrenciaResponseDTO convertToResponseDTO(Ocorrencia ocorrencia) {
        OcorrenciaResponseDTO dto = new OcorrenciaResponseDTO();
        dto.setNumBoletim(ocorrencia.getNumBoletim());
        dto.setDataHoraRegistro(ocorrencia.getDataHoraRegistro());
        dto.setDescricao(ocorrencia.getDescricao());

        if (ocorrencia.getDeclarante() != null) {
            dto.setDeclaranteCpf(ocorrencia.getDeclarante().getCpf());
        }

        if (ocorrencia.getPolicialRegistro() != null) {
            dto.setPolicialRegistroId(ocorrencia.getPolicialRegistro().getNumDistintivo());
        }

        return dto;
    }

    private void updateEntityFromDTO(Ocorrencia ocorrencia, OcorrenciaRequestDTO dto) {
        ocorrencia.setDataHoraRegistro(dto.getDataHoraRegistro());
        ocorrencia.setDescricao(dto.getDescricao());

        Pessoa declarante = pessoaRepository.findById(dto.getDeclaranteCpf())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Declarante (Pessoa) não encontrado com CPF: " + dto.getDeclaranteCpf()));
        ocorrencia.setDeclarante(declarante);

        Policial policialRegistro = policialService.getPolicialById(dto.getPolicialRegistroId());
        ocorrencia.setPolicialRegistro(policialRegistro);
    }
}