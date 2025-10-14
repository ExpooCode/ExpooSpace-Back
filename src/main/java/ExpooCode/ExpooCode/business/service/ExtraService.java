package ExpooCode.ExpooCode.business.service;

import ExpooCode.ExpooCode.business.DTO.ExtraDTO;

import java.util.List;

public interface ExtraService {

    ExtraDTO crearExtra(ExtraDTO extraDTO);

    List<ExtraDTO> listarExtras();

    ExtraDTO obtenerExtraPorId(Integer idExtra);

    ExtraDTO actualizarExtra(Integer idExtra, ExtraDTO extraDTO);

    void eliminarExtra(Integer idExtra);
}
