package com.projeton1.controller;

import com.projeton1.model.Aluguel;
import com.projeton1.model.Ferramenta;
import com.projeton1.model.ItemCarrinho;
import com.projeton1.model.Usuario;
import com.projeton1.service.AluguelService;
import com.projeton1.service.FerramentaService;
import com.projeton1.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrinho" )
public class CarrinhoController {

    private final FerramentaService ferramentaService;
    private final AluguelService aluguelService;
    private final UsuarioService usuarioService;

    public CarrinhoController(FerramentaService ferramentaService, AluguelService aluguelService, UsuarioService usuarioService) {
        this.ferramentaService = ferramentaService;
        this.aluguelService = aluguelService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/adicionar")
    public String adicionar(@RequestParam Long idFerramenta, @RequestParam Integer dias, 
                            @RequestParam String acao, HttpSession session) {
        List<ItemCarrinho> carrinho = (List<ItemCarrinho>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
            session.setAttribute("carrinho", carrinho);
        }
        
        // Cria uma variável final para usar na lambda
        final List<ItemCarrinho> carrinhoFinal = carrinho;
        
        ferramentaService.buscarPorId(idFerramenta).ifPresent(f -> {
            carrinhoFinal.add(new ItemCarrinho(f, dias));
        });
        
        return acao.equals("finalizar") ? "redirect:/carrinho" : "redirect:/catalogo";
    }

    @GetMapping
    public String exibirCarrinho(HttpSession session, Model model) {
        List<ItemCarrinho> carrinho = (List<ItemCarrinho>) session.getAttribute("carrinho");
        if (carrinho == null) carrinho = new ArrayList<>();
        double total = carrinho.stream().mapToDouble(ItemCarrinho::getSubtotal).sum();
        model.addAttribute("itens", carrinho);
        model.addAttribute("totalGeral", total);
        return "carrinho";
    }

    @PostMapping("/finalizar")
    public String finalizar(HttpSession session, RedirectAttributes ra) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<ItemCarrinho> carrinho = (List<ItemCarrinho>) session.getAttribute("carrinho");
        Usuario usuario = usuarioService.findByUsername(auth.getName()).orElse(null);

        if (carrinho == null || carrinho.isEmpty() || usuario == null) {
            return "redirect:/catalogo";
        }

        try {
            for (ItemCarrinho item : carrinho) {
                Aluguel aluguel = new Aluguel();
                aluguel.setUsuario(usuario);
                aluguel.setFerramenta(item.getFerramenta());
                aluguel.setDias(item.getDias());
                aluguelService.processarAluguel(aluguel);
            }
            session.removeAttribute("carrinho");
            ra.addFlashAttribute("sucesso", "Compra finalizada com sucesso! Seus aluguéis foram registrados.");
            return "redirect:/catalogo";
        } catch (Exception e) {
            ra.addFlashAttribute("erro", "Erro ao finalizar: " + e.getMessage());
            return "redirect:/carrinho";
        }
    }

    @GetMapping("/remover/{index}")
    public String remover(@PathVariable int index, HttpSession session) {
        List<ItemCarrinho> carrinho = (List<ItemCarrinho>) session.getAttribute("carrinho");
        if (carrinho != null && index < carrinho.size()) {
            carrinho.remove(index);
        }
        return "redirect:/carrinho";
    }
}
