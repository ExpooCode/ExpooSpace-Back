package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.NotificacionDTO;
import ExpooCode.ExpooCode.persistence.dao.NotificacionDao;
import ExpooCode.ExpooCode.persistence.dao.UsuarioDao;
import ExpooCode.ExpooCode.persistence.entity.Notificacion;
import ExpooCode.ExpooCode.persistence.entity.Usuario;
import ExpooCode.ExpooCode.persistence.enums.EstadoUsuario;
import ExpooCode.ExpooCode.persistence.enums.RolUsuario;
import ExpooCode.ExpooCode.persistence.mapper.NotificacionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests para NotificacionServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias (NotificacionDao, UsuarioDao, Mapper)
 * - Ejecución rápida
 * - Configurado para JaCoCo (cobertura) y SonarQube (calidad)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NotificacionService - Unit Tests")
public class NotificacionServiceTest {

    // ==================== DEPENDENCIAS MOCKEADAS ====================
    @Mock
    private NotificacionDao notificacionDao;

    @Mock
    private NotificacionMapper notificacionMapper;

    @Mock
    private UsuarioDao usuarioDao;

    // ==================== CLASE BAJO PRUEBA ====================
    @InjectMocks
    private NotificacionServiceImpl notificacionService;

    // ==================== DATOS DE PRUEBA ====================
    private NotificacionDTO validNotificacionDTO;
    private Notificacion validNotificacionEntity;
    private Usuario validUsuario;
    private Long validNotificacionId;
    private Long validUsuarioId;
    private LocalDateTime fixedDateTime;

    /**
     * Configuración ejecutada ANTES de cada test
     * Inicializa datos comunes reutilizables
     */
    @BeforeEach
    void setUp() {
        validNotificacionId = 2001L;
        validUsuarioId = 12L;
        fixedDateTime = LocalDateTime.of(2025, 7, 18, 10, 15, 0);

        // Usuario válido
        validUsuario = new Usuario();
        validUsuario.setIdUsuario(validUsuarioId);
        validUsuario.setNombre("Juan Pérez");
        validUsuario.setEmail("juan.perez@example.com");
        validUsuario.setPassword("password123");
        validUsuario.setRol(RolUsuario.Afiliado);
        validUsuario.setEstado(EstadoUsuario.Activo);

        // DTO válido para crear notificación
        validNotificacionDTO = new NotificacionDTO(
                null,
                validUsuarioId,
                "Tu reserva ha sido confirmada",
                null,
                false
        );

        // Entidad válida para simular respuestas del DAO
        validNotificacionEntity = new Notificacion();
        validNotificacionEntity.setIdNotificacion(null);
        validNotificacionEntity.setUsuario(validUsuario);
        validNotificacionEntity.setMensaje("Tu reserva ha sido confirmada");
        validNotificacionEntity.setFechaEnvio(fixedDateTime);
        validNotificacionEntity.setLeido(false);
    }

    // ==================== CREATE NOTIFICACION TESTS ====================

    @Test
    @DisplayName("CREATE - Notificación válida debe retornar notificación creada con ID")
    void crearNotificacion_DatosValidos_DebeRetornarNotificacionCreada() {
        // ARRANGE
        Notificacion savedEntity = new Notificacion();
        savedEntity.setIdNotificacion(validNotificacionId);
        savedEntity.setUsuario(validUsuario);
        savedEntity.setMensaje("Tu reserva ha sido confirmada");
        savedEntity.setFechaEnvio(fixedDateTime);
        savedEntity.setLeido(false);

        NotificacionDTO expectedDTO = new NotificacionDTO(
                validNotificacionId,
                validUsuarioId,
                "Tu reserva ha sido confirmada",
                fixedDateTime,
                false
        );

        when(notificacionMapper.toEntity(validNotificacionDTO)).thenReturn(validNotificacionEntity);
        when(usuarioDao.findById(validUsuarioId)).thenReturn(Optional.of(validUsuario));
        when(notificacionDao.save(any(Notificacion.class))).thenReturn(savedEntity);
        when(notificacionMapper.toDTO(savedEntity)).thenReturn(expectedDTO);

        // ACT
        NotificacionDTO result = notificacionService.crearNotificacion(validNotificacionDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getIdNotificacion()).isEqualTo(validNotificacionId);
        assertThat(result.getIdUsuario()).isEqualTo(validUsuarioId);
        assertThat(result.getMensaje()).isEqualTo("Tu reserva ha sido confirmada");
        assertThat(result.isLeido()).isFalse();
        assertThat(result.getFechaEnvio()).isNotNull();

        verify(notificacionMapper, times(1)).toEntity(validNotificacionDTO);
        verify(usuarioDao, times(1)).findById(validUsuarioId);
        verify(notificacionDao, times(1)).save(any(Notificacion.class));
        verify(notificacionMapper, times(1)).toDTO(savedEntity);
    }

    @Test
    @DisplayName("CREATE - Notificación debe inicializarse siempre como no leída")
    void crearNotificacion_SiempreDebeIniciarComoNoLeida() {
        // ARRANGE
        Notificacion savedEntity = new Notificacion();
        savedEntity.setIdNotificacion(validNotificacionId);
        savedEntity.setUsuario(validUsuario);
        savedEntity.setMensaje("Nueva notificación");
        savedEntity.setFechaEnvio(fixedDateTime);
        savedEntity.setLeido(false);

        NotificacionDTO expectedDTO = new NotificacionDTO(
                validNotificacionId,
                validUsuarioId,
                "Nueva notificación",
                fixedDateTime,
                false
        );

        when(notificacionMapper.toEntity(validNotificacionDTO)).thenReturn(validNotificacionEntity);
        when(usuarioDao.findById(validUsuarioId)).thenReturn(Optional.of(validUsuario));
        when(notificacionDao.save(any(Notificacion.class))).thenAnswer(invocation -> {
            Notificacion entity = invocation.getArgument(0);
            assertThat(entity.isLeido()).isFalse();
            return savedEntity;
        });
        when(notificacionMapper.toDTO(savedEntity)).thenReturn(expectedDTO);

        // ACT
        NotificacionDTO result = notificacionService.crearNotificacion(validNotificacionDTO);

        // ASSERT
        assertThat(result.isLeido()).isFalse();
        verify(notificacionDao, times(1)).save(argThat(n -> !n.isLeido()));
    }

    @Test
    @DisplayName("CREATE - Notificación debe asignar fecha actual automáticamente")
    void crearNotificacion_DebeAsignarFechaActual() {
        // ARRANGE
        Notificacion savedEntity = new Notificacion();
        savedEntity.setIdNotificacion(validNotificacionId);
        savedEntity.setUsuario(validUsuario);
        savedEntity.setMensaje("Test mensaje");
        savedEntity.setLeido(false);

        NotificacionDTO expectedDTO = new NotificacionDTO(
                validNotificacionId,
                validUsuarioId,
                "Test mensaje",
                LocalDateTime.now(),
                false
        );

        when(notificacionMapper.toEntity(validNotificacionDTO)).thenReturn(validNotificacionEntity);
        when(usuarioDao.findById(validUsuarioId)).thenReturn(Optional.of(validUsuario));
        when(notificacionDao.save(any(Notificacion.class))).thenAnswer(invocation -> {
            Notificacion entity = invocation.getArgument(0);
            assertThat(entity.getFechaEnvio()).isNotNull();
            savedEntity.setFechaEnvio(entity.getFechaEnvio());
            return savedEntity;
        });
        when(notificacionMapper.toDTO(any(Notificacion.class))).thenReturn(expectedDTO);

        // ACT
        NotificacionDTO result = notificacionService.crearNotificacion(validNotificacionDTO);

        // ASSERT
        assertThat(result.getFechaEnvio()).isNotNull();
        verify(notificacionDao, times(1)).save(argThat(n -> n.getFechaEnvio() != null));
    }

    @Test
    @DisplayName("CREATE - Usuario inexistente debe lanzar RuntimeException")
    void crearNotificacion_UsuarioInexistente_DebeLanzarException() {
        // ARRANGE
        Long idUsuarioInexistente = 999L;
        validNotificacionDTO.setIdUsuario(idUsuarioInexistente);

        when(notificacionMapper.toEntity(validNotificacionDTO)).thenReturn(validNotificacionEntity);
        when(usuarioDao.findById(idUsuarioInexistente)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> notificacionService.crearNotificacion(validNotificacionDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado con id: " + idUsuarioInexistente);

        verify(usuarioDao, times(1)).findById(idUsuarioInexistente);
        verify(notificacionDao, never()).save(any());
        verify(notificacionMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("CREATE - Notificación con mensaje largo debe crearse correctamente")
    void crearNotificacion_MensajeLargo_DebeCrearse() {
        // ARRANGE
        String mensajeLargo = "Este es un mensaje muy largo que contiene información importante sobre la reserva del usuario y detalles adicionales que deben ser comunicados de manera efectiva al destinatario de la notificación.";
        validNotificacionDTO.setMensaje(mensajeLargo);
        validNotificacionEntity.setMensaje(mensajeLargo);

        Notificacion savedEntity = new Notificacion();
        savedEntity.setIdNotificacion(validNotificacionId);
        savedEntity.setUsuario(validUsuario);
        savedEntity.setMensaje(mensajeLargo);
        savedEntity.setFechaEnvio(fixedDateTime);
        savedEntity.setLeido(false);

        NotificacionDTO expectedDTO = new NotificacionDTO(
                validNotificacionId,
                validUsuarioId,
                mensajeLargo,
                fixedDateTime,
                false
        );

        when(notificacionMapper.toEntity(validNotificacionDTO)).thenReturn(validNotificacionEntity);
        when(usuarioDao.findById(validUsuarioId)).thenReturn(Optional.of(validUsuario));
        when(notificacionDao.save(any(Notificacion.class))).thenReturn(savedEntity);
        when(notificacionMapper.toDTO(savedEntity)).thenReturn(expectedDTO);

        // ACT
        NotificacionDTO result = notificacionService.crearNotificacion(validNotificacionDTO);

        // ASSERT
        assertThat(result.getMensaje()).isEqualTo(mensajeLargo);
        assertThat(result.getMensaje().length()).isGreaterThan(100);
    }

    // ==================== READ NOTIFICACION TESTS ====================

    @Test
    @DisplayName("READ - Notificación existente debe retornar DTO")
    void obtenerNotificacion_IdExistente_DebeRetornarDTO() {
        // ARRANGE
        Notificacion entity = new Notificacion();
        entity.setIdNotificacion(validNotificacionId);
        entity.setUsuario(validUsuario);
        entity.setMensaje("Mensaje de prueba");
        entity.setFechaEnvio(fixedDateTime);
        entity.setLeido(false);

        NotificacionDTO expectedDTO = new NotificacionDTO(
                validNotificacionId,
                validUsuarioId,
                "Mensaje de prueba",
                fixedDateTime,
                false
        );

        when(notificacionDao.findById(validNotificacionId)).thenReturn(Optional.of(entity));
        when(notificacionMapper.toDTO(entity)).thenReturn(expectedDTO);

        // ACT
        NotificacionDTO result = notificacionService.obtenerNotificacion(validNotificacionId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getIdNotificacion()).isEqualTo(validNotificacionId);
        assertThat(result.getIdUsuario()).isEqualTo(validUsuarioId);
        assertThat(result.getMensaje()).isEqualTo("Mensaje de prueba");

        verify(notificacionDao, times(1)).findById(validNotificacionId);
        verify(notificacionMapper, times(1)).toDTO(entity);
    }

    @Test
    @DisplayName("READ - Notificación inexistente debe retornar null")
    void obtenerNotificacion_IdInexistente_DebeRetornarNull() {
        // ARRANGE
        Long idInexistente = 999L;
        when(notificacionDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        NotificacionDTO result = notificacionService.obtenerNotificacion(idInexistente);

        // ASSERT
        assertThat(result).isNull();

        verify(notificacionDao, times(1)).findById(idInexistente);
        verify(notificacionMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("READ - Notificación leída debe retornar con estado correcto")
    void obtenerNotificacion_NotificacionLeida_DebeRetornarEstadoCorrecto() {
        // ARRANGE
        Notificacion entity = new Notificacion();
        entity.setIdNotificacion(validNotificacionId);
        entity.setUsuario(validUsuario);
        entity.setMensaje("Notificación leída");
        entity.setFechaEnvio(fixedDateTime);
        entity.setLeido(true);

        NotificacionDTO expectedDTO = new NotificacionDTO(
                validNotificacionId,
                validUsuarioId,
                "Notificación leída",
                fixedDateTime,
                true
        );

        when(notificacionDao.findById(validNotificacionId)).thenReturn(Optional.of(entity));
        when(notificacionMapper.toDTO(entity)).thenReturn(expectedDTO);

        // ACT
        NotificacionDTO result = notificacionService.obtenerNotificacion(validNotificacionId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.isLeido()).isTrue();
    }

    // ==================== LIST NOTIFICACIONES TESTS ====================

    @Test
    @DisplayName("LIST - Debe retornar lista de notificaciones cuando existen datos")
    void listarNotificaciones_ConDatos_DebeRetornarLista() {
        // ARRANGE
        Notificacion notif1 = createNotificacionEntity(1L, "Notificación 1", false);
        Notificacion notif2 = createNotificacionEntity(2L, "Notificación 2", true);
        Notificacion notif3 = createNotificacionEntity(3L, "Notificación 3", false);

        List<Notificacion> entities = Arrays.asList(notif1, notif2, notif3);

        NotificacionDTO dto1 = new NotificacionDTO(1L, validUsuarioId, "Notificación 1", fixedDateTime, false);
        NotificacionDTO dto2 = new NotificacionDTO(2L, validUsuarioId, "Notificación 2", fixedDateTime, true);
        NotificacionDTO dto3 = new NotificacionDTO(3L, validUsuarioId, "Notificación 3", fixedDateTime, false);

        when(notificacionDao.findAll()).thenReturn(entities);
        when(notificacionMapper.toDTO(any(Notificacion.class)))
                .thenReturn(dto1)
                .thenReturn(dto2)
                .thenReturn(dto3);

        // ACT
        List<NotificacionDTO> result = notificacionService.listarNotificaciones();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);

        verify(notificacionDao, times(1)).findAll();
        verify(notificacionMapper, times(3)).toDTO(any(Notificacion.class));
    }

    @Test
    @DisplayName("LIST - Debe retornar lista vacía cuando no hay datos")
    void listarNotificaciones_SinDatos_DebeRetornarListaVacia() {
        // ARRANGE
        when(notificacionDao.findAll()).thenReturn(Collections.emptyList());

        // ACT
        List<NotificacionDTO> result = notificacionService.listarNotificaciones();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(notificacionDao, times(1)).findAll();
        verify(notificacionMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("LIST - Debe retornar todas las notificaciones sin filtros")
    void listarNotificaciones_TodasLasNotificaciones_DebeRetornarSinFiltros() {
        // ARRANGE
        Notificacion notif1 = createNotificacionEntity(1L, "Msg1", true);
        Notificacion notif2 = createNotificacionEntity(2L, "Msg2", false);

        List<Notificacion> entities = Arrays.asList(notif1, notif2);

        NotificacionDTO dto1 = new NotificacionDTO(1L, validUsuarioId, "Msg1", fixedDateTime, true);
        NotificacionDTO dto2 = new NotificacionDTO(2L, validUsuarioId, "Msg2", fixedDateTime, false);

        when(notificacionDao.findAll()).thenReturn(entities);
        when(notificacionMapper.toDTO(any(Notificacion.class)))
                .thenReturn(dto1)
                .thenReturn(dto2);

        // ACT
        List<NotificacionDTO> result = notificacionService.listarNotificaciones();

        // ASSERT
        assertThat(result).hasSize(2);
    }

    // ==================== MARCAR LEIDA TESTS ====================

    @Test
    @DisplayName("MARCAR LEIDA - Notificación existente debe marcarse como leída")
    void marcarLeida_NotificacionExistente_DebeMarcarseComoLeida() {
        // ARRANGE
        Notificacion entity = new Notificacion();
        entity.setIdNotificacion(validNotificacionId);
        entity.setUsuario(validUsuario);
        entity.setMensaje("Notificación sin leer");
        entity.setFechaEnvio(fixedDateTime);
        entity.setLeido(false);

        Notificacion updatedEntity = new Notificacion();
        updatedEntity.setIdNotificacion(validNotificacionId);
        updatedEntity.setUsuario(validUsuario);
        updatedEntity.setMensaje("Notificación sin leer");
        updatedEntity.setFechaEnvio(fixedDateTime);
        updatedEntity.setLeido(true);

        NotificacionDTO expectedDTO = new NotificacionDTO(
                validNotificacionId,
                validUsuarioId,
                "Notificación sin leer",
                fixedDateTime,
                true
        );

        when(notificacionDao.findById(validNotificacionId)).thenReturn(Optional.of(entity));
        when(notificacionDao.save(entity)).thenReturn(updatedEntity);
        when(notificacionMapper.toDTO(updatedEntity)).thenReturn(expectedDTO);

        // ACT
        NotificacionDTO result = notificacionService.marcarLeida(validNotificacionId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.isLeido()).isTrue();
        assertThat(result.getIdNotificacion()).isEqualTo(validNotificacionId);

        verify(notificacionDao, times(1)).findById(validNotificacionId);
        verify(notificacionDao, times(1)).save(argThat(n -> n.isLeido()));
        verify(notificacionMapper, times(1)).toDTO(updatedEntity);
    }

    @Test
    @DisplayName("MARCAR LEIDA - Notificación ya leída debe permanecer leída")
    void marcarLeida_NotificacionYaLeida_DebePermanecer() {
        // ARRANGE
        Notificacion entity = new Notificacion();
        entity.setIdNotificacion(validNotificacionId);
        entity.setUsuario(validUsuario);
        entity.setMensaje("Ya leída");
        entity.setFechaEnvio(fixedDateTime);
        entity.setLeido(true);

        NotificacionDTO expectedDTO = new NotificacionDTO(
                validNotificacionId,
                validUsuarioId,
                "Ya leída",
                fixedDateTime,
                true
        );

        when(notificacionDao.findById(validNotificacionId)).thenReturn(Optional.of(entity));
        when(notificacionDao.save(entity)).thenReturn(entity);
        when(notificacionMapper.toDTO(entity)).thenReturn(expectedDTO);

        // ACT
        NotificacionDTO result = notificacionService.marcarLeida(validNotificacionId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.isLeido()).isTrue();
    }

    @Test
    @DisplayName("MARCAR LEIDA - Notificación inexistente debe retornar null")
    void marcarLeida_NotificacionInexistente_DebeRetornarNull() {
        // ARRANGE
        Long idInexistente = 999L;
        when(notificacionDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        NotificacionDTO result = notificacionService.marcarLeida(idInexistente);

        // ASSERT
        assertThat(result).isNull();

        verify(notificacionDao, times(1)).findById(idInexistente);
        verify(notificacionDao, never()).save(any());
        verify(notificacionMapper, never()).toDTO(any());
    }

    // ==================== DELETE NOTIFICACION TESTS ====================

    @Test
    @DisplayName("DELETE - Notificación existente debe eliminarse correctamente")
    void eliminarNotificacion_IdExistente_DebeEliminarseCorrectamente() {
        // ARRANGE
        Notificacion entity = createNotificacionEntity(validNotificacionId, "A eliminar", false);

        when(notificacionDao.findById(validNotificacionId)).thenReturn(Optional.of(entity));
        doNothing().when(notificacionDao).delete(entity);

        // ACT
        boolean result = notificacionService.eliminarNotificacion(validNotificacionId);

        // ASSERT
        assertThat(result).isTrue();

        verify(notificacionDao, times(1)).findById(validNotificacionId);
        verify(notificacionDao, times(1)).delete(entity);
    }

    @Test
    @DisplayName("DELETE - Notificación inexistente debe retornar false")
    void eliminarNotificacion_IdInexistente_DebeRetornarFalse() {
        // ARRANGE
        Long idInexistente = 999L;
        when(notificacionDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        boolean result = notificacionService.eliminarNotificacion(idInexistente);

        // ASSERT
        assertThat(result).isFalse();

        verify(notificacionDao, times(1)).findById(idInexistente);
        verify(notificacionDao, never()).delete(any());
    }

    @Test
    @DisplayName("DELETE - Notificación leída debe poder eliminarse")
    void eliminarNotificacion_NotificacionLeida_DebePoderEliminarse() {
        // ARRANGE
        Notificacion entity = createNotificacionEntity(validNotificacionId, "Leída y a eliminar", true);

        when(notificacionDao.findById(validNotificacionId)).thenReturn(Optional.of(entity));
        doNothing().when(notificacionDao).delete(entity);

        // ACT
        boolean result = notificacionService.eliminarNotificacion(validNotificacionId);

        // ASSERT
        assertThat(result).isTrue();
        verify(notificacionDao, times(1)).delete(entity);
    }

    // ==================== CONTAR NO LEIDAS TESTS ====================

    @Test
    @DisplayName("CONTAR NO LEIDAS - Debe retornar el conteo correcto de notificaciones no leídas")
    void contarNoLeidas_ConNotificacionesPendientes_DebeRetornarConteo() {
        // ARRANGE
        long expectedCount = 5L;
        when(notificacionDao.countByLeidoFalse()).thenReturn(expectedCount);

        // ACT
        long result = notificacionService.contarNoLeidas();

        // ASSERT
        assertThat(result).isEqualTo(expectedCount);

        verify(notificacionDao, times(1)).countByLeidoFalse();
    }

    @Test
    @DisplayName("CONTAR NO LEIDAS - Sin notificaciones pendientes debe retornar cero")
    void contarNoLeidas_SinNotificacionesPendientes_DebeRetornarCero() {
        // ARRANGE
        when(notificacionDao.countByLeidoFalse()).thenReturn(0L);

        // ACT
        long result = notificacionService.contarNoLeidas();

        // ASSERT
        assertThat(result).isZero();

        verify(notificacionDao, times(1)).countByLeidoFalse();
    }

    @Test
    @DisplayName("CONTAR NO LEIDAS - Con múltiples notificaciones no leídas debe retornar número correcto")
    void contarNoLeidas_MultiplesNotificaciones_DebeRetornarNumeroCorrecto() {
        // ARRANGE
        long expectedCount = 42L;
        when(notificacionDao.countByLeidoFalse()).thenReturn(expectedCount);

        // ACT
        long result = notificacionService.contarNoLeidas();

        // ASSERT
        assertThat(result).isEqualTo(42L);
        assertThat(result).isPositive();
    }

    // ==================== MARCAR TODAS LEIDAS TESTS ====================

    @Test
    @DisplayName("MARCAR TODAS LEIDAS - Debe marcar todas las notificaciones como leídas")
    void marcarTodasLeidas_ConNotificacionesPendientes_DebeMarcarTodasComoLeidas() {
        // ARRANGE
        Notificacion notif1 = createNotificacionEntity(1L, "Msg1", false);
        Notificacion notif2 = createNotificacionEntity(2L, "Msg2", false);
        Notificacion notif3 = createNotificacionEntity(3L, "Msg3", true);

        List<Notificacion> notificaciones = Arrays.asList(notif1, notif2, notif3);

        when(notificacionDao.findAll()).thenReturn(notificaciones);
        when(notificacionDao.saveAll(anyList())).thenReturn(notificaciones);

        // ACT
        notificacionService.marcarTodasLeidas();

        // ASSERT
        assertThat(notif1.isLeido()).isTrue();
        assertThat(notif2.isLeido()).isTrue();
        assertThat(notif3.isLeido()).isTrue();

        verify(notificacionDao, times(1)).findAll();
        verify(notificacionDao, times(1)).saveAll(argThat(list ->
                list.stream().allMatch(Notificacion::isLeido)
        ));
    }

    @Test
    @DisplayName("MARCAR TODAS LEIDAS - Sin notificaciones debe ejecutarse sin errores")
    void marcarTodasLeidas_SinNotificaciones_DebeEjecutarseSinErrores() {
        // ARRANGE
        when(notificacionDao.findAll()).thenReturn(Collections.emptyList());
        when(notificacionDao.saveAll(Collections.emptyList())).thenReturn(Collections.emptyList());

        // ACT & ASSERT
        assertThatCode(() -> notificacionService.marcarTodasLeidas())
                .doesNotThrowAnyException();

        verify(notificacionDao, times(1)).findAll();
        verify(notificacionDao, times(1)).saveAll(Collections.emptyList());
    }

    @Test
    @DisplayName("MARCAR TODAS LEIDAS - Notificaciones ya leídas deben permanecer leídas")
    void marcarTodasLeidas_NotificacionesYaLeidas_DebenPermanecer() {
        // ARRANGE
        Notificacion notif1 = createNotificacionEntity(1L, "Msg1", true);
        Notificacion notif2 = createNotificacionEntity(2L, "Msg2", true);

        List<Notificacion> notificaciones = Arrays.asList(notif1, notif2);

        when(notificacionDao.findAll()).thenReturn(notificaciones);
        when(notificacionDao.saveAll(any())).thenReturn(notificaciones);

        // ACT
        notificacionService.marcarTodasLeidas();

        // ASSERT
        assertThat(notif1.isLeido()).isTrue();
        assertThat(notif2.isLeido()).isTrue();

        verify(notificacionDao, times(1)).findAll();
        verify(notificacionDao, times(1)).saveAll(notificaciones);
    }

    @Test
    @DisplayName("MARCAR TODAS LEIDAS - Debe procesar mezcla de notificaciones leídas y no leídas")
    void marcarTodasLeidas_MezclaDeEstados_DebeProcesar() {
        // ARRANGE
        Notificacion notif1 = createNotificacionEntity(1L, "Msg1", false);
        Notificacion notif2 = createNotificacionEntity(2L, "Msg2", true);
        Notificacion notif3 = createNotificacionEntity(3L, "Msg3", false);
        Notificacion notif4 = createNotificacionEntity(4L, "Msg4", false);

        List<Notificacion> notificaciones = Arrays.asList(notif1, notif2, notif3, notif4);

        when(notificacionDao.findAll()).thenReturn(notificaciones);
        when(notificacionDao.saveAll(anyList())).thenReturn(notificaciones);

        // ACT
        notificacionService.marcarTodasLeidas();

        // ASSERT
        assertThat(notificaciones).allMatch(Notificacion::isLeido);

        verify(notificacionDao, times(1)).findAll();
        verify(notificacionDao, times(1)).saveAll(notificaciones);
    }

    @Test
    @DisplayName("MARCAR TODAS LEIDAS - Con lista grande debe marcar todas correctamente")
    void marcarTodasLeidas_ListaGrande_DebeMarcarTodasCorrectamente() {
        // ARRANGE
        List<Notificacion> notificaciones = Arrays.asList(
                createNotificacionEntity(1L, "Msg1", false),
                createNotificacionEntity(2L, "Msg2", false),
                createNotificacionEntity(3L, "Msg3", true),
                createNotificacionEntity(4L, "Msg4", false),
                createNotificacionEntity(5L, "Msg5", false),
                createNotificacionEntity(6L, "Msg6", true),
                createNotificacionEntity(7L, "Msg7", false),
                createNotificacionEntity(8L, "Msg8", false),
                createNotificacionEntity(9L, "Msg9", false),
                createNotificacionEntity(10L, "Msg10", true)
        );

        when(notificacionDao.findAll()).thenReturn(notificaciones);
        when(notificacionDao.saveAll(anyList())).thenReturn(notificaciones);

        // ACT
        notificacionService.marcarTodasLeidas();

        // ASSERT
        assertThat(notificaciones).hasSize(10);
        assertThat(notificaciones).allMatch(Notificacion::isLeido);

        verify(notificacionDao, times(1)).findAll();
        verify(notificacionDao, times(1)).saveAll(argThat(list -> list.size() == 10));
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Método auxiliar para crear notificaciones de prueba
     */
    private Notificacion createNotificacionEntity(Long id, String mensaje, boolean leido) {
        Notificacion notif = new Notificacion();
        notif.setIdNotificacion(id);
        notif.setUsuario(validUsuario);
        notif.setMensaje(mensaje);
        notif.setFechaEnvio(fixedDateTime);
        notif.setLeido(leido);
        return notif;
    }
}