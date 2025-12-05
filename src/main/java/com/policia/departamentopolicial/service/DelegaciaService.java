package com.policia.departamentopolicial.service;

import com.policia.departamentopolicial.dto.DelegaciaRequestDTO;
import com.policia.departamentopolicial.dto.DelegaciaResponseDTO;
import com.policia.departamentopolicial.entity.Delegacia;
import com.policia.departamentopolicial.entity.Endereco;
import com.policia.departamentopolicial.repository.DelegaciaRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DelegaciaService {

    @Autowired
    private DelegaciaRepository delegaciaRepository;

    @Autowired
    private EnderecoService enderecoService;

    public Delegacia getDelegaciaById(int id) {
        Delegacia delegacia = delegaciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delegacia não encontrada"));
        return delegacia;
    }

    public List<DelegaciaResponseDTO> getDelegacias() {
        List<Delegacia> delegacias = delegaciaRepository.findAll();
        return delegacias.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DelegaciaResponseDTO create(DelegaciaRequestDTO dto) {
        Delegacia novaDelegacia = new Delegacia();
        updateEntityFromDTO(novaDelegacia, dto);

        Delegacia delegaciaSalva = delegaciaRepository.save(novaDelegacia);
        return convertToResponseDTO(delegaciaSalva);
    }

    @Transactional
    public DelegaciaResponseDTO update(int id, DelegaciaRequestDTO dto) {
        Delegacia delegaciaExistente = delegaciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delegacia não encontrada"));

        updateEntityFromDTO(delegaciaExistente, dto);

        Delegacia delegaciaSalva = delegaciaRepository.save(delegaciaExistente);
        return convertToResponseDTO(delegaciaSalva);
    }

    public void delete(int id) {
        if (!delegaciaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Delegacia não encontrada");
        }
        delegaciaRepository.deleteById(id);
    }


    private DelegaciaResponseDTO convertToResponseDTO(Delegacia delegacia) {
        DelegaciaResponseDTO dto = new DelegaciaResponseDTO();
        dto.setId(delegacia.getId());
        dto.setNome(delegacia.getNome());
        dto.setTelefone(delegacia.getTelefone());

        if (delegacia.getEndereco() != null) {
            dto.setEndereco(delegacia.getEndereco());
        }

        return dto;
    }

    private void updateEntityFromDTO(Delegacia delegacia, DelegaciaRequestDTO dto) {
        delegacia.setNome(dto.getNome());
        delegacia.setTelefone(dto.getTelefone());

        if (dto.getEndereco() != null && dto.getEndereco().getId() != null) {
            Endereco endereco = new Endereco();
            endereco.setId(dto.getEndereco().getId());
            delegacia.setEndereco(endereco);
        }
    }
}