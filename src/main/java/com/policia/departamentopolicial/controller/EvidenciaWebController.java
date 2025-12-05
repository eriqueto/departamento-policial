package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.EvidenciaRequestDTO;
import com.policia.departamentopolicial.service.EvidenciaService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/evidencias")
public class EvidenciaWebController {

    private final EvidenciaService service;

    public EvidenciaWebController(EvidenciaService service) {
        this.service = service;
    }

    @GetMapping
    public String listarEvidencias(Model model) {
        model.addAttribute("listaEvidencias", service.getEvidencias());
        return "evidencias-list";
    }

    @GetMapping("/novo")
    public String formNovaEvidencia(Model model) {
        EvidenciaRequestDTO dto = new EvidenciaRequestDTO();
        model.addAttribute("evidenciaDTO", dto);
        model.addAttribute("isEdit", false);
        return "evidencias-form";
    }

    @GetMapping("/editar/{id}")
    public String formEditarEvidencia(@PathVariable Integer id, Model model) {
        var evidenciaResponse = service.getEvidenciaById(id);

        EvidenciaRequestDTO dto = new EvidenciaRequestDTO();
        dto.setId(evidenciaResponse.getId());
        dto.setDescricao(evidenciaResponse.getDescricao());
        dto.setLocalizacao(evidenciaResponse.getLocalizacao());
        dto.setDataColeta(evidenciaResponse.getDataColeta());

        if (evidenciaResponse.getCaso() != null) {
            dto.setCasoId(evidenciaResponse.getCaso().getId());
        }

        model.addAttribute("evidenciaDTO", dto);
        model.addAttribute("isEdit", true);
        return "evidencias-form";
    }

    @PostMapping("/salvar")
    public String salvarEvidencia(@ModelAttribute EvidenciaRequestDTO dto,
                                  @RequestParam(required = false) boolean isEdit,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (isEdit && dto.getId() != null) {
                service.update(dto.getId(), dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Evidência atualizada com sucesso!");
            } else {
                service.create(dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Evidência registrada com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/evidencias";
    }

    @GetMapping("/deletar/{id}")
    public String deletarEvidencia(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Evidência excluída com sucesso!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível excluir: Esta evidência pode estar vinculada a outros registros.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/evidencias";
    }
}