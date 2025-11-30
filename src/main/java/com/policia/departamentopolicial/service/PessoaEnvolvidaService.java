package com.policia.departamentopolicial.service;

import com.policia.departamentopolicial.dto.PessoaEnvolvidaRequestDTO;
import com.policia.departamentopolicial.dto.PessoaEnvolvidaResponseDTO;
import com.policia.departamentopolicial.entity.PessoaEnvolvida;
import com.policia.departamentopolicial.entity.PessoaEnvolvidaId;
import com.policia.departamentopolicial.entity.Caso;
import com.policia.departamentopolicial.entity.Pessoa;
import com.policia.departamentopolicial.repository.PessoaEnvolvidaRepository;
import com.policia.departamentopolicial.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PessoaEnvolvidaService {

    @Autowired
    private PessoaEnvolvidaRepository pessoaEnvolvidaRepository;

    @Autowired
    private CasoService casoService; // Necessário para buscar o Caso

    @Autowired
    private PessoaService pessoaService; // Necessário para buscar a Pessoa
    @Autowired
    private PessoaRepository pessoaRepository;

    public PessoaEnvolvida getPessoaEnvolvidaById(Integer casoId, String pessoaCpf) {
        PessoaEnvolvidaId id = new PessoaEnvolvidaId();
        id.setCpfPessoa(pessoaCpf);
        id.setIdCaso(casoId);

        return pessoaEnvolvidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Relacionamento Pessoa-Caso não encontrado"));
    }

    public List<PessoaEnvolvidaResponseDTO> getPessoasEnvolvidas() {
        List<PessoaEnvolvida> envolvimentos = pessoaEnvolvidaRepository.findAll();
        return envolvimentos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public PessoaEnvolvidaResponseDTO create(PessoaEnvolvidaRequestDTO dto) {
        PessoaEnvolvidaId id = new PessoaEnvolvidaId();
        id.setIdCaso(dto.getCasoId());
        id.setCpfPessoa(dto.getPessoaCpf());

        if (pessoaEnvolvidaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pessoa já envolvida neste caso.");
        }

        PessoaEnvolvida novoEnvolvimento = new PessoaEnvolvida();

        novoEnvolvimento.setId(id);

        updateEntityFromDTO(novoEnvolvimento, dto);

        PessoaEnvolvida envolvimentoSalvo = pessoaEnvolvidaRepository.save(novoEnvolvimento);
        return convertToResponseDTO(envolvimentoSalvo);
    }

    public PessoaEnvolvidaResponseDTO update(Integer casoId, String pessoaCpf, PessoaEnvolvidaRequestDTO dto) {
        PessoaEnvolvidaId id = new PessoaEnvolvidaId();
        id.setIdCaso(dto.getCasoId());
        id.setCpfPessoa(dto.getPessoaCpf());

        PessoaEnvolvida envolvimentoExistente = pessoaEnvolvidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Relacionamento Pessoa-Caso não encontrado para atualização"));

        envolvimentoExistente.setTipoEnvolvimento(dto.getTipoEnvolvimento());

        PessoaEnvolvida envolvimentoSalvo = pessoaEnvolvidaRepository.save(envolvimentoExistente);
        return convertToResponseDTO(envolvimentoSalvo);
    }

    public void delete(Integer casoId, String pessoaCpf) {
        PessoaEnvolvidaId id = new PessoaEnvolvidaId();
        id.setIdCaso(casoId);
        id.setCpfPessoa(pessoaCpf);

        if (!pessoaEnvolvidaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Relacionamento Pessoa-Caso não encontrado.");
        }
        pessoaEnvolvidaRepository.deleteById(id);
    }


    private PessoaEnvolvidaResponseDTO convertToResponseDTO(PessoaEnvolvida envolvimento) {
        PessoaEnvolvidaResponseDTO dto = new PessoaEnvolvidaResponseDTO();

        dto.setCasoId(envolvimento.getId().getIdCaso());
        dto.setPessoaCpf(envolvimento.getId().getCpfPessoa());

        dto.setTipoEnvolvimento(envolvimento.getTipoEnvolvimento());

        return dto;
    }

    private void updateEntityFromDTO(PessoaEnvolvida envolvimento, PessoaEnvolvidaRequestDTO dto) {

        envolvimento.setTipoEnvolvimento(dto.getTipoEnvolvimento());

        Caso caso = casoService.getCasoById(dto.getCasoId());
        envolvimento.setCaso(caso);

        Pessoa pessoa = pessoaRepository.findById(dto.getPessoaCpf())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Pessoa não encontrado"));;
        envolvimento.setPessoa(pessoa);
    }
}