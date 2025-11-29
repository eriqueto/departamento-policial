package com.policia.departamentopolicial.service;


import com.policia.departamentopolicial.dto.EnderecoRequestDTO;
import com.policia.departamentopolicial.dto.EnderecoResponseDTO;
import com.policia.departamentopolicial.entity.Endereco;
import com.policia.departamentopolicial.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Endereco getEnderecoById(Integer id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));
        return endereco;
    }

    public List<EnderecoResponseDTO> getEnderecos() {
        List<Endereco> enderecos = enderecoRepository.findAll();
        return enderecos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public EnderecoResponseDTO create(EnderecoRequestDTO dto) {
        if (dto.getId() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }
        if (enderecoRepository.existsById(dto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id já cadastrado");
        }

        Endereco novoEndereco = new Endereco();
        updateEntityFromDTO(novoEndereco, dto);

        Endereco enderecoSalvo = enderecoRepository.save(novoEndereco);
        return convertToResponseDTO(enderecoSalvo);
    }

    public EnderecoResponseDTO update(Integer id, EnderecoRequestDTO dto) {
        Endereco enderecoExistente = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereco não encontrado"));

        updateEntityFromDTO(enderecoExistente, dto);

        Endereco enderecoSalvo = enderecoRepository.save(enderecoExistente);
        return convertToResponseDTO(enderecoSalvo);
    }

    public void delete(Integer id) {
        if (!enderecoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado");
        }
        enderecoRepository.deleteById(id);
    }


    private EnderecoResponseDTO convertToResponseDTO(Endereco endereco) {
        EnderecoResponseDTO dto = new EnderecoResponseDTO();
        dto.setId(endereco.getId());
        dto.setCep(endereco.getCep());
        dto.setLogradouro(endereco.getLogradouro());
        dto.setComplemento(endereco.getComplemento());
        dto.setBairro(endereco.getBairro());
        return dto;
    }

    private void updateEntityFromDTO(Endereco endereco, EnderecoRequestDTO dto) {
        if (endereco.getId() == null) {
            endereco.setId(dto.getId());
        }
        endereco.setCep(dto.getCep());
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());

    }
}
