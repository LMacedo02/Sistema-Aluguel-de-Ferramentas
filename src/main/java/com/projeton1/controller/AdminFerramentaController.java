package com.projeton1.controller;

import com.projeton1.model.Ferramenta;
import com.projeton1.service.FerramentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/ferramentas")
public class AdminFerramentaController {

    private final FerramentaService ferramentaService;

    public AdminFerramentaController(FerramentaService ferramentaService) {
        this.ferramentaService = ferramentaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ferramentas", ferramentaService.listarTodas());
        return "admin-ferramentas";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("ferramenta", new Ferramenta());
        return "form-ferramenta";
    }

@PostMapping("/salvar")
public String salvar(@ModelAttribute Ferramenta ferramenta) {
    String url = ferramenta.getImagemUrl();
    
    // Se o usuário digitou apenas "furadeira.jpg", nós transformamos em "/img/furadeira.jpg"
    if (url != null && !url.isEmpty() && !url.startsWith("/img/") && !url.startsWith("http" )) {
        ferramenta.setImagemUrl("/img/" + url);
    }
    
    ferramentaService.salvar(ferramenta);
    return "redirect:/admin/ferramentas?sucesso";
}


    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        ferramentaService.buscarPorId(id).ifPresent(f -> model.addAttribute("ferramenta", f));
        return "form-ferramenta";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        ferramentaService.excluir(id);
        return "redirect:/admin/ferramentas";
    }
}
