package ExpooCode.ExpooCode.persistence.mapper;

import ExpooCode.ExpooCode.business.DTO.PagoDTO;
import ExpooCode.ExpooCode.persistence.entity.Pago;
import org.mapstruct.*;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface PagoMapper {


    // De entidad a DTO
    @Mapping(source = "reserva.idReserva", target = "idReserva")
    @Mapping(source = "suscripcion.idSuscripcion", target = "idSuscripcion")
    @Mapping(source = "factura.idFactura", target = "idFactura")
    PagoDTO toDTO(Pago pago);

    List<PagoDTO> toDTOList(List<Pago> pagos);

    // De DTO a entidad
    @Mapping(target = "idPago", ignore = true)       // Autogenerado por BD
    @Mapping(target = "reserva", ignore = true)     // Se asigna manualmente en el servicio
    @Mapping(target = "suscripcion", ignore = true) // Se asigna manualmente en el servicio
    @Mapping(target = "factura", ignore = true)     // Se asigna manualmente en el servicio
    @Mapping(target = "fechaPago", ignore = true)   // Se genera automáticamente
    Pago toEntity(PagoDTO dto);

    // Actualización parcial
    @Mapping(target = "idPago", ignore = true)
    @Mapping(target = "reserva", ignore = true)
    @Mapping(target = "suscripcion", ignore = true)
    @Mapping(target = "factura", ignore = true)
    @Mapping(target = "fechaPago", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PagoDTO dto, @MappingTarget Pago pago);
}
