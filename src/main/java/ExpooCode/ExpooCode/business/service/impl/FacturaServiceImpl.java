package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.FacturaDTO;
import ExpooCode.ExpooCode.business.service.FacturaService;
import ExpooCode.ExpooCode.persistence.dao.FacturaDao;
import ExpooCode.ExpooCode.persistence.dao.PagoDao;
import ExpooCode.ExpooCode.persistence.entity.Factura;
import ExpooCode.ExpooCode.persistence.entity.Pago;
import ExpooCode.ExpooCode.persistence.mapper.FacturaMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class FacturaServiceImpl implements FacturaService {


    private final FacturaDao facturaDao;
    private final FacturaMapper facturaMapper;
    private final PagoDao pagoDao;

    public FacturaServiceImpl(FacturaDao facturaDao,PagoDao pagoDao, FacturaMapper facturaMapper) {
        this.facturaDao = facturaDao;
        this.facturaMapper = facturaMapper;
        this.pagoDao = pagoDao;
    }

    @Override
    @Transactional
    public List<FacturaDTO> getAllFacturas() {
        return facturaMapper.toDTOList(facturaDao.findAll());
    }

    @Override
    @Transactional
    public Optional<FacturaDTO> getFacturaById(Long id) {
        return facturaDao.findById(id)
                .map(facturaMapper::toDTO);
    }

    @Override
    @Transactional
    public List<FacturaDTO> getFacturasByUsuario(Long idUsuario) {
        return facturaMapper.toDTOList(facturaDao.findByUsuarioId(idUsuario));
    }

    @Override
    @Transactional
    public FacturaDTO createFactura(FacturaDTO facturaDTO) {
        Factura factura = facturaMapper.toEntity(facturaDTO);
        Pago pago = pagoDao.findById(facturaDTO.getIdPago())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Pago no encontrado con id: " + facturaDTO.getIdPago()));
        factura.setPago(pago);
        factura.setFechaEmision(LocalDateTime.now());
        factura.setNumeroFactura("FAC+" + factura.getIdFactura() + LocalDateTime.now());
        factura.setUrlDescarga("urldescarga");
        Factura saved = facturaDao.save(factura);
        return facturaMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public Optional<FacturaDTO> updateFactura(Long id, FacturaDTO facturaDTO) {
        return facturaDao.findById(id).map(factura -> {
           // if (facturaDTO.getIdPago() != null) factura.setPago(facturaDTO.getIdPago());
            if (facturaDTO.getNumeroFactura() != null) factura.setNumeroFactura(facturaDTO.getNumeroFactura());
            if (facturaDTO.getFechaEmision() != null) factura.setFechaEmision(facturaDTO.getFechaEmision());
            if (facturaDTO.getTipo() != null) factura.setTipo(facturaDTO.getTipo());
            if (facturaDTO.getSubtotal() != null) factura.setSubtotal(facturaDTO.getSubtotal());
            if (facturaDTO.getIva() != null) factura.setIva(facturaDTO.getIva());
            if (facturaDTO.getTotal() != null) factura.setTotal(facturaDTO.getTotal());
            if (facturaDTO.getUrlDescarga() != null) factura.setUrlDescarga(facturaDTO.getUrlDescarga());

            Factura updated = facturaDao.save(factura);
            return facturaMapper.toDTO(updated);
        });
    }

    @Override
    @Transactional
    public boolean deleteFactura(Long id) {
        return facturaDao.findById(id).map(factura -> {
            facturaDao.delete(factura);
            return true;
        }).orElse(false);
    }
}