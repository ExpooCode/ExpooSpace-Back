package ExpooCode.ExpooCode.persistence.mapper;

import ExpooCode.ExpooCode.business.DTO.BloqueoRecursoDTO;
import ExpooCode.ExpooCode.persistence.entity.BloqueoRecurso;
import org.mapstruct.*;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface BloqueoRecursoMapper {

    // De entidad a DTO
    @Mapping(source = "recurso.idRecurso", target = "idRecurso")
    @Mapping(source = "motivo", target = "motivo")
    BloqueoRecursoDTO toDTO(BloqueoRecurso bloqueo);

    List<BloqueoRecursoDTO> toDTOList(List<BloqueoRecurso> bloqueos);

    // De DTO a entidad
    @Mapping(target = "idBloqueo", ignore = true) // Autogenerado por BD
    @Mapping(target = "recurso", ignore = true)  // Se asigna manualmente en el servicio
    BloqueoRecurso toEntity(BloqueoRecursoDTO dto);

    // Actualización parcial
    @Mapping(target = "idBloqueo", ignore = true)
    @Mapping(target = "recurso", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(BloqueoRecursoDTO dto, @MappingTarget BloqueoRecurso bloqueo);
}
