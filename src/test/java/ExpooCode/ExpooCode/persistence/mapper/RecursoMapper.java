package ExpooCode.ExpooCode.persistence.mapper;

import ExpooCode.ExpooCode.business.DTO.RecursoDTO;
import ExpooCode.ExpooCode.persistence.entity.Recurso;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface RecursoMapper {

    // De entidad a DTO
    RecursoDTO toDTO(Recurso recurso);

    List<RecursoDTO> toDTOList(List<Recurso> recursos);

    // De DTO a entidad
    @Mapping(target = "idRecurso", ignore = true) // Autogenerado por BD
    @Mapping(target = "reservas", ignore = true)  // Se manejan aparte, se manejan en los servicios
    @Mapping(target = "bloqueos", ignore = true)  // Se manejan aparte, se manejan en los servicios
    Recurso toEntity(RecursoDTO dto);

    // Actualización parcial
    @Mapping(target = "idRecurso", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "bloqueos", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(RecursoDTO dto, @MappingTarget Recurso recurso);
}
