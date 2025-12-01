package com.policia.departamentopolicial.controller;

// Exemplo no seu Controller (assumindo que seja o mesmo que lida com Pessoa)

import com.policia.departamentopolicial.dto.OcorrenciaRequestDTO;
import com.policia.departamentopolicial.service.OcorrenciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private OcorrenciaService ocorrenciaService; // Serviço para salvar a ocorrência

    @GetMapping("/ocorrencia")
    public String showOcorrenciaForm(Model model) {
        model.addAttribute("ocorrenciaRequest", new OcorrenciaRequestDTO());
        return "register_ocorrencia";
    }

    // Mapeia para processar o formulário (POST)
    @PostMapping("/ocorrencia")
    public String registerOcorrencia(@Valid @ModelAttribute("ocorrenciaRequest") OcorrenciaRequestDTO dto,
                                     RedirectAttributes redirectAttributes) {
        try {
            ocorrenciaService.create(dto);
            redirectAttributes.addFlashAttribute("statusMessage", "✅ Sucesso! Ocorrência registrada com sucesso. (Num. Boletim: " + dto.getNumBoletim() + ")");
        } catch (ResponseStatusException e) {
            // Captura exceções do Spring, como NOT_FOUND se o Policial ou Declarante não existir
            redirectAttributes.addFlashAttribute("statusMessage", "❌ Erro ao registrar: " + e.getReason());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Captura o erro SQL/Trigger (se o SGBD retornar a mensagem de erro)
            redirectAttributes.addFlashAttribute("statusMessage", "❌ Erro de Banco de Dados: O CPF do declarante pode não existir (TRIGGER FAILED).");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("statusMessage", "❌ Erro inesperado: " + e.getMessage());
        }

        return "redirect:/register/ocorrencia";
    }
}
