package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Table(name = "DETALLE_PROCESO_ADQ")
@Entity
public class DetalleProcesoAdq implements Serializable {

    @OneToMany(mappedBy = "idDetProcesoAdq", fetch = FetchType.LAZY)
    private List<PreciosRefRubroEmp> preciosRefRubroEmpList;
    @OneToMany(mappedBy = "idDetProcesoAdq", fetch = FetchType.LAZY)
    private List<DetRubroMuestraIntere> detRubroMuestraIntereList;
    @Id
    @Column(name = "ID_DET_PROCESO_ADQ", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PROCESO_ADQ")
    private ProcesoAdquisicion idProcesoAdq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_RUBRO_ADQ")
    private RubrosAmostrarInteres idRubroAdq;

    @Column(name = "CREDITO")
    private Boolean credito;

    @Column(name = "HABILITAR")
    private Boolean habilitar;

    public Boolean getHabilitar() {
        return habilitar;
    }

    public void setHabilitar(Boolean habilitar) {
        this.habilitar = habilitar;
    }

    public Boolean getCredito() {
        return credito;
    }

    public void setCredito(Boolean credito) {
        this.credito = credito;
    }

    public RubrosAmostrarInteres getIdRubroAdq() {
        return idRubroAdq;
    }

    public void setIdRubroAdq(RubrosAmostrarInteres idRubroAdq) {
        this.idRubroAdq = idRubroAdq;
    }

    public ProcesoAdquisicion getIdProcesoAdq() {
        return idProcesoAdq;
    }

    public void setIdProcesoAdq(ProcesoAdquisicion idProcesoAdq) {
        this.idProcesoAdq = idProcesoAdq;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<PreciosRefRubroEmp> getPreciosRefRubroEmpList() {
        return preciosRefRubroEmpList;
    }

    public void setPreciosRefRubroEmpList(List<PreciosRefRubroEmp> preciosRefRubroEmpList) {
        this.preciosRefRubroEmpList = preciosRefRubroEmpList;
    }

    public List<DetRubroMuestraIntere> getDetRubroMuestraIntereList() {
        return detRubroMuestraIntereList;
    }

    public void setDetRubroMuestraIntereList(List<DetRubroMuestraIntere> detRubroMuestraIntereList) {
        this.detRubroMuestraIntereList = detRubroMuestraIntereList;
    }
}
