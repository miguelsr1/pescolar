/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.model;

import java.io.Serializable;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "DETALLE_CREDITO")
@NamedQueries({
    @NamedQuery(name = "DetalleCredito.findAll", query = "SELECT d FROM DetalleCredito d")})
public class DetalleCredito implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_DETALLE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalle_credito")
    @SequenceGenerator(name = "detalle_credito", sequenceName = "SEQ_DET_CREDITO", allocationSize = 1, initialValue = 1)
    private Long idDetalle;
    @Column(name = "CODIGO_ENTIDAD")
    private String codigoEntidad;
    @Basic(optional = false)
    @Column(name = "ESTADO_ELIMINACION")
    private Long estadoEliminacion;
    @Basic(optional = false)
    @Column(name = "FECHA_INSERCION")
    private LocalDate fechaInsercion;
    @Column(name = "USUARIO_MODIFICACION")
    private String usuarioModificacion;
    @Column(name = "FECHA_MODIFICACION")
    private LocalDate fechaModificacion;
    @Column(name = "FECHA_ELIMINACION")
    private LocalDate fechaEliminacion;
    @Basic(optional = false)
    @Column(name = "USUARIO_INSERCION")
    private String usuarioInsercion;
    @JoinColumn(name = "ID_CREDITO", referencedColumnName = "ID_CREDITO")
    @ManyToOne(fetch = FetchType.EAGER)
    private CreditoBancario idCredito;
    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID_CONTRATO")
    @ManyToOne(fetch = FetchType.EAGER)
    private ContratosOrdenesCompra idContrato;
    @Transient
    private Boolean eliminado = false;

    public DetalleCredito() {
    }

    public DetalleCredito(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public DetalleCredito(Long idDetalle, Long estadoEliminacion, LocalDate fechaInsercion, String usuarioInsercion) {
        this.idDetalle = idDetalle;
        this.estadoEliminacion = estadoEliminacion;
        this.fechaInsercion = fechaInsercion;
        this.usuarioInsercion = usuarioInsercion;
    }

    public Long getId() {
        return idDetalle;
    }

    public void setId(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public Long getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(Long estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    public LocalDate getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(LocalDate fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public LocalDate getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(LocalDate fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public String getUsuarioInsercion() {
        return usuarioInsercion;
    }

    public void setUsuarioInsercion(String usuarioInsercion) {
        this.usuarioInsercion = usuarioInsercion;
    }

    public CreditoBancario getIdCredito() {
        return idCredito;
    }

    public void setIdCredito(CreditoBancario idCredito) {
        this.idCredito = idCredito;
    }

    public ContratosOrdenesCompra getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(ContratosOrdenesCompra idContrato) {
        this.idContrato = idContrato;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalle != null ? idDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleCredito)) {
            return false;
        }
        DetalleCredito other = (DetalleCredito) object;
        if ((this.idDetalle == null && other.idDetalle != null) || (this.idDetalle != null && !this.idDetalle.equals(other.idDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.paquescolar.model.DetalleCredito[ idDetalle=" + idDetalle + " ]";
    }

    public Boolean getEliminado() {
        eliminado = estadoEliminacion.compareTo(1l) == 0;
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
        if (this.eliminado) {
            estadoEliminacion = 1l;
        } else {
            estadoEliminacion = 0l;
        }
    }
}
