package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Formula;

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "ESTADISTICA_CENSO")
public class EstadisticaCenso implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ESTADISTICA")
    @GeneratedValue(generator = "SEQ_ESTADISTICA_CENSO", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "SEQ_ESTADISTICA_CENSO", name = "SEQ_ESTADISTICA_CENSO", allocationSize = 1, initialValue = 1)
    private Long id;
    @Size(max = 5)
    @Column(name = "CODIGO_ENTIDAD")
    private String codigoEntidad;
    @Column(name = "MASCULINO")
    private BigInteger masculino;
    @Column(name = "FEMENIMO")
    private BigInteger femenimo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INSERCION")
    private LocalDate fechaInsercion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "USUARIO_INSERCION")
    private String usuarioInsercion;
    @Column(name = "FECHA_MODIFICACION")
    private LocalDate fechaModificacion;
    @Size(max = 25)
    @Column(name = "USUARIO_MODIFICACION")
    private String usuarioModificacion;
    @Column(name = "FECHA_ELIMINACION")
    private LocalDate fechaEliminacion;
    @Column(name = "ESTADO_ELIMINACION")
    private Short estadoEliminacion;
    @JoinColumn(name = "ID_NIVEL_EDUCATIVO", referencedColumnName = "ID_NIVEL_EDUCATIVO")
    @ManyToOne(fetch = FetchType.LAZY)
    private NivelEducativo idNivelEducativo;
    @JoinColumn(name = "ID_PROCESO_ADQ", referencedColumnName = "ID_PROCESO_ADQ")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProcesoAdquisicion idProcesoAdq;

    @Formula("masculino + femenimo")
    private Long totalMatricula;

    public EstadisticaCenso() {
        estadoEliminacion = 0;
        fechaInsercion = LocalDate.now();
    }

    public EstadisticaCenso(Long idEstadistica) {
        this.id = idEstadistica;
    }

    public EstadisticaCenso(Long idEstadistica, LocalDate fechaInsercion, String usuarioInsercion) {
        this.id = idEstadistica;
        this.fechaInsercion = fechaInsercion;
        this.usuarioInsercion = usuarioInsercion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long idEstadistica) {
        this.id = idEstadistica;
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public BigInteger getMasculino() {
        return masculino;
    }

    public void setMasculino(BigInteger masculino) {
        this.masculino = masculino;
    }

    public BigInteger getFemenimo() {
        return femenimo;
    }

    public void setFemenimo(BigInteger femenimo) {
        this.femenimo = femenimo;
    }

    public LocalDate getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(LocalDate fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public String getUsuarioInsercion() {
        return usuarioInsercion;
    }

    public void setUsuarioInsercion(String usuarioInsercion) {
        this.usuarioInsercion = usuarioInsercion;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public LocalDate getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(LocalDate fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public Short getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(Short estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    public NivelEducativo getIdNivelEducativo() {
        return idNivelEducativo;
    }

    public void setIdNivelEducativo(NivelEducativo idNivelEducativo) {
        this.idNivelEducativo = idNivelEducativo;
    }

    public ProcesoAdquisicion getIdProcesoAdq() {
        return idProcesoAdq;
    }

    public void setIdProcesoAdq(ProcesoAdquisicion idProcesoAdq) {
        this.idProcesoAdq = idProcesoAdq;
    }

    public Long getTotalMatricula() {
        return totalMatricula;
    }

    public void setTotalMatricula(Long totalMatricula) {
        this.totalMatricula = totalMatricula;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadisticaCenso)) {
            return false;
        }
        EstadisticaCenso other = (EstadisticaCenso) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "sv.gob.mined.pescolar.model.EstadisticaCenso[ idEstadistica=" + id + " ]";
    }

}
