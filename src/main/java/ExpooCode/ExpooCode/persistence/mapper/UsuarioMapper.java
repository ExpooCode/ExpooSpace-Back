package ExpooCode.ExpooCode.persistence.mapper;

import ExpooCode.ExpooCode.business.DTO.UsuarioDTO;
import ExpooCode.ExpooCode.persistence.entity.Usuario;
import org.mapstruct.*;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UsuarioMapper {

    // De entidad a DTO
    UsuarioDTO toDTO(Usuario usuario);

    List<UsuarioDTO> toDTOList(List<Usuario> usuarios);

    // De DTO a entidad
    @Mapping(target = "idUsuario", ignore = true) // Autogenerado por BD
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "notificaciones", ignore = true)
    @Mapping(target = "reportes", ignore = true)
    Usuario toEntity(UsuarioDTO dto);

    // Actualización parcial
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "notificaciones", ignore = true)
    @Mapping(target = "reportes", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UsuarioDTO dto, @MappingTarget Usuario usuario);
}
