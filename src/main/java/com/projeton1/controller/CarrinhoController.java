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
        List<ItemCarrinho> lista = (List<ItemCarrinho>) session.getAttribute("carrinho");
        if (lista == null) {
            lista = new ArrayList<>();
            session.setAttribute("carrinho", lista);
        }
        
        final List<ItemCarrinho> listaFinal = lista;
        ferramentaService.buscarPorId(idFerramenta).ifPresent(f -> {
            listaFinal.add(new ItemCarrinho(f, dias));
        });
        
        return acao.equals("finalizar") ? "redirect:/carrinho" : "redirect:/catalogo";
    }

    @GetMapping
    public String exibirCarrinho(HttpSession session, Model model) {
        List<ItemCarrinho> carrinho = (List<ItemCarrinho>) session.getAttribute("carrinho");
        if (carrinho == null) carrinho = new ArrayList<>();
        
        double subtotalGeral = carrinho.stream().mapToDouble(ItemCarrinho::getSubtotalBruto).sum();
        double descontoGeral = carrinho.stream().mapToDouble(ItemCarrinho::getValorDesconto).sum();
        double totalGeral = subtotalGeral - descontoGeral;

        model.addAttribute("itens", carrinho);
        model.addAttribute("subtotalGeral", subtotalGeral);
        model.addAttribute("descontoGeral", descontoGeral);
        model.addAttribute("totalGeral", totalGeral);
        
        return "carrinho";
    }

    @PostMapping("/finalizar")
    public String irParaPagamento(HttpSession session) {
        List<ItemCarrinho> carrinho = (List<ItemCarrinho>) session.getAttribute("carrinho");
        if (carrinho == null || carrinho.isEmpty()) return "redirect:/catalogo";
        
        double totalGeral = carrinho.stream().mapToDouble(ItemCarrinho::getSubtotalLiquido).sum();
        session.setAttribute("totalOriginal", totalGeral);
        
        return "redirect:/carrinho/pagamento";
    }

    @GetMapping("/pagamento")
    public String telaPagamento(HttpSession session, Model model) {
        Double total = (Double) session.getAttribute("totalOriginal");
        if (total == null) return "redirect:/catalogo";
        
        model.addAttribute("total", total);
        return "pagamento";
    }

        @PostMapping("/pagamento/concluir")
    public String concluirCompra(@RequestParam String metodo, HttpSession session, RedirectAttributes ra) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<ItemCarrinho> carrinho = (List<ItemCarrinho>) session.getAttribute("carrinho");
        Usuario usuario = usuarioService.findByUsername(auth.getName()).orElse(null);

        if (carrinho != null && usuario != null) {
            try {
                for (ItemCarrinho item : carrinho) {
                    Aluguel aluguel = new Aluguel();
                    aluguel.setUsuario(usuario);
                    aluguel.setFerramenta(item.getFerramenta());
                    aluguel.setDias(item.getDias());
                    aluguel.setDataAluguel(java.time.LocalDate.now());
                    aluguel.setStatus("PENDENTE");

                    // CÁLCULO FINAL:
                    double valor = item.getSubtotalLiquido(); // Já tem o desconto de 5+ dias
                    if ("pix".equals(metodo)) {
                        valor = valor * 0.95; // Aplica +5% se for PIX
                    }
                    aluguel.setValorTotal(valor);

                    aluguelService.processarAluguel(aluguel);
                }
                session.removeAttribute("carrinho");
                ra.addFlashAttribute("sucesso", "Compra realizada com sucesso, muito obrigado!");
                return "redirect:/catalogo";
            } catch (Exception e) {
                ra.addFlashAttribute("erro", "Erro: " + e.getMessage());
                return "redirect:/carrinho/pagamento";
            }
        }
        return "redirect:/catalogo";
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
