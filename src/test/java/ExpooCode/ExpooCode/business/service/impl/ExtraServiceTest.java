package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.ExtraDTO;
import ExpooCode.ExpooCode.persistence.dao.ExtraDao;
import ExpooCode.ExpooCode.persistence.entity.Extra;
import ExpooCode.ExpooCode.persistence.enums.TipoExtra;
import ExpooCode.ExpooCode.persistence.mapper.ExtraMapper;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExtraService - Pruebas Unitarias")
class ExtraServiceTest {

    @Mock
    private ExtraDao extraDao;

    @Mock
    private ExtraMapper extraMapper;

    @InjectMocks
    private ExtraServiceImpl service;

    // Datos de prueba reutilizables
    private Extra extraProyector;
    private ExtraDTO dtoProyector;
    private Extra extraCargador;
    private ExtraDTO dtoCargador;

    @BeforeEach
    void setUp() {
        // Extra tipo Proyector
        extraProyector = new Extra();
        extraProyector.setIdExtra(1);
        extraProyector.setNombre("Proyector Epson X200");
        extraProyector.setTipoExtra(TipoExtra.Proyector);

        dtoProyector = new ExtraDTO();
        dtoProyector.setIdExtra(1);
        dtoProyector.setNombre("Proyector Epson X200");
        dtoProyector.setTipoExtra(TipoExtra.Proyector);

        // Extra tipo Cargador
        extraCargador = new Extra();
        extraCargador.setIdExtra(2);
        extraCargador.setNombre("Cargador USB-C 65W");
        extraCargador.setTipoExtra(TipoExtra.Cargador);

        dtoCargador = new ExtraDTO();
        dtoCargador.setIdExtra(2);
        dtoCargador.setNombre("Cargador USB-C 65W");
        dtoCargador.setTipoExtra(TipoExtra.Cargador);
    }

    // ==================== CREAR EXTRA ====================

    @Test
    @DisplayName("Crear extra con datos válidos debe guardar correctamente")
    void crearExtra_DatosValidos_DebeGuardarCorrectamente() {
        // ARRANGE
        when(extraMapper.toEntity(dtoProyector)).thenReturn(extraProyector);
        when(extraDao.save(extraProyector)).thenReturn(extraProyector);
        when(extraMapper.toDTO(extraProyector)).thenReturn(dtoProyector);

        // ACT
        ExtraDTO resultado = service.crearExtra(dtoProyector);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdExtra()).isEqualTo(1);
        assertThat(resultado.getNombre()).isEqualTo("Proyector Epson X200");
        assertThat(resultado.getTipoExtra()).isEqualTo(TipoExtra.Proyector);

        verify(extraMapper).toEntity(dtoProyector);
        verify(extraDao).save(extraProyector);
        verify(extraMapper).toDTO(extraProyector);
    }

    @Test
    @DisplayName("Crear extra tipo Cargador debe guardar correctamente")
    void crearExtra_TipoCargador_DebeGuardarCorrectamente() {
        // ARRANGE
        when(extraMapper.toEntity(dtoCargador)).thenReturn(extraCargador);
        when(extraDao.save(extraCargador)).thenReturn(extraCargador);
        when(extraMapper.toDTO(extraCargador)).thenReturn(dtoCargador);

        // ACT
        ExtraDTO resultado = service.crearExtra(dtoCargador);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTipoExtra()).isEqualTo(TipoExtra.Cargador);
        assertThat(resultado.getNombre()).isEqualTo("Cargador USB-C 65W");

        verify(extraDao).save(extraCargador);
    }

    @Test
    @DisplayName("Crear extra tipo Cafetera debe guardar correctamente")
    void crearExtra_TipoCafetera_DebeGuardarCorrectamente() {
        // ARRANGE
        ExtraDTO dtoCafetera = new ExtraDTO();
        dtoCafetera.setNombre("Cafetera Nespresso");
        dtoCafetera.setTipoExtra(TipoExtra.Cafetera);

        Extra extraCafetera = new Extra();
        extraCafetera.setIdExtra(3);
        extraCafetera.setNombre("Cafetera Nespresso");
        extraCafetera.setTipoExtra(TipoExtra.Cafetera);

        when(extraMapper.toEntity(dtoCafetera)).thenReturn(extraCafetera);
        when(extraDao.save(extraCafetera)).thenReturn(extraCafetera);
        when(extraMapper.toDTO(extraCafetera)).thenReturn(dtoCafetera);

        // ACT
        ExtraDTO resultado = service.crearExtra(dtoCafetera);

        // ASSERT
        assertThat(resultado.getTipoExtra()).isEqualTo(TipoExtra.Cafetera);
        verify(extraDao).save(extraCafetera);
    }

    // ==================== LISTAR EXTRAS ====================

    @Test
    @DisplayName("Listar extras cuando existen registros debe retornar lista completa")
    void listarExtras_ConRegistrosExistentes_DebeRetornarListaCompleta() {
        // ARRANGE
        List<Extra> extras = Arrays.asList(extraProyector, extraCargador);
        when(extraDao.findAll()).thenReturn(extras);
        when(extraMapper.toDTO(extraProyector)).thenReturn(dtoProyector);
        when(extraMapper.toDTO(extraCargador)).thenReturn(dtoCargador);

        // ACT
        List<ExtraDTO> resultado = service.listarExtras();

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Proyector Epson X200");
        assertThat(resultado.get(1).getNombre()).isEqualTo("Cargador USB-C 65W");

        verify(extraDao).findAll();
        verify(extraMapper, times(2)).toDTO(any(Extra.class));
    }

    @Test
    @DisplayName("Listar extras cuando no hay registros debe retornar lista vacía")
    void listarExtras_SinRegistros_DebeRetornarListaVacia() {
        // ARRANGE
        when(extraDao.findAll()).thenReturn(Collections.emptyList());

        // ACT
        List<ExtraDTO> resultado = service.listarExtras();

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();

        verify(extraDao).findAll();
    }

    // ==================== OBTENER POR ID ====================

    @Test
    @DisplayName("Obtener por ID existente debe retornar el extra correcto")
    void obtenerExtraPorId_IdExistente_DebeRetornarExtra() {
        // ARRANGE
        Integer idBuscado = 1;
        when(extraDao.findById(idBuscado)).thenReturn(Optional.of(extraProyector));
        when(extraMapper.toDTO(extraProyector)).thenReturn(dtoProyector);

        // ACT
        ExtraDTO resultado = service.obtenerExtraPorId(idBuscado);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdExtra()).isEqualTo(1);
        assertThat(resultado.getNombre()).isEqualTo("Proyector Epson X200");
        assertThat(resultado.getTipoExtra()).isEqualTo(TipoExtra.Proyector);

        verify(extraDao).findById(idBuscado);
        verify(extraMapper).toDTO(extraProyector);
    }

    @Test
    @DisplayName("Obtener por ID inexistente debe lanzar excepción")
    void obtenerExtraPorId_IdInexistente_DebeLanzarExcepcion() {
        // ARRANGE
        Integer idInexistente = 999;
        when(extraDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> service.obtenerExtraPorId(idInexistente))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Extra no encontrado con ID: " + idInexistente);

        verify(extraDao).findById(idInexistente);
        verify(extraMapper, never()).toDTO(any());
    }

    // ==================== ACTUALIZAR EXTRA ====================

    @Test
    @DisplayName("Actualizar extra existente debe modificar correctamente")
    void actualizarExtra_IdExistente_DebeModificarCorrectamente() {
        // ARRANGE
        Integer idActualizar = 1;
        ExtraDTO dtoActualizado = new ExtraDTO();
        dtoActualizado.setNombre("Proyector Epson X300 Actualizado");
        dtoActualizado.setTipoExtra(TipoExtra.Proyector);

        Extra extraActualizado = new Extra();
        extraActualizado.setIdExtra(1);
        extraActualizado.setNombre("Proyector Epson X300 Actualizado");
        extraActualizado.setTipoExtra(TipoExtra.Proyector);

        when(extraDao.findById(idActualizar)).thenReturn(Optional.of(extraProyector));
        doNothing().when(extraMapper).updateEntityFromDTO(dtoActualizado, extraProyector);
        when(extraDao.save(extraProyector)).thenReturn(extraActualizado);
        when(extraMapper.toDTO(extraActualizado)).thenReturn(dtoActualizado);

        // ACT
        ExtraDTO resultado = service.actualizarExtra(idActualizar, dtoActualizado);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Proyector Epson X300 Actualizado");

        verify(extraDao).findById(idActualizar);
        verify(extraMapper).updateEntityFromDTO(dtoActualizado, extraProyector);
        verify(extraDao).save(extraProyector);
    }

    @Test
    @DisplayName("Actualizar extra inexistente debe lanzar excepción")
    void actualizarExtra_IdInexistente_DebeLanzarExcepcion() {
        // ARRANGE
        Integer idInexistente = 999;
        ExtraDTO dtoActualizado = new ExtraDTO();
        dtoActualizado.setNombre("Extra Actualizado");

        when(extraDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> service.actualizarExtra(idInexistente, dtoActualizado))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Extra no encontrado con ID: " + idInexistente);

        verify(extraDao).findById(idInexistente);
        verify(extraMapper, never()).updateEntityFromDTO(any(), any());
        verify(extraDao, never()).save(any());
    }

    @Test
    @DisplayName("Actualizar solo nombre debe mantener el tipo")
    void actualizarExtra_SoloNombre_DebeMantenerTipo() {
        // ARRANGE
        Integer idActualizar = 1;
        ExtraDTO dtoConNuevoNombre = new ExtraDTO();
        dtoConNuevoNombre.setNombre("Proyector Epson Nuevo Modelo");
        dtoConNuevoNombre.setTipoExtra(TipoExtra.Proyector);

        when(extraDao.findById(idActualizar)).thenReturn(Optional.of(extraProyector));
        doNothing().when(extraMapper).updateEntityFromDTO(dtoConNuevoNombre, extraProyector);
        when(extraDao.save(extraProyector)).thenReturn(extraProyector);
        when(extraMapper.toDTO(extraProyector)).thenReturn(dtoConNuevoNombre);

        // ACT
        ExtraDTO resultado = service.actualizarExtra(idActualizar, dtoConNuevoNombre);

        // ASSERT
        assertThat(resultado.getTipoExtra()).isEqualTo(TipoExtra.Proyector);
        verify(extraMapper).updateEntityFromDTO(dtoConNuevoNombre, extraProyector);
    }

    // ==================== ELIMINAR EXTRA ====================

    @Test
    @DisplayName("Eliminar extra existente debe eliminarlo correctamente")
    void eliminarExtra_IdExistente_DebeEliminarCorrectamente() {
        // ARRANGE
        Integer idEliminar = 1;
        when(extraDao.findById(idEliminar)).thenReturn(Optional.of(extraProyector));
        doNothing().when(extraDao).delete(extraProyector);

        // ACT
        service.eliminarExtra(idEliminar);

        // ASSERT
        verify(extraDao).findById(idEliminar);
        verify(extraDao).delete(extraProyector);
    }

    @Test
    @DisplayName("Eliminar extra inexistente debe lanzar excepción")
    void eliminarExtra_IdInexistente_DebeLanzarExcepcion() {
        // ARRANGE
        Integer idInexistente = 999;
        when(extraDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> service.eliminarExtra(idInexistente))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Extra no encontrado con ID: " + idInexistente);

        verify(extraDao).findById(idInexistente);
        verify(extraDao, never()).delete(any());
    }

    // ==================== TESTS ADICIONALES ====================

    @Test
    @DisplayName("Crear extra tipo Extension debe guardar correctamente")
    void crearExtra_TipoExtension_DebeGuardarCorrectamente() {
        // ARRANGE
        ExtraDTO dtoExtension = new ExtraDTO();
        dtoExtension.setNombre("Extensión eléctrica 5m");
        dtoExtension.setTipoExtra(TipoExtra.Extension);

        Extra extraExtension = new Extra();
        extraExtension.setIdExtra(4);
        extraExtension.setNombre("Extensión eléctrica 5m");
        extraExtension.setTipoExtra(TipoExtra.Extension);

        when(extraMapper.toEntity(dtoExtension)).thenReturn(extraExtension);
        when(extraDao.save(extraExtension)).thenReturn(extraExtension);
        when(extraMapper.toDTO(extraExtension)).thenReturn(dtoExtension);

        // ACT
        ExtraDTO resultado = service.crearExtra(dtoExtension);

        // ASSERT
        assertThat(resultado.getTipoExtra()).isEqualTo(TipoExtra.Extension);
        assertThat(resultado.getNombre()).isEqualTo("Extensión eléctrica 5m");
        verify(extraDao).save(extraExtension);
    }

    @Test
    @DisplayName("Listar extras debe usar el mapper correctamente")
    void listarExtras_DebeUsarMapperCorrectamente() {
        // ARRANGE
        List<Extra> extras = Arrays.asList(extraProyector);
        when(extraDao.findAll()).thenReturn(extras);
        when(extraMapper.toDTO(extraProyector)).thenReturn(dtoProyector);

        // ACT
        List<ExtraDTO> resultado = service.listarExtras();

        // ASSERT
        assertThat(resultado).hasSize(1);
        verify(extraMapper).toDTO(extraProyector);
    }
}