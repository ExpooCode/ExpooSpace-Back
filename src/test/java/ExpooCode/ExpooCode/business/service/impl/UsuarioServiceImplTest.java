package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.UsuarioDTO;
import ExpooCode.ExpooCode.persistence.entity.Reserva;
import ExpooCode.ExpooCode.persistence.entity.Usuario;
import ExpooCode.ExpooCode.persistence.enums.EstadoUsuario;
import ExpooCode.ExpooCode.persistence.enums.RolUsuario;
import ExpooCode.ExpooCode.persistence.mapper.UsuarioMapper;
import ExpooCode.ExpooCode.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para UsuarioServiceImpl")
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    private List<Usuario> usuarioList;
    private List<UsuarioDTO> usuarioDTOList;

    @BeforeEach
    public void setUp() {
        // Given - Preparar datos de prueba
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan.perez@email.com");
        usuario.setPassword("Password123");
        usuario.setRol(RolUsuario.Afiliado);
        usuario.setEstado(EstadoUsuario.Activo);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(1L);
        usuarioDTO.setNombre("Juan Pérez");
        usuarioDTO.setEmail("juan.perez@email.com");
        usuarioDTO.setPassword("Password123");
        usuarioDTO.setRol(RolUsuario.Afiliado);
        usuarioDTO.setEstado(EstadoUsuario.Activo);

        usuarioList = Arrays.asList(usuario);
        usuarioDTOList = Arrays.asList(usuarioDTO);
    }

    // ==================== GET ALL USUARIOS ====================

    @Test
    @DisplayName("getAllUsuarios - Debe retornar lista de usuarios exitosamente")
    public void getAllUsuarios_DebeRetornarListaDeUsuarios() {
        // Given
        when(usuarioRepository.findAll()).thenReturn(usuarioList);
        when(usuarioMapper.toDTOList(usuarioList)).thenReturn(usuarioDTOList);

        // When
        List<UsuarioDTO> resultado = usuarioService.getAllUsuarios();

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEmail()).isEqualTo("juan.perez@email.com");
        verify(usuarioRepository).findAll();
        verify(usuarioMapper).toDTOList(usuarioList);
    }

    @Test
    @DisplayName("getAllUsuarios - Debe retornar lista vacía cuando no hay usuarios")
    public void getAllUsuarios_DebeRetornarListaVaciaCuandoNoHayUsuarios() {
        // Given
        when(usuarioRepository.findAll()).thenReturn(new ArrayList<>());
        when(usuarioMapper.toDTOList(anyList())).thenReturn(new ArrayList<>());

        // When
        List<UsuarioDTO> resultado = usuarioService.getAllUsuarios();

        // Then
        assertThat(resultado).isEmpty();
        verify(usuarioRepository).findAll();
    }

    // ==================== CREATE USUARIO ====================

    @Test
    @DisplayName("createUsuario - Debe crear usuario exitosamente")
    public void createUsuario_DebeCrearUsuarioExitosamente() {
        // Given
        when(usuarioRepository.existsByEmail(usuarioDTO.getEmail())).thenReturn(false);
        when(usuarioMapper.toEntity(usuarioDTO)).thenReturn(usuario);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        // When
        UsuarioDTO resultado = usuarioService.createUsuario(usuarioDTO);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEmail()).isEqualTo("juan.perez@email.com");
        verify(usuarioRepository).existsByEmail(usuarioDTO.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioMapper).toEntity(usuarioDTO);
        verify(usuarioMapper).toDTO(usuario);
    }

    @Test
    @DisplayName("createUsuario - Debe lanzar excepción cuando el email ya existe")
    public void createUsuario_DebeLanzarConflictoCuandoEmailExiste() {
        // Given
        when(usuarioRepository.existsByEmail(usuarioDTO.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> usuarioService.createUsuario(usuarioDTO))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Ya existe un usuario con el email")
                .extracting("status")
                .isEqualTo(HttpStatus.CONFLICT);

        verify(usuarioRepository).existsByEmail(usuarioDTO.getEmail());
        verify(usuarioRepository, never()).save(any());
    }

    // ==================== GET USUARIO BY ID ====================

    @Test
    @DisplayName("getUsuarioById - Debe retornar usuario por ID exitosamente")
    public void getUsuarioById_DebeRetornarUsuario() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        // When
        UsuarioDTO resultado = usuarioService.getUsuarioById(1L);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdUsuario()).isEqualTo(1L);
        assertThat(resultado.getEmail()).isEqualTo("juan.perez@email.com");
        verify(usuarioRepository).findById(1L);
        verify(usuarioMapper).toDTO(usuario);
    }

    @Test
    @DisplayName("getUsuarioById - Debe lanzar excepción cuando el ID es inválido")
    public void getUsuarioById_DebeLanzarExcepcionCuandoIdInvalido() {
        // When & Then
        assertThatThrownBy(() -> usuarioService.getUsuarioById(0L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ID inválido");

        assertThatThrownBy(() -> usuarioService.getUsuarioById(-1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ID inválido");

        verify(usuarioRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("getUsuarioById - Debe lanzar excepción cuando el usuario no existe")
    public void getUsuarioById_DebeLanzarExcepcionCuandoNoExiste() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> usuarioService.getUsuarioById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuario no encontrado")
                .extracting("status")
                .isEqualTo(HttpStatus.NOT_FOUND);

        verify(usuarioRepository).findById(99L);
    }

    // ==================== UPDATE USUARIO ====================

    @Test
    @DisplayName("updateUsuario - Debe actualizar usuario exitosamente")
    public void updateUsuario_DebeActualizarUsuarioExitosamente() {
        // Given
        UsuarioDTO updateDTO = new UsuarioDTO();
        updateDTO.setNombre("Carlos Actualizado");
        updateDTO.setEmail("carlos.nuevo@email.com");
        updateDTO.setPassword("NewPassword456");
        updateDTO.setRol(RolUsuario.Administrador);
        updateDTO.setEstado(EstadoUsuario.Activo);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(updateDTO);

        // When
        UsuarioDTO resultado = usuarioService.updateUsuario(1L, updateDTO);

        // Then
        assertThat(resultado).isNotNull();
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioMapper).toDTO(usuario);
    }

    @Test
    @DisplayName("updateUsuario - Debe lanzar excepción cuando el email es inválido")
    public void updateUsuario_DebeLanzarExcepcionCuandoEmailInvalido() {
        // Given
        usuarioDTO.setEmail("emailinvalido");

        // When & Then
        assertThatThrownBy(() -> usuarioService.updateUsuario(1L, usuarioDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email inválido");

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateUsuario - Debe lanzar excepción cuando el usuario no existe")
    public void updateUsuario_DebeLanzarExcepcionCuandoUsuarioNoExiste() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> usuarioService.updateUsuario(99L, usuarioDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(usuarioRepository).findById(99L);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateUsuario - Debe actualizar solo campos no nulos")
    public void updateUsuario_DebeActualizarSoloCamposNoNulos() {
        // Given
        UsuarioDTO partialUpdate = new UsuarioDTO();
        partialUpdate.setNombre("Solo Nombre Actualizado");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        // When
        UsuarioDTO resultado = usuarioService.updateUsuario(1L, partialUpdate);

        // Then
        assertThat(resultado).isNotNull();
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    // ==================== DELETE USUARIO ====================

    @Test
    @DisplayName("deleteUsuario - Debe eliminar usuario exitosamente")
    public void deleteUsuario_DebeEliminarUsuarioExitosamente() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        // When
        usuarioService.deleteUsuario(1L);

        // Then
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    @DisplayName("deleteUsuario - Debe lanzar excepción cuando el usuario no existe")
    public void deleteUsuario_DebeLanzarExcepcionCuandoNoExiste() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> usuarioService.deleteUsuario(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado con ID: 99");

        verify(usuarioRepository).findById(99L);
        verify(usuarioRepository, never()).delete(any());
    }

    // ==================== LOGIN ====================

    @Test
    @DisplayName("login - Debe retornar true cuando las credenciales son correctas")
    public void login_DebeRetornarTrueCuandoCredencialesSonCorrectas() {
        // Given
        when(usuarioRepository.findByEmail("juan.perez@email.com")).thenReturn(Optional.of(usuario));

        // When
        boolean resultado = usuarioService.login("juan.perez@email.com", "Password123");

        // Then
        assertThat(resultado).isTrue();
        verify(usuarioRepository).findByEmail("juan.perez@email.com");
    }

    @Test
    @DisplayName("login - Debe retornar false cuando el usuario no existe")
    public void login_DebeRetornarFalseCuandoUsuarioNoExiste() {
        // Given
        when(usuarioRepository.findByEmail("noexiste@email.com")).thenReturn(Optional.empty());

        // When
        boolean resultado = usuarioService.login("noexiste@email.com", "cualquierpassword");

        // Then
        assertThat(resultado).isFalse();
        verify(usuarioRepository).findByEmail("noexiste@email.com");
    }

    @Test
    @DisplayName("login - Debe retornar false cuando la contraseña es incorrecta")
    public void login_DebeRetornarFalseCuandoPasswordIncorrecta() {
        // Given
        when(usuarioRepository.findByEmail("juan.perez@email.com")).thenReturn(Optional.of(usuario));

        // When
        boolean resultado = usuarioService.login("juan.perez@email.com", "PasswordIncorrecta");

        // Then
        assertThat(resultado).isFalse();
        verify(usuarioRepository).findByEmail("juan.perez@email.com");
    }

    @Test
    @DisplayName("login - Debe retornar false cuando el password del usuario es null")
    public void login_DebeRetornarFalseCuandoPasswordEsNull() {
        // Given
        usuario.setPassword(null);
        when(usuarioRepository.findByEmail("juan.perez@email.com")).thenReturn(Optional.of(usuario));

        // When
        boolean resultado = usuarioService.login("juan.perez@email.com", "Password123");

        // Then
        assertThat(resultado).isFalse();
        verify(usuarioRepository).findByEmail("juan.perez@email.com");
    }

    // ==================== LOGOUT ====================

    @Test
    @DisplayName("logout - Debe ejecutarse sin errores")
    public void logout_DebeEjecutarseSinErrores() {
        // When & Then
        assertThatCode(() -> usuarioService.logout()).doesNotThrowAnyException();
    }

    // ==================== GET RESERVAS DE USUARIO ====================

    @Test
    @DisplayName("getReservasDeUsuario - Debe retornar lista de reservas exitosamente")
    public void getReservasDeUsuario_DebeRetornarListaDeReservas() {
        // Given
        Reserva reserva1 = mock(Reserva.class);
        Reserva reserva2 = mock(Reserva.class);
        when(reserva1.getIdReserva()).thenReturn(10L);
        when(reserva2.getIdReserva()).thenReturn(20L);

        List<Reserva> reservas = Arrays.asList(reserva1, reserva2);
        usuario.setReservas(reservas);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // When
        List<String> resultado = usuarioService.getReservasDeUsuario(1L);

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado).contains("Reserva id: 10", "Reserva id: 20");
        verify(usuarioRepository).findById(1L);
    }

    @Test
    @DisplayName("getReservasDeUsuario - Debe retornar lista vacía cuando no hay reservas")
    public void getReservasDeUsuario_DebeRetornarListaVaciaCuandoNoHayReservas() {
        // Given
        usuario.setReservas(null);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // When
        List<String> resultado = usuarioService.getReservasDeUsuario(1L);

        // Then
        assertThat(resultado).isEmpty();
        verify(usuarioRepository).findById(1L);
    }

    @Test
    @DisplayName("getReservasDeUsuario - Debe lanzar excepción cuando el usuario no existe")
    public void getReservasDeUsuario_DebeLanzarExcepcionCuandoUsuarioNoExiste() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> usuarioService.getReservasDeUsuario(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado con ID: 99");

        verify(usuarioRepository).findById(99L);
    }

    @Test
    @DisplayName("getReservasDeUsuario - Debe manejar excepciones al obtener ID de reserva")
    public void getReservasDeUsuario_DebeUsarToStringCuandoFallaGetIdReserva() {
        // Given
        Reserva reservaMalformada = mock(Reserva.class);
        when(reservaMalformada.getIdReserva()).thenThrow(new RuntimeException("Error al obtener ID"));
        when(reservaMalformada.toString()).thenReturn("Reserva con error");

        usuario.setReservas(Arrays.asList(reservaMalformada));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // When
        List<String> resultado = usuarioService.getReservasDeUsuario(1L);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo("Reserva con error");
        verify(usuarioRepository).findById(1L);
    }

    // ==================== GET SUSCRIPCIONES DE USUARIO ====================

    @Test
    @DisplayName("getSuscripcionesDeUsuario - Debe retornar lista vacía")
    public void getSuscripcionesDeUsuario_DebeRetornarListaVacia() {
        // Given
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // When
        List<String> resultado = usuarioService.getSuscripcionesDeUsuario(1L);

        // Then
        assertThat(resultado).isEmpty();
        verify(usuarioRepository).findById(1L);
    }

    @Test
    @DisplayName("getSuscripcionesDeUsuario - Debe lanzar excepción cuando el usuario no existe")
    public void getSuscripcionesDeUsuario_DebeLanzarExcepcionCuandoUsuarioNoExiste() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> usuarioService.getSuscripcionesDeUsuario(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado con ID: 99");

        verify(usuarioRepository).findById(99L);
    }

    // ==================== CAMBIAR ESTADO USUARIO ====================

    @Test
    @DisplayName("cambiarEstadoUsuario - Debe cambiar de Activo a Inactivo")
    public void cambiarEstadoUsuario_DebeCambiarDeActivoAInactivo() {
        // Given
        usuario.setEstado(EstadoUsuario.Activo);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        // When
        UsuarioDTO resultado = usuarioService.cambiarEstadoUsuario(1L);

        // Then
        assertThat(resultado).isNotNull();
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioMapper).toDTO(usuario);
    }

    @Test
    @DisplayName("cambiarEstadoUsuario - Debe cambiar de Inactivo a Activo")
    public void cambiarEstadoUsuario_DebeCambiarDeInactivoAActivo() {
        // Given
        usuario.setEstado(EstadoUsuario.Inactivo);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        // When
        UsuarioDTO resultado = usuarioService.cambiarEstadoUsuario(1L);

        // Then
        assertThat(resultado).isNotNull();
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("cambiarEstadoUsuario - Debe cambiar de Disponible a Activo")
    public void cambiarEstadoUsuario_DebeCambiarDeDisponibleAActivo() {
        // Given
        usuario.setEstado(EstadoUsuario.Disponible);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        // When
        UsuarioDTO resultado = usuarioService.cambiarEstadoUsuario(1L);

        // Then
        assertThat(resultado).isNotNull();
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("cambiarEstadoUsuario - Debe lanzar excepción cuando el usuario está Bloqueado")
    public void cambiarEstadoUsuario_DebeLanzarExcepcionCuandoBloqueado() {
        // Given
        usuario.setEstado(EstadoUsuario.Bloqueado);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // When & Then
        assertThatThrownBy(() -> usuarioService.cambiarEstadoUsuario(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No se puede cambiar el estado de un usuario bloqueado");

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("cambiarEstadoUsuario - Debe lanzar excepción cuando el estado es null")
    public void cambiarEstadoUsuario_DebeLanzarExcepcionCuandoEstadoNull() {
        // Given
        usuario.setEstado(null);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // When & Then
        assertThatThrownBy(() -> usuarioService.cambiarEstadoUsuario(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("El usuario no tiene estado definido");

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("cambiarEstadoUsuario - Debe lanzar excepción cuando el usuario no existe")
    public void cambiarEstadoUsuario_DebeLanzarExcepcionCuandoUsuarioNoExiste() {
        // Given
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> usuarioService.cambiarEstadoUsuario(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado con ID: 99");

        verify(usuarioRepository).findById(99L);
        verify(usuarioRepository, never()).save(any());
    }
}