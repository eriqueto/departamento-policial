package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.EnderecoRequestDTO;
import com.policia.departamentopolicial.service.EnderecoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enderecos")
public class EnderecoWebController {

    private final EnderecoService service;

    public EnderecoWebController(EnderecoService service) {
        this.service = service;
    }

    @GetMapping
    public String listarEnderecos(Model model) {
        model.addAttribute("listaEnderecos", service.getEnderecos());
        return "enderecos-list";
    }

    @GetMapping("/novo")
    public String formNovoEndereco(Model model) {
        EnderecoRequestDTO dto = new EnderecoRequestDTO();
        model.addAttribute("enderecoDTO", dto);
        model.addAttribute("isEdit", false);
        return "enderecos-form";
    }

    @GetMapping("/editar/{id}")
    public String formEditarEndereco(@PathVariable Integer id, Model model) {
        var endereco = service.getEnderecoById(id);

        EnderecoRequestDTO dto = new EnderecoRequestDTO();
        dto.setId(endereco.getId());
        dto.setLogradouro(endereco.getLogradouro());
        dto.setComplemento(endereco.getComplemento());
        dto.setBairro(endereco.getBairro());
        dto.setCep(endereco.getCep());

        model.addAttribute("enderecoDTO", dto);
        model.addAttribute("isEdit", true);
        return "enderecos-form";
    }

    @PostMapping("/salvar")
    public String salvarEndereco(@ModelAttribute EnderecoRequestDTO dto,
                                 @RequestParam(required = false) boolean isEdit,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (isEdit && dto.getId() != null) {
                service.update(dto.getId(), dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Endereço atualizado com sucesso!");
            } else {
                service.create(dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Endereço criado com sucesso!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/enderecos";
    }

    @GetMapping("/deletar/{id}")
    public String deletarEndereco(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Endereço excluído com sucesso!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível excluir: Este endereço está vinculado a uma Delegacia ou Pessoa.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir: " + e.getMessage());
        }
        return "redirect:/enderecos";
    }
}