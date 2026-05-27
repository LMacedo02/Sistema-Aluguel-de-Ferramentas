package com.projeton1.service;

import com.projeton1.model.Aluguel;
import com.projeton1.model.Usuario;
import com.projeton1.repository.AluguelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;

    public AluguelService(AluguelRepository aluguelRepository) {
        this.aluguelRepository = aluguelRepository;
    }

    @Transactional
    public Aluguel processarAluguel(Aluguel aluguel) throws Exception {
        long ativos = aluguelRepository.countByUsuarioAndStatus(aluguel.getUsuario(), "PENDENTE");
        if (ativos >= 3) {
            throw new Exception("Você já possui 3 aluguéis pendentes. Devolva algum para continuar.");
        }
        return aluguelRepository.save(aluguel);
    }

    // MÉTODO CORRIGIDO PARA COMBINAR COM O REPOSITORY:
    public List<Aluguel> listarPorUsuario(Usuario usuario) {
        return aluguelRepository.findByUsuarioOrderByIdDesc(usuario);
    }

    public void finalizarAluguel(Long id) {
        aluguelRepository.findById(id).ifPresent(a -> {
            a.setStatus("FINALIZADO");
            aluguelRepository.save(a);
        });
    }
}
