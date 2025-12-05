package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.OcorrenciaRequestDTO;
import com.policia.departamentopolicial.service.OcorrenciaService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ocorrencias")
public class OcorrenciaWebController {

    private final OcorrenciaService service;

    public OcorrenciaWebController(OcorrenciaService service) {
        this.service = service;
    }

    @GetMapping
    public String listarOcorrencias(Model model) {
        model.addAttribute("listaOcorrencias", service.getOcorrencias());
        return "ocorrencias-list";
    }

    @GetMapping("/novo")
    public String formNovaOcorrencia(Model model) {
        OcorrenciaRequestDTO dto = new OcorrenciaRequestDTO();
        // Sugere a hora atual no form se quiser, mas deixamos null para o usuário preencher
        model.addAttribute("ocorrenciaDTO", dto);
        model.addAttribute("isEdit", false);
        return "ocorrencias-form";
    }

    @GetMapping("/editar/{id}")
    public String formEditarOcorrencia(@PathVariable Integer id, Model model) {
        var ocorrencia = service.getOcorrenciaById(id);

        OcorrenciaRequestDTO dto = new OcorrenciaRequestDTO();
        dto.setNumBoletim(ocorrencia.getNumBoletim());
        dto.setDataHoraRegistro(ocorrencia.getDataHoraRegistro());
        dto.setDescricao(ocorrencia.getDescricao());

        if (ocorrencia.getDeclarante() != null) {
            dto.setDeclaranteCpf(ocorrencia.getDeclarante().getCpf());
        }
        if (ocorrencia.getPolicialRegistro() != null) {
            dto.setPolicialRegistroId(ocorrencia.getPolicialRegistro().getNumDistintivo());
        }

        model.addAttribute("ocorrenciaDTO", dto);
        model.addAttribute("isEdit", true);
        return "ocorrencias-form";
    }

    @PostMapping("/salvar")
    public String salvarOcorrencia(@ModelAttribute OcorrenciaRequestDTO dto,
                                   @RequestParam(required = false) boolean isEdit,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (isEdit && dto.getNumBoletim() != null) {
                service.update(dto.getNumBoletim(), dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Ocorrência atualizada com sucesso!");
            } else {
                service.create(dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Ocorrência registrada com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/ocorrencias";
    }

    @GetMapping("/deletar/{id}")
    public String deletarOcorrencia(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Ocorrência excluída com sucesso!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível excluir: Existem evidências ou relatórios vinculados.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/ocorrencias";
    }
}