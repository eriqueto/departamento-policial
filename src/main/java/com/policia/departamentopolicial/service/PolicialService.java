package com.policia.departamentopolicial.service;

import com.policia.departamentopolicial.dto.PolicialRequestDTO;
import com.policia.departamentopolicial.dto.PolicialResponseDTO;
import com.policia.departamentopolicial.entity.Policial;
import com.policia.departamentopolicial.entity.Pessoa;
import com.policia.departamentopolicial.entity.Delegacia;
import com.policia.departamentopolicial.repository.PessoaRepository;
import com.policia.departamentopolicial.repository.PolicialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PolicialService {

    @Autowired
    private PolicialRepository policialRepository;
    @Autowired
    private DelegaciaService delegaciaService;
    @Autowired
    private PessoaRepository pessoaRepository;

    public Policial getPolicialById(int id) {
        Policial policial = policialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Policial não encontrado"));
        return policial;
    }

    public List<PolicialResponseDTO> getPoliciais() {
        List<Policial> policiais = policialRepository.findAll();
        return policiais.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public PolicialResponseDTO create(PolicialRequestDTO dto) {
        if (dto.getNumDistintivo() == null || dto.getNumDistintivo() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Número do Distintivo é obrigatório e deve ser positivo");
        }
        if (policialRepository.existsById(dto.getNumDistintivo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Número do Distintivo já cadastrado");
        }

        Policial novoPolicial = new Policial();
        updateEntityFromDTO(novoPolicial, dto);

        Policial policialSalvo = policialRepository.save(novoPolicial);
        return convertToResponseDTO(policialSalvo);
    }

    public PolicialResponseDTO update(int id, PolicialRequestDTO dto) {
        Policial policialExistente = policialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Policial não encontrado"));

        // Note: For update, the ID (numDistintivo) is taken from the path variable 'id', not the DTO, as per the established pattern.
        updateEntityFromDTO(policialExistente, dto);

        Policial policialSalvo = policialRepository.save(policialExistente);
        return convertToResponseDTO(policialSalvo);
    }

    public void delete(int id) {
        if (!policialRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Policial não encontrado");
        }
        policialRepository.deleteById(id);
    }


    private PolicialResponseDTO convertToResponseDTO(Policial policial) {
        PolicialResponseDTO dto = new PolicialResponseDTO();
        dto.setNumDistintivo(policial.getNumDistintivo());
        dto.setCargo(policial.getCargo());

        if (policial.getPessoa() != null) {
            dto.setPessoaCpf(policial.getPessoa().getCpf());
        }

        if (policial.getDelegacia() != null) {
            dto.setDelegaciaId(policial.getDelegacia().getId());
        }

        return dto;
    }

    private void updateEntityFromDTO(Policial policial, PolicialRequestDTO dto) {
        if (policial.getNumDistintivo() == null) {
            policial.setNumDistintivo(dto.getNumDistintivo());
        }

        policial.setCargo(dto.getCargo());

        Pessoa pessoa = pessoaRepository.findById(dto.getPessoaCPF())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));;
        policial.setPessoa(pessoa);

        Delegacia delegacia = delegaciaService.getDelegaciaById(dto.getDelegaciaId());
        policial.setDelegacia(delegacia);
    }
}