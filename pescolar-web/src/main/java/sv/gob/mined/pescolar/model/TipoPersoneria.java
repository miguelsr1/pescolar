package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "TIPO_PERSONERIA")
@Entity
public class TipoPersoneria implements Serializable {
    @Id
    @Column(name = "ID_PERSONERIA", nullable = false)
    private Long id;

    @Column(name = "DESCRIPCION_PERSONERIA", length = 150)
    private String descripcionPersoneria;

    public String getDescripcionPersoneria() {
        return descripcionPersoneria;
    }

    public void setDescripcionPersoneria(String descripcionPersoneria) {
        this.descripcionPersoneria = descripcionPersoneria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}