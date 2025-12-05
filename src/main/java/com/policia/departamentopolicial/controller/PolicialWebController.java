package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.PolicialRequestDTO;
import com.policia.departamentopolicial.service.PolicialService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/policiais")
public class PolicialWebController {

    private final PolicialService service;

    public PolicialWebController(PolicialService service) {
        this.service = service;
    }

    @GetMapping
    public String listarPoliciais(Model model) {
        model.addAttribute("listaPoliciais", service.getPoliciais());
        return "policiais-list";
    }

    @GetMapping("/novo")
    public String formNovoPolicial(Model model) {
        PolicialRequestDTO dto = new PolicialRequestDTO();
        model.addAttribute("policialDTO", dto);
        model.addAttribute("isEdit", false);
        return "policiais-form";
    }

    @GetMapping("/editar/{id}")
    public String formEditarPolicial(@PathVariable Integer id, Model model) {
        var policial = service.getPolicialById(id);

        PolicialRequestDTO dto = new PolicialRequestDTO();
        dto.setNumDistintivo(policial.getNumDistintivo());
        dto.setCargo(policial.getCargo());

        if (policial.getPessoa() != null) dto.setPessoaCPF(policial.getPessoa().getCpf());
        if (policial.getDelegacia() != null) dto.setDelegaciaId(policial.getDelegacia().getId());

        model.addAttribute("policialDTO", dto);
        model.addAttribute("isEdit", true);
        return "policiais-form";
    }

    @PostMapping("/salvar")
    public String salvarPolicial(@ModelAttribute PolicialRequestDTO dto,
                                 @RequestParam(required = false) boolean isEdit,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (isEdit) {
                service.update(dto.getNumDistintivo(), dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Policial atualizado com sucesso!");
            } else {
                service.create(dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Policial criado com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/policiais";
    }

    @GetMapping("/deletar/{id}")
    public String deletarPolicial(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Policial excluído com sucesso!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível excluir: Este policial está vinculado a outros registros.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/policiais";
    }

    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensagemErro", "ID inválido fornecido na URL.");
        return "redirect:/policiais";
    }
}