package com.policia.departamentopolicial.service;

import com.policia.departamentopolicial.dto.PessoaRequestDTO;
import com.policia.departamentopolicial.dto.PessoaResponseDTO;
import com.policia.departamentopolicial.entity.Pessoa;
import com.policia.departamentopolicial.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public PessoaResponseDTO findById(String cpf) {
        Pessoa pessoa = pessoaRepository.findById(cpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
        return convertToResponseDTO(pessoa);
    }

    public List<PessoaResponseDTO> findAll() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        return pessoas.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public PessoaResponseDTO create(PessoaRequestDTO dto) {
        if (dto.getCpf() == null || dto.getCpf().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF é obrigatório");
        }
        if (pessoaRepository.existsById(dto.getCpf())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado");
        }

        Pessoa novaPessoa = new Pessoa();
        updateEntityFromDTO(novaPessoa, dto);

        Pessoa pessoaSalva = pessoaRepository.save(novaPessoa);
        return convertToResponseDTO(pessoaSalva);
    }

    public PessoaResponseDTO update(String cpf, PessoaRequestDTO dto) {
        Pessoa pessoaExistente = pessoaRepository.findById(cpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));

        updateEntityFromDTO(pessoaExistente, dto);

        Pessoa pessoaSalva = pessoaRepository.save(pessoaExistente);
        return convertToResponseDTO(pessoaSalva);
    }

    public void delete(String cpf) {
        if (!pessoaRepository.existsById(cpf)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada");
        }
        pessoaRepository.deleteById(cpf);
    }


    private PessoaResponseDTO convertToResponseDTO(Pessoa pessoa) {
        PessoaResponseDTO dto = new PessoaResponseDTO();
        dto.setCpf(pessoa.getCpf());
        dto.setNome(pessoa.getNome());
        dto.setSexo(pessoa.getSexo());
        dto.setDataNascimento(pessoa.getDataNascimento());
        dto.setTelefone(pessoa.getTelefone());
        return dto;
    }

    private void updateEntityFromDTO(Pessoa pessoa, PessoaRequestDTO dto) {
        if (pessoa.getCpf() == null) {
            pessoa.setCpf(dto.getCpf());
        }
        pessoa.setNome(dto.getNome());
        pessoa.setSexo(dto.getSexo());
        pessoa.setDataNascimento(dto.getDataNascimento());
        pessoa.setTelefone(dto.getTelefone());
    }
}