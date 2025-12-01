package com.policia.departamentopolicial.controller;

import com.policia.departamentopolicial.dto.EnderecoRequestDTO;
import com.policia.departamentopolicial.dto.OcorrenciaRequestDTO;
import com.policia.departamentopolicial.dto.PessoaRequestDTO;
import com.policia.departamentopolicial.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class WebController {

    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private EnderecoService enderecoService;
    @Autowired
    private PolicialService policialService;
    @Autowired
    private DelegaciaService delegaciaService;
    @Autowired
    private OcorrenciaService ocorrenciaService;
    @Autowired
    private CasoService casoService;
    @Autowired
    private EvidenciaService evidenciaService;
    @Autowired
    private RelatorioService relatorioService;
    @Autowired
    private DepoimentoService depoimentoService;
    @Autowired
    private ViewService viewService;
    @Autowired
    private ProceduralService proceduralService;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    // Método utilitário para converter uma lista de DTOs/Entidades em uma lista de Mapas,
    // facilitando a renderização genérica da tabela no Thymeleaf.
    private <T> List<Map<String, Object>> convertToMapList(List<T> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> mapList = new ArrayList<>();
        Class<?> clazz = dtoList.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (T dto : dtoList) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(dto);
                    // Lógica simplificada para exibir objetos aninhados (como o Endereco no DelegaciaResponseDTO)
                    if (value != null && value.getClass().getPackageName().startsWith("com.policia.departamentopolicial.entity")) {
                        map.put(field.getName(), value.getClass().getSimpleName() + " (ID: " + getEntityId(value) + ")");
                    } else {
                        map.put(field.getName(), value);
                    }
                } catch (IllegalAccessException e) {
                    map.put(field.getName(), "Erro");
                }
            }
            mapList.add(map);
        }
        return mapList;
    }

    private Integer getEntityId(Object entity) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            return (Integer) idField.get(entity);
        } catch (Exception e) {
            try {
                Field idField = entity.getClass().getDeclaredField("numDistintivo");
                idField.setAccessible(true);
                return (Integer) idField.get(entity);
            } catch (Exception ex) {
                return null;
            }
        }
    }


    // Endpoints para Visualização de Listas

    @GetMapping("/register/pessoa")
    public String registerPessoaForm(Model model) {
        PessoaRequestDTO pessoaDto = new PessoaRequestDTO();
        pessoaDto.setEndereco(new EnderecoRequestDTO());

        model.addAttribute("pessoaRequest", pessoaDto);
        model.addAttribute("statusMessage", model.asMap().get("statusMessage"));
        return "register_pessoa";
    }

    @PostMapping("/register/pessoa")
    public String registerPessoaSubmit(@Valid @ModelAttribute("pessoaRequest") PessoaRequestDTO pessoaRequest,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining("; "));

            model.addAttribute("statusMessage", "❌ Erro de validação: " + errorMessage);
            return "register_pessoa";
        }
        try {
            pessoaService.create(pessoaRequest);
            redirectAttributes.addFlashAttribute("statusMessage", "Pessoa e Endereço cadastrados com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("statusMessage", "Erro ao cadastrar Pessoa: " + e.getMessage());
            return "redirect:/register/pessoa";
        }

        return "redirect:/view/pessoas";
    }

    @GetMapping("/view/pessoas")
    public String viewPessoas(Model model) {
        List<?> dataList = pessoaService.findAll();
        List<Map<String, Object>> dataMapList = convertToMapList(dataList);

        model.addAttribute("title", "Pessoas Cadastradas");
        model.addAttribute("dataList", dataMapList);

        if (!dataMapList.isEmpty()) {
            model.addAttribute("headers", dataMapList.get(0).keySet().stream().collect(Collectors.toList()));
        } else {
            model.addAttribute("headers", List.of("cpf", "nome", "sexo", "dataNascimento", "telefone"));
        }
        return "list";
    }

    @GetMapping("/view/enderecos")
    public String viewEnderecos(Model model) {
        List<?> dataList = enderecoService.getEnderecos();
        List<Map<String, Object>> dataMapList = convertToMapList(dataList);

        model.addAttribute("title", "Endereços Cadastrados");
        model.addAttribute("dataList", dataMapList);

        if (!dataMapList.isEmpty()) {
            model.addAttribute("headers", dataMapList.get(0).keySet().stream().collect(Collectors.toList()));
        } else {
            model.addAttribute("headers", List.of("id", "cep", "logradouro", "complemento", "bairro"));
        }
        return "list";
    }

    @GetMapping("/view/policiais")
    public String viewPoliciais(Model model) {
        List<?> dataList = policialService.getPoliciais();
        List<Map<String, Object>> dataMapList = convertToMapList(dataList);

        model.addAttribute("title", "Policiais Cadastrados");
        model.addAttribute("dataList", dataMapList);

        if (!dataMapList.isEmpty()) {
            model.addAttribute("headers", dataMapList.get(0).keySet().stream().collect(Collectors.toList()));
        } else {
            model.addAttribute("headers", List.of("numDistintivo", "pessoaCpf", "cargo", "delegaciaId"));
        }
        return "list";
    }

    @GetMapping("/view/delegacias")
    public String viewDelegacias(Model model) {
        List<?> dataList = delegaciaService.getDelegacias();
        List<Map<String, Object>> dataMapList = convertToMapList(dataList);

        model.addAttribute("title", "Delegacias Cadastradas");
        model.addAttribute("dataList", dataMapList);

        if (!dataMapList.isEmpty()) {
            model.addAttribute("headers", dataMapList.get(0).keySet().stream().collect(Collectors.toList()));
        } else {
            model.addAttribute("headers", List.of("id", "nome", "telefone", "endereco"));
        }
        return "list";
    }

    @GetMapping("/view/ocorrencias")
    public String viewOcorrencias(Model model) {
        List<?> dataList = ocorrenciaService.getOcorrencias();
        List<Map<String, Object>> dataMapList = convertToMapList(dataList);

        model.addAttribute("title", "Ocorrências Registradas");
        model.addAttribute("dataList", dataMapList);

        if (!dataMapList.isEmpty()) {
            model.addAttribute("headers", dataMapList.get(0).keySet().stream().collect(Collectors.toList()));
        } else {
            model.addAttribute("headers", List.of("numBoletim", "dataHoraRegistro", "declaranteCpf", "policialRegistroId", "descricao"));
        }
        return "list";
    }

    @GetMapping("/view/casos")
    public String viewCasos(Model model) {
        List<?> dataList = casoService.getCasos();
        List<Map<String, Object>> dataMapList = convertToMapList(dataList);

        model.addAttribute("title", "Casos em Andamento/Fechados");
        model.addAttribute("dataList", dataMapList);

        if (!dataMapList.isEmpty()) {
            model.addAttribute("headers", dataMapList.get(0).keySet().stream().collect(Collectors.toList()));
        } else {
            model.addAttribute("headers", List.of("id", "ocorrenciaNumBoletim", "policialResponsavelId", "statusCaso", "dataAbertura", "dataFechamento"));
        }
        return "list";
    }

    @GetMapping("/view/evidencias")
    public String viewEvidencias(Model model) {
        List<?> dataList = evidenciaService.getEvidencias();
        List<Map<String, Object>> dataMapList = convertToMapList(dataList);

        model.addAttribute("title", "Evidências Coletadas");
        model.addAttribute("dataList", dataMapList);

        if (!dataMapList.isEmpty()) {
            model.addAttribute("headers", dataMapList.get(0).keySet().stream().collect(Collectors.toList()));
        } else {
            model.addAttribute("headers", List.of("id", "casoId", "descricao", "localizacao", "dataColeta"));
        }
        return "list";
    }

    @GetMapping("/view/relatorios")
    public String viewRelatorios(Model model) {
        List<?> dataList = relatorioService.getRelatorios();
        List<Map<String, Object>> dataMapList = convertToMapList(dataList);

        model.addAttribute("title", "Relatórios Emitidos");
        model.addAttribute("dataList", dataMapList);

        if (!dataMapList.isEmpty()) {
            model.addAttribute("headers", dataMapList.get(0).keySet().stream().collect(Collectors.toList()));
        } else {
            model.addAttribute("headers", List.of("id", "casoId", "policialEmissorId", "conteudo"));
        }
        return "list";
    }

    @GetMapping("/view/depoimentos")
    public String viewDepoimentos(Model model) {
        List<?> dataList = depoimentoService.getDepoimentos();
        List<Map<String, Object>> dataMapList = convertToMapList(dataList);

        model.addAttribute("title", "Depoimentos Registrados");
        model.addAttribute("dataList", dataMapList);

        if (!dataMapList.isEmpty()) {
            model.addAttribute("headers", dataMapList.get(0).keySet().stream().collect(Collectors.toList()));
        } else {
            model.addAttribute("headers", List.of("id", "casoId", "pessoaCpf", "dataHoraDepoimento", "conteudoDepoimento"));
        }
        return "list";
    }

    @GetMapping("/view/casos-ativos")
    public String listCasosAtivos(Model model) {
        List<Map<String, Object>> casosAtivos = viewService.getCasosAtivosDetails();

        if (!casosAtivos.isEmpty()) {
            Set<String> headers = casosAtivos.get(0).keySet();
            model.addAttribute("headers", headers);
            model.addAttribute("dataList", casosAtivos);
        } else {
            model.addAttribute("dataList", Collections.emptyList());
            model.addAttribute("headers", Arrays.asList("ID_Caso", "Status", "Nome_Policial_Responsavel", "Nome_Declarante")); // Headers de fallback
        }

        model.addAttribute("title", "Casos Ativos (VIEW)");
        return "list";
    }

    @GetMapping("/procedure/encerrar-caso")
    public String showEncerrarCasoForm() {
        return "encerrar-caso";
    }

    @PostMapping("/procedure/encerrar-caso")
    public String encerrarCaso(@RequestParam("idCaso") Integer idCaso,
                               @RequestParam("conteudoRelatorio") String conteudoRelatorio,
                               RedirectAttributes redirectAttributes) {
        try {
            proceduralService.executeEncerrarCaso(idCaso, conteudoRelatorio);
            redirectAttributes.addFlashAttribute("statusMessage", "✅ Sucesso! Caso " + idCaso + " encerrado e relatório final criado.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("statusMessage", "❌ Erro: " + e.getMessage());
        }

        return "redirect:/procedure/encerrar-caso";
    }
}