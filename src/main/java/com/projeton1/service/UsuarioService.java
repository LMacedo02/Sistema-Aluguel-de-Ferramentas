package com.projeton1.service;

import com.projeton1.model.Usuario;
import com.projeton1.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario salvar(Usuario usuario) {
        // Lógica de validação de idade
        if (usuario.getDataNascimento() != null) {
            int idade = Period.between(usuario.getDataNascimento(), LocalDate.now()).getYears();
            if (idade < 18) {
                throw new IllegalArgumentException("Você precisa ter pelo menos 18 anos para se cadastrar.");
            }
        }
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Outros métodos do serviço, se houver
}
