package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.SuscripcionDTO;
import ExpooCode.ExpooCode.persistence.dao.SuscripcionDao;
import ExpooCode.ExpooCode.persistence.dao.UsuarioDao;
import ExpooCode.ExpooCode.persistence.entity.Suscripcion;
import ExpooCode.ExpooCode.persistence.entity.Usuario;
import ExpooCode.ExpooCode.persistence.enums.EstadoSuscripcion;
import ExpooCode.ExpooCode.persistence.enums.EstadoUsuario;
import ExpooCode.ExpooCode.persistence.enums.RolUsuario;
import ExpooCode.ExpooCode.persistence.enums.TipoPlan;
import ExpooCode.ExpooCode.persistence.mapper.SuscripcionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests para SuscripcionServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias (SuscripcionDao, UsuarioDao, Mapper)
 * - Ejecución rápida
 * - Configurado para JaCoCo (cobertura) y SonarQube (calidad)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SuscripcionService - Unit Tests")
public class SuscripcionServiceTest {

    // ==================== DEPENDENCIAS MOCKEADAS ====================
    @Mock
    private SuscripcionDao suscripcionDao;

    @Mock
    private UsuarioDao usuarioDao;

    @Mock
    private SuscripcionMapper mapper;

    // ==================== CLASE BAJO PRUEBA ====================
    @InjectMocks
    private SuscripcionServiceImpl suscripcionService;

    // ==================== DATOS DE PRUEBA ====================
    private SuscripcionDTO validSuscripcionDTO;
    private Suscripcion validSuscripcionEntity;
    private Usuario validUsuario;
    private Long validSuscripcionId;
    private Long validUsuarioId;

    /**
     * Configuración ejecutada ANTES de cada test
     * Inicializa datos comunes reutilizables
     */
    @BeforeEach
    void setUp() {
        validSuscripcionId = 1L;
        validUsuarioId = 100L;

        // Usuario válido para usar en tests
        validUsuario = new Usuario();
        validUsuario.setIdUsuario(validUsuarioId);
        validUsuario.setNombre("Juan Pérez");
        validUsuario.setEmail("juan@example.com");
        validUsuario.setPassword("password123");
        validUsuario.setRol(RolUsuario.Afiliado);
        validUsuario.setEstado(EstadoUsuario.Activo);

        // DTO válido para crear suscripción
        validSuscripcionDTO = new SuscripcionDTO(
                null, // ID null para CREATE
                validUsuarioId,
                TipoPlan.Basico,
                40,
                new BigDecimal("49999.99"),
                LocalDateTime.of(2025, 1, 1, 8, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59),
                EstadoSuscripcion.Activa,
                true
        );

        // Entidad válida para simular respuestas del DAO
        validSuscripcionEntity = new Suscripcion();
        validSuscripcionEntity.setIdSuscripcion(validSuscripcionId);
        validSuscripcionEntity.setUsuario(validUsuario);
        validSuscripcionEntity.setTipoPlan(TipoPlan.Basico);
        validSuscripcionEntity.setHorasIncluidas(40);
        validSuscripcionEntity.setPrecioMensual(new BigDecimal("49999.99"));
        validSuscripcionEntity.setFechaInicio(LocalDateTime.of(2025, 1, 1, 8, 0));
        validSuscripcionEntity.setFechaFin(LocalDateTime.of(2025, 12, 31, 23, 59));
        validSuscripcionEntity.setEstado(EstadoSuscripcion.Activa);
        validSuscripcionEntity.setRenovacionAutomatica(true);
    }

    // ==================== CREATE SUSCRIPCION TESTS ====================

    @Test
    @DisplayName("CREATE - Suscripción válida debe retornar suscripción creada con ID")
    void crearSuscripcion_DatosValidos_DebeRetornarSuscripcionCreada() {
        // ARRANGE (Given) - Preparar el escenario
        SuscripcionDTO expectedDTO = new SuscripcionDTO(
                validSuscripcionId,
                validUsuarioId,
                TipoPlan.Basico,
                40,
                new BigDecimal("49999.99"),
                LocalDateTime.of(2025, 1, 1, 8, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59),
                EstadoSuscripcion.Activa,
                true
        );

        // Mock: Usuario existe en BD
        when(usuarioDao.findById(validUsuarioId))
                .thenReturn(Optional.of(validUsuario));

        // Mock: Mapper convierte DTO a Entity
        when(mapper.toEntity(validSuscripcionDTO))
                .thenReturn(validSuscripcionEntity);

        // Mock: DAO guarda y retorna entidad con ID
        when(suscripcionDao.save(validSuscripcionEntity))
                .thenReturn(validSuscripcionEntity);

        // Mock: Mapper convierte Entity a DTO
        when(mapper.toDTO(validSuscripcionEntity))
                .thenReturn(expectedDTO);

        // ACT (When) - Ejecutar el método bajo prueba
        SuscripcionDTO result = suscripcionService.crearSuscripcion(validSuscripcionDTO);

        // ASSERT (Then) - Verificar los resultados
        assertThat(result).isNotNull();
        assertThat(result.getIdSuscripcion()).isEqualTo(validSuscripcionId);
        assertThat(result.getIdUsuario()).isEqualTo(validUsuarioId);
        assertThat(result.getTipoPlan()).isEqualTo(TipoPlan.Basico);
        assertThat(result.getHorasIncluidas()).isEqualTo(40);
        assertThat(result.getPrecioMensual()).isEqualByComparingTo(new BigDecimal("49999.99"));
        assertThat(result.getEstado()).isEqualTo(EstadoSuscripcion.Activa);
        assertThat(result.isRenovacionAutomatica()).isTrue();

        // Verificar interacciones con los mocks
        verify(usuarioDao, times(1)).findById(validUsuarioId);
        verify(mapper, times(1)).toEntity(validSuscripcionDTO);
        verify(suscripcionDao, times(1)).save(validSuscripcionEntity);
        verify(mapper, times(1)).toDTO(validSuscripcionEntity);
    }

    @Test
    @DisplayName("CREATE - Usuario inexistente debe lanzar RuntimeException")
    void crearSuscripcion_UsuarioInexistente_DebeLanzarExcepcion() {
        // ARRANGE
        Long usuarioInexistenteId = 999L;
        validSuscripcionDTO.setIdUsuario(usuarioInexistenteId);

        // Mock: Usuario NO existe en BD
        when(usuarioDao.findById(usuarioInexistenteId))
                .thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> suscripcionService.crearSuscripcion(validSuscripcionDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("El usuario con ID " + usuarioInexistenteId + " no existe");

        // Verificar que NO se intentó guardar la suscripción
        verify(usuarioDao, times(1)).findById(usuarioInexistenteId);
        verify(mapper, never()).toEntity(any());
        verify(suscripcionDao, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Suscripción Profesional debe crearse correctamente")
    void crearSuscripcion_TipoProfesional_DebeCrearseCorrectamente() {
        // ARRANGE
        validSuscripcionDTO.setTipoPlan(TipoPlan.Profesional);
        validSuscripcionDTO.setHorasIncluidas(80);
        validSuscripcionDTO.setPrecioMensual(new BigDecimal("99999.99"));

        validSuscripcionEntity.setTipoPlan(TipoPlan.Profesional);
        validSuscripcionEntity.setHorasIncluidas(80);
        validSuscripcionEntity.setPrecioMensual(new BigDecimal("99999.99"));

        SuscripcionDTO expectedDTO = new SuscripcionDTO(
                validSuscripcionId,
                validUsuarioId,
                TipoPlan.Profesional,
                80,
                new BigDecimal("99999.99"),
                validSuscripcionDTO.getFechaInicio(),
                validSuscripcionDTO.getFechaFin(),
                EstadoSuscripcion.Activa,
                true
        );

        when(usuarioDao.findById(validUsuarioId)).thenReturn(Optional.of(validUsuario));
        when(mapper.toEntity(validSuscripcionDTO)).thenReturn(validSuscripcionEntity);
        when(suscripcionDao.save(validSuscripcionEntity)).thenReturn(validSuscripcionEntity);
        when(mapper.toDTO(validSuscripcionEntity)).thenReturn(expectedDTO);

        // ACT
        SuscripcionDTO result = suscripcionService.crearSuscripcion(validSuscripcionDTO);

        // ASSERT
        assertThat(result.getTipoPlan()).isEqualTo(TipoPlan.Profesional);
        assertThat(result.getHorasIncluidas()).isEqualTo(80);
        assertThat(result.getPrecioMensual()).isEqualByComparingTo(new BigDecimal("99999.99"));
    }

    @Test
    @DisplayName("CREATE - Suscripción Empresarial debe crearse correctamente")
    void crearSuscripcion_TipoEmpresarial_DebeCrearseCorrectamente() {
        // ARRANGE
        validSuscripcionDTO.setTipoPlan(TipoPlan.Empresarial);
        validSuscripcionDTO.setHorasIncluidas(120);
        validSuscripcionDTO.setPrecioMensual(new BigDecimal("149999.99"));

        validSuscripcionEntity.setTipoPlan(TipoPlan.Empresarial);
        validSuscripcionEntity.setHorasIncluidas(120);
        validSuscripcionEntity.setPrecioMensual(new BigDecimal("149999.99"));

        SuscripcionDTO expectedDTO = new SuscripcionDTO(
                validSuscripcionId,
                validUsuarioId,
                TipoPlan.Empresarial,
                120,
                new BigDecimal("149999.99"),
                validSuscripcionDTO.getFechaInicio(),
                validSuscripcionDTO.getFechaFin(),
                EstadoSuscripcion.Activa,
                true
        );

        when(usuarioDao.findById(validUsuarioId)).thenReturn(Optional.of(validUsuario));
        when(mapper.toEntity(validSuscripcionDTO)).thenReturn(validSuscripcionEntity);
        when(suscripcionDao.save(validSuscripcionEntity)).thenReturn(validSuscripcionEntity);
        when(mapper.toDTO(validSuscripcionEntity)).thenReturn(expectedDTO);

        // ACT
        SuscripcionDTO result = suscripcionService.crearSuscripcion(validSuscripcionDTO);

        // ASSERT
        assertThat(result.getTipoPlan()).isEqualTo(TipoPlan.Empresarial);
        assertThat(result.getHorasIncluidas()).isEqualTo(120);
        assertThat(result.getPrecioMensual()).isEqualByComparingTo(new BigDecimal("149999.99"));
    }

    // ==================== READ SUSCRIPCION TESTS ====================

    @Test
    @DisplayName("READ - Suscripción existente debe retornar DTO")
    void obtenerSuscripcion_IdExistente_DebeRetornarDTO() {
        // ARRANGE
        SuscripcionDTO expectedDTO = new SuscripcionDTO(
                validSuscripcionId,
                validUsuarioId,
                TipoPlan.Basico,
                40,
                new BigDecimal("49999.99"),
                LocalDateTime.of(2025, 1, 1, 8, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59),
                EstadoSuscripcion.Activa,
                true
        );

        when(suscripcionDao.findById(validSuscripcionId))
                .thenReturn(Optional.of(validSuscripcionEntity));
        when(mapper.toDTO(validSuscripcionEntity))
                .thenReturn(expectedDTO);

        // ACT
        SuscripcionDTO result = suscripcionService.obtenerSuscripcion(validSuscripcionId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getIdSuscripcion()).isEqualTo(validSuscripcionId);
        assertThat(result.getTipoPlan()).isEqualTo(TipoPlan.Basico);

        verify(suscripcionDao, times(1)).findById(validSuscripcionId);
        verify(mapper, times(1)).toDTO(validSuscripcionEntity);
    }

    @Test
    @DisplayName("READ - Suscripción inexistente debe retornar null")
    void obtenerSuscripcion_IdInexistente_DebeRetornarNull() {
        // ARRANGE
        Long idInexistente = 999L;
        when(suscripcionDao.findById(idInexistente))
                .thenReturn(Optional.empty());

        // ACT
        SuscripcionDTO result = suscripcionService.obtenerSuscripcion(idInexistente);

        // ASSERT
        assertThat(result).isNull();

        verify(suscripcionDao, times(1)).findById(idInexistente);
        verify(mapper, never()).toDTO(any());
    }

    // ==================== LIST SUSCRIPCIONES TESTS ====================

    @Test
    @DisplayName("LIST - Debe retornar lista de suscripciones cuando existen datos")
    void listarSuscripciones_ConDatos_DebeRetornarLista() {
        // ARRANGE
        Suscripcion suscripcion1 = new Suscripcion();
        suscripcion1.setIdSuscripcion(1L);
        suscripcion1.setTipoPlan(TipoPlan.Basico);

        Suscripcion suscripcion2 = new Suscripcion();
        suscripcion2.setIdSuscripcion(2L);
        suscripcion2.setTipoPlan(TipoPlan.Profesional);

        Suscripcion suscripcion3 = new Suscripcion();
        suscripcion3.setIdSuscripcion(3L);
        suscripcion3.setTipoPlan(TipoPlan.Empresarial);

        List<Suscripcion> entities = Arrays.asList(suscripcion1, suscripcion2, suscripcion3);

        SuscripcionDTO dto1 = new SuscripcionDTO();
        dto1.setIdSuscripcion(1L);
        dto1.setTipoPlan(TipoPlan.Basico);

        SuscripcionDTO dto2 = new SuscripcionDTO();
        dto2.setIdSuscripcion(2L);
        dto2.setTipoPlan(TipoPlan.Profesional);

        SuscripcionDTO dto3 = new SuscripcionDTO();
        dto3.setIdSuscripcion(3L);
        dto3.setTipoPlan(TipoPlan.Empresarial);

        when(suscripcionDao.findAll()).thenReturn(entities);
        when(mapper.toDTO(suscripcion1)).thenReturn(dto1);
        when(mapper.toDTO(suscripcion2)).thenReturn(dto2);
        when(mapper.toDTO(suscripcion3)).thenReturn(dto3);

        // ACT
        List<SuscripcionDTO> result = suscripcionService.listarSuscripciones();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getIdSuscripcion()).isEqualTo(1L);
        assertThat(result.get(1).getIdSuscripcion()).isEqualTo(2L);
        assertThat(result.get(2).getIdSuscripcion()).isEqualTo(3L);
        assertThat(result).extracting("tipoPlan")
                .containsExactly(TipoPlan.Basico, TipoPlan.Profesional, TipoPlan.Empresarial);

        verify(suscripcionDao, times(1)).findAll();
        verify(mapper, times(3)).toDTO(any(Suscripcion.class));
    }

    @Test
    @DisplayName("LIST - Debe retornar lista vacía cuando no hay datos")
    void listarSuscripciones_SinDatos_DebeRetornarListaVacia() {
        // ARRANGE
        when(suscripcionDao.findAll()).thenReturn(Collections.emptyList());

        // ACT
        List<SuscripcionDTO> result = suscripcionService.listarSuscripciones();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(suscripcionDao, times(1)).findAll();
        verify(mapper, never()).toDTO(any());
    }

    // ==================== UPDATE SUSCRIPCION TESTS ====================

    @Test
    @DisplayName("UPDATE - Suscripción existente debe actualizarse correctamente")
    void actualizarSuscripcion_DatosValidos_DebeActualizarCorrectamente() {
        // ARRANGE
        SuscripcionDTO updateDTO = new SuscripcionDTO(
                validSuscripcionId,
                validUsuarioId,
                TipoPlan.Empresarial, // Cambio de plan
                120, // Más horas
                new BigDecimal("149999.99"), // Nuevo precio
                LocalDateTime.of(2025, 2, 1, 8, 0),
                LocalDateTime.of(2026, 1, 31, 23, 59),
                EstadoSuscripcion.Activa,
                false // Desactivar renovación
        );

        Suscripcion existingEntity = new Suscripcion();
        existingEntity.setIdSuscripcion(validSuscripcionId);
        existingEntity.setTipoPlan(TipoPlan.Basico);
        existingEntity.setHorasIncluidas(40);
        existingEntity.setPrecioMensual(new BigDecimal("49999.99"));
        existingEntity.setRenovacionAutomatica(true);
        existingEntity.setEstado(EstadoSuscripcion.Activa);
        existingEntity.setFechaInicio(LocalDateTime.of(2025, 1, 1, 8, 0));
        existingEntity.setFechaFin(LocalDateTime.of(2025, 12, 31, 23, 59));

        Suscripcion updatedEntity = new Suscripcion();
        updatedEntity.setIdSuscripcion(validSuscripcionId);
        updatedEntity.setTipoPlan(TipoPlan.Empresarial);
        updatedEntity.setHorasIncluidas(120);
        updatedEntity.setPrecioMensual(new BigDecimal("149999.99"));
        updatedEntity.setRenovacionAutomatica(false);
        updatedEntity.setEstado(EstadoSuscripcion.Activa);
        updatedEntity.setFechaInicio(LocalDateTime.of(2025, 2, 1, 8, 0));
        updatedEntity.setFechaFin(LocalDateTime.of(2026, 1, 31, 23, 59));

        SuscripcionDTO expectedDTO = new SuscripcionDTO(
                validSuscripcionId,
                validUsuarioId,
                TipoPlan.Empresarial,
                120,
                new BigDecimal("149999.99"),
                updateDTO.getFechaInicio(),
                updateDTO.getFechaFin(),
                EstadoSuscripcion.Activa,
                false
        );

        when(suscripcionDao.findById(validSuscripcionId))
                .thenReturn(Optional.of(existingEntity));
        when(suscripcionDao.save(any(Suscripcion.class)))
                .thenReturn(updatedEntity);
        when(mapper.toDTO(updatedEntity))
                .thenReturn(expectedDTO);

        // ACT
        SuscripcionDTO result = suscripcionService.actualizarSuscripcion(validSuscripcionId, updateDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getTipoPlan()).isEqualTo(TipoPlan.Empresarial);
        assertThat(result.getHorasIncluidas()).isEqualTo(120);
        assertThat(result.getPrecioMensual()).isEqualByComparingTo(new BigDecimal("149999.99"));
        assertThat(result.isRenovacionAutomatica()).isFalse();

        verify(suscripcionDao, times(1)).findById(validSuscripcionId);
        verify(suscripcionDao, times(1)).save(any(Suscripcion.class));
        verify(mapper, times(1)).toDTO(updatedEntity);
    }

    @Test
    @DisplayName("UPDATE - Suscripción inexistente debe retornar null")
    void actualizarSuscripcion_IdInexistente_DebeRetornarNull() {
        // ARRANGE
        Long idInexistente = 999L;
        when(suscripcionDao.findById(idInexistente))
                .thenReturn(Optional.empty());

        // ACT
        SuscripcionDTO result = suscripcionService.actualizarSuscripcion(idInexistente, validSuscripcionDTO);

        // ASSERT
        assertThat(result).isNull();

        verify(suscripcionDao, times(1)).findById(idInexistente);
        verify(suscripcionDao, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("UPDATE - Cambio de estado a Cancelada debe aplicarse correctamente")
    void actualizarSuscripcion_CambioEstadoCancelada_DebeAplicarse() {
        // ARRANGE
        SuscripcionDTO updateDTO = new SuscripcionDTO(
                validSuscripcionId,
                validUsuarioId,
                TipoPlan.Basico,
                40,
                new BigDecimal("49999.99"),
                LocalDateTime.of(2025, 1, 1, 8, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59),
                EstadoSuscripcion.Cancelada, // Cambio de estado
                false
        );

        Suscripcion existingEntity = new Suscripcion();
        existingEntity.setIdSuscripcion(validSuscripcionId);
        existingEntity.setEstado(EstadoSuscripcion.Activa);
        existingEntity.setTipoPlan(TipoPlan.Basico);
        existingEntity.setHorasIncluidas(40);
        existingEntity.setPrecioMensual(new BigDecimal("49999.99"));
        existingEntity.setFechaInicio(LocalDateTime.of(2025, 1, 1, 8, 0));
        existingEntity.setFechaFin(LocalDateTime.of(2025, 12, 31, 23, 59));
        existingEntity.setRenovacionAutomatica(true);

        Suscripcion updatedEntity = new Suscripcion();
        updatedEntity.setIdSuscripcion(validSuscripcionId);
        updatedEntity.setEstado(EstadoSuscripcion.Cancelada);
        updatedEntity.setRenovacionAutomatica(false);

        SuscripcionDTO expectedDTO = new SuscripcionDTO(
                validSuscripcionId,
                validUsuarioId,
                TipoPlan.Basico,
                40,
                new BigDecimal("49999.99"),
                updateDTO.getFechaInicio(),
                updateDTO.getFechaFin(),
                EstadoSuscripcion.Cancelada,
                false
        );

        when(suscripcionDao.findById(validSuscripcionId))
                .thenReturn(Optional.of(existingEntity));
        when(suscripcionDao.save(any(Suscripcion.class)))
                .thenReturn(updatedEntity);
        when(mapper.toDTO(updatedEntity))
                .thenReturn(expectedDTO);

        // ACT
        SuscripcionDTO result = suscripcionService.actualizarSuscripcion(validSuscripcionId, updateDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isEqualTo(EstadoSuscripcion.Cancelada);
        assertThat(result.isRenovacionAutomatica()).isFalse();

        verify(suscripcionDao, times(1)).findById(validSuscripcionId);
        verify(suscripcionDao, times(1)).save(any(Suscripcion.class));
    }

    @Test
    @DisplayName("UPDATE - Cambio de estado a Vencida debe aplicarse correctamente")
    void actualizarSuscripcion_CambioEstadoVencida_DebeAplicarse() {
        // ARRANGE
        SuscripcionDTO updateDTO = new SuscripcionDTO(
                validSuscripcionId,
                validUsuarioId,
                TipoPlan.Basico,
                40,
                new BigDecimal("49999.99"),
                LocalDateTime.of(2025, 1, 1, 8, 0),
                LocalDateTime.of(2025, 12, 31, 23, 59),
                EstadoSuscripcion.Vencida,
                false
        );

        Suscripcion existingEntity = new Suscripcion();
        existingEntity.setIdSuscripcion(validSuscripcionId);
        existingEntity.setEstado(EstadoSuscripcion.Activa);
        existingEntity.setTipoPlan(TipoPlan.Basico);
        existingEntity.setHorasIncluidas(40);
        existingEntity.setPrecioMensual(new BigDecimal("49999.99"));
        existingEntity.setFechaInicio(LocalDateTime.of(2025, 1, 1, 8, 0));
        existingEntity.setFechaFin(LocalDateTime.of(2025, 12, 31, 23, 59));
        existingEntity.setRenovacionAutomatica(true);

        Suscripcion updatedEntity = new Suscripcion();
        updatedEntity.setIdSuscripcion(validSuscripcionId);
        updatedEntity.setEstado(EstadoSuscripcion.Vencida);

        SuscripcionDTO expectedDTO = new SuscripcionDTO(
                validSuscripcionId,
                validUsuarioId,
                TipoPlan.Basico,
                40,
                new BigDecimal("49999.99"),
                updateDTO.getFechaInicio(),
                updateDTO.getFechaFin(),
                EstadoSuscripcion.Vencida,
                false
        );

        when(suscripcionDao.findById(validSuscripcionId))
                .thenReturn(Optional.of(existingEntity));
        when(suscripcionDao.save(any(Suscripcion.class)))
                .thenReturn(updatedEntity);
        when(mapper.toDTO(updatedEntity))
                .thenReturn(expectedDTO);

        // ACT
        SuscripcionDTO result = suscripcionService.actualizarSuscripcion(validSuscripcionId, updateDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isEqualTo(EstadoSuscripcion.Vencida);

        verify(suscripcionDao, times(1)).findById(validSuscripcionId);
        verify(suscripcionDao, times(1)).save(any(Suscripcion.class));
    }

    // ==================== DELETE SUSCRIPCION TESTS ====================

    @Test
    @DisplayName("DELETE - Suscripción existente debe eliminarse correctamente")
    void eliminarSuscripcion_IdExistente_DebeEliminarseCorrectamente() {
        // ARRANGE
        when(suscripcionDao.findById(validSuscripcionId))
                .thenReturn(Optional.of(validSuscripcionEntity));
        doNothing().when(suscripcionDao).delete(validSuscripcionEntity);

        // ACT
        boolean result = suscripcionService.eliminarSuscripcion(validSuscripcionId);

        // ASSERT
        assertThat(result).isTrue();

        verify(suscripcionDao, times(1)).findById(validSuscripcionId);
        verify(suscripcionDao, times(1)).delete(validSuscripcionEntity);
    }

    @Test
    @DisplayName("DELETE - Suscripción inexistente debe retornar false")
    void eliminarSuscripcion_IdInexistente_DebeRetornarFalse() {
        // ARRANGE
        Long idInexistente = 999L;
        when(suscripcionDao.findById(idInexistente))
                .thenReturn(Optional.empty());

        // ACT
        boolean result = suscripcionService.eliminarSuscripcion(idInexistente);

        // ASSERT
        assertThat(result).isFalse();

        verify(suscripcionDao, times(1)).findById(idInexistente);
        verify(suscripcionDao, never()).delete(any());
    }

    @Test
    @DisplayName("DELETE - Múltiples eliminaciones deben funcionar correctamente")
    void eliminarSuscripcion_MultiplesSuscripciones_DebeFuncionar() {
        // ARRANGE
        Long id1 = 1L;
        Long id2 = 2L;

        Suscripcion suscripcion1 = new Suscripcion();
        suscripcion1.setIdSuscripcion(id1);

        Suscripcion suscripcion2 = new Suscripcion();
        suscripcion2.setIdSuscripcion(id2);

        when(suscripcionDao.findById(id1)).thenReturn(Optional.of(suscripcion1));
        when(suscripcionDao.findById(id2)).thenReturn(Optional.of(suscripcion2));
        doNothing().when(suscripcionDao).delete(any(Suscripcion.class));

        // ACT
        boolean result1 = suscripcionService.eliminarSuscripcion(id1);
        boolean result2 = suscripcionService.eliminarSuscripcion(id2);

        // ASSERT
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();

        verify(suscripcionDao, times(1)).findById(id1);
        verify(suscripcionDao, times(1)).findById(id2);
        verify(suscripcionDao, times(2)).delete(any(Suscripcion.class));
    }
}
