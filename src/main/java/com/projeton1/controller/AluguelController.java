package com.projeton1.controller;

import com.projeton1.model.Aluguel;
import com.projeton1.model.Ferramenta;
import com.projeton1.model.Usuario;
import com.projeton1.service.AluguelService;
import com.projeton1.service.FerramentaService;
import com.projeton1.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AluguelController {

    private final FerramentaService ferramentaService;
    private final UsuarioService usuarioService;
    private final AluguelService aluguelService;

    public AluguelController(FerramentaService ferramentaService, UsuarioService usuarioService, AluguelService aluguelService) {
        this.ferramentaService = ferramentaService;
        this.usuarioService = usuarioService;
        this.aluguelService = aluguelService;
    }

    // AGORA A ROTA COMBINA COM O BOTÃO DO CATÁLOGO!
    @GetMapping("/checkout/{id}")
    public String checkout(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            ra.addFlashAttribute("erro", "Você precisa estar logado para alugar uma ferramenta.");
            return "redirect:/login";
        }

        Optional<Ferramenta> ferramentaOpt = ferramentaService.buscarPorId(id);
        if (ferramentaOpt.isEmpty()) {
            ra.addFlashAttribute("erro", "Ferramenta não encontrada.");
            return "redirect:/catalogo";
        }

        model.addAttribute("ferramenta", ferramentaOpt.get());
        return "checkout";
    }

    @PostMapping("/alugar/confirmar")
    public String confirmarAluguel(@RequestParam Long idFerramenta, @RequestParam Integer dias, Model model, RedirectAttributes ra) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Usuario> usuarioOpt = usuarioService.findByUsername(auth.getName());
        Optional<Ferramenta> ferramentaOpt = ferramentaService.buscarPorId(idFerramenta);

        if (usuarioOpt.isPresent() && ferramentaOpt.isPresent()) {
            Aluguel aluguel = new Aluguel();
            aluguel.setUsuario(usuarioOpt.get());
            aluguel.setFerramenta(ferramentaOpt.get());
            aluguel.setDias(dias);

            try {
                aluguelService.processarAluguel(aluguel);
                model.addAttribute("aluguel", aluguel);
                return "redirect:/catalogo"; // Redireciona de volta com sucesso
            } catch (Exception e) {
                ra.addFlashAttribute("erro", e.getMessage());
                return "redirect:/checkout/" + idFerramenta;
            }
        }
        return "redirect:/catalogo";
    }
        @GetMapping("/meus-alugueis")
    public String meusAlugueis(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        usuarioService.findByUsername(auth.getName()).ifPresent(u -> {
            model.addAttribute("alugueis", aluguelService.listarPorUsuario(u));
        });
        return "meus-alugueis";
    }

    @PostMapping("/devolver/{id}")
    public String devolver(@PathVariable Long id) {
        aluguelService.finalizarAluguel(id);
        return "redirect:/meus-alugueis?sucesso";
    }

}
