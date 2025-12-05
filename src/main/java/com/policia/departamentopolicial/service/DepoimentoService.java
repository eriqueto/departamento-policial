package com.policia.departamentopolicial.service;

import com.policia.departamentopolicial.dto.DepoimentoRequestDTO;
import com.policia.departamentopolicial.dto.DepoimentoResponseDTO;
import com.policia.departamentopolicial.entity.Depoimento;
import com.policia.departamentopolicial.entity.PessoaEnvolvida;
import com.policia.departamentopolicial.repository.DepoimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepoimentoService {

    @Autowired
    private DepoimentoRepository depoimentoRepository;

    @Autowired
    private PessoaEnvolvidaService pessoaEnvolvidaService;

    public Depoimento getDepoimentoById(int id) {
        return depoimentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Depoimento não encontrado"));
    }

    public List<DepoimentoResponseDTO> getDepoimentos() {
        return depoimentoRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DepoimentoResponseDTO create(DepoimentoRequestDTO dto) {
        Depoimento novoDepoimento = new Depoimento();
        updateEntityFromDTO(novoDepoimento, dto);
        Depoimento depoimentoSalvo = depoimentoRepository.save(novoDepoimento);
        return convertToResponseDTO(depoimentoSalvo);
    }

    @Transactional // Adicionado
    public DepoimentoResponseDTO update(int id, DepoimentoRequestDTO dto) {
        Depoimento depoimentoExistente = depoimentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Depoimento não encontrado"));

        updateEntityFromDTO(depoimentoExistente, dto);

        Depoimento depoimentoSalvo = depoimentoRepository.save(depoimentoExistente);
        return convertToResponseDTO(depoimentoSalvo);
    }

    public void delete(int id) {
        if (!depoimentoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Depoimento não encontrado");
        }
        depoimentoRepository.deleteById(id);
    }

    private DepoimentoResponseDTO convertToResponseDTO(Depoimento depoimento) {
        DepoimentoResponseDTO dto = new DepoimentoResponseDTO();
        dto.setId(depoimento.getId());
        dto.setDataHoraDepoimento(depoimento.getDataHoraDepoimento());
        dto.setConteudoDepoimento(depoimento.getConteudoDepoimento());

        if (depoimento.getEnvolvido() != null && depoimento.getEnvolvido().getId() != null) {
            dto.setCasoId(depoimento.getEnvolvido().getId().getIdCaso());
            dto.setPessoaCpf(depoimento.getEnvolvido().getId().getCpfPessoa());
        }
        return dto;
    }

    private void updateEntityFromDTO(Depoimento depoimento, DepoimentoRequestDTO dto) {
        if (depoimento.getId() == null && dto.getId() != null) {
            depoimento.setId(dto.getId());
        }

        depoimento.setDataHoraDepoimento(dto.getDataHoraDepoimento());
        depoimento.setConteudoDepoimento(dto.getConteudoDepoimento());

        PessoaEnvolvida envolvido = pessoaEnvolvidaService.buscarOuCriar(
                dto.getCasoId(),
                dto.getPessoaCpf()
        );

        depoimento.setEnvolvido(envolvido);
    }
}