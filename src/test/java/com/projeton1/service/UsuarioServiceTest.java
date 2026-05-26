package com.projeton1.service;

import com.projeton1.model.Usuario;
import com.projeton1.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void naoDeveCadastrarUsuarioMenorDeIdade() {
        Usuario jovem = new Usuario();
        jovem.setNome("Menor de Idade");
        jovem.setDataNascimento(LocalDate.now().minusYears(15)); // 15 anos
        jovem.setSenha("123");

        // Não precisamos mockar o save aqui, pois esperamos uma exceção antes de salvar
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.salvar(jovem);
        });

        assertEquals("Você precisa ter pelo menos 18 anos para se cadastrar.", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class)); // Verifica que save nunca foi chamado
    }

    @Test
    void devePermitirCadastrarUsuarioMaiorDeIdade() {
        Usuario adulto = new Usuario();
        adulto.setNome("Maior de Idade");
        adulto.setDataNascimento(LocalDate.now().minusYears(20)); // 20 anos
        adulto.setSenha("123");

        // Quando o save for chamado, retorne o próprio usuário (simulando o comportamento do JPA)
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(adulto);

        assertDoesNotThrow(() -> {
            usuarioService.salvar(adulto);
        });

        verify(usuarioRepository, times(1)).save(any(Usuario.class)); // Verifica que save foi chamado uma vez
    }
}
