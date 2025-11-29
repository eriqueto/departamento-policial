package com.policia.departamentopolicial.service;

import com.policia.departamentopolicial.dto.RelatorioRequestDTO;
import com.policia.departamentopolicial.dto.RelatorioResponseDTO;
import com.policia.departamentopolicial.entity.Relatorio;
import com.policia.departamentopolicial.entity.Caso;
import com.policia.departamentopolicial.entity.Policial;
import com.policia.departamentopolicial.repository.RelatorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private RelatorioRepository relatorioRepository;

    @Autowired
    private CasoService casoService; // Necessário para buscar o Caso relacionado

    @Autowired
    private PolicialService policialService; // Necessário para buscar o Policial Emissor

    public Relatorio getRelatorioById(int id) {
        Relatorio relatorio = relatorioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Relatório não encontrado"));
        return relatorio;
    }

    public List<RelatorioResponseDTO> getRelatorios() {
        List<Relatorio> relatorios = relatorioRepository.findAll();
        return relatorios.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public RelatorioResponseDTO create(RelatorioRequestDTO dto) {
        if (dto.getId() != null && dto.getId() > 0) {
            if (relatorioRepository.existsById(dto.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de Relatório já cadastrado");
            }
        }

        Relatorio novoRelatorio = new Relatorio();
        updateEntityFromDTO(novoRelatorio, dto);

        Relatorio relatorioSalvo = relatorioRepository.save(novoRelatorio);
        return convertToResponseDTO(relatorioSalvo);
    }

    public RelatorioResponseDTO update(int id, RelatorioRequestDTO dto) {
        Relatorio relatorioExistente = relatorioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Relatório não encontrado"));

        updateEntityFromDTO(relatorioExistente, dto);

        Relatorio relatorioSalvo = relatorioRepository.save(relatorioExistente);
        return convertToResponseDTO(relatorioSalvo);
    }

    public void delete(int id) {
        if (!relatorioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Relatório não encontrado");
        }
        relatorioRepository.deleteById(id);
    }


    private RelatorioResponseDTO convertToResponseDTO(Relatorio relatorio) {
        RelatorioResponseDTO dto = new RelatorioResponseDTO();
        dto.setId(relatorio.getId());
        dto.setConteudo(relatorio.getConteudo());

        if (relatorio.getCaso() != null) {
            dto.setCasoId(relatorio.getCaso().getId());
        }

        if (relatorio.getPolicialEmissor() != null) {
            dto.setPolicialEmissorId(relatorio.getPolicialEmissor().getNumDistintivo());
        }

        return dto;
    }

    private void updateEntityFromDTO(Relatorio relatorio, RelatorioRequestDTO dto) {
        if (relatorio.getId() == null && dto.getId() != null) {
            relatorio.setId(dto.getId());
        }

        relatorio.setConteudo(dto.getConteudo());

        Caso caso = casoService.getCasoById(dto.getCasoId());
        relatorio.setCaso(caso);

        Policial policialEmissor = policialService.getPolicialById(dto.getPolicialEmissorId());
        relatorio.setPolicialEmissor(policialEmissor);
    }
}