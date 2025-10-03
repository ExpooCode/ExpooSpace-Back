package ExpooCode.ExpooCode.persistence.mapper;

import ExpooCode.ExpooCode.business.DTO.ReporteDTO;
import ExpooCode.ExpooCode.persistence.entity.Reporte;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ReporteMapper {


    // De entidad a DTO
    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    ReporteDTO toDTO(Reporte reporte);

    List<ReporteDTO> toDTOList(List<Reporte> reportes);

    // De DTO a entidad
    @Mapping(target = "idReporte", ignore = true) // Autogenerado por BD
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fechaGeneracion", ignore = true) // Se genera automáticamente
    Reporte toEntity(ReporteDTO dto);

    // Actualización parcial
    @Mapping(target = "idReporte", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "fechaGeneracion", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ReporteDTO dto, @MappingTarget Reporte reporte);
}
