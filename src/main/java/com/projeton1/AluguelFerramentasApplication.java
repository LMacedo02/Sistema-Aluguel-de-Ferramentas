package com.projeton1;

import com.projeton1.model.Usuario;
import com.projeton1.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class AluguelFerramentasApplication {

    public static void main(String[] args) {
        SpringApplication.run(AluguelFerramentasApplication.class, args);
    }

    @Bean
    CommandLineRunner criarAdmin(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Em vez de deleteAll(), apenas verificamos se o admin já existe
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setUsername("admin");
                admin.setSenha(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                admin.setDataNascimento(LocalDate.of(1990, 1, 1));
                admin.setAtivo(true);
                
                usuarioRepository.save(admin);
                System.out.println(">>> Usuário ADMIN criado com sucesso (admin / admin123)");
            } else {
                System.out.println(">>> Usuário ADMIN já existe no banco.");
            }
        };
    }
}
