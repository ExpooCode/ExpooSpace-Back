package ExpooCode.ExpooCode.persistence.mapper;

import ExpooCode.ExpooCode.business.DTO.SuscripcionDTO;
import ExpooCode.ExpooCode.persistence.entity.Suscripcion;
import org.mapstruct.*;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface SuscripcionMapper {

    // De entidad a DTO
    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    SuscripcionDTO toDTO(Suscripcion suscripcion);

    List<SuscripcionDTO> toDTOList(List<Suscripcion> suscripciones);

    // De DTO a entidad (crear)
    @Mapping(target = "idSuscripcion", ignore = true) // Generado por la BD
    @Mapping(target = "usuario", source = "idUsuario", qualifiedByName = "createUsuarioFromId")
    Suscripcion toEntity(SuscripcionDTO dto);

    // Actualización parcial
    @Mapping(target = "idSuscripcion", ignore = true)
    @Mapping(target = "usuario", ignore = true) // Usuario no cambia en update
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(SuscripcionDTO dto, @MappingTarget Suscripcion suscripcion);
}
