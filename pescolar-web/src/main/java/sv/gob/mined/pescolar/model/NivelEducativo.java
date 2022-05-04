package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "NIVEL_EDUCATIVO")
@Entity
public class NivelEducativo implements Serializable {

    @OneToMany(mappedBy = "idNivelEducativo", fetch = FetchType.LAZY)
    private List<PreciosRefRubroEmp> preciosRefRubroEmpList;

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
    @OneToMany(mappedBy = "idNivelEducativo", fetch = FetchType.LAZY)
    private List<EstadisticaCenso> estadisticaCensoList;

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

    public List<PreciosRefRubroEmp> getPreciosRefRubroEmpList() {
        return preciosRefRubroEmpList;
    }

    public void setPreciosRefRubroEmpList(List<PreciosRefRubroEmp> preciosRefRubroEmpList) {
        this.preciosRefRubroEmpList = preciosRefRubroEmpList;
    }

    public List<EstadisticaCenso> getEstadisticaCensoList() {
        return estadisticaCensoList;
    }

    public void setEstadisticaCensoList(List<EstadisticaCenso> estadisticaCensoList) {
        this.estadisticaCensoList = estadisticaCensoList;
    }

    @Override
    public String toString() {
        return descripcionNivel;
    }
}
