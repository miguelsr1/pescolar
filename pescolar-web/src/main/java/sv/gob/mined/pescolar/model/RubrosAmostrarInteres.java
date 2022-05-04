package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "RUBROS_AMOSTRAR_INTERES")
@Entity
public class RubrosAmostrarInteres implements Serializable {

    @OneToMany(mappedBy = "idRubroAdq", fetch = FetchType.LAZY)
    private List<DetalleProcesoAdq> detalleProcesoAdqList;
    @OneToMany(mappedBy = "idRubroInteres", fetch = FetchType.LAZY)
    private List<DetRubroMuestraIntere> detRubroMuestraIntereList;
    @Id
    @Column(name = "ID_RUBRO_INTERES", nullable = false)
    private Long id;

    @Column(name = "DESCRIPCION_RUBRO", length = 150)
    private String descripcionRubro;

    @Column(name = "NOMBRE_CORTO", length = 25)
    private String nombreCorto;

    @Column(name = "ID_RUBRO_UNIFORME")
    private Long idRubroUniforme;

    public Long getIdRubroUniforme() {
        return idRubroUniforme;
    }

    public void setIdRubroUniformes(Long idRubroUniforme) {
        this.idRubroUniforme = idRubroUniforme;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public String getDescripcionRubro() {
        return descripcionRubro;
    }

    public void setDescripcionRubro(String descripcionRubro) {
        this.descripcionRubro = descripcionRubro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RubrosAmostrarInteres() {
    }

    public List<DetalleProcesoAdq> getDetalleProcesoAdqList() {
        return detalleProcesoAdqList;
    }

    public void setDetalleProcesoAdqList(List<DetalleProcesoAdq> detalleProcesoAdqList) {
        this.detalleProcesoAdqList = detalleProcesoAdqList;
    }

    public List<DetRubroMuestraIntere> getDetRubroMuestraIntereList() {
        return detRubroMuestraIntereList;
    }

    public void setDetRubroMuestraIntereList(List<DetRubroMuestraIntere> detRubroMuestraIntereList) {
        this.detRubroMuestraIntereList = detRubroMuestraIntereList;
    }
}
