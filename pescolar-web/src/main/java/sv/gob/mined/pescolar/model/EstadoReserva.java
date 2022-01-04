package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ESTADO_RESERVA")
@Entity
public class EstadoReserva implements Serializable {
    @Id
    @Column(name = "ID_ESTADO_RESERVA", nullable = false)
    private Long id;

    @Column(name = "DESCRIPCION_RESERVA", length = 25)
    private String descripcionReserva;

    public String getDescripcionReserva() {
        return descripcionReserva;
    }

    public void setDescripcionReserva(String descripcionReserva) {
        this.descripcionReserva = descripcionReserva;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}