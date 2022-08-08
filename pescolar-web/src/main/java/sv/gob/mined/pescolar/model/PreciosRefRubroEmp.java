package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Filter;

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "PRECIOS_REF_RUBRO_EMP")
@NamedQueries({
    @NamedQuery(name = "PreciosRefRubroEmp.findAll", query = "SELECT p FROM PreciosRefRubroEmp p")})
@Filter(name = "eliminado", condition = "estadoEliminacion=0") 
public class PreciosRefRubroEmp implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PRECIO_REF_EMP")
    private Long idPrecioRefEmp;
    @Column(name = "PRECIO_REFERENCIA")
    private BigDecimal precioReferencia;
    @Size(max = 25)
    @Column(name = "USUARIO_INSERCION")
    private String usuarioInsercion;
    @Column(name = "FECHA_INSERCION")
    private LocalDate fechaInsercion;
    @Size(max = 25)
    @Column(name = "USUARIO_MODIFICACION")
    private String usuarioModificacion;
    @Column(name = "FECHA_MODIFICACION")
    private LocalDate fechaModificacion;
    @Column(name = "FECHA_ELIMINACION")
    private LocalDate fechaEliminacion;
    @Column(name = "ESTADO_ELIMINACION")
    private BigInteger estadoEliminacion;
    @Size(max = 3)
    @Column(name = "NO_ITEM")
    private String noItem;
    @Column(name = "ID_PROCESO_ADQ")
    private Integer idProcesoAdq;
    @JoinColumn(name = "ID_PRODUCTO", referencedColumnName = "ID_PRODUCTO")
    @ManyToOne(fetch = FetchType.LAZY)
    private CatalogoProducto idProducto;
    @JoinColumn(name = "ID_DET_PROCESO_ADQ", referencedColumnName = "ID_DET_PROCESO_ADQ")
    @ManyToOne(fetch = FetchType.LAZY)
    private DetalleProcesoAdq idDetProcesoAdq;
    @JoinColumn(name = "ID_MUESTRA_INTERES", referencedColumnName = "ID_MUESTRA_INTERES")
    @ManyToOne(fetch = FetchType.LAZY)
    private DetRubroMuestraIntere idMuestraInteres;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID_EMPRESA")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empresa idEmpresa;
    @JoinColumn(name = "ID_NIVEL_EDUCATIVO", referencedColumnName = "ID_NIVEL_EDUCATIVO")
    @ManyToOne(fetch = FetchType.LAZY)
    private NivelEducativo idNivelEducativo;

    public PreciosRefRubroEmp() {
    }

    public Long getIdPrecioRefEmp() {
        return idPrecioRefEmp;
    }

    public void setIdPrecioRefEmp(Long idPrecioRefEmp) {
        this.idPrecioRefEmp = idPrecioRefEmp;
    }

    public BigDecimal getPrecioReferencia() {
        return precioReferencia;
    }

    public void setPrecioReferencia(BigDecimal precioReferencia) {
        this.precioReferencia = precioReferencia;
    }

    public String getUsuarioInsercion() {
        return usuarioInsercion;
    }

    public void setUsuarioInsercion(String usuarioInsercion) {
        this.usuarioInsercion = usuarioInsercion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public BigInteger getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(BigInteger estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    public String getNoItem() {
        return noItem;
    }

    public void setNoItem(String noItem) {
        this.noItem = noItem;
    }

    public Integer getIdProcesoAdq() {
        return idProcesoAdq;
    }

    public void setIdProcesoAdq(Integer idProcesoAdq) {
        this.idProcesoAdq = idProcesoAdq;
    }

    public CatalogoProducto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(CatalogoProducto idProducto) {
        this.idProducto = idProducto;
    }

    public DetalleProcesoAdq getIdDetProcesoAdq() {
        return idDetProcesoAdq;
    }

    public void setIdDetProcesoAdq(DetalleProcesoAdq idDetProcesoAdq) {
        this.idDetProcesoAdq = idDetProcesoAdq;
    }

    public DetRubroMuestraIntere getIdMuestraInteres() {
        return idMuestraInteres;
    }

    public void setIdMuestraInteres(DetRubroMuestraIntere idMuestraInteres) {
        this.idMuestraInteres = idMuestraInteres;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public NivelEducativo getIdNivelEducativo() {
        return idNivelEducativo;
    }

    public void setIdNivelEducativo(NivelEducativo idNivelEducativo) {
        this.idNivelEducativo = idNivelEducativo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPrecioRefEmp != null ? idPrecioRefEmp.hashCode() : 0);
        return hash;
    }

    public LocalDate getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(LocalDate fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
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

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreciosRefRubroEmp)) {
            return false;
        }
        PreciosRefRubroEmp other = (PreciosRefRubroEmp) object;
        return !((this.idPrecioRefEmp == null && other.idPrecioRefEmp != null) || (this.idPrecioRefEmp != null && !this.idPrecioRefEmp.equals(other.idPrecioRefEmp)));
    }

    @Override
    public String toString() {
        return "sv.gob.mined.pescolar.model.PreciosRefRubroEmp[ idPrecioRefEmp=" + idPrecioRefEmp + " ]";
    }

}
