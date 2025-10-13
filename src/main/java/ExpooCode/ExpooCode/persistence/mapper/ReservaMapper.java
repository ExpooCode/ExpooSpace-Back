package ExpooCode.ExpooCode.persistence.mapper;

import ExpooCode.ExpooCode.business.DTO.ReservaDTO;
import ExpooCode.ExpooCode.persistence.entity.Reserva;
import org.mapstruct.*;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ReservaMapper {

    // De entidad a DTO
    @Mapping(source = "usuario.idUsuario", target = "usuarioId")
    @Mapping(source = "usuario.nombre", target = "usuarioNombre")
    @Mapping(source = "recurso.idRecurso", target = "recursoId")
    @Mapping(source = "recurso.nombre", target = "recursoNombre")
    @Mapping(source = "extra.idExtra", target = "extraId")
    @Mapping(source = "extra.nombre", target = "extraNombre")
    @Mapping(source = "pago.idPago", target = "pagoId")
    @Mapping(source = "pago.estado", target = "pagoEstado")
    ReservaDTO toDTO(Reserva reserva);

    List<ReservaDTO> toDTOList(List<Reserva> reservas);

    // De DTO a entidad
    @Mapping(target = "idReserva", ignore = true) // Autogenerado por BD
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "recurso", ignore = true)
    @Mapping(target = "extra", ignore = true)
    @Mapping(target = "pago", ignore = true)
    Reserva toEntity(ReservaDTO dto);

    // Actualización parcial
    @Mapping(target = "idReserva", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "recurso", ignore = true)
    @Mapping(target = "extra", ignore = true)
    @Mapping(target = "pago", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ReservaDTO dto, @MappingTarget Reserva reserva);
}
