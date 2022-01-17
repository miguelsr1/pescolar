package sv.gob.mined.dhcomitesso.model.dhevaluacion;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "EST_ORGANIZATIVA", schema = "RHEVALUACION1")
@Entity
public class EstOrganizativa implements Serializable {

    @Id
    @Column(name = "CODIGO_ESTRUCTURA", nullable = false)
    private Integer id;

    @Column(name = "ABREV_ESTRUCTURA", length = 50)
    private String abrevEstructura;

    @Column(name = "NOMBRE_ESTRUCTURA", length = 200)
    private String nombreEstructura;

    @Column(name = "CODIGO_SUP")
    private Integer codigoSup;

    @Column(name = "INUNIORG", length = 100)
    private String inuniorg;

    public String getInuniorg() {
        return inuniorg;
    }

    public void setInuniorg(String inuniorg) {
        this.inuniorg = inuniorg;
    }

    public Integer getCodigoSup() {
        return codigoSup;
    }

    public void setCodigoSup(Integer codigoSup) {
        this.codigoSup = codigoSup;
    }

    public String getNombreEstructura() {
        return nombreEstructura;
    }

    public void setNombreEstructura(String nombreEstructura) {
        this.nombreEstructura = nombreEstructura;
    }

    public String getAbrevEstructura() {
        return abrevEstructura;
    }

    public void setAbrevEstructura(String abrevEstructura) {
        this.abrevEstructura = abrevEstructura;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
