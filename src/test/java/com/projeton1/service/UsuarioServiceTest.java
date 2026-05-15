package com.projeton1.service;

import com.projeton1.model.Usuario;
import com.projeton1.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    void naoDeveCadastrarUsuarioMenorDeIdade() {
        Usuario jovem = new Usuario();
        jovem.setNome("Menor de Idade");
        jovem.setDataNascimento(LocalDate.now().minusYears(15)); // 15 anos
        jovem.setSenha("123");

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.salvar(jovem);
        });

        assertEquals("Você precisa ter pelo menos 18 anos para se cadastrar.", exception.getMessage());
    }

    @Test
    void devePermitirCadastrarUsuarioMaiorDeIdade() {
        Usuario adulto = new Usuario();
        adulto.setNome("Maior de Idade");
        adulto.setDataNascimento(LocalDate.now().minusYears(20)); // 20 anos
        adulto.setSenha("123");

        assertDoesNotThrow(() -> {
            usuarioService.salvar(adulto);
        });
    }
}
