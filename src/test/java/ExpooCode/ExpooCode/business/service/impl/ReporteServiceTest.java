package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.ReporteDTO;
import ExpooCode.ExpooCode.persistence.dao.ReporteDao;
import ExpooCode.ExpooCode.persistence.dao.UsuarioDao;
import ExpooCode.ExpooCode.persistence.entity.Reporte;
import ExpooCode.ExpooCode.persistence.entity.Usuario;
import ExpooCode.ExpooCode.persistence.enums.TipoReporte;
import ExpooCode.ExpooCode.persistence.mapper.ReporteMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ReporteServiceImpl")
public class ReporteServiceTest {

    @Mock
    private ReporteDao reporteDao;

    @Mock
    private ReporteMapper reporteMapper;

    @Mock
    private UsuarioDao usuarioDao;

    @InjectMocks
    private ReporteServiceImpl reporteService;

    private Reporte reporte;
    private ReporteDTO reporteDTO;
    private Usuario usuario;
    private List<Reporte> reporteList;
    private List<ReporteDTO> reporteDTOList;

    @BeforeEach
    public void setUp() {
        // Given - Preparar datos de prueba
        usuario = new Usuario();
        usuario.setIdUsuario(12L);
        usuario.setNombre("Admin Usuario");
        usuario.setEmail("admin@email.com");

        reporte = new Reporte();
        reporte.setIdReporte(101L);
        reporte.setUsuario(usuario);
        reporte.setTipo(TipoReporte.Ocupacion);
        reporte.setFechaGeneracion(LocalDateTime.of(2025, 4, 5, 9, 0));
        reporte.setContenido("Información detallada del reporte");

        reporteDTO = new ReporteDTO();
        reporteDTO.setIdReporte(101L);
        reporteDTO.setIdUsuario(12L);
        reporteDTO.setTipo(TipoReporte.Ocupacion);
        reporteDTO.setFechaGeneracion(LocalDateTime.of(2025, 4, 5, 9, 0));
        reporteDTO.setContenido("Información detallada del reporte");

        reporteList = Arrays.asList(reporte);
        reporteDTOList = Arrays.asList(reporteDTO);
    }

    // ==================== GENERAR REPORTE ====================

    @Test
    @DisplayName("generarReporte - Debe generar reporte exitosamente con contenido")
    public void generarReporte_DebeGenerarReporteConContenido() {
        // Given
        when(reporteMapper.toEntity(reporteDTO)).thenReturn(reporte);
        when(usuarioDao.findById(12L)).thenReturn(Optional.of(usuario));
        when(reporteDao.save(any(Reporte.class))).thenReturn(reporte);
        when(reporteMapper.toDTO(reporte)).thenReturn(reporteDTO);

        // When
        ReporteDTO resultado = reporteService.generarReporte(reporteDTO);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdReporte()).isEqualTo(101L);
        assertThat(resultado.getIdUsuario()).isEqualTo(12L);
        verify(usuarioDao).findById(12L);
        verify(reporteDao).save(any(Reporte.class));
        verify(reporteMapper).toDTO(reporte);
    }

    @Test
    @DisplayName("generarReporte - Debe generar reporte con contenido por defecto cuando es null")
    public void generarReporte_DebeGenerarReporteConContenidoPorDefecto() {
        // Given
        Reporte reporteSinContenido = new Reporte();
        reporteSinContenido.setIdReporte(101L);
        reporteSinContenido.setUsuario(usuario);
        reporteSinContenido.setTipo(TipoReporte.Ocupacion);
        reporteSinContenido.setContenido(null);

        when(reporteMapper.toEntity(reporteDTO)).thenReturn(reporteSinContenido);
        when(usuarioDao.findById(12L)).thenReturn(Optional.of(usuario));
        when(reporteDao.save(any(Reporte.class))).thenReturn(reporte);
        when(reporteMapper.toDTO(reporte)).thenReturn(reporteDTO);

        // When
        ReporteDTO resultado = reporteService.generarReporte(reporteDTO);

        // Then
        assertThat(resultado).isNotNull();
        verify(usuarioDao).findById(12L);
        verify(reporteDao).save(any(Reporte.class));
    }

    @Test
    @DisplayName("generarReporte - Debe generar reporte con contenido por defecto cuando es vacío")
    public void generarReporte_DebeGenerarReporteConContenidoPorDefectoCuandoVacio() {
        // Given
        Reporte reporteContenidoVacio = new Reporte();
        reporteContenidoVacio.setIdReporte(101L);
        reporteContenidoVacio.setUsuario(usuario);
        reporteContenidoVacio.setTipo(TipoReporte.Ingresos);
        reporteContenidoVacio.setContenido("");

        when(reporteMapper.toEntity(reporteDTO)).thenReturn(reporteContenidoVacio);
        when(usuarioDao.findById(12L)).thenReturn(Optional.of(usuario));
        when(reporteDao.save(any(Reporte.class))).thenReturn(reporte);
        when(reporteMapper.toDTO(reporte)).thenReturn(reporteDTO);

        // When
        ReporteDTO resultado = reporteService.generarReporte(reporteDTO);

        // Then
        assertThat(resultado).isNotNull();
        verify(usuarioDao).findById(12L);
        verify(reporteDao).save(any(Reporte.class));
    }

    @Test
    @DisplayName("generarReporte - Debe lanzar excepción cuando el usuario no existe")
    public void generarReporte_DebeLanzarExcepcionCuandoUsuarioNoExiste() {
        // Given
        when(reporteMapper.toEntity(reporteDTO)).thenReturn(reporte);
        when(usuarioDao.findById(12L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reporteService.generarReporte(reporteDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado con id: 12");

        verify(usuarioDao).findById(12L);
        verify(reporteDao, never()).save(any());
    }

    // ==================== LISTAR REPORTES ====================

    @Test
    @DisplayName("listarReportes - Debe retornar lista de reportes exitosamente")
    public void listarReportes_DebeRetornarListaDeReportes() {
        // Given
        when(reporteDao.findAll()).thenReturn(reporteList);
        when(reporteMapper.toDTO(reporte)).thenReturn(reporteDTO);

        // When
        List<ReporteDTO> resultado = reporteService.listarReportes();

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdReporte()).isEqualTo(101L);
        verify(reporteDao).findAll();
        verify(reporteMapper, times(1)).toDTO(any(Reporte.class));
    }

    @Test
    @DisplayName("listarReportes - Debe retornar lista vacía cuando no hay reportes")
    public void listarReportes_DebeRetornarListaVaciaCuandoNoHayReportes() {
        // Given
        when(reporteDao.findAll()).thenReturn(Arrays.asList());

        // When
        List<ReporteDTO> resultado = reporteService.listarReportes();

        // Then
        assertThat(resultado).isEmpty();
        verify(reporteDao).findAll();
        verify(reporteMapper, never()).toDTO(any());
    }

    // ==================== OBTENER REPORTE ====================

    @Test
    @DisplayName("obtenerReporte - Debe retornar reporte por ID exitosamente")
    public void obtenerReporte_DebeRetornarReporte() {
        // Given
        when(reporteDao.findById(101L)).thenReturn(Optional.of(reporte));
        when(reporteMapper.toDTO(reporte)).thenReturn(reporteDTO);

        // When
        ReporteDTO resultado = reporteService.obtenerReporte(101L);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdReporte()).isEqualTo(101L);
        assertThat(resultado.getTipo()).isEqualTo(TipoReporte.Ocupacion);
        verify(reporteDao).findById(101L);
        verify(reporteMapper).toDTO(reporte);
    }

    @Test
    @DisplayName("obtenerReporte - Debe retornar null cuando el reporte no existe")
    public void obtenerReporte_DebeRetornarNullCuandoNoExiste() {
        // Given
        when(reporteDao.findById(999L)).thenReturn(Optional.empty());

        // When
        ReporteDTO resultado = reporteService.obtenerReporte(999L);

        // Then
        assertThat(resultado).isNull();
        verify(reporteDao).findById(999L);
        verify(reporteMapper, never()).toDTO(any());
    }

    // ==================== ELIMINAR REPORTE ====================

    @Test
    @DisplayName("eliminarReporte - Debe eliminar reporte exitosamente")
    public void eliminarReporte_DebeEliminarReporteExitosamente() {
        // Given
        when(reporteDao.findById(101L)).thenReturn(Optional.of(reporte));
        doNothing().when(reporteDao).delete(reporte);

        // When
        boolean resultado = reporteService.eliminarReporte(101L);

        // Then
        assertThat(resultado).isTrue();
        verify(reporteDao).findById(101L);
        verify(reporteDao).delete(reporte);
    }

    @Test
    @DisplayName("eliminarReporte - Debe retornar false cuando el reporte no existe")
    public void eliminarReporte_DebeRetornarFalseCuandoNoExiste() {
        // Given
        when(reporteDao.findById(999L)).thenReturn(Optional.empty());

        // When
        boolean resultado = reporteService.eliminarReporte(999L);

        // Then
        assertThat(resultado).isFalse();
        verify(reporteDao).findById(999L);
        verify(reporteDao, never()).delete(any());
    }


    // ==================== GENERAR REPORTE OCUPACION ====================

    @Test
    @DisplayName("generarReporteOcupacion - Debe generar reporte de ocupación exitosamente")
    public void generarReporteOcupacion_DebeGenerarReporteExitosamente() {
        // Given
        ReporteDTO reporteEntrada = new ReporteDTO();
        reporteEntrada.setIdUsuario(12L);
        reporteEntrada.setTipo(TipoReporte.Ocupacion);
        reporteEntrada.setContenido("");

        Reporte reporteOcupacion = new Reporte();
        reporteOcupacion.setIdReporte(102L);
        reporteOcupacion.setUsuario(usuario);
        reporteOcupacion.setTipo(TipoReporte.Ocupacion);
        reporteOcupacion.setContenido("Reporte de tipo Ocupacion generado automáticamente");
        reporteOcupacion.setFechaGeneracion(LocalDateTime.now());

        ReporteDTO reporteOcupacionDTO = new ReporteDTO();
        reporteOcupacionDTO.setIdReporte(102L);
        reporteOcupacionDTO.setIdUsuario(12L);
        reporteOcupacionDTO.setTipo(TipoReporte.Ocupacion);
        reporteOcupacionDTO.setContenido("Reporte de tipo Ocupacion generado automáticamente");

        when(reporteMapper.toEntity(reporteEntrada)).thenReturn(new Reporte());
        when(usuarioDao.findById(12L)).thenReturn(Optional.of(usuario));
        when(reporteDao.save(any(Reporte.class))).thenReturn(reporteOcupacion);
        when(reporteMapper.toDTO(reporteOcupacion)).thenReturn(reporteOcupacionDTO);

        // When
        ReporteDTO resultado = reporteService.generarReporteOcupacion(reporteEntrada);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdReporte()).isEqualTo(102L);
        assertThat(resultado.getTipo()).isEqualTo(TipoReporte.Ocupacion);
        assertThat(resultado.getContenido()).contains("Ocupacion");
        verify(reporteMapper).toEntity(reporteEntrada);
        verify(usuarioDao).findById(12L);
        verify(reporteDao).save(any(Reporte.class));
        verify(reporteMapper).toDTO(reporteOcupacion);
    }

    @Test
    @DisplayName("generarReporteOcupacion - Debe lanzar excepción cuando usuario no existe")
    public void generarReporteOcupacion_DebeLanzarExcepcionCuandoUsuarioNoExiste() {
        // Given
        ReporteDTO reporteEntrada = new ReporteDTO();
        reporteEntrada.setIdUsuario(999L);
        reporteEntrada.setTipo(TipoReporte.Ocupacion);

        when(reporteMapper.toEntity(reporteEntrada)).thenReturn(new Reporte());
        when(usuarioDao.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reporteService.generarReporteOcupacion(reporteEntrada))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado con id: 999");

        verify(usuarioDao).findById(999L);
        verify(reporteDao, never()).save(any());
    }


    // ==================== GENERAR REPORTE INGRESOS ====================

    // ==================== GENERAR REPORTE INGRESOS ====================

    @Test
    @DisplayName("generarReporteIngresos - Debe generar reporte de ingresos exitosamente")
    public void generarReporteIngresos_DebeGenerarReporteExitosamente() {
        // Given
        ReporteDTO reporteEntrada = new ReporteDTO();
        reporteEntrada.setIdUsuario(12L);
        reporteEntrada.setTipo(TipoReporte.Ingresos);
        reporteEntrada.setContenido("");

        Reporte reporteIngresos = new Reporte();
        reporteIngresos.setIdReporte(103L);
        reporteIngresos.setUsuario(usuario);
        reporteIngresos.setTipo(TipoReporte.Ingresos);
        reporteIngresos.setContenido("Reporte de tipo Ingresos generado automáticamente");
        reporteIngresos.setFechaGeneracion(LocalDateTime.now());

        ReporteDTO reporteIngresosDTO = new ReporteDTO();
        reporteIngresosDTO.setIdReporte(103L);
        reporteIngresosDTO.setIdUsuario(12L);
        reporteIngresosDTO.setTipo(TipoReporte.Ingresos);
        reporteIngresosDTO.setContenido("Reporte de tipo Ingresos generado automáticamente");

        when(reporteMapper.toEntity(reporteEntrada)).thenReturn(new Reporte());
        when(usuarioDao.findById(12L)).thenReturn(Optional.of(usuario));
        when(reporteDao.save(any(Reporte.class))).thenReturn(reporteIngresos);
        when(reporteMapper.toDTO(reporteIngresos)).thenReturn(reporteIngresosDTO);

        // When
        ReporteDTO resultado = reporteService.generarReporteIngresos(reporteEntrada);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdReporte()).isEqualTo(103L);
        assertThat(resultado.getTipo()).isEqualTo(TipoReporte.Ingresos);
        assertThat(resultado.getContenido()).contains("Ingresos");
        verify(reporteMapper).toEntity(reporteEntrada);
        verify(usuarioDao).findById(12L);
        verify(reporteDao).save(any(Reporte.class));
        verify(reporteMapper).toDTO(reporteIngresos);
    }

    @Test
    @DisplayName("generarReporteIngresos - Debe lanzar excepción cuando usuario no existe")
    public void generarReporteIngresos_DebeLanzarExcepcionCuandoUsuarioNoExiste() {
        // Given
        ReporteDTO reporteEntrada = new ReporteDTO();
        reporteEntrada.setIdUsuario(999L);
        reporteEntrada.setTipo(TipoReporte.Ingresos);

        when(reporteMapper.toEntity(reporteEntrada)).thenReturn(new Reporte());
        when(usuarioDao.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reporteService.generarReporteIngresos(reporteEntrada))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado con id: 999");

        verify(usuarioDao).findById(999L);
        verify(reporteDao, never()).save(any());
    }
    // ==================== TESTS ADICIONALES ====================

    @Test
    @DisplayName("generarReporte - Debe setear fecha de generación automáticamente")
    public void generarReporte_DebeSetearFechaAutomaticamente() {
        // Given
        when(reporteMapper.toEntity(reporteDTO)).thenReturn(reporte);
        when(usuarioDao.findById(12L)).thenReturn(Optional.of(usuario));
        when(reporteDao.save(any(Reporte.class))).thenAnswer(invocation -> {
            Reporte saved = invocation.getArgument(0);
            assertThat(saved.getFechaGeneracion()).isNotNull();
            return saved;
        });
        when(reporteMapper.toDTO(any(Reporte.class))).thenReturn(reporteDTO);

        // When
        ReporteDTO resultado = reporteService.generarReporte(reporteDTO);

        // Then
        assertThat(resultado).isNotNull();
        verify(reporteDao).save(any(Reporte.class));
    }

    @Test
    @DisplayName("listarReportes - Debe retornar múltiples reportes")
    public void listarReportes_DebeRetornarMultiplesReportes() {
        // Given
        Reporte reporte2 = new Reporte();
        reporte2.setIdReporte(102L);
        reporte2.setUsuario(usuario);
        reporte2.setTipo(TipoReporte.Ingresos);

        ReporteDTO reporteDTO2 = new ReporteDTO();
        reporteDTO2.setIdReporte(102L);
        reporteDTO2.setTipo(TipoReporte.Ingresos);

        List<Reporte> multipleReportes = Arrays.asList(reporte, reporte2);

        when(reporteDao.findAll()).thenReturn(multipleReportes);
        when(reporteMapper.toDTO(reporte)).thenReturn(reporteDTO);
        when(reporteMapper.toDTO(reporte2)).thenReturn(reporteDTO2);

        // When
        List<ReporteDTO> resultado = reporteService.listarReportes();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getIdReporte()).isEqualTo(101L);
        assertThat(resultado.get(1).getIdReporte()).isEqualTo(102L);
        verify(reporteDao).findAll();
    }
}