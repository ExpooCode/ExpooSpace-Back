package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.RecursoDTO;
import ExpooCode.ExpooCode.persistence.dao.RecursoDao;
import ExpooCode.ExpooCode.persistence.entity.Recurso;
import ExpooCode.ExpooCode.persistence.enums.EstadoRecurso;
import ExpooCode.ExpooCode.persistence.enums.Tipo;
import ExpooCode.ExpooCode.persistence.mapper.RecursoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests para RecursoServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias (RecursoDao, Mapper)
 * - Ejecución rápida
 * - Configurado para JaCoCo (cobertura) y SonarQube (calidad)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RecursoService - Unit Tests")
public class RecursoServiceTest {

    // ==================== DEPENDENCIAS MOCKEADAS ====================
    @Mock
    private RecursoDao recursoDao;

    @Mock
    private RecursoMapper recursoMapper;

    // ==================== CLASE BAJO PRUEBA ====================
    @InjectMocks
    private RecursoServiceImpl recursoService;

    // ==================== DATOS DE PRUEBA ====================
    private RecursoDTO validRecursoDTO;
    private Recurso validRecursoEntity;
    private Long validRecursoId;

    /**
     * Configuración ejecutada ANTES de cada test
     * Inicializa datos comunes reutilizables
     */
    @BeforeEach
    void setUp() {
        validRecursoId = 1L;

        // DTO válido para crear recurso
        validRecursoDTO = new RecursoDTO(
                null, // ID null para CREATE
                "Sala de Reuniones A",
                Tipo.SalaDeReunion,
                20,
                EstadoRecurso.Disponible
        );

        // Entidad válida para simular respuestas del DAO
        validRecursoEntity = new Recurso();
        validRecursoEntity.setIdRecurso(validRecursoId);
        validRecursoEntity.setNombre("Sala de Reuniones A");
        validRecursoEntity.setTipo(Tipo.SalaDeReunion);
        validRecursoEntity.setCapacidad(20);
        validRecursoEntity.setEstadoRecurso(EstadoRecurso.Disponible);
    }

    // ==================== CREATE RECURSO TESTS ====================

    @Test
    @DisplayName("CREATE - Recurso válido debe retornar recurso creado con ID")
    void createRecurso_DatosValidos_DebeRetornarRecursoCreado() {
        // ARRANGE (Given)
        RecursoDTO expectedDTO = new RecursoDTO(
                validRecursoId,
                "Sala de Reuniones A",
                Tipo.SalaDeReunion,
                20,
                EstadoRecurso.Disponible
        );

        when(recursoMapper.toEntity(validRecursoDTO)).thenReturn(validRecursoEntity);
        when(recursoDao.save(validRecursoEntity)).thenReturn(validRecursoEntity);
        when(recursoMapper.toDTO(validRecursoEntity)).thenReturn(expectedDTO);

        // ACT (When)
        RecursoDTO result = recursoService.createRecurso(validRecursoDTO);

        // ASSERT (Then)
        assertThat(result).isNotNull();
        assertThat(result.getIdRecurso()).isEqualTo(validRecursoId);
        assertThat(result.getNombre()).isEqualTo("Sala de Reuniones A");
        assertThat(result.getTipo()).isEqualTo(Tipo.SalaDeReunion);
        assertThat(result.getCapacidad()).isEqualTo(20);
        assertThat(result.getEstadoRecurso()).isEqualTo(EstadoRecurso.Disponible);

        verify(recursoMapper, times(1)).toEntity(validRecursoDTO);
        verify(recursoDao, times(1)).save(validRecursoEntity);
        verify(recursoMapper, times(1)).toDTO(validRecursoEntity);
    }

    @Test
    @DisplayName("CREATE - Sala de Exposiciones debe crearse correctamente")
    void createRecurso_SalaDeExposiciones_DebeCrearseCorrectamente() {
        // ARRANGE
        validRecursoDTO.setNombre("Sala de Exposiciones B");
        validRecursoDTO.setTipo(Tipo.SalaDeExposiciones);
        validRecursoDTO.setCapacidad(50);

        validRecursoEntity.setNombre("Sala de Exposiciones B");
        validRecursoEntity.setTipo(Tipo.SalaDeExposiciones);
        validRecursoEntity.setCapacidad(50);

        RecursoDTO expectedDTO = new RecursoDTO(
                validRecursoId,
                "Sala de Exposiciones B",
                Tipo.SalaDeExposiciones,
                50,
                EstadoRecurso.Disponible
        );

        when(recursoMapper.toEntity(validRecursoDTO)).thenReturn(validRecursoEntity);
        when(recursoDao.save(validRecursoEntity)).thenReturn(validRecursoEntity);
        when(recursoMapper.toDTO(validRecursoEntity)).thenReturn(expectedDTO);

        // ACT
        RecursoDTO result = recursoService.createRecurso(validRecursoDTO);

        // ASSERT
        assertThat(result.getTipo()).isEqualTo(Tipo.SalaDeExposiciones);
        assertThat(result.getCapacidad()).isEqualTo(50);
    }

    @Test
    @DisplayName("CREATE - Sala de Trabajo debe crearse correctamente")
    void createRecurso_SalaDeTrabajo_DebeCrearseCorrectamente() {
        // ARRANGE
        validRecursoDTO.setNombre("Sala de Trabajo C");
        validRecursoDTO.setTipo(Tipo.SalaDeTrabajo);
        validRecursoDTO.setCapacidad(10);

        validRecursoEntity.setNombre("Sala de Trabajo C");
        validRecursoEntity.setTipo(Tipo.SalaDeTrabajo);
        validRecursoEntity.setCapacidad(10);

        RecursoDTO expectedDTO = new RecursoDTO(
                validRecursoId,
                "Sala de Trabajo C",
                Tipo.SalaDeTrabajo,
                10,
                EstadoRecurso.Disponible
        );

        when(recursoMapper.toEntity(validRecursoDTO)).thenReturn(validRecursoEntity);
        when(recursoDao.save(validRecursoEntity)).thenReturn(validRecursoEntity);
        when(recursoMapper.toDTO(validRecursoEntity)).thenReturn(expectedDTO);

        // ACT
        RecursoDTO result = recursoService.createRecurso(validRecursoDTO);

        // ASSERT
        assertThat(result.getTipo()).isEqualTo(Tipo.SalaDeTrabajo);
        assertThat(result.getCapacidad()).isEqualTo(10);
    }

    // ==================== READ RECURSO TESTS ====================

    @Test
    @DisplayName("READ - Recurso existente debe retornar Optional con DTO")
    void getRecursoById_IdExistente_DebeRetornarOptionalConDTO() {
        // ARRANGE
        RecursoDTO expectedDTO = new RecursoDTO(
                validRecursoId,
                "Sala de Reuniones A",
                Tipo.SalaDeReunion,
                20,
                EstadoRecurso.Disponible
        );

        when(recursoDao.findById(validRecursoId)).thenReturn(Optional.of(validRecursoEntity));
        when(recursoMapper.toDTO(validRecursoEntity)).thenReturn(expectedDTO);

        // ACT
        Optional<RecursoDTO> result = recursoService.getRecursoById(validRecursoId);

        // ASSERT
        assertThat(result).isPresent();
        assertThat(result.get().getIdRecurso()).isEqualTo(validRecursoId);
        assertThat(result.get().getNombre()).isEqualTo("Sala de Reuniones A");

        verify(recursoDao, times(1)).findById(validRecursoId);
        verify(recursoMapper, times(1)).toDTO(validRecursoEntity);
    }

    @Test
    @DisplayName("READ - Recurso inexistente debe retornar Optional vacío")
    void getRecursoById_IdInexistente_DebeRetornarOptionalVacio() {
        // ARRANGE
        Long idInexistente = 999L;
        when(recursoDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        Optional<RecursoDTO> result = recursoService.getRecursoById(idInexistente);

        // ASSERT
        assertThat(result).isEmpty();

        verify(recursoDao, times(1)).findById(idInexistente);
        verify(recursoMapper, never()).toDTO(any());
    }

    // ==================== LIST RECURSOS TESTS ====================

    @Test
    @DisplayName("LIST - Debe retornar lista de recursos cuando existen datos")
    void getAllRecursos_ConDatos_DebeRetornarLista() {
        // ARRANGE
        Recurso recurso1 = new Recurso();
        recurso1.setIdRecurso(1L);
        recurso1.setNombre("Sala A");
        recurso1.setTipo(Tipo.SalaDeReunion);

        Recurso recurso2 = new Recurso();
        recurso2.setIdRecurso(2L);
        recurso2.setNombre("Sala B");
        recurso2.setTipo(Tipo.SalaDeExposiciones);

        Recurso recurso3 = new Recurso();
        recurso3.setIdRecurso(3L);
        recurso3.setNombre("Sala C");
        recurso3.setTipo(Tipo.SalaDeTrabajo);

        List<Recurso> entities = Arrays.asList(recurso1, recurso2, recurso3);

        RecursoDTO dto1 = new RecursoDTO(1L, "Sala A", Tipo.SalaDeReunion, 20, EstadoRecurso.Disponible);
        RecursoDTO dto2 = new RecursoDTO(2L, "Sala B", Tipo.SalaDeExposiciones, 50, EstadoRecurso.Disponible);
        RecursoDTO dto3 = new RecursoDTO(3L, "Sala C", Tipo.SalaDeTrabajo, 10, EstadoRecurso.Disponible);

        List<RecursoDTO> expectedDTOs = Arrays.asList(dto1, dto2, dto3);

        when(recursoDao.findAll()).thenReturn(entities);
        when(recursoMapper.toDTOList(entities)).thenReturn(expectedDTOs);

        // ACT
        List<RecursoDTO> result = recursoService.getAllRecursos();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).extracting("nombre")
                .containsExactly("Sala A", "Sala B", "Sala C");
        assertThat(result).extracting("tipo")
                .containsExactly(Tipo.SalaDeReunion, Tipo.SalaDeExposiciones, Tipo.SalaDeTrabajo);

        verify(recursoDao, times(1)).findAll();
        verify(recursoMapper, times(1)).toDTOList(entities);
    }

    @Test
    @DisplayName("LIST - Debe retornar lista vacía cuando no hay datos")
    void getAllRecursos_SinDatos_DebeRetornarListaVacia() {
        // ARRANGE
        when(recursoDao.findAll()).thenReturn(Collections.emptyList());
        when(recursoMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // ACT
        List<RecursoDTO> result = recursoService.getAllRecursos();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(recursoDao, times(1)).findAll();
    }

    // ==================== UPDATE RECURSO TESTS ====================

    @Test
    @DisplayName("UPDATE - Recurso existente debe actualizarse correctamente")
    void updateRecurso_DatosValidos_DebeActualizarCorrectamente() {
        // ARRANGE
        RecursoDTO updateDTO = new RecursoDTO(
                validRecursoId,
                "Sala Actualizada",
                Tipo.SalaDeExposiciones,
                30,
                EstadoRecurso.Ocupado
        );

        Recurso existingEntity = new Recurso();
        existingEntity.setIdRecurso(validRecursoId);
        existingEntity.setNombre("Sala Vieja");
        existingEntity.setTipo(Tipo.SalaDeReunion);
        existingEntity.setCapacidad(20);
        existingEntity.setEstadoRecurso(EstadoRecurso.Disponible);

        Recurso updatedEntity = new Recurso();
        updatedEntity.setIdRecurso(validRecursoId);
        updatedEntity.setNombre("Sala Actualizada");
        updatedEntity.setTipo(Tipo.SalaDeExposiciones);
        updatedEntity.setCapacidad(30);
        updatedEntity.setEstadoRecurso(EstadoRecurso.Ocupado);

        RecursoDTO expectedDTO = new RecursoDTO(
                validRecursoId,
                "Sala Actualizada",
                Tipo.SalaDeExposiciones,
                30,
                EstadoRecurso.Ocupado
        );

        when(recursoDao.findById(validRecursoId)).thenReturn(Optional.of(existingEntity));
        when(recursoDao.save(any(Recurso.class))).thenReturn(updatedEntity);
        when(recursoMapper.toDTO(updatedEntity)).thenReturn(expectedDTO);

        // ACT
        Optional<RecursoDTO> result = recursoService.updateRecurso(validRecursoId, updateDTO);

        // ASSERT
        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Sala Actualizada");
        assertThat(result.get().getTipo()).isEqualTo(Tipo.SalaDeExposiciones);
        assertThat(result.get().getCapacidad()).isEqualTo(30);
        assertThat(result.get().getEstadoRecurso()).isEqualTo(EstadoRecurso.Ocupado);

        verify(recursoDao, times(1)).findById(validRecursoId);
        verify(recursoDao, times(1)).save(any(Recurso.class));
        verify(recursoMapper, times(1)).toDTO(updatedEntity);
    }

    @Test
    @DisplayName("UPDATE - Actualización parcial solo con nombre debe funcionar")
    void updateRecurso_SoloNombre_DebeActualizarSoloNombre() {
        // ARRANGE
        RecursoDTO updateDTO = new RecursoDTO();
        updateDTO.setNombre("Nuevo Nombre");
        // Otros campos null

        Recurso existingEntity = new Recurso();
        existingEntity.setIdRecurso(validRecursoId);
        existingEntity.setNombre("Nombre Viejo");
        existingEntity.setTipo(Tipo.SalaDeReunion);
        existingEntity.setCapacidad(20);
        existingEntity.setEstadoRecurso(EstadoRecurso.Disponible);

        RecursoDTO expectedDTO = new RecursoDTO(
                validRecursoId,
                "Nuevo Nombre",
                Tipo.SalaDeReunion,
                20,
                EstadoRecurso.Disponible
        );

        when(recursoDao.findById(validRecursoId)).thenReturn(Optional.of(existingEntity));
        when(recursoDao.save(existingEntity)).thenReturn(existingEntity);
        when(recursoMapper.toDTO(existingEntity)).thenReturn(expectedDTO);

        // ACT
        Optional<RecursoDTO> result = recursoService.updateRecurso(validRecursoId, updateDTO);

        // ASSERT
        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Nuevo Nombre");
        assertThat(result.get().getTipo()).isEqualTo(Tipo.SalaDeReunion); // No cambió
        assertThat(result.get().getCapacidad()).isEqualTo(20); // No cambió
    }

    @Test
    @DisplayName("UPDATE - Cambio de estado a Mantenimiento debe aplicarse")
    void updateRecurso_CambioEstadoMantenimiento_DebeAplicarse() {
        // ARRANGE
        RecursoDTO updateDTO = new RecursoDTO();
        updateDTO.setEstadoRecurso(EstadoRecurso.Mantenimiento);

        Recurso existingEntity = new Recurso();
        existingEntity.setIdRecurso(validRecursoId);
        existingEntity.setNombre("Sala A");
        existingEntity.setTipo(Tipo.SalaDeReunion);
        existingEntity.setCapacidad(20);
        existingEntity.setEstadoRecurso(EstadoRecurso.Disponible);

        RecursoDTO expectedDTO = new RecursoDTO(
                validRecursoId,
                "Sala A",
                Tipo.SalaDeReunion,
                20,
                EstadoRecurso.Mantenimiento
        );

        when(recursoDao.findById(validRecursoId)).thenReturn(Optional.of(existingEntity));
        when(recursoDao.save(existingEntity)).thenReturn(existingEntity);
        when(recursoMapper.toDTO(existingEntity)).thenReturn(expectedDTO);

        // ACT
        Optional<RecursoDTO> result = recursoService.updateRecurso(validRecursoId, updateDTO);

        // ASSERT
        assertThat(result).isPresent();
        assertThat(result.get().getEstadoRecurso()).isEqualTo(EstadoRecurso.Mantenimiento);
    }

    @Test
    @DisplayName("UPDATE - Recurso inexistente debe retornar Optional vacío")
    void updateRecurso_IdInexistente_DebeRetornarOptionalVacio() {
        // ARRANGE
        Long idInexistente = 999L;
        when(recursoDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        Optional<RecursoDTO> result = recursoService.updateRecurso(idInexistente, validRecursoDTO);

        // ASSERT
        assertThat(result).isEmpty();

        verify(recursoDao, times(1)).findById(idInexistente);
        verify(recursoDao, never()).save(any());
        verify(recursoMapper, never()).toDTO(any());
    }

    // ==================== DELETE RECURSO TESTS ====================

    @Test
    @DisplayName("DELETE - Recurso existente debe eliminarse correctamente")
    void deleteRecurso_IdExistente_DebeEliminarseCorrectamente() {
        // ARRANGE
        when(recursoDao.findById(validRecursoId)).thenReturn(Optional.of(validRecursoEntity));
        doNothing().when(recursoDao).delete(validRecursoEntity);

        // ACT
        boolean result = recursoService.deleteRecurso(validRecursoId);

        // ASSERT
        assertThat(result).isTrue();

        verify(recursoDao, times(1)).findById(validRecursoId);
        verify(recursoDao, times(1)).delete(validRecursoEntity);
    }

    @Test
    @DisplayName("DELETE - Recurso inexistente debe retornar false")
    void deleteRecurso_IdInexistente_DebeRetornarFalse() {
        // ARRANGE
        long idInexistente = 999L;
        when(recursoDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        boolean result = recursoService.deleteRecurso(idInexistente);

        // ASSERT
        assertThat(result).isFalse();

        verify(recursoDao, times(1)).findById(idInexistente);
        verify(recursoDao, never()).delete(any());
    }

    // ==================== GET TIPOS DE RECURSOS TESTS ====================

    @Test
    @DisplayName("GET TIPOS - Debe retornar Set con tipos únicos de recursos")
    void getTiposDeRecursos_ConDatos_DebeRetornarSetDeTipos() {
        // ARRANGE
        Recurso recurso1 = new Recurso();
        recurso1.setTipo(Tipo.SalaDeReunion);

        Recurso recurso2 = new Recurso();
        recurso2.setTipo(Tipo.SalaDeReunion); // Duplicado

        Recurso recurso3 = new Recurso();
        recurso3.setTipo(Tipo.SalaDeExposiciones);

        Recurso recurso4 = new Recurso();
        recurso4.setTipo(Tipo.SalaDeTrabajo);

        List<Recurso> recursos = Arrays.asList(recurso1, recurso2, recurso3, recurso4);

        when(recursoDao.findAll()).thenReturn(recursos);

        // ACT
        Set<String> result = recursoService.getTiposDeRecursos();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3); // Sin duplicados
        assertThat(result).containsExactlyInAnyOrder(
                "SalaDeReunion",
                "SalaDeExposiciones",
                "SalaDeTrabajo"
        );

        verify(recursoDao, times(1)).findAll();
    }

    @Test
    @DisplayName("GET TIPOS - Sin recursos debe retornar Set vacío")
    void getTiposDeRecursos_SinDatos_DebeRetornarSetVacio() {
        // ARRANGE
        when(recursoDao.findAll()).thenReturn(Collections.emptyList());

        // ACT
        Set<String> result = recursoService.getTiposDeRecursos();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(recursoDao, times(1)).findAll();
    }

    @Test
    @DisplayName("GET TIPOS - Todos del mismo tipo debe retornar Set con un elemento")
    void getTiposDeRecursos_TodosMismoTipo_DebeRetornarSetConUnElemento() {
        // ARRANGE
        Recurso recurso1 = new Recurso();
        recurso1.setTipo(Tipo.SalaDeReunion);

        Recurso recurso2 = new Recurso();
        recurso2.setTipo(Tipo.SalaDeReunion);

        Recurso recurso3 = new Recurso();
        recurso3.setTipo(Tipo.SalaDeReunion);

        List<Recurso> recursos = Arrays.asList(recurso1, recurso2, recurso3);

        when(recursoDao.findAll()).thenReturn(recursos);

        // ACT
        Set<String> result = recursoService.getTiposDeRecursos();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly("SalaDeReunion");
    }
}