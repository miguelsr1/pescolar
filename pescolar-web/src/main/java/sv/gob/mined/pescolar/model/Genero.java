package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "GENERO")
@Entity
public class Genero implements Serializable {
    @Id
    @Column(name = "ID_GENERO", nullable = false)
    private Long id;

    @Column(name = "CODIGO_GENERO", length = 12)
    private String codigoGenero;

    @Column(name = "DESCRIPCION_GENERO", length = 150)
    private String descripcionGenero;

    public String getDescripcionGenero() {
        return descripcionGenero;
    }

    public void setDescripcionGenero(String descripcionGenero) {
        this.descripcionGenero = descripcionGenero;
    }

    public String getCodigoGenero() {
        return codigoGenero;
    }

    public void setCodigoGenero(String codigoGenero) {
        this.codigoGenero = codigoGenero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}