package ExpooCode.ExpooCode.persistence.entity;

import ExpooCode.ExpooCode.persistence.enums.EstadoRecurso;
import ExpooCode.ExpooCode.persistence.enums.Tipo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "recurso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recurso")
    private Integer idRecurso;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private Tipo tipo;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoRecurso estadoRecurso;

    //  Relaciones
    @OneToMany(mappedBy = "recurso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "recurso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BloqueoRecurso> bloqueos;
}
