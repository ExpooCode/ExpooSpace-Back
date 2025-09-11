package ExpooCode.ExpooCode.persistence.entity;

import ExpooCode.ExpooCode.persistence.entity.enums.EstadoSuscripcion;
import ExpooCode.ExpooCode.persistence.entity.enums.TipoPlan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "suscripcion")
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_suscripcion")
    private Long idSuscripcion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_plan", nullable = false)
    private TipoPlan tipoPlan;

    @Column(name = "horas_incluidas", nullable = false)
    private Integer horasIncluidas;

    @Column(name = "precio_mensual", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioMensual;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoSuscripcion estado;

    @Column(name = "renovacion_automatica", nullable = false)
    private boolean renovacionAutomatica;

    @OneToMany(mappedBy = "suscripcion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos;
}
