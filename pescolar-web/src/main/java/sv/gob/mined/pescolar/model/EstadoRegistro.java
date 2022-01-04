package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ESTADO_REGISTRO")
@Entity
public class EstadoRegistro implements Serializable {
    @Id
    @Column(name = "ID_ESTADO_REGISTRO", nullable = false)
    private Long id;

    @Column(name = "DESCRIPCION_ESTADO", length = 75)
    private String descripcionEstado;

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}