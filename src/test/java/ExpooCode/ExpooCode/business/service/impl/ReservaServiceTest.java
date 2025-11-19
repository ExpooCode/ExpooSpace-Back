package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.ReservaDTO;
import ExpooCode.ExpooCode.persistence.dao.ExtraDao;
import ExpooCode.ExpooCode.persistence.dao.RecursoDao;
import ExpooCode.ExpooCode.persistence.dao.ReservaDao;
import ExpooCode.ExpooCode.persistence.dao.UsuarioDao;
import ExpooCode.ExpooCode.persistence.entity.Extra;
import ExpooCode.ExpooCode.persistence.entity.Recurso;
import ExpooCode.ExpooCode.persistence.entity.Reserva;
import ExpooCode.ExpooCode.persistence.entity.Usuario;
import ExpooCode.ExpooCode.persistence.enums.EstadoReserva;
import ExpooCode.ExpooCode.persistence.mapper.ReservaMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ReservaServiceImpl")
public class ReservaServiceTest {

    @Mock
    private ReservaDao reservaDao;

    @Mock
    private UsuarioDao usuarioDao;

    @Mock
    private RecursoDao recursoDao;

    @Mock
    private ExtraDao extraDao;

    @Mock
    private ReservaMapper reservaMapper;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    private Reserva reserva;
    private ReservaDTO reservaDTO;
    private Usuario usuario;
    private Recurso recurso;
    private Extra extra;
    private List<Reserva> reservaList;
    private List<ReservaDTO> reservaDTOList;

    @BeforeEach
    public void setUp() {
        // Given - Preparar datos de prueba
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan@email.com");

        recurso = new Recurso();
        recurso.setIdRecurso(5L);
        recurso.setNombre("Sala de Conferencias A");

        extra = new Extra();
        extra.setIdExtra(4);
        extra.setNombre("Proyector");

        reserva = new Reserva();
        reserva.setIdReserva(101L);
        reserva.setUsuario(usuario);
        reserva.setRecurso(recurso);
        reserva.setExtra(extra);
        reserva.setFechaInicio(LocalDateTime.of(2025, 9, 20, 10, 0));
        reserva.setFechaFin(LocalDateTime.of(2025, 9, 20, 12, 0));
        reserva.setEstado(EstadoReserva.Pendiente);

        reservaDTO = new ReservaDTO();
        reservaDTO.setIdReserva(101L);
        reservaDTO.setUsuarioId(1L);
        reservaDTO.setRecursoId(5L);
        reservaDTO.setExtraId(4);
        reservaDTO.setFechaInicio(LocalDateTime.of(2025, 9, 20, 10, 0));
        reservaDTO.setFechaFin(LocalDateTime.of(2025, 9, 20, 12, 0));
        reservaDTO.setEstado(EstadoReserva.Pendiente);

        reservaList = Arrays.asList(reserva);
        reservaDTOList = Arrays.asList(reservaDTO);
    }

    // ==================== GET ALL RESERVAS ====================

    @Test
    @DisplayName("getAllReservas - Debe retornar lista de reservas exitosamente")
    public void getAllReservas_DebeRetornarListaDeReservas() {
        // Given
        when(reservaDao.findAll()).thenReturn(reservaList);
        when(reservaMapper.toDTOList(reservaList)).thenReturn(reservaDTOList);

        // When
        List<ReservaDTO> resultado = reservaService.getAllReservas();

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdReserva()).isEqualTo(101L);
        verify(reservaDao).findAll();
        verify(reservaMapper).toDTOList(reservaList);
    }

    @Test
    @DisplayName("getAllReservas - Debe retornar lista vacía cuando no hay reservas")
    public void getAllReservas_DebeRetornarListaVaciaCuandoNoHayReservas() {
        // Given
        when(reservaDao.findAll()).thenReturn(Arrays.asList());
        when(reservaMapper.toDTOList(anyList())).thenReturn(Arrays.asList());

        // When
        List<ReservaDTO> resultado = reservaService.getAllReservas();

        // Then
        assertThat(resultado).isEmpty();
        verify(reservaDao).findAll();
    }

    // ==================== GET RESERVA BY ID ====================

    @Test
    @DisplayName("getReservaById - Debe retornar reserva por ID exitosamente")
    public void getReservaById_DebeRetornarReserva() {
        // Given
        when(reservaDao.findById(101L)).thenReturn(Optional.of(reserva));
        when(reservaMapper.toDTO(reserva)).thenReturn(reservaDTO);

        // When
        ReservaDTO resultado = reservaService.getReservaById(101L);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdReserva()).isEqualTo(101L);
        assertThat(resultado.getUsuarioId()).isEqualTo(1L);
        verify(reservaDao).findById(101L);
        verify(reservaMapper).toDTO(reserva);
    }

    @Test
    @DisplayName("getReservaById - Debe lanzar excepción cuando el ID es null")
    public void getReservaById_DebeLanzarExcepcionCuandoIdNull() {
        // When & Then
        assertThatThrownBy(() -> reservaService.getReservaById(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ID inválido");

        verify(reservaDao, never()).findById(any());
    }

    @Test
    @DisplayName("getReservaById - Debe lanzar excepción cuando el ID es cero o negativo")
    public void getReservaById_DebeLanzarExcepcionCuandoIdInvalido() {
        // When & Then
        assertThatThrownBy(() -> reservaService.getReservaById(0L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ID inválido");

        assertThatThrownBy(() -> reservaService.getReservaById(-1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ID inválido");

        verify(reservaDao, never()).findById(anyLong());
    }

    @Test
    @DisplayName("getReservaById - Debe lanzar excepción cuando la reserva no existe")
    public void getReservaById_DebeLanzarExcepcionCuandoNoExiste() {
        // Given
        when(reservaDao.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reservaService.getReservaById(999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Reserva no encontrada")
                .extracting("status")
                .isEqualTo(HttpStatus.NOT_FOUND);

        verify(reservaDao).findById(999L);
    }

    // ==================== GET RESERVAS BY USUARIO ====================

    @Test
    @DisplayName("getReservasByUsuario - Debe retornar lista de reservas del usuario")
    public void getReservasByUsuario_DebeRetornarReservasDelUsuario() {
        // Given
        when(reservaDao.findByUsuarioId(1L)).thenReturn(reservaList);
        when(reservaMapper.toDTOList(reservaList)).thenReturn(reservaDTOList);

        // When
        List<ReservaDTO> resultado = reservaService.getReservasByUsuario(1L);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getUsuarioId()).isEqualTo(1L);
        verify(reservaDao).findByUsuarioId(1L);
        verify(reservaMapper).toDTOList(reservaList);
    }

    @Test
    @DisplayName("getReservasByUsuario - Debe retornar lista vacía cuando el usuario no tiene reservas")
    public void getReservasByUsuario_DebeRetornarListaVaciaCuandoNoHayReservas() {
        // Given
        when(reservaDao.findByUsuarioId(99L)).thenReturn(Arrays.asList());
        when(reservaMapper.toDTOList(anyList())).thenReturn(Arrays.asList());

        // When
        List<ReservaDTO> resultado = reservaService.getReservasByUsuario(99L);

        // Then
        assertThat(resultado).isEmpty();
        verify(reservaDao).findByUsuarioId(99L);
    }

    // ==================== CREATE RESERVA ====================

    @Test
    @DisplayName("createReserva - Debe crear reserva exitosamente con extra")
    public void createReserva_DebeCrearReservaConExtra() {
        // Given
        when(reservaMapper.toEntity(reservaDTO)).thenReturn(reserva);
        when(usuarioDao.findById(1L)).thenReturn(Optional.of(usuario));
        when(recursoDao.findById(5L)).thenReturn(Optional.of(recurso));
        when(extraDao.findById(4)).thenReturn(Optional.of(extra));
        when(reservaDao.save(any(Reserva.class))).thenReturn(reserva);
        when(reservaMapper.toDTO(reserva)).thenReturn(reservaDTO);

        // When
        ReservaDTO resultado = reservaService.createReserva(reservaDTO);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdReserva()).isEqualTo(101L);
        verify(usuarioDao).findById(1L);
        verify(recursoDao).findById(5L);
        verify(extraDao).findById(4);
        verify(reservaDao).save(any(Reserva.class));
        verify(reservaMapper).toDTO(reserva);
    }

    @Test
    @DisplayName("createReserva - Debe crear reserva exitosamente sin extra")
    public void createReserva_DebeCrearReservaSinExtra() {
        // Given
        reservaDTO.setExtraId(null);
        when(reservaMapper.toEntity(reservaDTO)).thenReturn(reserva);
        when(usuarioDao.findById(1L)).thenReturn(Optional.of(usuario));
        when(recursoDao.findById(5L)).thenReturn(Optional.of(recurso));
        when(reservaDao.save(any(Reserva.class))).thenReturn(reserva);
        when(reservaMapper.toDTO(reserva)).thenReturn(reservaDTO);

        // When
        ReservaDTO resultado = reservaService.createReserva(reservaDTO);

        // Then
        assertThat(resultado).isNotNull();
        verify(usuarioDao).findById(1L);
        verify(recursoDao).findById(5L);
        verify(extraDao, never()).findById(any());
        verify(reservaDao).save(any(Reserva.class));
    }

    @Test
    @DisplayName("createReserva - Debe lanzar excepción cuando el usuario no existe")
    public void createReserva_DebeLanzarExcepcionCuandoUsuarioNoExiste() {
        // Given
        when(reservaMapper.toEntity(reservaDTO)).thenReturn(reserva);
        when(usuarioDao.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reservaService.createReserva(reservaDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado con id: 1");

        verify(usuarioDao).findById(1L);
        verify(reservaDao, never()).save(any());
    }

    @Test
    @DisplayName("createReserva - Debe lanzar excepción cuando el recurso no existe")
    public void createReserva_DebeLanzarExcepcionCuandoRecursoNoExiste() {
        // Given
        when(reservaMapper.toEntity(reservaDTO)).thenReturn(reserva);
        when(usuarioDao.findById(1L)).thenReturn(Optional.of(usuario));
        when(recursoDao.findById(5L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reservaService.createReserva(reservaDTO))
                .hasMessageContaining("Recurso no encontrado con id: 5");

        verify(usuarioDao).findById(1L);
        verify(recursoDao).findById(5L);
        verify(reservaDao, never()).save(any());
    }

    @Test
    @DisplayName("createReserva - Debe lanzar excepción cuando el extra no existe")
    public void createReserva_DebeLanzarExcepcionCuandoExtraNoExiste() {
        // Given
        when(reservaMapper.toEntity(reservaDTO)).thenReturn(reserva);
        when(usuarioDao.findById(1L)).thenReturn(Optional.of(usuario));
        when(recursoDao.findById(5L)).thenReturn(Optional.of(recurso)); // ← DEBE ESTAR
        when(extraDao.findById(4)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reservaService.createReserva(reservaDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Extra no encontrado con id: 4");

        verify(usuarioDao).findById(1L);
        verify(recursoDao).findById(5L); // ← DEBE ESTAR
        verify(extraDao).findById(4);
        verify(reservaDao, never()).save(any());
    }

    // ==================== UPDATE RESERVA ====================

    @Test
    @DisplayName("updateReserva - Debe actualizar reserva exitosamente")
    public void updateReserva_DebeActualizarReservaExitosamente() {
        // Given
        ReservaDTO updateDTO = new ReservaDTO();
        updateDTO.setFechaInicio(LocalDateTime.of(2025, 9, 21, 14, 0));
        updateDTO.setEstado(EstadoReserva.Confirmada);
        updateDTO.setUsuarioId(1L);

        when(reservaDao.findById(101L)).thenReturn(Optional.of(reserva));
        when(usuarioDao.findById(1L)).thenReturn(Optional.of(usuario));
        when(reservaDao.save(any(Reserva.class))).thenReturn(reserva);
        when(reservaMapper.toDTO(reserva)).thenReturn(reservaDTO);

        // When
        ReservaDTO resultado = reservaService.updateReserva(101L, updateDTO);

        // Then
        assertThat(resultado).isNotNull();
        verify(reservaDao).findById(101L);
        verify(usuarioDao).findById(1L);
        verify(reservaDao).save(any(Reserva.class));
        verify(reservaMapper).toDTO(reserva);
    }

    @Test
    @DisplayName("updateReserva - Debe actualizar solo campos no nulos")
    public void updateReserva_DebeActualizarSoloCamposNoNulos() {
        // Given
        ReservaDTO partialUpdate = new ReservaDTO();
        partialUpdate.setEstado(EstadoReserva.Confirmada);

        when(reservaDao.findById(101L)).thenReturn(Optional.of(reserva));
        when(reservaDao.save(any(Reserva.class))).thenReturn(reserva);
        when(reservaMapper.toDTO(reserva)).thenReturn(reservaDTO);

        // When
        ReservaDTO resultado = reservaService.updateReserva(101L, partialUpdate);

        // Then
        assertThat(resultado).isNotNull();
        verify(reservaDao).findById(101L);
        verify(reservaDao).save(any(Reserva.class));
        verify(usuarioDao, never()).findById(any());
    }

    @Test
    @DisplayName("updateReserva - Debe lanzar excepción cuando el ID es null")
    public void updateReserva_DebeLanzarExcepcionCuandoIdNull() {
        // When & Then
        assertThatThrownBy(() -> reservaService.updateReserva(null, reservaDTO))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("ID inválido")
                .extracting("status")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        verify(reservaDao, never()).findById(any());
    }

    @Test
    @DisplayName("updateReserva - Debe lanzar excepción cuando el ID es inválido")
    public void updateReserva_DebeLanzarExcepcionCuandoIdInvalido() {
        // When & Then
        assertThatThrownBy(() -> reservaService.updateReserva(0L, reservaDTO))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("ID inválido")
                .extracting("status")
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThatThrownBy(() -> reservaService.updateReserva(-1L, reservaDTO))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("ID inválido");

        verify(reservaDao, never()).findById(anyLong());
    }

    @Test
    @DisplayName("updateReserva - Debe lanzar excepción cuando la reserva no existe")
    public void updateReserva_DebeLanzarExcepcionCuandoReservaNoExiste() {
        // Given
        when(reservaDao.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reservaService.updateReserva(999L, reservaDTO))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Reserva no encontrada")
                .extracting("status")
                .isEqualTo(HttpStatus.NOT_FOUND);

        verify(reservaDao).findById(999L);
        verify(reservaDao, never()).save(any());
    }

    @Test
    @DisplayName("updateReserva - Debe lanzar excepción cuando el usuario no existe")
    public void updateReserva_DebeLanzarExcepcionCuandoUsuarioNoExiste() {
        // Given
        ReservaDTO updateDTO = new ReservaDTO();
        updateDTO.setUsuarioId(999L);

        when(reservaDao.findById(101L)).thenReturn(Optional.of(reserva));
        when(usuarioDao.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reservaService.updateReserva(101L, updateDTO))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuario no encontrado con id: 999")
                .extracting("status")
                .isEqualTo(HttpStatus.NOT_FOUND);

        verify(reservaDao).findById(101L);
        verify(usuarioDao).findById(999L);
        verify(reservaDao, never()).save(any());
    }

    // ==================== DELETE RESERVA ====================

    @Test
    @DisplayName("deleteReserva - Debe eliminar reserva exitosamente")
    public void deleteReserva_DebeEliminarReservaExitosamente() {
        // Given
        when(reservaDao.findById(101L)).thenReturn(Optional.of(reserva));
        doNothing().when(reservaDao).delete(reserva);

        // When
        boolean resultado = reservaService.deleteReserva(101L);

        // Then
        assertThat(resultado).isTrue();
        verify(reservaDao).findById(101L);
        verify(reservaDao).delete(reserva);
    }

    @Test
    @DisplayName("deleteReserva - Debe retornar false cuando la reserva no existe")
    public void deleteReserva_DebeRetornarFalseCuandoNoExiste() {
        // Given
        when(reservaDao.findById(999L)).thenReturn(Optional.empty());

        // When
        boolean resultado = reservaService.deleteReserva(999L);

        // Then
        assertThat(resultado).isFalse();
        verify(reservaDao).findById(999L);
        verify(reservaDao, never()).delete(any());
    }
}