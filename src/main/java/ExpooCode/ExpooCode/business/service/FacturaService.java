package ExpooCode.ExpooCode.business.service;

import ExpooCode.ExpooCode.business.DTO.FacturaDTO;

import java.util.List;
import java.util.Optional;


public interface FacturaService {

    List<FacturaDTO> getAllFacturas();

    Optional<FacturaDTO> getFacturaById(Long id);

    List<FacturaDTO> getFacturasByUsuario(Long idUsuario);

    FacturaDTO createFactura(FacturaDTO facturaDTO);

    Optional<FacturaDTO> updateFactura(Long id, FacturaDTO facturaDTO);

    boolean deleteFactura(Long id);

}
