package com.projeton1.service;

import com.projeton1.model.Aluguel;
import com.projeton1.model.Usuario;
import com.projeton1.repository.AluguelRepository;
import org.springframework.stereotype.Service;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;

    public AluguelService(AluguelRepository aluguelRepository) {
        this.aluguelRepository = aluguelRepository;
    }

public Aluguel processarAluguel(Aluguel aluguel) throws Exception {
    Usuario user = aluguel.getUsuario();
    
    // Regra de limite de 3 aluguéis ativos
    long ativos = aluguelRepository.countByUsuarioAndStatus(user, "PENDENTE");
    if (ativos >= 3) {
        throw new Exception("Você já possui 3 aluguéis pendentes. Finalize os anteriores primeiro.");
    }

    // NOVA REGRA DE CÁLCULO COM DESCONTO DE 5%
    double subtotal = aluguel.getDias() * aluguel.getFerramenta().getPrecoDiaria();
    double valorFinal = subtotal;

    if (aluguel.getDias() >= 5) {
        valorFinal = subtotal * 0.95; // Aplica 5% de desconto
    }

    aluguel.setValorTotal(valorFinal);
    return aluguelRepository.save(aluguel);
}

}
