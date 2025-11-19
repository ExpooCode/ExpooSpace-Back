package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.PagoDTO;
import ExpooCode.ExpooCode.persistence.dao.PagoDao;
import ExpooCode.ExpooCode.persistence.entity.Pago;
import ExpooCode.ExpooCode.persistence.enums.EstadoPago;
import ExpooCode.ExpooCode.persistence.enums.MetodoPago;
import ExpooCode.ExpooCode.persistence.mapper.PagoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PagoService - Unit Tests")
public class PagoServiceTest {

    @Mock
    private PagoDao pagoDao;

    @Mock
    private PagoMapper pagoMapper;

    @InjectMocks
    private PagoServiceImpl pagoService;

    private Pago pagoEntity;
    private PagoDTO pagoDTO;

    @BeforeEach
    void setUp() {
        // Inicializar entidad Pago
        pagoEntity = new Pago();
        pagoEntity.setIdPago(1L);
        pagoEntity.setMonto(new BigDecimal("150000.00"));
        pagoEntity.setMetodo(MetodoPago.Tarjeta);
        pagoEntity.setEstado(EstadoPago.Pendiente);
        pagoEntity.setFechaPago(LocalDateTime.now());

        // Inicializar DTO
        pagoDTO = new PagoDTO();
        pagoDTO.setIdPago(1L);
        pagoDTO.setMonto(new BigDecimal("150000.00"));
        pagoDTO.setMetodo(MetodoPago.Tarjeta);
        pagoDTO.setEstado(EstadoPago.Pendiente);
        pagoDTO.setFechaPago(LocalDateTime.now());
    }

    // ==================== TESTS: getAllPagos() ====================

    @Test
    @DisplayName("getAllPagos - Debe retornar lista con pagos existentes")
    void getAllPagos_CuandoHayPagos_DebeRetornarLista() {
        // ARRANGE
        List<Pago> pagos = Arrays.asList(pagoEntity);
        List<PagoDTO> pagosDTO = Arrays.asList(pagoDTO);
        when(pagoDao.findAll()).thenReturn(pagos);
        when(pagoMapper.toDTOList(pagos)).thenReturn(pagosDTO);

        // ACT
        List<PagoDTO> resultado = pagoService.getAllPagos();

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdPago()).isEqualTo(1L);
        verify(pagoDao, times(1)).findAll();
        verify(pagoMapper, times(1)).toDTOList(pagos);
    }

    @Test
    @DisplayName("getAllPagos - Debe retornar lista vacía cuando no hay pagos")
    void getAllPagos_CuandoNoHayPagos_DebeRetornarListaVacia() {
        // ARRANGE
        when(pagoDao.findAll()).thenReturn(Collections.emptyList());
        when(pagoMapper.toDTOList(anyList())).thenReturn(Collections.emptyList());

        // ACT
        List<PagoDTO> resultado = pagoService.getAllPagos();

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
        verify(pagoDao, times(1)).findAll();
    }

    // ==================== TESTS: createPago() ====================

    @Test
    @DisplayName("createPago - Debe crear pago con estado Pendiente por defecto")
    void createPago_ConDatosValidos_DebeCrearPagoConEstadoPendiente() {
        // ARRANGE
        PagoDTO inputDTO = new PagoDTO();
        inputDTO.setMonto(new BigDecimal("200000.00"));
        inputDTO.setMetodo(MetodoPago.PayU);

        Pago pagoNuevo = new Pago();
        when(pagoMapper.toEntity(inputDTO)).thenReturn(pagoNuevo);
        when(pagoDao.save(any(Pago.class))).thenReturn(pagoEntity);
        when(pagoMapper.toDTO(pagoEntity)).thenReturn(pagoDTO);

        // ACT
        PagoDTO resultado = pagoService.createPago(inputDTO);

        // ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdPago()).isEqualTo(1L);

        // Verificar que se estableció el estado Pendiente
        ArgumentCaptor<Pago> pagoCaptor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoDao, times(1)).save(pagoCaptor.capture());
        assertThat(pagoCaptor.getValue().getEstado()).isEqualTo(EstadoPago.Pendiente);
        assertThat(pagoCaptor.getValue().getFechaPago()).isNotNull();
    }

    @Test
    @DisplayName("createPago - Debe establecer fecha de pago automáticamente")
    void createPago_DebeEstablecerFechaPagoAutomaticamente() {
        // ARRANGE
        PagoDTO inputDTO = new PagoDTO();
        inputDTO.setMonto(new BigDecimal("100000.00"));
        inputDTO.setMetodo(MetodoPago.Tarjeta);

        Pago pagoNuevo = new Pago();
        when(pagoMapper.toEntity(inputDTO)).thenReturn(pagoNuevo);
        when(pagoDao.save(any(Pago.class))).thenReturn(pagoEntity);
        when(pagoMapper.toDTO(pagoEntity)).thenReturn(pagoDTO);

        LocalDateTime antes = LocalDateTime.now();

        // ACT
        pagoService.createPago(inputDTO);

        // ASSERT
        ArgumentCaptor<Pago> pagoCaptor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoDao).save(pagoCaptor.capture());
        LocalDateTime despues = LocalDateTime.now();

        assertThat(pagoCaptor.getValue().getFechaPago()).isNotNull();
        assertThat(pagoCaptor.getValue().getFechaPago()).isBetween(antes, despues);
    }

    // ==================== TESTS: getPagoById() ====================

    @Test
    @DisplayName("getPagoById - Debe retornar pago cuando existe")
    void getPagoById_CuandoExiste_DebeRetornarPago() {
        // ARRANGE
        Long idPago = 1L;
        when(pagoDao.findById(idPago)).thenReturn(Optional.of(pagoEntity));
        when(pagoMapper.toDTO(pagoEntity)).thenReturn(pagoDTO);

        // ACT
        Optional<PagoDTO> resultado = pagoService.getPagoById(idPago);

        // ASSERT
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdPago()).isEqualTo(1L);
        assertThat(resultado.get().getMonto()).isEqualByComparingTo(new BigDecimal("150000.00"));
        verify(pagoDao, times(1)).findById(idPago);
        verify(pagoMapper, times(1)).toDTO(pagoEntity);
    }

    @Test
    @DisplayName("getPagoById - Debe retornar Optional vacío cuando no existe")
    void getPagoById_CuandoNoExiste_DebeRetornarOptionalVacio() {
        // ARRANGE
        Long idInexistente = 999L;
        when(pagoDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        Optional<PagoDTO> resultado = pagoService.getPagoById(idInexistente);

        // ASSERT
        assertThat(resultado).isEmpty();
        verify(pagoDao, times(1)).findById(idInexistente);
        verify(pagoMapper, never()).toDTO(any());
    }

    // ==================== TESTS: updatePago() ====================

    @Test
    @DisplayName("updatePago - Debe actualizar todos los campos cuando se proporcionan")
    void updatePago_ConTodosLosCampos_DebeActualizarCorrectamente() {
        // ARRANGE
        Long idPago = 1L;
        PagoDTO updateDTO = new PagoDTO();
        updateDTO.setMonto(new BigDecimal("250000.00"));
        updateDTO.setMetodo(MetodoPago.Stripe);
        updateDTO.setEstado(EstadoPago.Pagado);

        when(pagoDao.findById(idPago)).thenReturn(Optional.of(pagoEntity));
        when(pagoDao.save(any(Pago.class))).thenReturn(pagoEntity);
        when(pagoMapper.toDTO(pagoEntity)).thenReturn(pagoDTO);

        // ACT
        Optional<PagoDTO> resultado = pagoService.updatePago(idPago, updateDTO);

        // ASSERT
        assertThat(resultado).isPresent();

        ArgumentCaptor<Pago> pagoCaptor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoDao).save(pagoCaptor.capture());

        Pago pagoActualizado = pagoCaptor.getValue();
        assertThat(pagoActualizado.getMonto()).isEqualByComparingTo(new BigDecimal("250000.00"));
        assertThat(pagoActualizado.getMetodo()).isEqualTo(MetodoPago.Stripe);
        assertThat(pagoActualizado.getEstado()).isEqualTo(EstadoPago.Pagado);
    }

    @Test
    @DisplayName("updatePago - Debe actualizar solo los campos proporcionados")
    void updatePago_ConCamposParciales_DebeActualizarSoloEsos() {
        // ARRANGE
        Long idPago = 1L;
        PagoDTO updateDTO = new PagoDTO();
        updateDTO.setMonto(new BigDecimal("180000.00"));
        // No se establecen metodo ni estado

        when(pagoDao.findById(idPago)).thenReturn(Optional.of(pagoEntity));
        when(pagoDao.save(any(Pago.class))).thenReturn(pagoEntity);
        when(pagoMapper.toDTO(pagoEntity)).thenReturn(pagoDTO);

        // ACT
        Optional<PagoDTO> resultado = pagoService.updatePago(idPago, updateDTO);

        // ASSERT
        assertThat(resultado).isPresent();

        ArgumentCaptor<Pago> pagoCaptor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoDao).save(pagoCaptor.capture());

        // Solo se actualizó el monto
        assertThat(pagoCaptor.getValue().getMonto()).isEqualByComparingTo(new BigDecimal("180000.00"));
    }

    @Test
    @DisplayName("updatePago - Debe retornar Optional vacío cuando el pago no existe")
    void updatePago_CuandoNoExiste_DebeRetornarOptionalVacio() {
        // ARRANGE
        Long idInexistente = 999L;
        PagoDTO updateDTO = new PagoDTO();
        updateDTO.setMonto(new BigDecimal("100000.00"));

        when(pagoDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        Optional<PagoDTO> resultado = pagoService.updatePago(idInexistente, updateDTO);

        // ASSERT
        assertThat(resultado).isEmpty();
        verify(pagoDao, times(1)).findById(idInexistente);
        verify(pagoDao, never()).save(any());
    }

    // ==================== TESTS: deletePago() ====================

    @Test
    @DisplayName("deletePago - Debe eliminar y retornar true cuando existe")
    void deletePago_CuandoExiste_DebeEliminarYRetornarTrue() {
        // ARRANGE
        Long idPago = 1L;
        when(pagoDao.findById(idPago)).thenReturn(Optional.of(pagoEntity));

        // ACT
        boolean resultado = pagoService.deletePago(idPago);

        // ASSERT
        assertThat(resultado).isTrue();
        verify(pagoDao, times(1)).findById(idPago);
        verify(pagoDao, times(1)).delete(pagoEntity);
    }

    @Test
    @DisplayName("deletePago - Debe retornar false cuando no existe")
    void deletePago_CuandoNoExiste_DebeRetornarFalse() {
        // ARRANGE
        Long idInexistente = 999L;
        when(pagoDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        boolean resultado = pagoService.deletePago(idInexistente);

        // ASSERT
        assertThat(resultado).isFalse();
        verify(pagoDao, times(1)).findById(idInexistente);
        verify(pagoDao, never()).delete(any());
    }

    // ==================== TESTS: cambiarEstadoPago() ====================

    @Test
    @DisplayName("cambiarEstadoPago - Debe cambiar estado correctamente cuando existe")
    void cambiarEstadoPago_CuandoExiste_DebeCambiarEstado() {
        // ARRANGE
        Long idPago = 1L;
        EstadoPago nuevoEstado = EstadoPago.Pagado;

        when(pagoDao.findById(idPago)).thenReturn(Optional.of(pagoEntity));
        when(pagoDao.save(any(Pago.class))).thenReturn(pagoEntity);
        when(pagoMapper.toDTO(pagoEntity)).thenReturn(pagoDTO);

        // ACT
        Optional<PagoDTO> resultado = pagoService.cambiarEstadoPago(idPago, nuevoEstado);

        // ASSERT
        assertThat(resultado).isPresent();

        ArgumentCaptor<Pago> pagoCaptor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoDao).save(pagoCaptor.capture());
        assertThat(pagoCaptor.getValue().getEstado()).isEqualTo(EstadoPago.Pagado);
    }

    @Test
    @DisplayName("cambiarEstadoPago - Debe retornar Optional vacío cuando no existe")
    void cambiarEstadoPago_CuandoNoExiste_DebeRetornarOptionalVacio() {
        // ARRANGE
        Long idInexistente = 999L;
        when(pagoDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        Optional<PagoDTO> resultado = pagoService.cambiarEstadoPago(idInexistente, EstadoPago.Pagado);

        // ASSERT
        assertThat(resultado).isEmpty();
        verify(pagoDao, times(1)).findById(idInexistente);
        verify(pagoDao, never()).save(any());
    }

    // ==================== TESTS: procesarPago() ====================

    @Test
    @DisplayName("procesarPago - Debe cambiar estado a Pagado cuando existe")
    void procesarPago_CuandoExiste_DebeCambiarEstadoAPagado() {
        // ARRANGE
        Long idPago = 1L;
        when(pagoDao.findById(idPago)).thenReturn(Optional.of(pagoEntity));

        // ACT
        pagoService.procesarPago(idPago);

        // ASSERT
        ArgumentCaptor<Pago> pagoCaptor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoDao, times(1)).save(pagoCaptor.capture());
        assertThat(pagoCaptor.getValue().getEstado()).isEqualTo(EstadoPago.Pagado);
        verify(pagoDao, times(1)).findById(idPago);
    }

    @Test
    @DisplayName("procesarPago - No debe hacer nada cuando no existe")
    void procesarPago_CuandoNoExiste_NoDebeHacerNada() {
        // ARRANGE
        Long idInexistente = 999L;
        when(pagoDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        pagoService.procesarPago(idInexistente);

        // ASSERT
        verify(pagoDao, times(1)).findById(idInexistente);
        verify(pagoDao, never()).save(any());
    }

    // ==================== TESTS: reembolsarPago() ====================

    @Test
    @DisplayName("reembolsarPago - Debe cambiar estado a Reembolsado cuando existe")
    void reembolsarPago_CuandoExiste_DebeCambiarEstadoAReembolsado() {
        // ARRANGE
        Long idPago = 1L;
        when(pagoDao.findById(idPago)).thenReturn(Optional.of(pagoEntity));

        // ACT
        pagoService.reembolsarPago(idPago);

        // ASSERT
        ArgumentCaptor<Pago> pagoCaptor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoDao, times(1)).save(pagoCaptor.capture());
        assertThat(pagoCaptor.getValue().getEstado()).isEqualTo(EstadoPago.Reembolsado);
        verify(pagoDao, times(1)).findById(idPago);
    }

    @Test
    @DisplayName("reembolsarPago - No debe hacer nada cuando no existe")
    void reembolsarPago_CuandoNoExiste_NoDebeHacerNada() {
        // ARRANGE
        Long idInexistente = 999L;
        when(pagoDao.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT
        pagoService.reembolsarPago(idInexistente);

        // ASSERT
        verify(pagoDao, times(1)).findById(idInexistente);
        verify(pagoDao, never()).save(any());
    }
}