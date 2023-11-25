package com.upao.adventure.Services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import com.upao.adventure.dto.SignUpDTO;
import com.upao.adventure.exception.ContrasenaInconrrectaException;
import com.upao.adventure.exception.EntidadNoEncontradaException;
import com.upao.adventure.exception.UsuarioYaExisteException;
import com.upao.adventure.entity.Pais;
import com.upao.adventure.entity.Usuario;
import com.upao.adventure.repository.PaisRepository;
import com.upao.adventure.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.Optional;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class UsuarioServicesTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PaisRepository paisRepository;

    @InjectMocks
    private UsuarioServices usuarioService;

    private SignUpDTO signUpDTO;
    private Usuario usuario;
    private Pais paisOrigen;
    private Pais paisVive;

    @BeforeEach
    void setUp() {
        // Inicializar datos de prueba
        signUpDTO = new SignUpDTO(
                "Juan", "Perez", "MASCULINO", LocalDate.of(1990, 1, 1),
                "Peru", "Peru", "Desc", "juan@example.com", "pass123", "Mochileros",
                "http://link-to-profile", "http://facebook.com/juan",
                "http://twitter.com/juan", "http://instagram.com/juan"
        );

        usuario = new Usuario();
        paisOrigen = new Pais("PE", "Peru");
        paisVive = new Pais("PE", "Peru");
        usuario.setId(1L); // Suponiendo que el ID es un Long
        usuario.setCorreo("usuario@test.com");
        usuario.setContrasena("contrasenaCorrecta");

        // Configurar el comportamiento simulado (mock)
        when(paisRepository.findByNombreIgnoreCase("Peru")).thenReturn(Optional.of(paisOrigen));
        when(paisRepository.findByNombreIgnoreCase("Peru")).thenReturn(Optional.of(paisVive));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
    }


    ///////****REGISTRAR USUARIO**////////

    @Test
    void cuandoSeRegistraNuevoUsuarioExitosamente() {
        // Configurar más comportamiento simulado si es necesario

        // Act
        Usuario result = usuarioService.registrarUsuario(signUpDTO);

        // Assert
        assertNotNull(result);
        verify(usuarioRepository).save(any(Usuario.class));

    }

    @Test
    void cuandoElCorreoYaExisteDeberiaLanzarExcepcion() {
        // Configurar el comportamiento simulado para cuando el correo ya existe
        when(usuarioRepository.findByCorreo(signUpDTO.getCorreo())).thenReturn(Optional.of(new Usuario()));

        Exception exception = assertThrows(UsuarioYaExisteException.class, () -> {
            usuarioService.registrarUsuario(signUpDTO);
        });

        String expectedMessage = "El correo indicado ya está registrado";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    ///////****INICIAR SESIÓN**////////

    @Test
    void inicioDeSesionExitoso() {

        when(usuarioRepository.findByCorreo("usuario@test.com")).thenReturn(Optional.of(usuario));

        Optional<String> resultado = usuarioService.validarUsuario("usuario@test.com", "contrasenaCorrecta");

        assertFalse(resultado.isPresent());

        verify(usuarioRepository).findByCorreo("usuario@test.com");
    }


    @Test
    void cuandoCorreoNoExiste() {
        // Dado (Given)
        String correoInexistente = "correo@inexistente.com";
        when(usuarioRepository.findByCorreo(correoInexistente)).thenReturn(Optional.empty());

        EntidadNoEncontradaException thrown = assertThrows(
                EntidadNoEncontradaException.class,
                () -> usuarioService.validarUsuario(correoInexistente, "contrasenaCualquiera"),
                "Se esperaba que validarUsuario lanzara una EntidadNoEncontradaException"
        );

        assertEquals("Correo no encontrado.", thrown.getMessage());
    }


    @Test
    void cuandoContrasenaEsIncorrecta() {

        String correoExistente = "usuario@ejemplo.com";
        String contrasenaIncorrecta = "contrasenaErronea";
        Usuario usuarioMock = mock(Usuario.class);
        when(usuarioMock.getContrasena()).thenReturn("contrasenaCorrecta");
        when(usuarioRepository.findByCorreo(correoExistente)).thenReturn(Optional.of(usuarioMock));


        ContrasenaInconrrectaException thrown = assertThrows(
                ContrasenaInconrrectaException.class,
                () -> usuarioService.validarUsuario(correoExistente, contrasenaIncorrecta),
                "Se esperaba que validarUsuario lanzara una ContrasenaIncorrectaException"
        );


        assertEquals("Contraseña incorrecta.", thrown.getMessage());
    }

}