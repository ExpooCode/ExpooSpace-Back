package ExpooCode.ExpooCode.persistence.entity;

import ExpooCode.ExpooCode.persistence.enums.TipoExtra;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "extra")
public class Extra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_extra")
    private Integer idExtra;

    @Column(name = "nombre", nullable = false)
    private String nombre;


    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoExtra tipoExtra;


    //  Relaciones
    @OneToMany(mappedBy = "extra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;




}
