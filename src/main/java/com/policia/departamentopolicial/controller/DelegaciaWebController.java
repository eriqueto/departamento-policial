package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.DelegaciaRequestDTO;
import com.policia.departamentopolicial.entity.Endereco;
import com.policia.departamentopolicial.service.DelegaciaService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/delegacias")
public class DelegaciaWebController {

    private final DelegaciaService service;

    public DelegaciaWebController(DelegaciaService service) {
        this.service = service;
    }

    @GetMapping
    public String listarDelegacias(Model model) {
        model.addAttribute("listaDelegacias", service.getDelegacias());
        return "delegacias-list";
    }

    @GetMapping("/novo")
    public String formNovaDelegacia(Model model) {
        DelegaciaRequestDTO dto = new DelegaciaRequestDTO();
        // Inicializa o objeto endereço para não dar erro no Thymeleaf ao acessar endereco.id
        dto.setEndereco(new Endereco());

        model.addAttribute("delegaciaDTO", dto);
        model.addAttribute("isEdit", false);
        return "delegacias-form";
    }

    @GetMapping("/editar/{id}")
    public String formEditarDelegacia(@PathVariable Integer id, Model model) {
        var delegacia = service.getDelegaciaById(id);

        DelegaciaRequestDTO dto = new DelegaciaRequestDTO();
        dto.setId(delegacia.getId());
        dto.setNome(delegacia.getNome());
        dto.setTelefone(delegacia.getTelefone());

        if (delegacia.getEndereco() != null) {
            dto.setEndereco(delegacia.getEndereco());
        } else {
            dto.setEndereco(new Endereco());
        }

        model.addAttribute("delegaciaDTO", dto);
        model.addAttribute("isEdit", true);
        return "delegacias-form";
    }

    @PostMapping("/salvar")
    public String salvarDelegacia(@ModelAttribute DelegaciaRequestDTO dto,
                                  @RequestParam(required = false) boolean isEdit,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (isEdit && dto.getId() != null) {
                service.update(dto.getId(), dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Delegacia atualizada com sucesso!");
            } else {
                service.create(dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Delegacia criada com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/delegacias";
    }

    @GetMapping("/deletar/{id}")
    public String deletarDelegacia(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Delegacia excluída com sucesso!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível excluir: Esta delegacia possui vínculos (policiais, casos, etc).");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/delegacias";
    }
}