package ExpooCode.ExpooCode.persistence.mapper;

import ExpooCode.ExpooCode.business.DTO.FacturaDTO;
import ExpooCode.ExpooCode.persistence.entity.Factura;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface FacturaMapper {

    // De entidad a DTO
    @Mapping(source = "pago.idPago", target = "idPago")
    FacturaDTO toDTO(Factura factura);

    List<FacturaDTO> toDTOList(List<Factura> facturas);

    // De DTO a entidad
    @Mapping(target = "idFactura", ignore = true)   // Autogenerado por BD
    @Mapping(target = "pago", ignore = true)       // Se asigna manualmente en el servicio
    @Mapping(target = "numeroFactura", ignore = true) // Se genera automáticamente
    @Mapping(target = "fechaEmision", ignore = true)  // Se genera automáticamente
    @Mapping(target = "urlDescarga", ignore = true)   // Se genera automáticamente
    Factura toEntity(FacturaDTO dto);

    // Actualización parcial
    @Mapping(target = "idFactura", ignore = true)
    @Mapping(target = "pago", ignore = true)
    @Mapping(target = "numeroFactura", ignore = true)
    @Mapping(target = "fechaEmision", ignore = true)
    @Mapping(target = "urlDescarga", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(FacturaDTO dto, @MappingTarget Factura factura);
}
