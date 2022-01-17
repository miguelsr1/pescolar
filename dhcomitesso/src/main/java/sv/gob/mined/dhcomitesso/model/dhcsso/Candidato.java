package sv.gob.mined.dhcomitesso.model.dhcsso;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "CANDIDATO", schema = "SIECSSO")
@Entity
public class Candidato implements Serializable {

    @Id
    @Column(name = "ID_CANDIDATO", nullable = false)
    @GeneratedValue(generator = "seqCandidato", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seqCandidato", sequenceName = "SEQ_CANDIDATO", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(name = "CODIGO_EMPLEADO")
    private String codigoEmpleado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PROCESO")
    private ProcesoEleccion procesoEleccion;

    @Column(name = "NUMERO_TELEFONO")
    private String numeroTelefono;

    @Column(name = "PATH_IMG")
    private String pathImg;

    public ProcesoEleccion getProcesoEleccion() {
        return procesoEleccion;
    }

    public void setProcesoEleccion(ProcesoEleccion procesoEleccion) {
        this.procesoEleccion = procesoEleccion;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }
}
