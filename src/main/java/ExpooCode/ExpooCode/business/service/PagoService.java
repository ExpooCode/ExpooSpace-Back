package ExpooCode.ExpooCode.business.service;

import ExpooCode.ExpooCode.business.DTO.PagoDTO;
import ExpooCode.ExpooCode.persistence.enums.EstadoPago;

import java.util.List;
import java.util.Optional;

public interface PagoService {

    List<PagoDTO> getAllPagos();

    PagoDTO createPago(PagoDTO pagoDTO);

    Optional<PagoDTO> getPagoById(Long id);

    Optional<PagoDTO> updatePago(Long id, PagoDTO pagoDTO);

    boolean deletePago(Long id);

    Optional<PagoDTO> cambiarEstadoPago(Long id, EstadoPago nuevoEstado);

    void procesarPago(Long id);

    void reembolsarPago(Long id);

}
