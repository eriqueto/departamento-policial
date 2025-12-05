package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.CasoRequestDTO;
import com.policia.departamentopolicial.service.CasoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/casos")
public class CasoWebController {

    private final CasoService service;

    public CasoWebController(CasoService service) {
        this.service = service;
    }

    @GetMapping
    public String listarCasos(Model model) {
        model.addAttribute("listaCasos", service.getCasos());
        return "casos-list";
    }

    @GetMapping("/novo")
    public String formNovoCaso(Model model) {
        CasoRequestDTO dto = new CasoRequestDTO();
        // Opcional: Definir status padrão
        dto.setStatusCaso("Aberto");
        model.addAttribute("casoDTO", dto);
        model.addAttribute("isEdit", false);
        return "casos-form";
    }

    @GetMapping("/editar/{id}")
    public String formEditarCaso(@PathVariable Integer id, Model model) {
        var casoResponse = service.getCasoById(id);

        CasoRequestDTO dto = new CasoRequestDTO();
        dto.setId(casoResponse.getId());
        dto.setStatusCaso(casoResponse.getStatusCaso());
        dto.setDataAbertura(casoResponse.getDataAbertura());
        dto.setDataFechamento(casoResponse.getDataFechamento());

        if (casoResponse.getOcorrencia() != null) {
            dto.setOcorrenciaNumBoletim(casoResponse.getOcorrencia().getNumBoletim());
        }
        if (casoResponse.getPolicialResponsavel() != null) {
            dto.setPolicialResponsavelId(casoResponse.getPolicialResponsavel().getNumDistintivo());
        }

        model.addAttribute("casoDTO", dto);
        model.addAttribute("isEdit", true);
        return "casos-form";
    }

    @PostMapping("/salvar")
    public String salvarCaso(@ModelAttribute CasoRequestDTO dto,
                             @RequestParam(required = false) boolean isEdit,
                             RedirectAttributes redirectAttributes) {
        try {
            if (isEdit && dto.getId() != null) {
                service.update(dto.getId(), dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Caso atualizado com sucesso!");
            } else {
                service.create(dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Caso aberto com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/casos";
    }

    @GetMapping("/deletar/{id}")
    public String deletarCaso(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Caso excluído com sucesso!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível excluir: Existem evidências ou relatórios vinculados a este caso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/casos";
    }
}