package com.projeton1.repository;

import com.projeton1.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Este é o método que permite buscar o usuário pelo login
    Optional<Usuario> findByUsername(String username);
}
