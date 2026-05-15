package com.projeton1.controller;

import com.projeton1.service.FerramentaService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CatalogoController {

    private final FerramentaService ferramentaService;

    public CatalogoController(FerramentaService ferramentaService) {
        this.ferramentaService = ferramentaService;
    }

    @GetMapping("/catalogo")
    public String listar(Model model) {
        model.addAttribute("ferramentas", ferramentaService.listarAtivas());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            model.addAttribute("isLogado", true);
            model.addAttribute("usuarioLogado", auth.getName());
            
            // Verifica se o usuário tem a role de ADMIN
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        } else {
            model.addAttribute("isLogado", false);
            model.addAttribute("isAdmin", false);
        }

        return "catalogo";
    }
}
