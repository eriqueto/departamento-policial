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
import org.springframework.transaction.annotation.Transactional; // Importante
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired
    private RelatorioRepository relatorioRepository;

    @Autowired
    private CasoService casoService;

    @Autowired
    private PolicialService policialService;

    public Relatorio getRelatorioById(int id) {
        return relatorioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Relatório não encontrado"));
    }

    public List<RelatorioResponseDTO> getRelatorios() {
        return relatorioRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional // Adicionado
    public RelatorioResponseDTO create(RelatorioRequestDTO dto) {
        // ID é automático (GenerationType.IDENTITY), não verificamos manualmente

        Relatorio novoRelatorio = new Relatorio();
        updateEntityFromDTO(novoRelatorio, dto);

        Relatorio relatorioSalvo = relatorioRepository.save(novoRelatorio);
        return convertToResponseDTO(relatorioSalvo);
    }

    @Transactional // Adicionado
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
        // ID é gerenciado pelo banco, não alteramos aqui

        relatorio.setConteudo(dto.getConteudo());

        // Busca e associa as entidades relacionadas
        Caso caso = casoService.getCasoById(dto.getCasoId());
        relatorio.setCaso(caso);

        Policial policialEmissor = policialService.getPolicialById(dto.getPolicialEmissorId());
        relatorio.setPolicialEmissor(policialEmissor);
    }
}