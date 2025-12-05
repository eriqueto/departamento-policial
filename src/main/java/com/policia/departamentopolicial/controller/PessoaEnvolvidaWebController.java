package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.PessoaEnvolvidaRequestDTO;
import com.policia.departamentopolicial.service.PessoaEnvolvidaService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pessoas-envolvidas")
public class PessoaEnvolvidaWebController {

    private final PessoaEnvolvidaService service;

    public PessoaEnvolvidaWebController(PessoaEnvolvidaService service) {
        this.service = service;
    }

    @GetMapping
    public String listarEnvolvidos(Model model) {
        model.addAttribute("listaEnvolvidos", service.getPessoasEnvolvidas());
        return "pessoas-envolvidas-list";
    }

    @GetMapping("/novo")
    public String formNovoEnvolvimento(Model model) {
        PessoaEnvolvidaRequestDTO dto = new PessoaEnvolvidaRequestDTO();
        model.addAttribute("envolvimentoDTO", dto);
        model.addAttribute("isEdit", false);
        return "pessoas-envolvidas-form";
    }

    // Como a chave é composta, precisamos de dois parâmetros na URL
    @GetMapping("/editar/{idCaso}/{cpfPessoa}")
    public String formEditarEnvolvimento(@PathVariable Integer idCaso,
                                         @PathVariable String cpfPessoa,
                                         Model model) {

        var envolvimento = service.getPessoaEnvolvidaById(idCaso, cpfPessoa);

        PessoaEnvolvidaRequestDTO dto = new PessoaEnvolvidaRequestDTO();

        dto.setCasoId(envolvimento.getId().getIdCaso());
        dto.setPessoaCpf(envolvimento.getId().getCpfPessoa());

        dto.setTipoEnvolvimento(envolvimento.getTipoEnvolvimento());

        model.addAttribute("envolvimentoDTO", dto);
        model.addAttribute("isEdit", true);
        return "pessoas-envolvidas-form";
    }

    @PostMapping("/salvar")
    public String salvarEnvolvimento(@ModelAttribute PessoaEnvolvidaRequestDTO dto,
                                     @RequestParam(required = false) boolean isEdit,
                                     RedirectAttributes redirectAttributes) {
        try {
            if (isEdit) {
                // No update passamos as chaves originais
                service.update(dto.getCasoId(), dto.getPessoaCpf(), dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Envolvimento atualizado com sucesso!");
            } else {
                service.create(dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Pessoa vinculada ao caso com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/pessoas-envolvidas";
    }

    @GetMapping("/deletar/{idCaso}/{cpfPessoa}")
    public String deletarEnvolvimento(@PathVariable Integer idCaso,
                                      @PathVariable String cpfPessoa,
                                      RedirectAttributes redirectAttributes) {
        try {
            service.delete(idCaso, cpfPessoa);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Vínculo removido com sucesso!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível remover: Existem depoimentos vinculados a este envolvimento.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/pessoas-envolvidas";
    }
}