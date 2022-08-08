package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

@Table(name = "PROCESO_ADQUISICION")
@Entity
public class ProcesoAdquisicion implements Serializable {

    @OneToMany(mappedBy = "idProcesoAdq", fetch = FetchType.LAZY)
    private List<DetalleProcesoAdq> detalleProcesoAdqList;

    @OneToMany(mappedBy = "idProcesoAdq", fetch = FetchType.LAZY)
    private List<EstadisticaCenso> estadisticaCensoList;
    @OneToMany(mappedBy = "idProcesoAdq", fetch = FetchType.LAZY)
    private List<CapaInstPorRubro> capaInstPorRurboCensoList;
    @OneToMany(mappedBy = "padreIdProcesoAdq", fetch = FetchType.LAZY)
    private List<ProcesoAdquisicion> procesoAdquisicionList;
    @Id
    @Column(name = "ID_PROCESO_ADQ", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ANHO")
    private Anho idAnho;

    @Column(name = "DESCRIPCION_PROCESO_ADQ", length = 200)
    private String descripcionProcesoAdq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PADRE_ID_PROCESO_ADQ")
    private ProcesoAdquisicion padreIdProcesoAdq;

    public ProcesoAdquisicion getPadreIdProcesoAdq() {
        return padreIdProcesoAdq;
    }

    public void setPadreIdProcesoAdq(ProcesoAdquisicion padreIdProcesoAdq) {
        this.padreIdProcesoAdq = padreIdProcesoAdq;
    }

    public String getDescripcionProcesoAdq() {
        return descripcionProcesoAdq;
    }

    public void setDescripcionProcesoAdq(String descripcionProcesoAdq) {
        this.descripcionProcesoAdq = descripcionProcesoAdq;
    }

    public Anho getIdAnho() {
        return idAnho;
    }

    public void setIdAnho(Anho idAnho) {
        this.idAnho = idAnho;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public List<EstadisticaCenso> getEstadisticaCensoList() {
        return estadisticaCensoList;
    }

    public void setEstadisticaCensoList(List<EstadisticaCenso> estadisticaCensoList) {
        this.estadisticaCensoList = estadisticaCensoList;
    }

    @XmlTransient
    public List<ProcesoAdquisicion> getProcesoAdquisicionList() {
        return procesoAdquisicionList;
    }

    public void setProcesoAdquisicionList(List<ProcesoAdquisicion> procesoAdquisicionList) {
        this.procesoAdquisicionList = procesoAdquisicionList;
    }

    public List<DetalleProcesoAdq> getDetalleProcesoAdqList() {
        return detalleProcesoAdqList;
    }

    public void setDetalleProcesoAdqList(List<DetalleProcesoAdq> detalleProcesoAdqList) {
        this.detalleProcesoAdqList = detalleProcesoAdqList;
    }

    public List<CapaInstPorRubro> getCapaInstPorRurboCensoList() {
        return capaInstPorRurboCensoList;
    }

    public void setCapaInstPorRurboCensoList(List<CapaInstPorRubro> capaInstPorRurboCensoList) {
        this.capaInstPorRurboCensoList = capaInstPorRurboCensoList;
    }
}
