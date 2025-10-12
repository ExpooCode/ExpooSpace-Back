package ExpooCode.ExpooCode.persistence.mapper;

import ExpooCode.ExpooCode.business.DTO.NotificacionDTO;
import ExpooCode.ExpooCode.persistence.entity.Notificacion;
import org.mapstruct.*;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface NotificacionMapper {

    // De entidad a DTO
    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    NotificacionDTO toDTO(Notificacion notificacion);

    List<NotificacionDTO> toDTOList(List<Notificacion> notificaciones);

    // De DTO a entidad
    @Mapping(target = "idNotificacion", ignore = true) // Autogenerado por BD
    @Mapping(target = "usuario", ignore = true)       // Se asigna manualmente en el servicio
    @Mapping(target = "fechaEnvio", ignore = true)   // Se genera automáticamente
    Notificacion toEntity(NotificacionDTO dto);

    // Actualización parcial
    @Mapping(target = "idNotificacion", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fechaEnvio", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(NotificacionDTO dto, @MappingTarget Notificacion notificacion);
}
