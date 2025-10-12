package ExpooCode.ExpooCode.business.service.impl;

import ExpooCode.ExpooCode.business.DTO.PagoDTO;
import ExpooCode.ExpooCode.business.service.PagoService;
import ExpooCode.ExpooCode.persistence.dao.PagoDao;
import ExpooCode.ExpooCode.persistence.entity.Pago;
import ExpooCode.ExpooCode.persistence.enums.EstadoPago;
import ExpooCode.ExpooCode.persistence.mapper.PagoMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class PagoServiceImpl implements PagoService {

    private final PagoDao pagoDao;
    private final PagoMapper pagoMapper;

    public PagoServiceImpl(PagoDao pagoDao, PagoMapper pagoMapper) {
        this.pagoDao = pagoDao;
        this.pagoMapper = pagoMapper;
    }

    @Override
    @Transactional
    public List<PagoDTO> getAllPagos() {
        return pagoMapper.toDTOList(pagoDao.findAll());
    }

    @Override
    @Transactional
    public PagoDTO createPago(PagoDTO pagoDTO) {
        Pago pago = pagoMapper.toEntity(pagoDTO);
        pago.setEstado(EstadoPago.Pendiente); // Estado inicial por defecto
        pago.setFechaPago(LocalDateTime.now()); //pasar la fecha actual al crear el pago
        Pago saved = pagoDao.save(pago);
        return pagoMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public Optional<PagoDTO> getPagoById(Long id) {
        return pagoDao.findById(id)
                .map(pagoMapper::toDTO);
    }

    @Override
    @Transactional
    public Optional<PagoDTO> updatePago(Long id, PagoDTO pagoDTO) {
        return pagoDao.findById(id).map(pago -> {
            if (pagoDTO.getMonto() != null) pago.setMonto(pagoDTO.getMonto());
            if (pagoDTO.getMetodo() != null) pago.setMetodo(pagoDTO.getMetodo());
            if (pagoDTO.getEstado() != null) pago.setEstado(pagoDTO.getEstado());

            Pago updated = pagoDao.save(pago);
            return pagoMapper.toDTO(updated);
        });
    }

    @Override
    @Transactional
    public boolean deletePago(Long id) {
        return pagoDao.findById(id).map(pago -> {
            pagoDao.delete(pago);
            return true;
        }).orElse(false);
    }

    @Override
    @Transactional
    public Optional<PagoDTO> cambiarEstadoPago(Long id, EstadoPago nuevoEstado) {
        return pagoDao.findById(id).map(pago -> {
            pago.setEstado(nuevoEstado);
            Pago updated = pagoDao.save(pago);
            return pagoMapper.toDTO(updated);
        });
    }

    @Override
    @Transactional
    public void procesarPago(Long id) {
        pagoDao.findById(id).ifPresent(pago -> {
            // Aquí podrías meter lógica de integración con pasarela de pagos
            pago.setEstado(EstadoPago.Pagado);
            pagoDao.save(pago);
            log.info("Pago con ID {} procesado correctamente.", id);
        });
    }

    @Override
    @Transactional
    public void reembolsarPago(Long id) {
        pagoDao.findById(id).ifPresent(pago -> {
            // Aquí podrías meter lógica de reembolso real
            pago.setEstado(EstadoPago.Reembolsado);
            pagoDao.save(pago);
            log.info("Pago con ID {} reembolsado correctamente.", id);
        });
    }
}
