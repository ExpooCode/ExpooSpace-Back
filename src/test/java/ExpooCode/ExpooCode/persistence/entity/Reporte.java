package ExpooCode.ExpooCode.persistence.entity;
import ExpooCode.ExpooCode.persistence.enums.TipoReporte;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reporte")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long idReporte;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario; // Solo administradores

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoReporte tipo;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    @Column(name = "contenido", nullable = false)
    private String contenido;
}
