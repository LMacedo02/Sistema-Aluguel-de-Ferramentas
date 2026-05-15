package com.projeton1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.projeton1.model.Usuario;
import com.projeton1.repository.UsuarioRepository;

@SpringBootApplication
public class AluguelFerramentasApplication {

    public static void main(String[] args) {
        SpringApplication.run(AluguelFerramentasApplication.class, args);
    }

    @Bean
    CommandLineRunner criarAdmin(
            UsuarioRepository repo,
            PasswordEncoder encoder) {

        return args -> {

            repo.deleteAll();

            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setUsername("admin");
            admin.setSenha(encoder.encode("123"));
            admin.setRole("ADMIN");
            admin.setAtivo(true);

            repo.save(admin);

            System.out.println(">>> Usuário ADMIN criado com sucesso");
        };
    }
}