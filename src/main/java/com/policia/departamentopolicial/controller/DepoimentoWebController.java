package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.DepoimentoRequestDTO;
import com.policia.departamentopolicial.service.DepoimentoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/depoimentos")
public class DepoimentoWebController {

    private final DepoimentoService service;

    public DepoimentoWebController(DepoimentoService service) {
        this.service = service;
    }

    @GetMapping
    public String listarDepoimentos(Model model) {
        model.addAttribute("listaDepoimentos", service.getDepoimentos());
        return "depoimentos-list";
    }

    @GetMapping("/novo")
    public String formNovoDepoimento(Model model) {
        DepoimentoRequestDTO dto = new DepoimentoRequestDTO();
        model.addAttribute("depoimentoDTO", dto);
        model.addAttribute("isEdit", false);
        return "depoimentos-form";
    }

    @GetMapping("/editar/{id}")
    public String formEditarDepoimento(@PathVariable Integer id, Model model) {
        var depoimento = service.getDepoimentoById(id);

        DepoimentoRequestDTO dto = new DepoimentoRequestDTO();
        dto.setId(depoimento.getId());
        dto.setDataHoraDepoimento(depoimento.getDataHoraDepoimento());
        dto.setConteudoDepoimento(depoimento.getConteudoDepoimento());

        if (depoimento.getEnvolvido() != null && depoimento.getEnvolvido().getId() != null) {
            dto.setCasoId(depoimento.getEnvolvido().getId().getIdCaso());
            dto.setPessoaCpf(depoimento.getEnvolvido().getId().getCpfPessoa());
        }

        model.addAttribute("depoimentoDTO", dto);
        model.addAttribute("isEdit", true);
        return "depoimentos-form";
    }

    @PostMapping("/salvar")
    public String salvarDepoimento(@ModelAttribute DepoimentoRequestDTO dto,
                                   @RequestParam(required = false) boolean isEdit,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (isEdit && dto.getId() != null) {
                service.update(dto.getId(), dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Depoimento atualizado com sucesso!");
            } else {
                service.create(dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Depoimento registrado com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/depoimentos";
    }

    @GetMapping("/deletar/{id}")
    public String deletarDepoimento(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Depoimento excluído com sucesso!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível excluir: Depoimento vinculado a outros registros.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/depoimentos";
    }
}