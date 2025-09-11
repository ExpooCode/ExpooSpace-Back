package ExpooCode.ExpooCode.persistence.entity;
import ExpooCode.ExpooCode.persistence.entity.enums.EstadoPago;
import ExpooCode.ExpooCode.persistence.entity.enums.MetodoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @OneToOne
    @JoinColumn(name = "id_reserva", nullable = true, unique = true)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_suscripcion", nullable = true)
    private Suscripcion suscripcion;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo", nullable = false)
    private MetodoPago metodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPago estado;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @OneToOne(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
    private Factura factura;
}
