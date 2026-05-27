package com.projeton1.service;

import com.projeton1.model.Usuario;
import com.projeton1.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // MÉTODO QUE ESTAVA FALTANDO E CAUSOU O ERRO:
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Usuario salvar(Usuario usuario) {
        // 1. Validação de Idade (Regra de Negócio)
        if (usuario.getDataNascimento() != null) {
            int idade = Period.between(usuario.getDataNascimento(), LocalDate.now()).getYears();
            if (idade < 18) {
                throw new IllegalArgumentException("Você precisa ter pelo menos 18 anos para se cadastrar.");
            }
        }

        // 2. Encriptar a senha (Segurança para o login funcionar)
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        
        // 3. Definir papel padrão
        if (usuario.getRole() == null || usuario.getRole().isEmpty()) {
            usuario.setRole("USER");
        }

        // 4. Salvar no Banco
        return usuarioRepository.save(usuario);
    }
}
