package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.BloqueoRecursoDTO;
import ExpooCode.ExpooCode.persistence.entity.BloqueoRecurso;
import ExpooCode.ExpooCode.persistence.entity.Recurso;
import ExpooCode.ExpooCode.persistence.enums.EstadoRecurso;
import ExpooCode.ExpooCode.persistence.enums.MotivoBloqueo;
import ExpooCode.ExpooCode.persistence.enums.Tipo;
import ExpooCode.ExpooCode.persistence.repository.BloqueoRecursoRepository;
import ExpooCode.ExpooCode.persistence.repository.RecursoRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BloqueoRecursoService - Pruebas Unitarias")
class BloqueoRecursoServiceTest {

    @Mock
    private BloqueoRecursoRepository bloqueoRecursoRepository;

    @Mock
    private RecursoRepository recursoRepository;

    @InjectMocks
    private BloqueoRecursoServiceImpl service;

    // Datos de prueba
    private Recurso recursoValido;
    private BloqueoRecurso bloqueoValido;
    private BloqueoRecursoDTO dtoValido;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    @BeforeEach
    void setUp() {
        // Inicializar fechas
        fechaInicio = LocalDateTime.of(2025, 10, 20, 8, 0);
        fechaFin = LocalDateTime.of(2025, 10, 20, 18, 0);

        // Crear recurso válido
        recursoValido = new Recurso();
        recursoValido.setIdRecurso(1L);
        recursoValido.setNombre("Sala de Conferencias A");
        recursoValido.setTipo(Tipo.SalaDeExposiciones);
        recursoValido.setCapacidad(50);
        recursoValido.setEstadoRecurso(EstadoRecurso.Disponible);

        // Crear bloqueo válido
        bloqueoValido = new BloqueoRecurso();
        bloqueoValido.setIdBloqueo(100L);
        bloqueoValido.setRecurso(recursoValido);
        bloqueoValido.setFechaInicio(fechaInicio);
        bloqueoValido.setFechaFin(fechaFin);
        bloqueoValido.setMotivo(MotivoBloqueo.Mantenimiento);
        bloqueoValido.setDescripcion("Revisión técnica programada");
        bloqueoValido.setActivo(true);

        // Crear DTO válido
        dtoValido = new BloqueoRecursoDTO();
        dtoValido.setIdRecurso(1);
        dtoValido.setFechaInicio(fechaInicio);
        dtoValido.setFechaFin(fechaFin);
        dtoValido.setMotivo("Mantenimiento");
        dtoValido.setDescripcion("Revisión técnica programada");
        dtoValido.setActivo(true);
    }

    // ==================== LISTAR BLOQUEOS ====================

    @Test
    @DisplayName("Listar bloqueos cuando existen registros debe retornar lista completa")
    void listarBloqueos_ConRegistrosExistentes_DebeRetornarListaCompleta() {
        // ARRANGE - Crear varios bloqueos
        BloqueoRecurso bloqueo2 = new BloqueoRecurso();
        bloqueo2.setIdBloqueo(101L);
        bloqueo2.setRecurso(recursoValido);
        bloqueo2.setFechaInicio(fechaInicio.plusDays(1));
        bloqueo2.setFechaFin(fechaFin.plusDays(1));
        bloqueo2.setMotivo(MotivoBloqueo.Reparacion);
        bloqueo2.setDescripcion("Reparación de equipo");
        bloqueo2.setActivo(true);

        List<BloqueoRecurso> bloqueos = Arrays.asList(bloqueoValido, bloqueo2);
        when(bloqueoRecursoRepository.findAll()).thenReturn(bloqueos);

        // ACT
        List<BloqueoRecursoDTO> resultado = service.listarBloqueos();

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getIdBloqueo()).isEqualTo(100L);
        assertThat(resultado.get(0).getMotivo()).isEqualTo("Mantenimiento");
        assertThat(resultado.get(1).getIdBloqueo()).isEqualTo(101L);
        assertThat(resultado.get(1).getMotivo()).isEqualTo("Reparacion");

        verify(bloqueoRecursoRepository).findAll();
    }

    @Test
    @DisplayName("Listar bloqueos cuando no hay registros debe retornar lista vacía")
    void listarBloqueos_SinRegistros_DebeRetornarListaVacia() {
        // ARRANGE
        when(bloqueoRecursoRepository.findAll()).thenReturn(Collections.emptyList());

        // ACT
        List<BloqueoRecursoDTO> resultado = service.listarBloqueos();

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();

        verify(bloqueoRecursoRepository).findAll();
    }

    // ==================== OBTENER POR ID ====================

    @Test
    @DisplayName("Obtener por ID existente debe retornar el bloqueo correcto")
    void obtenerPorId_IdExistente_DebeRetornarBloqueo() {
        // ARRANGE
        Long idBuscado = 100L;
        when(bloqueoRecursoRepository.findById(idBuscado)).thenReturn(Optional.of(bloqueoValido));

        // ACT
        BloqueoRecursoDTO resultado = service.obtenerPorId(idBuscado);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdBloqueo()).isEqualTo(100L);
        assertThat(resultado.getIdRecurso()).isEqualTo(1);
        assertThat(resultado.getMotivo()).isEqualTo("Mantenimiento");
        assertThat(resultado.getDescripcion()).isEqualTo("Revisión técnica programada");
        assertThat(resultado.getFechaInicio()).isEqualTo(fechaInicio);
        assertThat(resultado.getFechaFin()).isEqualTo(fechaFin);
        assertThat(resultado.isActivo()).isTrue();

        verify(bloqueoRecursoRepository).findById(idBuscado);
    }

    @Test
    @DisplayName("Obtener por ID inexistente debe lanzar excepción")
    void obtenerPorId_IdInexistente_DebeLanzarExcepcion() {
        // ARRANGE
        Long idInexistente = 999L;
        when(bloqueoRecursoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> service.obtenerPorId(idInexistente))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Bloqueo no encontrado con ID: " + idInexistente);

        verify(bloqueoRecursoRepository).findById(idInexistente);
    }

    // ==================== CREAR BLOQUEO ====================

    @Test
    @DisplayName("Crear bloqueo con datos válidos debe guardar correctamente")
    void crearBloqueo_DatosValidos_DebeGuardarCorrectamente() {
        // ARRANGE
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recursoValido));
        when(bloqueoRecursoRepository.save(any(BloqueoRecurso.class))).thenReturn(bloqueoValido);

        // ACT
        BloqueoRecursoDTO resultado = service.crearBloqueo(dtoValido);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdBloqueo()).isEqualTo(100L);
        assertThat(resultado.getIdRecurso()).isEqualTo(1);
        assertThat(resultado.getMotivo()).isEqualTo("Mantenimiento");
        assertThat(resultado.getDescripcion()).isEqualTo("Revisión técnica programada");
        assertThat(resultado.isActivo()).isTrue();

        // Verificar que se buscó el recurso y se guardó el bloqueo
        verify(recursoRepository).findById(1L);
        verify(bloqueoRecursoRepository).save(any(BloqueoRecurso.class));
    }

    @Test
    @DisplayName("Crear bloqueo con recurso inexistente debe lanzar excepción")
    void crearBloqueo_RecursoInexistente_DebeLanzarExcepcion() {
        // ARRANGE
        dtoValido.setIdRecurso(999);
        when(recursoRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> service.crearBloqueo(dtoValido))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Recurso no encontrado con ID: 999");

        // Verificar que intentó buscar el recurso pero NO guardó nada
        verify(recursoRepository).findById(999L);
        verify(bloqueoRecursoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Crear bloqueo por mantenimiento debe asignar el motivo correcto")
    void crearBloqueo_MotivoMantenimiento_DebeAsignarMotivoCorrectamente() {
        // ARRANGE
        dtoValido.setMotivo("Mantenimiento");
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recursoValido));
        when(bloqueoRecursoRepository.save(any(BloqueoRecurso.class))).thenReturn(bloqueoValido);

        // ACT
        BloqueoRecursoDTO resultado = service.crearBloqueo(dtoValido);

        // ASSERT
        assertThat(resultado.getMotivo()).isEqualTo("Mantenimiento");
        verify(bloqueoRecursoRepository).save(any(BloqueoRecurso.class));
    }

    @Test
    @DisplayName("Crear bloqueo por feriado debe asignar el motivo correcto")
    void crearBloqueo_MotivoFeriado_DebeAsignarMotivoCorrectamente() {
        // ARRANGE
        dtoValido.setMotivo("Feriado");
        dtoValido.setDescripcion("Día festivo nacional");

        BloqueoRecurso bloqueoFeriado = new BloqueoRecurso();
        bloqueoFeriado.setIdBloqueo(102L);
        bloqueoFeriado.setRecurso(recursoValido);
        bloqueoFeriado.setFechaInicio(fechaInicio);
        bloqueoFeriado.setFechaFin(fechaFin);
        bloqueoFeriado.setMotivo(MotivoBloqueo.Feriado);
        bloqueoFeriado.setDescripcion("Día festivo nacional");
        bloqueoFeriado.setActivo(true);

        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recursoValido));
        when(bloqueoRecursoRepository.save(any(BloqueoRecurso.class))).thenReturn(bloqueoFeriado);

        // ACT
        BloqueoRecursoDTO resultado = service.crearBloqueo(dtoValido);

        // ASSERT
        assertThat(resultado.getMotivo()).isEqualTo("Feriado");
        assertThat(resultado.getDescripcion()).isEqualTo("Día festivo nacional");
        verify(bloqueoRecursoRepository).save(any(BloqueoRecurso.class));
    }

    @Test
    @DisplayName("Crear bloqueo inactivo debe guardar con estado correcto")
    void crearBloqueo_EstadoInactivo_DebeGuardarConEstadoCorrecto() {
        // ARRANGE
        dtoValido.setActivo(false);

        BloqueoRecurso bloqueoInactivo = new BloqueoRecurso();
        bloqueoInactivo.setIdBloqueo(103L);
        bloqueoInactivo.setRecurso(recursoValido);
        bloqueoInactivo.setFechaInicio(fechaInicio);
        bloqueoInactivo.setFechaFin(fechaFin);
        bloqueoInactivo.setMotivo(MotivoBloqueo.Mantenimiento);
        bloqueoInactivo.setDescripcion("Revisión técnica programada");
        bloqueoInactivo.setActivo(false);

        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recursoValido));
        when(bloqueoRecursoRepository.save(any(BloqueoRecurso.class))).thenReturn(bloqueoInactivo);

        // ACT
        BloqueoRecursoDTO resultado = service.crearBloqueo(dtoValido);

        // ASSERT
        assertThat(resultado.isActivo()).isFalse();
        verify(bloqueoRecursoRepository).save(any(BloqueoRecurso.class));
    }

    // ==================== ELIMINAR BLOQUEO ====================

    @Test
    @DisplayName("Eliminar bloqueo existente debe eliminarlo correctamente")
    void eliminarBloqueo_IdExistente_DebeEliminarCorrectamente() {
        // ARRANGE
        Long idEliminar = 100L;
        when(bloqueoRecursoRepository.existsById(idEliminar)).thenReturn(true);
        doNothing().when(bloqueoRecursoRepository).deleteById(idEliminar);

        // ACT
        service.eliminarBloqueo(idEliminar);

        // ASSERT - Verificar que se verificó existencia y se eliminó
        verify(bloqueoRecursoRepository).existsById(idEliminar);
        verify(bloqueoRecursoRepository).deleteById(idEliminar);
    }

    @Test
    @DisplayName("Eliminar bloqueo inexistente debe lanzar excepción")
    void eliminarBloqueo_IdInexistente_DebeLanzarExcepcion() {
        // ARRANGE
        Long idInexistente = 999L;
        when(bloqueoRecursoRepository.existsById(idInexistente)).thenReturn(false);

        // ACT & ASSERT
        assertThatThrownBy(() -> service.eliminarBloqueo(idInexistente))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe un bloqueo con ID: " + idInexistente);

        // Verificar que se verificó existencia pero NO se intentó eliminar
        verify(bloqueoRecursoRepository).existsById(idInexistente);
        verify(bloqueoRecursoRepository, never()).deleteById(any());
    }
}