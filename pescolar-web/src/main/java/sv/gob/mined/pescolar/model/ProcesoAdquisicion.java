package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "PROCESO_ADQUISICION")
@Entity
public class ProcesoAdquisicion implements Serializable {
    @Id
    @Column(name = "ID_PROCESO_ADQ", nullable = false)
    private Integer id;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}