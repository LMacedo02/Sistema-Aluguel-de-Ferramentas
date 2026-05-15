package com.projeton1.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        // Pega as informações de quem está logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Se o usuário não estiver logado, o Spring Security coloca "anonymousUser"
        if (auth != null && auth.isAuthenticated() && !username.equals("anonymousUser")) {
            model.addAttribute("usuarioLogado", username);
            model.addAttribute("isLogado", true);
        } else {
            model.addAttribute("isLogado", false);
        }

        return "home";
    }
}
