package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "HISTORIAL_CAM_EST_RES_ADJ")
@NamedQueries({
    @NamedQuery(name = "HistorialCamEstResAdj.findAll", query = "SELECT h FROM HistorialCamEstResAdj h")})
public class HistorialCamEstResAdj implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_HISTORIAL_CAM")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historial")
    @SequenceGenerator(name = "historial", sequenceName = "SEQ_HISTORIAL_CAM", allocationSize = 1, initialValue = 1)
    private Long idHistorialCam;
    @Column(name = "ESTADO_ANTERIOR")
    private Long estadoAnterior;
    @Column(name = "ESTADO_NUEVO")
    private Long estadoNuevo;
    @Column(name = "COMENTARIO_HISTORIAL")
    private String comentarioHistorial;
    @Column(name = "ARCHIVO_RESPALDO")
    private String archivoRespaldo;
    @Column(name = "FECHA_CAMBIO_ESTADO")
    private LocalDateTime fechaCambioEstado;
    @Column(name = "USUARIO")
    private String usuario;
    @JoinColumn(name = "ID_RESOLUCION_ADJ", referencedColumnName = "ID_RESOLUCION_ADJ")
    @ManyToOne(fetch = FetchType.EAGER)
    private ResolucionesAdjudicativa idResolucionAdj;

    public HistorialCamEstResAdj() {
    }

    public HistorialCamEstResAdj(Long idHistorialCam) {
        this.idHistorialCam = idHistorialCam;
    }

    public Long getIdHistorialCam() {
        return idHistorialCam;
    }

    public void setIdHistorialCam(Long idHistorialCam) {
        this.idHistorialCam = idHistorialCam;
    }

    public Long getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(Long estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public Long getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(Long estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public String getComentarioHistorial() {
        return comentarioHistorial;
    }

    public void setComentarioHistorial(String comentarioHistorial) {
        this.comentarioHistorial = comentarioHistorial;
    }

    public String getArchivoRespaldo() {
        return archivoRespaldo;
    }

    public void setArchivoRespaldo(String archivoRespaldo) {
        this.archivoRespaldo = archivoRespaldo;
    }

    public LocalDateTime getFechaCambioEstado() {
        return fechaCambioEstado;
    }

    public void setFechaCambioEstado(LocalDateTime fechaCambioEstado) {
        this.fechaCambioEstado = fechaCambioEstado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public ResolucionesAdjudicativa getIdResolucionAdj() {
        return idResolucionAdj;
    }

    public void setIdResolucionAdj(ResolucionesAdjudicativa idResolucionAdj) {
        this.idResolucionAdj = idResolucionAdj;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistorialCam != null ? idHistorialCam.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistorialCamEstResAdj)) {
            return false;
        }
        HistorialCamEstResAdj other = (HistorialCamEstResAdj) object;
        if ((this.idHistorialCam == null && other.idHistorialCam != null) || (this.idHistorialCam != null && !this.idHistorialCam.equals(other.idHistorialCam))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.paquescolar.model.HistorialCamEstResAdj[ idHistorialCam=" + idHistorialCam + " ]";
    }

}
