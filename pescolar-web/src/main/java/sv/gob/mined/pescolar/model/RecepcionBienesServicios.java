/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import sv.gob.mined.pescolar.web.contratacion.ContratosOrdenesComprasView;

/**
 *
 * @author DesarrolloPc
 */
@Entity
@Table(name = "RECEPCION_BIENES_SERVICIOS")
@NamedQueries({
    @NamedQuery(name = "RecepcionBienesServicios.findAll", query = "SELECT r FROM RecepcionBienesServicios r")})
public class RecepcionBienesServicios implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_RECEPCION")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recepcion")
    @SequenceGenerator(name = "recepcion", sequenceName = "SEQ_RECEPCION_BIENES", allocationSize = 1, initialValue = 1)
    private Long idRecepcion;
    @Column(name = "FECHA_ORDEN_INICIO_ENTREGA1")
    private LocalDateTime fechaOrdenInicioEntrega1;
    @Column(name = "FECHA_ORDEN_INICIO_ENTREGA2")
    private LocalDateTime fechaOrdenInicioEntrega2;
    @Basic(optional = false)
    @Column(name = "USUARIO_INSERCION")
    private String usuarioInsercion;
    @Basic(optional = false)
    @Column(name = "FECHA_INSERCION")
    private LocalDateTime fechaInsercion;
    @Column(name = "USUARIO_MODIFICACION")
    private String usuarioModificacion;
    @Column(name = "FECHA_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaModificacion;
    @Column(name = "FECHA_ELIMINACION")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaEliminacion;
    @Basic(optional = false)
    @Column(name = "ESTADO_ELIMINACION")
    private Long estadoEliminacion;
    @JoinColumn(name = "ID_ESTADO_SEGUIMIENTO", referencedColumnName = "ID_ESTADO_SEGUIMIENTO")
    @ManyToOne(fetch = FetchType.EAGER)
    private EstadoSeguimiento idEstadoSeguimiento;
    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID_CONTRATO")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ContratosOrdenesCompra idContrato;

    @OneToMany(mappedBy = "idRecepcion", fetch = FetchType.EAGER)
    @OrderBy("fechaRecepcion ASC")
    private List<DetalleRecepcion> detalleRecepcionList;

    public RecepcionBienesServicios() {
    }

    public List<DetalleRecepcion> getDetalleRecepcionList() {
        return detalleRecepcionList;
    }

    public void setDetalleRecepcionList(List<DetalleRecepcion> detalleRecepcionList) {
        this.detalleRecepcionList = detalleRecepcionList;
    }

    public Long getIdRecepcion() {
        return idRecepcion;
    }

    public void setIdRecepcion(Long idRecepcion) {
        this.idRecepcion = idRecepcion;
    }

    public LocalDateTime getFechaOrdenInicioEntrega1() {
        return fechaOrdenInicioEntrega1;
    }

    public void setFechaOrdenInicioEntrega1(LocalDateTime fechaOrdenInicioEntrega1) {
        this.fechaOrdenInicioEntrega1 = fechaOrdenInicioEntrega1;
    }

    public LocalDateTime getFechaOrdenInicioEntrega2() {
        return fechaOrdenInicioEntrega2;
    }

    public void setFechaOrdenInicioEntrega2(LocalDateTime fechaOrdenInicioEntrega2) {
        this.fechaOrdenInicioEntrega2 = fechaOrdenInicioEntrega2;
    }

    public String getUsuarioInsercion() {
        return usuarioInsercion;
    }

    public void setUsuarioInsercion(String usuarioInsercion) {
        this.usuarioInsercion = usuarioInsercion;
    }

    public LocalDateTime getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(LocalDateTime fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public LocalDateTime getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(LocalDateTime fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public Long getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(Long estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    public EstadoSeguimiento getIdEstadoSeguimiento() {
        return idEstadoSeguimiento;
    }

    public void setIdEstadoSeguimiento(EstadoSeguimiento idEstadoSeguimiento) {
        this.idEstadoSeguimiento = idEstadoSeguimiento;
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
        hash += (idRecepcion != null ? idRecepcion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecepcionBienesServicios)) {
            return false;
        }
        RecepcionBienesServicios other = (RecepcionBienesServicios) object;
        if ((this.idRecepcion == null && other.idRecepcion != null) || (this.idRecepcion != null && !this.idRecepcion.equals(other.idRecepcion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.paquescolar.model.RecepcionBienesServicios[ idRecepcion=" + idRecepcion + " ]";
    }

}
