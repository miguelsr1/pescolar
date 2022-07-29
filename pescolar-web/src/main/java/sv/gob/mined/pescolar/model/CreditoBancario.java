/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "CREDITO_BANCARIO")
@NamedQueries({
    @NamedQuery(name = "CreditoBancario.findAll", query = "SELECT c FROM CreditoBancario c")})
public class CreditoBancario implements Serializable {

    @JoinColumn(name = "ID_DET_PROCESO_ADQ", referencedColumnName = "ID_DET_PROCESO_ADQ")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private DetalleProcesoAdq idDetProcesoAdq;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CREDITO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credito")
    @SequenceGenerator(name = "credito", sequenceName = "SEQ_CREDITO", allocationSize = 1, initialValue = 1)
    private Long idCredito;
    @Column(name = "ID_EMPRESA")
    private Long idEmpresa;
    @Column(name = "REF_PRESTAMO")
    private String refPrestamo;
    @Column(name = "MONTO_CREDITO")
    private BigDecimal montoCredito;
    @Basic(optional = false)
    @Column(name = "USUARIO_INSERCION")
    private String usuarioInsercion;
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
    @Column(name = "ESTADO_ELIMINACION")
    private Long estadoEliminacion;
    @Basic(optional = false)
    @Column(name = "ID_PROCESO")
    private Long idProceso;
    @Column(name = "FECHA_APROBACION")
    private LocalDate fechaAprobacion;
    @Column(name = "FECHA_VENCIMIENTO")
    private LocalDate fechaVencimiento;
    @Column(name = "TASA_INTERES")
    private BigDecimal tasaInteres;
    @Basic(optional = false)
    @Column(name = "CREDITO_ACTIVO")
    private Long creditoActivo;
    @Column(name = "NUMERO_NIT")
    private String numeroNit;
    @OneToMany(mappedBy = "idCredito", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("idContrato.idResolucionAdj.idParticipante.idOferta.codigoEntidad.codigoEntidad ASC")
    private List<DetalleCredito> detalleCreditoList;
    @JoinColumn(name = "COD_ENT_FINANCIERA", referencedColumnName = "COD_ENT_FINANCIERA")
    @ManyToOne(fetch = FetchType.EAGER)
    private EntidadFinanciera codEntFinanciera;

    public CreditoBancario() {
    }

    public CreditoBancario(Long idCredito) {
        this.idCredito = idCredito;
    }

    public CreditoBancario(Long idCredito, String usuarioInsercion, LocalDate fechaInsercion, Long estadoEliminacion, Long idProceso, Long creditoActivo) {
        this.idCredito = idCredito;
        this.usuarioInsercion = usuarioInsercion;
        this.fechaInsercion = fechaInsercion;
        this.estadoEliminacion = estadoEliminacion;
        this.idProceso = idProceso;
        this.creditoActivo = creditoActivo;
    }

    public Long getId() {
        return idCredito;
    }

    public void setId(Long idCredito) {
        this.idCredito = idCredito;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getRefPrestamo() {
        return refPrestamo;
    }

    public void setRefPrestamo(String refPrestamo) {
        this.refPrestamo = refPrestamo;
    }

    public BigDecimal getMontoCredito() {
        return montoCredito;
    }

    public void setMontoCredito(BigDecimal montoCredito) {
        this.montoCredito = montoCredito;
    }

    public String getUsuarioInsercion() {
        return usuarioInsercion;
    }

    public void setUsuarioInsercion(String usuarioInsercion) {
        this.usuarioInsercion = usuarioInsercion;
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

    public Long getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(Long estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    public Long getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(Long idProceso) {
        this.idProceso = idProceso;
    }

    public LocalDate getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(LocalDate fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(BigDecimal tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public Long getCreditoActivo() {
        return creditoActivo;
    }

    public void setCreditoActivo(Long creditoActivo) {
        this.creditoActivo = creditoActivo;
    }

    public String getNumeroNit() {
        return numeroNit;
    }

    public void setNumeroNit(String numeroNit) {
        this.numeroNit = numeroNit;
    }

    public List<DetalleCredito> getDetalleCreditoList() {
        return detalleCreditoList;
    }

    public void setDetalleCreditoList(List<DetalleCredito> detalleCreditoList) {
        this.detalleCreditoList = detalleCreditoList;
    }

    public EntidadFinanciera getCodEntFinanciera() {
        return codEntFinanciera;
    }

    public void setCodEntFinanciera(EntidadFinanciera codEntFinanciera) {
        this.codEntFinanciera = codEntFinanciera;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCredito != null ? idCredito.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CreditoBancario)) {
            return false;
        }
        CreditoBancario other = (CreditoBancario) object;
        if ((this.idCredito == null && other.idCredito != null) || (this.idCredito != null && !this.idCredito.equals(other.idCredito))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.paquescolar.model.CreditoBancario[ idCredito=" + idCredito + " ]";
    }

    public DetalleProcesoAdq getIdDetProcesoAdq() {
        return idDetProcesoAdq;
    }

    public void setIdDetProcesoAdq(DetalleProcesoAdq idDetProcesoAdq) {
        this.idDetProcesoAdq = idDetProcesoAdq;
    }

}
