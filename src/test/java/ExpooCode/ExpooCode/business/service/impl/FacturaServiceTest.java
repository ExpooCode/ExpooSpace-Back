package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.FacturaDTO;
import ExpooCode.ExpooCode.persistence.dao.FacturaDao;
import ExpooCode.ExpooCode.persistence.dao.PagoDao;
import ExpooCode.ExpooCode.persistence.entity.Factura;
import ExpooCode.ExpooCode.persistence.entity.Pago;
import ExpooCode.ExpooCode.persistence.enums.EstadoPago;
import ExpooCode.ExpooCode.persistence.enums.MetodoPago;
import ExpooCode.ExpooCode.persistence.enums.TipoFactura;
import ExpooCode.ExpooCode.persistence.mapper.FacturaMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests para FacturaServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias (FacturaDao, PagoDao, Mapper)
 * - Ejecución rápida
 * - Configurado para JaCoCo (cobertura) y SonarQube (calidad)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FacturaService - Unit Tests")
public class FacturaServiceTest {

    // ==================== DEPENDENCIAS MOCKEADAS ====================
    @Mock
    private FacturaDao facturaDao;

    @Mock
    private FacturaMapper facturaMapper;

    @Mock
    private PagoDao pagoDao;

    // ==================== CLASE BAJO PRUEBA ====================
    @InjectMocks
    private FacturaServiceImpl facturaService;

    // ==================== DATOS DE PRUEBA ====================
    private FacturaDTO validFacturaDTO;
    private Factura validFacturaEntity;
    private Pago validPago;
    private Long validFacturaId;
    private Long validPagoId;
    private Long validUsuarioId;
    private LocalDateTime fixedDateTime;

    /**
     * Configuración ejecutada ANTES de cada test
     * Inicializa datos comunes reutilizables
     */
    @BeforeEach
    void setUp() {
        validFacturaId = 9001L;
        validPagoId = 501L;
        validUsuarioId = 12L;
        fixedDateTime = LocalDateTime.of(2025, 8, 20, 11, 30, 0);

        // Pago válido
        validPago = new Pago();
        validPago.setIdPago(validPagoId);
        validPago.setMonto(new BigDecimal("142800.00"));
        validPago.setMetodo(MetodoPago.Tarjeta);
        validPago.setEstado(EstadoPago.Pagado);
        validPago.setFechaPago(fixedDateTime);

        // DTO válido para crear factura
        validFacturaDTO = new FacturaDTO(
                null, // ID null para CREATE
                validPagoId,
                null, // numeroFactura generado automáticamente
                null, // fechaEmision generada automáticamente
                TipoFactura.Reserva,
                new BigDecimal("120000.00"),
                new BigDecimal("22800.00"),
                new BigDecimal("142800.00"),
                null // urlDescarga generada automáticamente
        );

        // Entidad válida para simular respuestas del DAO
        validFacturaEntity = new Factura();
        validFacturaEntity.setIdFactura(null);
        validFacturaEntity.setPago(validPago);
        validFacturaEntity.setNumeroFactura("FAC001");
        validFacturaEntity.setFechaEmision(fixedDateTime);
        validFacturaEntity.setTipo(TipoFactura.Reserva);
        validFacturaEntity.setSubtotal(new BigDecimal("120000.00"));
        validFacturaEntity.setIva(new BigDecimal("22800.00"));
        validFacturaEntity.setTotal(new BigDecimal("142800.00"));
        validFacturaEntity.setUrlDescarga("urldescarga");
    }

    // ==================== CREATE FACTURA TESTS ====================

    @Test
    @DisplayName("CREATE - Factura válida debe retornar factura creada con ID")
    void createFactura_DatosValidos_DebeRetornarFacturaCreada() {
        // ARRANGE
        Factura savedEntity = new Factura();
        savedEntity.setIdFactura(validFacturaId);
        savedEntity.setPago(validPago);
        savedEntity.setNumeroFactura("FAC+" + validFacturaId + fixedDateTime);
        savedEntity.setFechaEmision(fixedDateTime);
        savedEntity.setTipo(TipoFactura.Reserva);
        savedEntity.setSubtotal(new BigDecimal("120000.00"));
        savedEntity.setIva(new BigDecimal("22800.00"));
        savedEntity.setTotal(new BigDecimal("142800.00"));
        savedEntity.setUrlDescarga("urldescarga");

        FacturaDTO expectedDTO = new FacturaDTO(
                validFacturaId,
                validPagoId,
                "FAC+" + validFacturaId + fixedDateTime,
                fixedDateTime,
                TipoFactura.Reserva,
                new BigDecimal("120000.00"),
                new BigDecimal("22800.00"),
                new BigDecimal("142800.00"),
                "urldescarga"
        );

        when(facturaMapper.toEntity(validFacturaDTO)).thenReturn(validFacturaEntity);
        when(pagoDao.findById(validPagoId)).thenReturn(Optional.of(validPago));
        when(facturaDao.save(any(Factura.class))).thenReturn(savedEntity);
        when(facturaMapper.toDTO(savedEntity)).thenReturn(expectedDTO);

        // ACT
        FacturaDTO result = facturaService.createFactura(validFacturaDTO);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getIdFactura()).isEqualTo(validFacturaId);
        assertThat(result.getIdPago()).isEqualTo(validPagoId);
        assertThat(result.getTipo()).isEqualTo(TipoFactura.Reserva);
        assertThat(result.getSubtotal()).isEqualByComparingTo(new BigDecimal("120000.00"));
        assertThat(result.getIva()).isEqualByComparingTo(new BigDecimal("22800.00"));
        assertThat(result.getTotal()).isEqualByComparingTo(new BigDecimal("142800.00"));
        assertThat(result.getNumeroFactura()).isNotNull();
        assertThat(result.getFechaEmision()).isNotNull();
        assertThat(result.getUrlDescarga()).isNotNull();

        verify(facturaMapper, times(1)).toEntity(validFacturaDTO);
        verify(pagoDao, times(1)).findById(validPagoId);
        verify(facturaDao, times(1)).save(any(Factura.class));
        verify(facturaMapper, times(1)).toDTO(savedEntity);
    }

    @Test
    @DisplayName("CREATE - Factura debe generar número de factura automáticamente")
    void createFactura_DebeGenerarNumeroFactura() {
        // ARRANGE
        Factura savedEntity = new Factura();
        savedEntity.setIdFactura(validFacturaId);
        savedEntity.setPago(validPago);
        savedEntity.setFechaEmision(fixedDateTime);

        FacturaDTO expectedDTO = new FacturaDTO();
        expectedDTO.setNumeroFactura("FAC+" + validFacturaId + fixedDateTime);

        when(facturaMapper.toEntity(validFacturaDTO)).thenReturn(validFacturaEntity);
        when(pagoDao.findById(validPagoId)).thenReturn(Optional.of(validPago));
        when(facturaDao.save(any(Factura.class))).thenAnswer(invocation -> {
            Factura factura = invocation.getArgument(0);
            assertThat(factura.getNumeroFactura()).isNotNull();
            assertThat(factura.getNumeroFactura()).startsWith("FAC+");
            savedEntity.setNumeroFactura(factura.getNumeroFactura());
            return savedEntity;
        });
        when(facturaMapper.toDTO(any(Factura.class))).thenReturn(expectedDTO);

        // ACT
        FacturaDTO result = facturaService.createFactura(validFacturaDTO);

        // ASSERT
        assertThat(result.getNumeroFactura()).isNotNull();
        verify(facturaDao, times(1)).save(argThat(f -> f.getNumeroFactura() != null));
    }

    @Test
    @DisplayName("CREATE - Factura debe asignar fecha de emisión automáticamente")
    void createFactura_DebeAsignarFechaEmision() {
        // ARRANGE
        Factura savedEntity = new Factura();
        savedEntity.setIdFactura(validFacturaId);
        savedEntity.setPago(validPago);

        FacturaDTO expectedDTO = new FacturaDTO();
        expectedDTO.setFechaEmision(LocalDateTime.now());

        when(facturaMapper.toEntity(validFacturaDTO)).thenReturn(validFacturaEntity);
        when(pagoDao.findById(validPagoId)).thenReturn(Optional.of(validPago));
        when(facturaDao.save(any(Factura.class))).thenAnswer(invocation -> {
            Factura factura = invocation.getArgument(0);
            assertThat(factura.getFechaEmision()).isNotNull();
            savedEntity.setFechaEmision(factura.getFechaEmision());
            return savedEntity;
        });
        when(facturaMapper.toDTO(any(Factura.class))).thenReturn(expectedDTO);

        // ACT
        FacturaDTO result = facturaService.createFactura(validFacturaDTO);

        // ASSERT
        assertThat(result.getFechaEmision()).isNotNull();
        verify(facturaDao, times(1)).save(argThat(f -> f.getFechaEmision() != null));
    }

    @Test
    @DisplayName("CREATE - Factura debe asignar URL de descarga automáticamente")
    void createFactura_DebeAsignarUrlDescarga() {
        // ARRANGE
        Factura savedEntity = new Factura();
        savedEntity.setIdFactura(validFacturaId);
        savedEntity.setPago(validPago);

        FacturaDTO expectedDTO = new FacturaDTO();
        expectedDTO.setUrlDescarga("urldescarga");

        when(facturaMapper.toEntity(validFacturaDTO)).thenReturn(validFacturaEntity);
        when(pagoDao.findById(validPagoId)).thenReturn(Optional.of(validPago));
        when(facturaDao.save(any(Factura.class))).thenAnswer(invocation -> {
            Factura factura = invocation.getArgument(0);
            assertThat(factura.getUrlDescarga()).isEqualTo("urldescarga");
            savedEntity.setUrlDescarga(factura.getUrlDescarga());
            return savedEntity;
        });
        when(facturaMapper.toDTO(any(Factura.class))).thenReturn(expectedDTO);

        // ACT
        FacturaDTO result = facturaService.createFactura(validFacturaDTO);

        // ASSERT
        assertThat(result.getUrlDescarga()).isNotNull();
        assertThat(result.getUrlDescarga()).isEqualTo("urldescarga");
    }

    @Test
    @DisplayName("CREATE - Pago inexistente debe lanzar EntityNotFoundException")
    void createFactura_PagoInexistente_DebeLanzarException() {
        // ARRANGE
        Long idPagoInexistente = 999L;
        validFacturaDTO.setIdPago(idPagoInexistente);

        when(facturaMapper.toEntity(validFacturaDTO)).thenReturn(validFacturaEntity);
        when(pagoDao.findById(idPagoInexistente)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> facturaService.createFactura(validFacturaDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Pago no encontrado con id: " + idPagoInexistente);

        verify(pagoDao, times(1)).findById(idPagoInexistente);
        verify(facturaDao, never()).save(any());
        verify(facturaMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("CREATE - Factura tipo Suscripción debe crearse correctamente")
    void createFactura_TipoSuscripcion_DebeCrearse() {
        // ARRANGE
        validFacturaDTO.setTipo(TipoFactura.Suscripcion);
        validFacturaEntity.setTipo(TipoFactura.Suscripcion);

        Factura savedEntity = new Factura();
        savedEntity.setIdFactura(validFacturaId);
        savedEntity.setPago(validPago);
        savedEntity.setTipo(TipoFactura.Suscripcion);
        savedEntity.setNumeroFactura("FAC001");
        savedEntity.setFechaEmision(fixedDateTime);
        savedEntity.setSubtotal(new BigDecimal("120000.00"));
        savedEntity.setIva(new BigDecimal("22800.00"));
        savedEntity.setTotal(new BigDecimal("142800.00"));
        savedEntity.setUrlDescarga("urldescarga");

        FacturaDTO expectedDTO = new FacturaDTO(
                validFacturaId,
                validPagoId,
                "FAC001",
                fixedDateTime,
                TipoFactura.Suscripcion,
                new BigDecimal("120000.00"),
                new BigDecimal("22800.00"),
                new BigDecimal("142800.00"),
                "urldescarga"
        );

        when(facturaMapper.toEntity(validFacturaDTO)).thenReturn(validFacturaEntity);
        when(pagoDao.findById(validPagoId)).thenReturn(Optional.of(validPago));
        when(facturaDao.save(any(Factura.class))).thenReturn(savedEntity);
        when(facturaMapper.toDTO(savedEntity)).thenReturn(expectedDTO);

        // ACT
        FacturaDTO result = facturaService.createFactura(validFacturaDTO);

        // ASSERT
        assertThat(result.getTipo()).isEqualTo(TipoFactura.Suscripcion);
    }

    @Test
    @DisplayName("CREATE - Factura con montos decimales debe crearse correctamente")
    void createFactura_MontosDecimales_DebeCrearse() {
        // ARRANGE
        BigDecimal subtotal = new BigDecimal("99999.99");
        BigDecimal iva = new BigDecimal("19000.00");
        BigDecimal total = new BigDecimal("118999.99");

        validFacturaDTO.setSubtotal(subtotal);
        validFacturaDTO.setIva(iva);
        validFacturaDTO.setTotal(total);

        validFacturaEntity.setSubtotal(subtotal);
        validFacturaEntity.setIva(iva);
        validFacturaEntity.setTotal(total);

        Factura savedEntity = new Factura();
        savedEntity.setIdFactura(validFacturaId);
        savedEntity.setPago(validPago);
        savedEntity.setNumeroFactura("FAC001");
        savedEntity.setFechaEmision(fixedDateTime);
        savedEntity.setTipo(TipoFactura.Reserva);
        savedEntity.setSubtotal(subtotal);
        savedEntity.setIva(iva);
        savedEntity.setTotal(total);
        savedEntity.setUrlDescarga("urldescarga");

        FacturaDTO expectedDTO = new FacturaDTO(
                validFacturaId,
                validPagoId,
                "FAC001",
                fixedDateTime,
                TipoFactura.Reserva,
                subtotal,
                iva,
                total,
                "urldescarga"
        );

        when(facturaMapper.toEntity(validFacturaDTO)).thenReturn(validFacturaEntity);
        when(pagoDao.findById(validPagoId)).thenReturn(Optional.of(validPago));
        when(facturaDao.save(any(Factura.class))).thenReturn(savedEntity);
        when(facturaMapper.toDTO(savedEntity)).thenReturn(expectedDTO);

        // ACT
        FacturaDTO result = facturaService.createFactura(validFacturaDTO);

        // ASSERT
        assertThat(result.getSubtotal()).isEqualByComparingTo(subtotal);
        assertThat(result.getIva()).isEqualByComparingTo(iva);
        assertThat(result.getTotal()).isEqualByComparingTo(total);
    }

    // ==================== READ FACTURA BY ID TESTS ====================

    @Test
    @DisplayName("READ - Factura existente debe retornar Optional con DTO")
    void getFacturaById_IdExistente_DebeRetornarOptionalConDTO() {
        // ARRANGE
        Factura entity = new Factura();
        entity.setIdFactura(validFacturaId);
        entity.setPago(validPago);
        entity.setNumeroFactura("FAC001");
        entity.setFechaEmision(fixedDateTime);
        entity.setTipo(TipoFactura.Reserva);
        entity.setSubtotal(new BigDecimal("120000.00"));
        entity.setIva(new BigDecimal("22800.00"));
        entity.setTotal(new BigDecimal("142800.00"));
        entity.setUrlDescarga("urldescarga");

        FacturaDTO expectedDTO = new FacturaDTO(
                validFacturaId,
                validPagoId,
                "FAC001",
                fixedDateTime,
                TipoFactura.Reserva,
                new BigDecimal("120000.00"),
                new BigDecimal("22800.00"),
                new BigDecimal("142800.00"),
                "urldescarga"
        );

        when(facturaDao.findById(validFacturaId)).thenReturn(Optional.of(entity));
        when(facturaMapper.toDTO(entity)).thenReturn(expectedDTO);

        // ACT
        Optional<FacturaDTO> result = facturaService.getFacturaById(validFacturaId);

        // ASSERT
        assertThat(result).isPresent();
        assertThat(result.get().getIdFactura()).isEqualTo(validFacturaId);
        assertThat(result.get().getNumeroFactura()).isEqualTo("FAC001");
        assertThat(result.get().getTipo()).isEqualTo(TipoFactura.Reserva);

        verify(facturaDao, times(1)).findById(validFacturaId);
        verify(facturaMapper, times(1)).toDTO(entity);
    }

    @Test
    @DisplayName("READ - Factura inexistente debe retornar Optional vacío")
    void getFacturaById_IdInexistente_DebeRetornarOptionalVacio() {
        // ARRANGE
        Long idInexistente = 999L;
        when(facturaDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        Optional<FacturaDTO> result = facturaService.getFacturaById(idInexistente);

        // ASSERT
        assertThat(result).isEmpty();

        verify(facturaDao, times(1)).findById(idInexistente);
        verify(facturaMapper, never()).toDTO(any());
    }

    // ==================== LIST ALL FACTURAS TESTS ====================

    @Test
    @DisplayName("LIST ALL - Debe retornar lista de facturas cuando existen datos")
    void getAllFacturas_ConDatos_DebeRetornarLista() {
        // ARRANGE
        Factura factura1 = createFacturaEntity(1L, "FAC001", TipoFactura.Reserva);
        Factura factura2 = createFacturaEntity(2L, "FAC002", TipoFactura.Suscripcion);
        Factura factura3 = createFacturaEntity(3L, "FAC003", TipoFactura.Reserva);

        List<Factura> entities = new ArrayList<>(Arrays.asList(factura1, factura2, factura3));

        FacturaDTO dto1 = new FacturaDTO(1L, validPagoId, "FAC001", fixedDateTime, TipoFactura.Reserva,
                new BigDecimal("100000"), new BigDecimal("19000"), new BigDecimal("119000"), "url1");
        FacturaDTO dto2 = new FacturaDTO(2L, validPagoId, "FAC002", fixedDateTime, TipoFactura.Suscripcion,
                new BigDecimal("200000"), new BigDecimal("38000"), new BigDecimal("238000"), "url2");
        FacturaDTO dto3 = new FacturaDTO(3L, validPagoId, "FAC003", fixedDateTime, TipoFactura.Reserva,
                new BigDecimal("150000"), new BigDecimal("28500"), new BigDecimal("178500"), "url3");

        List<FacturaDTO> expectedDTOs = new ArrayList<>(Arrays.asList(dto1, dto2, dto3));

        when(facturaDao.findAll()).thenReturn(entities);
        when(facturaMapper.toDTOList(entities)).thenReturn(expectedDTOs);

        // ACT
        List<FacturaDTO> result = facturaService.getAllFacturas();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).extracting("numeroFactura")
                .containsExactly("FAC001", "FAC002", "FAC003");
        assertThat(result).extracting("tipo")
                .containsExactly(TipoFactura.Reserva, TipoFactura.Suscripcion, TipoFactura.Reserva);

        verify(facturaDao, times(1)).findAll();
        verify(facturaMapper, times(1)).toDTOList(entities);
    }

    @Test
    @DisplayName("LIST ALL - Debe retornar lista vacía cuando no hay datos")
    void getAllFacturas_SinDatos_DebeRetornarListaVacia() {
        // ARRANGE
        when(facturaDao.findAll()).thenReturn(Collections.emptyList());
        when(facturaMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // ACT
        List<FacturaDTO> result = facturaService.getAllFacturas();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(facturaDao, times(1)).findAll();
    }

    // ==================== GET FACTURAS BY USUARIO TESTS ====================

    @Test
    @DisplayName("GET BY USUARIO - Debe retornar facturas del usuario específico")
    void getFacturasByUsuario_UsuarioConFacturas_DebeRetornarLista() {
        // ARRANGE
        Factura factura1 = createFacturaEntity(1L, "FAC001", TipoFactura.Reserva);
        Factura factura2 = createFacturaEntity(2L, "FAC002", TipoFactura.Suscripcion);

        List<Factura> entities = new ArrayList<>(Arrays.asList(factura1, factura2));

        FacturaDTO dto1 = new FacturaDTO(1L, validPagoId, "FAC001", fixedDateTime, TipoFactura.Reserva,
                new BigDecimal("100000"), new BigDecimal("19000"), new BigDecimal("119000"), "url1");
        FacturaDTO dto2 = new FacturaDTO(2L, validPagoId, "FAC002", fixedDateTime, TipoFactura.Suscripcion,
                new BigDecimal("200000"), new BigDecimal("38000"), new BigDecimal("238000"), "url2");

        List<FacturaDTO> expectedDTOs = new ArrayList<>(Arrays.asList(dto1, dto2));

        when(facturaDao.findByUsuarioId(validUsuarioId)).thenReturn(entities);
        when(facturaMapper.toDTOList(entities)).thenReturn(expectedDTOs);

        // ACT
        List<FacturaDTO> result = facturaService.getFacturasByUsuario(validUsuarioId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting("numeroFactura")
                .containsExactly("FAC001", "FAC002");

        verify(facturaDao, times(1)).findByUsuarioId(validUsuarioId);
        verify(facturaMapper, times(1)).toDTOList(entities);
    }

    @Test
    @DisplayName("GET BY USUARIO - Usuario sin facturas debe retornar lista vacía")
    void getFacturasByUsuario_UsuarioSinFacturas_DebeRetornarListaVacia() {
        // ARRANGE
        when(facturaDao.findByUsuarioId(validUsuarioId)).thenReturn(Collections.emptyList());
        when(facturaMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // ACT
        List<FacturaDTO> result = facturaService.getFacturasByUsuario(validUsuarioId);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(facturaDao, times(1)).findByUsuarioId(validUsuarioId);
    }

    @Test
    @DisplayName("GET BY USUARIO - Debe retornar solo facturas del usuario solicitado")
    void getFacturasByUsuario_DebeRetornarSoloDelUsuario() {
        // ARRANGE
        Long otroUsuarioId = 99L;
        Factura factura1 = createFacturaEntity(1L, "FAC001", TipoFactura.Reserva);

        List<Factura> entities = new ArrayList<>(Arrays.asList(factura1));

        FacturaDTO dto1 = new FacturaDTO(1L, validPagoId, "FAC001", fixedDateTime, TipoFactura.Reserva,
                new BigDecimal("100000"), new BigDecimal("19000"), new BigDecimal("119000"), "url1");

        List<FacturaDTO> expectedDTOs = new ArrayList<>(Arrays.asList(dto1));

        when(facturaDao.findByUsuarioId(validUsuarioId)).thenReturn(entities);
        when(facturaMapper.toDTOList(entities)).thenReturn(expectedDTOs);

        // ACT
        List<FacturaDTO> result = facturaService.getFacturasByUsuario(validUsuarioId);

        // ASSERT
        assertThat(result).hasSize(1);
        verify(facturaDao, times(1)).findByUsuarioId(validUsuarioId);
        verify(facturaDao, never()).findByUsuarioId(otroUsuarioId);
    }

    // ==================== UPDATE FACTURA TESTS ====================

    @Test
    @DisplayName("UPDATE - Factura existente debe actualizarse correctamente")
    void updateFactura_DatosValidos_DebeActualizarCorrectamente() {
        // ARRANGE
        FacturaDTO updateDTO = new FacturaDTO(
                validFacturaId,
                validPagoId,
                "FAC_UPDATED",
                fixedDateTime.plusDays(1),
                TipoFactura.Suscripcion,
                new BigDecimal("200000.00"),
                new BigDecimal("38000.00"),
                new BigDecimal("238000.00"),
                "nueva_url"
        );

        Factura existingEntity = new Factura();
        existingEntity.setIdFactura(validFacturaId);
        existingEntity.setPago(validPago);
        existingEntity.setNumeroFactura("FAC001");
        existingEntity.setFechaEmision(fixedDateTime);
        existingEntity.setTipo(TipoFactura.Reserva);
        existingEntity.setSubtotal(new BigDecimal("120000.00"));
        existingEntity.setIva(new BigDecimal("22800.00"));
        existingEntity.setTotal(new BigDecimal("142800.00"));
        existingEntity.setUrlDescarga("url_vieja");

        Factura updatedEntity = new Factura();
        updatedEntity.setIdFactura(validFacturaId);
        updatedEntity.setPago(validPago);
        updatedEntity.setNumeroFactura("FAC_UPDATED");
        updatedEntity.setFechaEmision(fixedDateTime.plusDays(1));
        updatedEntity.setTipo(TipoFactura.Suscripcion);
        updatedEntity.setSubtotal(new BigDecimal("200000.00"));
        updatedEntity.setIva(new BigDecimal("38000.00"));
        updatedEntity.setTotal(new BigDecimal("238000.00"));
        updatedEntity.setUrlDescarga("nueva_url");

        FacturaDTO expectedDTO = new FacturaDTO(
                validFacturaId,
                validPagoId,
                "FAC_UPDATED",
                fixedDateTime.plusDays(1),
                TipoFactura.Suscripcion,
                new BigDecimal("200000.00"),
                new BigDecimal("38000.00"),
                new BigDecimal("238000.00"),
                "nueva_url"
        );

        when(facturaDao.findById(validFacturaId)).thenReturn(Optional.of(existingEntity));
        when(facturaDao.save(any(Factura.class))).thenReturn(updatedEntity);
        when(facturaMapper.toDTO(updatedEntity)).thenReturn(expectedDTO);

        // ACT
        Optional<FacturaDTO> result = facturaService.updateFactura(validFacturaId, updateDTO);

        // ASSERT
        assertThat(result).isPresent();
        assertThat(result.get().getNumeroFactura()).isEqualTo("FAC_UPDATED");
        assertThat(result.get().getTipo()).isEqualTo(TipoFactura.Suscripcion);
        assertThat(result.get().getSubtotal()).isEqualByComparingTo(new BigDecimal("200000.00"));
        assertThat(result.get().getIva()).isEqualByComparingTo(new BigDecimal("38000.00"));
        assertThat(result.get().getTotal()).isEqualByComparingTo(new BigDecimal("238000.00"));
        assertThat(result.get().getUrlDescarga()).isEqualTo("nueva_url");

        verify(facturaDao, times(1)).findById(validFacturaId);
        verify(facturaDao, times(1)).save(any(Factura.class));
        verify(facturaMapper, times(1)).toDTO(updatedEntity);
    }

    @Test
    @DisplayName("UPDATE - Actualización parcial solo con número de factura debe funcionar")
    void updateFactura_SoloNumeroFactura_DebeActualizarSoloNumero() {
        // ARRANGE
        FacturaDTO updateDTO = new FacturaDTO();
        updateDTO.setNumeroFactura("FAC_NUEVO");
        // Otros campos null

        Factura existingEntity = new Factura();
        existingEntity.setIdFactura(validFacturaId);
        existingEntity.setPago(validPago);
        existingEntity.setNumeroFactura("FAC_VIEJO");
        existingEntity.setFechaEmision(fixedDateTime);
        existingEntity.setTipo(TipoFactura.Reserva);
        existingEntity.setSubtotal(new BigDecimal("120000.00"));
        existingEntity.setIva(new BigDecimal("22800.00"));
        existingEntity.setTotal(new BigDecimal("142800.00"));
        existingEntity.setUrlDescarga("url1");

        FacturaDTO expectedDTO = new FacturaDTO(
                validFacturaId,
                validPagoId,
                "FAC_NUEVO",
                fixedDateTime,
                TipoFactura.Reserva,
                new BigDecimal("120000.00"),
                new BigDecimal("22800.00"),
                new BigDecimal("142800.00"),
                "url1"
        );

        when(facturaDao.findById(validFacturaId)).thenReturn(Optional.of(existingEntity));
        when(facturaDao.save(existingEntity)).thenReturn(existingEntity);
        when(facturaMapper.toDTO(existingEntity)).thenReturn(expectedDTO);

        // ACT
        Optional<FacturaDTO> result = facturaService.updateFactura(validFacturaId, updateDTO);

        // ASSERT
        assertThat(result).isPresent();
        assertThat(result.get().getNumeroFactura()).isEqualTo("FAC_NUEVO");
        assertThat(result.get().getTipo()).isEqualTo(TipoFactura.Reserva); // No cambió
        assertThat(result.get().getTotal()).isEqualByComparingTo(new BigDecimal("142800.00")); // No cambió
    }

    @Test
    @DisplayName("UPDATE - Actualización parcial solo con tipo debe funcionar")
    void updateFactura_SoloTipo_DebeActualizarSoloTipo() {
        // ARRANGE
        FacturaDTO updateDTO = new FacturaDTO();
        updateDTO.setTipo(TipoFactura.Suscripcion);

        Factura existingEntity = new Factura();
        existingEntity.setIdFactura(validFacturaId);
        existingEntity.setPago(validPago);
        existingEntity.setNumeroFactura("FAC001");
        existingEntity.setFechaEmision(fixedDateTime);
        existingEntity.setTipo(TipoFactura.Reserva);
        existingEntity.setSubtotal(new BigDecimal("120000.00"));
        existingEntity.setIva(new BigDecimal("22800.00"));
        existingEntity.setTotal(new BigDecimal("142800.00"));
        existingEntity.setUrlDescarga("url1");

        FacturaDTO expectedDTO = new FacturaDTO(
                validFacturaId,
                validPagoId,
                "FAC001",
                fixedDateTime,
                TipoFactura.Suscripcion,
                new BigDecimal("120000.00"),
                new BigDecimal("22800.00"),
                new BigDecimal("142800.00"),
                "url1"
        );

        when(facturaDao.findById(validFacturaId)).thenReturn(Optional.of(existingEntity));
        when(facturaDao.save(existingEntity)).thenReturn(existingEntity);
        when(facturaMapper.toDTO(existingEntity)).thenReturn(expectedDTO);

        // ACT
        Optional<FacturaDTO> result = facturaService.updateFactura(validFacturaId, updateDTO);

        // ASSERT
        assertThat(result).isPresent();
        assertThat(result.get().getTipo()).isEqualTo(TipoFactura.Suscripcion);
    }

    @Test
    @DisplayName("UPDATE - Actualización parcial de montos debe funcionar")
    void updateFactura_SoloMontos_DebeActualizarMontos() {
        // ARRANGE
        FacturaDTO updateDTO = new FacturaDTO();
        updateDTO.setSubtotal(new BigDecimal("300000.00"));
        updateDTO.setIva(new BigDecimal("57000.00"));
        updateDTO.setTotal(new BigDecimal("357000.00"));

        Factura existingEntity = new Factura();
        existingEntity.setIdFactura(validFacturaId);
        existingEntity.setPago(validPago);
        existingEntity.setNumeroFactura("FAC001");
        existingEntity.setFechaEmision(fixedDateTime);
        existingEntity.setTipo(TipoFactura.Reserva);
        existingEntity.setSubtotal(new BigDecimal("120000.00"));
        existingEntity.setIva(new BigDecimal("22800.00"));
        existingEntity.setTotal(new BigDecimal("142800.00"));
        existingEntity.setUrlDescarga("url1");

        FacturaDTO expectedDTO = new FacturaDTO(
                validFacturaId,
                validPagoId,
                "FAC001",
                fixedDateTime,
                TipoFactura.Reserva,
                new BigDecimal("300000.00"),
                new BigDecimal("57000.00"),
                new BigDecimal("357000.00"),
                "url1"
        );

        when(facturaDao.findById(validFacturaId)).thenReturn(Optional.of(existingEntity));
        when(facturaDao.save(existingEntity)).thenReturn(existingEntity);
        when(facturaMapper.toDTO(existingEntity)).thenReturn(expectedDTO);

        // ACT
        Optional<FacturaDTO> result = facturaService.updateFactura(validFacturaId, updateDTO);

        // ASSERT
        assertThat(result).isPresent();
        assertThat(result.get().getSubtotal()).isEqualByComparingTo(new BigDecimal("300000.00"));
        assertThat(result.get().getIva()).isEqualByComparingTo(new BigDecimal("57000.00"));
        assertThat(result.get().getTotal()).isEqualByComparingTo(new BigDecimal("357000.00"));
    }

    @Test
    @DisplayName("UPDATE - Actualización de URL de descarga debe funcionar")
    void updateFactura_SoloUrlDescarga_DebeActualizar() {
        // ARRANGE
        FacturaDTO updateDTO = new FacturaDTO();
        updateDTO.setUrlDescarga("https://nueva-url.com/factura.pdf");

        Factura existingEntity = new Factura();
        existingEntity.setIdFactura(validFacturaId);
        existingEntity.setPago(validPago);
        existingEntity.setNumeroFactura("FAC001");
        existingEntity.setFechaEmision(fixedDateTime);
        existingEntity.setTipo(TipoFactura.Reserva);
        existingEntity.setSubtotal(new BigDecimal("120000.00"));
        existingEntity.setIva(new BigDecimal("22800.00"));
        existingEntity.setTotal(new BigDecimal("142800.00"));
        existingEntity.setUrlDescarga("url_vieja");

        FacturaDTO expectedDTO = new FacturaDTO(
                validFacturaId,
                validPagoId,
                "FAC001",
                fixedDateTime,
                TipoFactura.Reserva,
                new BigDecimal("120000.00"),
                new BigDecimal("22800.00"),
                new BigDecimal("142800.00"),
                "https://nueva-url.com/factura.pdf"
        );

        when(facturaDao.findById(validFacturaId)).thenReturn(Optional.of(existingEntity));
        when(facturaDao.save(existingEntity)).thenReturn(existingEntity);
        when(facturaMapper.toDTO(existingEntity)).thenReturn(expectedDTO);

        // ACT
        Optional<FacturaDTO> result = facturaService.updateFactura(validFacturaId, updateDTO);

        // ASSERT
        assertThat(result).isPresent();
        assertThat(result.get().getUrlDescarga()).isEqualTo("https://nueva-url.com/factura.pdf");
    }

    @Test
    @DisplayName("UPDATE - Factura inexistente debe retornar Optional vacío")
    void updateFactura_IdInexistente_DebeRetornarOptionalVacio() {
        // ARRANGE
        Long idInexistente = 999L;
        when(facturaDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        Optional<FacturaDTO> result = facturaService.updateFactura(idInexistente, validFacturaDTO);

        // ASSERT
        assertThat(result).isEmpty();

        verify(facturaDao, times(1)).findById(idInexistente);
        verify(facturaDao, never()).save(any());
        verify(facturaMapper, never()).toDTO(any());
    }

    // ==================== DELETE FACTURA TESTS ====================

    @Test
    @DisplayName("DELETE - Factura existente debe eliminarse correctamente")
    void deleteFactura_IdExistente_DebeEliminarseCorrectamente() {
        // ARRANGE
        Factura entity = createFacturaEntity(validFacturaId, "FAC001", TipoFactura.Reserva);

        when(facturaDao.findById(validFacturaId)).thenReturn(Optional.of(entity));
        doNothing().when(facturaDao).delete(entity);

        // ACT
        boolean result = facturaService.deleteFactura(validFacturaId);

        // ASSERT
        assertThat(result).isTrue();

        verify(facturaDao, times(1)).findById(validFacturaId);
        verify(facturaDao, times(1)).delete(entity);
    }

    @Test
    @DisplayName("DELETE - Factura inexistente debe retornar false")
    void deleteFactura_IdInexistente_DebeRetornarFalse() {
        // ARRANGE
        Long idInexistente = 999L;
        when(facturaDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        boolean result = facturaService.deleteFactura(idInexistente);

        // ASSERT
        assertThat(result).isFalse();

        verify(facturaDao, times(1)).findById(idInexistente);
        verify(facturaDao, never()).delete(any());
    }

    @Test
    @DisplayName("DELETE - Factura tipo Suscripción debe poder eliminarse")
    void deleteFactura_TipoSuscripcion_DebePoderEliminarse() {
        // ARRANGE
        Factura entity = createFacturaEntity(validFacturaId, "FAC001", TipoFactura.Suscripcion);

        when(facturaDao.findById(validFacturaId)).thenReturn(Optional.of(entity));
        doNothing().when(facturaDao).delete(entity);

        // ACT
        boolean result = facturaService.deleteFactura(validFacturaId);

        // ASSERT
        assertThat(result).isTrue();
        verify(facturaDao, times(1)).delete(entity);
    }

    @Test
    @DisplayName("DELETE - Factura tipo Reserva debe poder eliminarse")
    void deleteFactura_TipoReserva_DebePoderEliminarse() {
        // ARRANGE
        Factura entity = createFacturaEntity(validFacturaId, "FAC001", TipoFactura.Reserva);

        when(facturaDao.findById(validFacturaId)).thenReturn(Optional.of(entity));
        doNothing().when(facturaDao).delete(entity);

        // ACT
        boolean result = facturaService.deleteFactura(validFacturaId);

        // ASSERT
        assertThat(result).isTrue();
        verify(facturaDao, times(1)).delete(entity);
    }

    // ==================== EDGE CASES & VALIDATIONS ====================

    @Test
    @DisplayName("CREATE - Factura con monto cero debe crearse")
    void createFactura_MontoCero_DebeCrearse() {
        // ARRANGE
        validFacturaDTO.setSubtotal(BigDecimal.ZERO);
        validFacturaDTO.setIva(BigDecimal.ZERO);
        validFacturaDTO.setTotal(BigDecimal.ZERO);

        validFacturaEntity.setSubtotal(BigDecimal.ZERO);
        validFacturaEntity.setIva(BigDecimal.ZERO);
        validFacturaEntity.setTotal(BigDecimal.ZERO);

        Factura savedEntity = new Factura();
        savedEntity.setIdFactura(validFacturaId);
        savedEntity.setPago(validPago);
        savedEntity.setNumeroFactura("FAC001");
        savedEntity.setFechaEmision(fixedDateTime);
        savedEntity.setTipo(TipoFactura.Reserva);
        savedEntity.setSubtotal(BigDecimal.ZERO);
        savedEntity.setIva(BigDecimal.ZERO);
        savedEntity.setTotal(BigDecimal.ZERO);
        savedEntity.setUrlDescarga("urldescarga");

        FacturaDTO expectedDTO = new FacturaDTO(
                validFacturaId,
                validPagoId,
                "FAC001",
                fixedDateTime,
                TipoFactura.Reserva,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                "urldescarga"
        );

        when(facturaMapper.toEntity(validFacturaDTO)).thenReturn(validFacturaEntity);
        when(pagoDao.findById(validPagoId)).thenReturn(Optional.of(validPago));
        when(facturaDao.save(any(Factura.class))).thenReturn(savedEntity);
        when(facturaMapper.toDTO(savedEntity)).thenReturn(expectedDTO);

        // ACT
        FacturaDTO result = facturaService.createFactura(validFacturaDTO);

        // ASSERT
        assertThat(result.getSubtotal()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getIva()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getTotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("GET BY USUARIO - Usuario con múltiples facturas de diferentes tipos")
    void getFacturasByUsuario_MultiplesTipos_DebeRetornarTodas() {
        // ARRANGE
        Factura factura1 = createFacturaEntity(1L, "FAC001", TipoFactura.Reserva);
        Factura factura2 = createFacturaEntity(2L, "FAC002", TipoFactura.Suscripcion);
        Factura factura3 = createFacturaEntity(3L, "FAC003", TipoFactura.Reserva);
        Factura factura4 = createFacturaEntity(4L, "FAC004", TipoFactura.Suscripcion);

        List<Factura> entities = new ArrayList<>(Arrays.asList(factura1, factura2, factura3, factura4));

        FacturaDTO dto1 = new FacturaDTO(1L, validPagoId, "FAC001", fixedDateTime, TipoFactura.Reserva,
                new BigDecimal("100000"), new BigDecimal("19000"), new BigDecimal("119000"), "url1");
        FacturaDTO dto2 = new FacturaDTO(2L, validPagoId, "FAC002", fixedDateTime, TipoFactura.Suscripcion,
                new BigDecimal("200000"), new BigDecimal("38000"), new BigDecimal("238000"), "url2");
        FacturaDTO dto3 = new FacturaDTO(3L, validPagoId, "FAC003", fixedDateTime, TipoFactura.Reserva,
                new BigDecimal("150000"), new BigDecimal("28500"), new BigDecimal("178500"), "url3");
        FacturaDTO dto4 = new FacturaDTO(4L, validPagoId, "FAC004", fixedDateTime, TipoFactura.Suscripcion,
                new BigDecimal("250000"), new BigDecimal("47500"), new BigDecimal("297500"), "url4");

        List<FacturaDTO> expectedDTOs = new ArrayList<>(Arrays.asList(dto1, dto2, dto3, dto4));

        when(facturaDao.findByUsuarioId(validUsuarioId)).thenReturn(entities);
        when(facturaMapper.toDTOList(entities)).thenReturn(expectedDTOs);

        // ACT
        List<FacturaDTO> result = facturaService.getFacturasByUsuario(validUsuarioId);

        // ASSERT
        assertThat(result).hasSize(4);
        assertThat(result).extracting("tipo")
                .containsExactly(TipoFactura.Reserva, TipoFactura.Suscripcion,
                        TipoFactura.Reserva, TipoFactura.Suscripcion);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Método auxiliar para crear facturas de prueba
     */
    private Factura createFacturaEntity(Long id, String numeroFactura, TipoFactura tipo) {
        Factura factura = new Factura();
        factura.setIdFactura(id);
        factura.setPago(validPago);
        factura.setNumeroFactura(numeroFactura);
        factura.setFechaEmision(fixedDateTime);
        factura.setTipo(tipo);
        factura.setSubtotal(new BigDecimal("120000.00"));
        factura.setIva(new BigDecimal("22800.00"));
        factura.setTotal(new BigDecimal("142800.00"));
        factura.setUrlDescarga("urldescarga");
        return factura;
    }
}