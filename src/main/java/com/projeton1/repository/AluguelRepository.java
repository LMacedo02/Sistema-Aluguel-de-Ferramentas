package com.projeton1.repository;

import com.projeton1.model.Aluguel;
import com.projeton1.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    List<Aluguel> findByUsuario(Usuario usuario);
    long countByUsuarioAndStatus(Usuario usuario, String status);
}
