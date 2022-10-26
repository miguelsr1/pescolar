/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Where;

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "DETALLE_RECEPCION")
@NamedQueries({
    @NamedQuery(name = "DetalleRecepcion.findAll", query = "SELECT d FROM DetalleRecepcion d")})
@Where(clause = "ESTADO_ELIMINACION = 0")
public class DetalleRecepcion implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_DETALLE_RECEPCION")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detallerecep_modif")
    @SequenceGenerator(name = "detallerecep_modif", sequenceName = "SEQ_DETALLE_RECEPCION", allocationSize = 1, initialValue = 1)
    private Long idDetalleRecepcion;
    @Column(name = "FECHA_RECEPCION")
    private LocalDateTime fechaRecepcion;
    @Column(name = "PORCENTAJE_ENTERGA")
    private BigInteger porcentajeEnterga;
    @Column(name = "FECHA_PREVISTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPrevista;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Basic(optional = false)
    @Column(name = "CONSOLIDADO_ESP_TEC")
    private String consolidadoEspTec;
    @Basic(optional = false)
    @Column(name = "NO_ITEM")
    private String noItem;
    @Basic(optional = false)
    @Column(name = "USUARIO_INSERCION")
    private String usuarioInsercion;
    @Basic(optional = false)
    @Column(name = "FECHA_INSERCION")
    private LocalDateTime fechaInsercion;
    @Column(name = "USUARIO_MODIFICACION")
    private String usuarioModificacion;
    @Column(name = "TIPO_ENTREGA")
    private String tipoEntrega;
    @Column(name = "FECHA_MODIFICACION")
    private LocalDateTime fechaModificacion;
    @Column(name = "FECHA_ELIMINACION")
    private LocalDateTime fechaEliminacion;
    @Basic(optional = false)
    @Column(name = "ESTADO_ELIMINACION")
    private BigInteger estadoEliminacion;
    @Basic(optional = false)
    @Column(name = "CANTIDAD_ENTREGADA")
    private BigInteger cantidadEntregada;
    @JoinColumn(name = "ID_RECEPCION", referencedColumnName = "ID_RECEPCION")
    @ManyToOne(optional = false)
    private RecepcionBienesServicios idRecepcion;

    public DetalleRecepcion() {
    }
    
    public DetalleRecepcion(Long idDetalleRecepcion) {
        this.idDetalleRecepcion = idDetalleRecepcion;
    }


    public Long getIdDetalleRecepcion() {
        return idDetalleRecepcion;
    }

    public void setIdDetalleRecepcion(Long idDetalleRecepcion) {
        this.idDetalleRecepcion = idDetalleRecepcion;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public BigInteger getPorcentajeEnterga() {
        return porcentajeEnterga;
    }

    public void setPorcentajeEnterga(BigInteger porcentajeEnterga) {
        this.porcentajeEnterga = porcentajeEnterga;
    }

    public Date getFechaPrevista() {
        return fechaPrevista;
    }

    public void setFechaPrevista(Date fechaPrevista) {
        this.fechaPrevista = fechaPrevista;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public BigInteger getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(BigInteger estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    public BigInteger getCantidadEntregada() {
        return cantidadEntregada;
    }

    public void setCantidadEntregada(BigInteger cantidadEntregada) {
        this.cantidadEntregada = cantidadEntregada;
    }

    public RecepcionBienesServicios getIdRecepcion() {
        return idRecepcion;
    }

    public void setIdRecepcion(RecepcionBienesServicios idRecepcion) {
        this.idRecepcion = idRecepcion;
    }

    public String getConsolidadoEspTec() {
        return consolidadoEspTec;
    }

    public void setConsolidadoEspTec(String consolidadoEspTec) {
        this.consolidadoEspTec = consolidadoEspTec;
    }

    public String getNoItem() {
        return noItem;
    }

    public void setNoItem(String noItem) {
        this.noItem = noItem;
    }

    public String getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(String tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleRecepcion != null ? idDetalleRecepcion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleRecepcion)) {
            return false;
        }
        DetalleRecepcion other = (DetalleRecepcion) object;
        return !((this.idDetalleRecepcion == null && other.idDetalleRecepcion != null) || (this.idDetalleRecepcion != null && !this.idDetalleRecepcion.equals(other.idDetalleRecepcion)));
    }

    @Override
    public String toString() {
        return "sv.gob.mined.paquescolar.model.DetalleRecepcion[ idDetalleRecepcion=" + idDetalleRecepcion + " ]";
    }
}
