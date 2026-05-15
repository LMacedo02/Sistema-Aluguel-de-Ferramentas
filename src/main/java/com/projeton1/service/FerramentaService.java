package com.projeton1.service;

import com.projeton1.model.Ferramenta;
import com.projeton1.repository.FerramentaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FerramentaService {

    private final FerramentaRepository ferramentaRepository;

    public FerramentaService(FerramentaRepository ferramentaRepository) {
        this.ferramentaRepository = ferramentaRepository;
    }

    // Método que o erro de compilação estava pedindo:
    public List<Ferramenta> listarAtivas() {
        return ferramentaRepository.findByAtivaTrue();
    }

    public Ferramenta salvar(Ferramenta ferramenta) {
        ferramenta.setAtiva(true);
        return ferramentaRepository.save(ferramenta);
    }

    public List<Ferramenta> listarTodas() {
        return ferramentaRepository.findAll();
    }

    public Optional<Ferramenta> buscarPorId(Long id) {
        return ferramentaRepository.findById(id);
    }

    public void excluir(Long id) {
        ferramentaRepository.deleteById(id);
    }

    @PostConstruct
    public void criarFerramentasExemplo() {
        if (ferramentaRepository.count() == 0) {
            // 1. Furadeira
            Ferramenta f1 = new Ferramenta();
            f1.setNome("Furadeira de Impacto");
            f1.setDescricao("Bosch Professional 500W com maleta");
            f1.setPrecoDiaria(35.0);
            f1.setImagemUrl("/img/furadeira.jpg");
            f1.setAtiva(true);

            // 2. Serra Circular
            Ferramenta f2 = new Ferramenta();
            f2.setNome("Serra Circular");
            f2.setDescricao("DeWalt 1800W para cortes precisos em madeira");
            f2.setPrecoDiaria(55.0);
            f2.setImagemUrl("/img/serra.jpg");
            f2.setAtiva(true);

            // 3. Lixadeira
            Ferramenta f3 = new Ferramenta();
            f3.setNome("Lixadeira Orbital");
            f3.setDescricao("Makita ideal para acabamentos finos");
            f3.setPrecoDiaria(25.0);
            f3.setImagemUrl("/img/lixadeira.jpg");
            f3.setAtiva(true);

            // 4. Martelete
            Ferramenta f4 = new Ferramenta();
            f4.setNome("Martelete Perfurador");
            f4.setDescricao("Martelete SDS-Plus para concreto pesado");
            f4.setPrecoDiaria(70.0);
            f4.setImagemUrl("/img/martelete.jpg");
            f4.setAtiva(true);

            ferramentaRepository.save(f1);
            ferramentaRepository.save(f2);
            ferramentaRepository.save(f3);
            ferramentaRepository.save(f4);
            
            System.out.println(">>> 4 Ferramentas criadas com sucesso!");
        }
    }
}
