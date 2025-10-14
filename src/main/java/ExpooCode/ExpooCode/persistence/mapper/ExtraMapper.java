package ExpooCode.ExpooCode.persistence.mapper;

import ExpooCode.ExpooCode.business.DTO.ExtraDTO;
import ExpooCode.ExpooCode.persistence.entity.Extra;
import org.mapstruct.*;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ExtraMapper {

    // De entidad a DTO
    ExtraDTO toDTO(Extra extra);

    List<ExtraDTO> toDTOList(List<Extra> extras);

    // De DTO a entidad
    @Mapping(target = "idExtra", ignore = true) // Autogenerado por BD
    @Mapping(target = "reservas", ignore = true) // Relación manejada desde servicios
    Extra toEntity(ExtraDTO dto);

    // Actualización parcial
    @Mapping(target = "idExtra", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ExtraDTO dto, @MappingTarget Extra extra);
}
