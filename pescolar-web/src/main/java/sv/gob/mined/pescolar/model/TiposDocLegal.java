package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "TIPOS_DOC_LEGAL")
@Entity
public class TiposDocLegal implements Serializable {
    @Id
    @Column(name = "ID_DOC_LEGAL", nullable = false)
    private Long id;

    @Column(name = "DESCRIPCION_DOC_LEGAL", length = 150)
    private String descripcionDocLegal;

    public String getDescripcionDocLegal() {
        return descripcionDocLegal;
    }

    public void setDescripcionDocLegal(String descripcionDocLegal) {
        this.descripcionDocLegal = descripcionDocLegal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}