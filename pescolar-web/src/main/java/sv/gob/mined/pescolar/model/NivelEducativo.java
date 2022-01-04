package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "NIVEL_EDUCATIVO")
@Entity
public class NivelEducativo implements Serializable {
    @Id
    @Column(name = "ID_NIVEL_EDUCATIVO", nullable = false)
    private Long id;

    @Column(name = "DESCRIPCION_NIVEL", length = 75)
    private String descripcionNivel;

    @Column(name = "ID_NIVEL_PADRE")
    private Long idNivelPadre;

    @Column(name = "ORDEN")
    private Integer orden;

    @Column(name = "ORDEN2")
    private Integer orden2;

    public Integer getOrden2() {
        return orden2;
    }

    public void setOrden2(Integer orden2) {
        this.orden2 = orden2;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Long getIdNivelPadre() {
        return idNivelPadre;
    }

    public void setIdNivelPadre(Long idNivelPadre) {
        this.idNivelPadre = idNivelPadre;
    }

    public String getDescripcionNivel() {
        return descripcionNivel;
    }

    public void setDescripcionNivel(String descripcionNivel) {
        this.descripcionNivel = descripcionNivel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}