package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.RelatorioRequestDTO;
import com.policia.departamentopolicial.service.RelatorioService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/relatorios")
public class RelatorioWebController {

    private final RelatorioService service;

    public RelatorioWebController(RelatorioService service) {
        this.service = service;
    }

    @GetMapping
    public String listarRelatorios(Model model) {
        model.addAttribute("listaRelatorios", service.getRelatorios());
        return "relatorios-list";
    }

    @GetMapping("/novo")
    public String formNovoRelatorio(Model model) {
        RelatorioRequestDTO dto = new RelatorioRequestDTO();
        model.addAttribute("relatorioDTO", dto);
        model.addAttribute("isEdit", false);
        return "relatorios-form";
    }

    @GetMapping("/editar/{id}")
    public String formEditarRelatorio(@PathVariable Integer id, Model model) {
        var relatorioResponse = service.getRelatorioById(id);

        RelatorioRequestDTO dto = new RelatorioRequestDTO();
        dto.setId(relatorioResponse.getId());
        dto.setConteudo(relatorioResponse.getConteudo());

        if (relatorioResponse.getCaso() != null) {
            dto.setCasoId(relatorioResponse.getCaso().getId());
        }
        if (relatorioResponse.getPolicialEmissor() != null) {
            dto.setPolicialEmissorId(relatorioResponse.getPolicialEmissor().getNumDistintivo());
        }

        model.addAttribute("relatorioDTO", dto);
        model.addAttribute("isEdit", true);
        return "relatorios-form";
    }

    @PostMapping("/salvar")
    public String salvarRelatorio(@ModelAttribute RelatorioRequestDTO dto,
                                  @RequestParam(required = false) boolean isEdit,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (isEdit && dto.getId() != null) {
                service.update(dto.getId(), dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Relatório atualizado com sucesso!");
            } else {
                service.create(dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Relatório criado com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/relatorios";
    }

    @GetMapping("/deletar/{id}")
    public String deletarRelatorio(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Relatório excluído com sucesso!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível excluir: Relatório vinculado a outros dados.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/relatorios";
    }
}