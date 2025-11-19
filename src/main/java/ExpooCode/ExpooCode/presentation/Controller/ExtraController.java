package ExpooCode.ExpooCode.presentation.Controller;

import ExpooCode.ExpooCode.business.DTO.ExtraDTO;
import ExpooCode.ExpooCode.business.service.ExtraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expooSpace/extras")
@Tag(name = "Extras", description = "Operaciones CRUD para extras (proyectores, cargadores, etc.)")
public class ExtraController {

    private final ExtraService extraService;

    public ExtraController(ExtraService extraService) {
        this.extraService = extraService;
    }

    @GetMapping
    @Operation(summary = "Listar extras")
    public ResponseEntity<List<ExtraDTO>> listar() {
        return ResponseEntity.ok(extraService.listarExtras());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo extra")
    public ResponseEntity<ExtraDTO> crear(@RequestBody ExtraDTO extraDTO) {
        ExtraDTO creado = extraService.crearExtra(extraDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener extra por ID")
    public ResponseEntity<ExtraDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(extraService.obtenerExtraPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar extra")
    public ResponseEntity<ExtraDTO> actualizar(@PathVariable Integer id, @RequestBody ExtraDTO extraDTO) {
        return ResponseEntity.ok(extraService.actualizarExtra(id, extraDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar extra")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        extraService.eliminarExtra(id);
        return ResponseEntity.noContent().build();
    }
}
