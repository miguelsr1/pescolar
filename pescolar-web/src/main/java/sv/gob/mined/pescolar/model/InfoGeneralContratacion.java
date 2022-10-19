package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "INFO_GENERAL_CONTRATACION")
@NamedQueries({
    @NamedQuery(name = "InfoGeneralContratacion.findAll", query = "SELECT i FROM InfoGeneralContratacion i")})
public class InfoGeneralContratacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_INFO_GENERAL")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "info_general")
    @SequenceGenerator(name = "info_general", sequenceName = "SEQ_INFO_GENERAL_CON", allocationSize = 1, initialValue = 1)
    private Long idInfoGeneral;
    @Column(name = "CODIGO_ENTIDAD")
    private String codigoEntidad;
    @Column(name = "ID_CONTRATO")
    private Long idContrato;
    @Column(name = "ID_EMPRESA")
    private Long idEmpresa;
    @Column(name = "ID_DET_PROCESO_ADQ")
    private Long idDetProcesoAdq;
    @Column(name = "CODIGO_DEPARTAMENTO")
    private String codigoDepartamento;
    @Column(name = "NOMBRE_DEPARTAMENTO")
    private String nombreDepartamento;
    @Column(name = "CODIGO_MUNICIPIO")
    private String codigoMunicipio;
    @Column(name = "NOMBRE_MUNICIPIO")
    private String nombreMunicipio;
    @Column(name = "MONTO_CONTRATO")
    private BigDecimal montoContrato;
    @Column(name = "CANTIDAD_CONTRATO")
    private Long cantidadContrato;
    @Basic(optional = false)
    @Column(name = "FECHA_INSERCION")
    private LocalDateTime fechaInsercion;

    public InfoGeneralContratacion() {
    }

    public InfoGeneralContratacion(Long idInfoGeneral) {
        this.idInfoGeneral = idInfoGeneral;
    }

    public Long getIdInfoGeneral() {
        return idInfoGeneral;
    }

    public void setIdInfoGeneral(Long idInfoGeneral) {
        this.idInfoGeneral = idInfoGeneral;
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Long getIdDetProcesoAdq() {
        return idDetProcesoAdq;
    }

    public void setIdDetProcesoAdq(Long idDetProcesoAdq) {
        this.idDetProcesoAdq = idDetProcesoAdq;
    }

    public String getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(String codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public String getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(String codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    public BigDecimal getMontoContrato() {
        return montoContrato;
    }

    public void setMontoContrato(BigDecimal montoContrato) {
        this.montoContrato = montoContrato;
    }

    public Long getCantidadContrato() {
        return cantidadContrato;
    }

    public void setCantidadContrato(Long cantidadContrato) {
        this.cantidadContrato = cantidadContrato;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInfoGeneral != null ? idInfoGeneral.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InfoGeneralContratacion)) {
            return false;
        }
        InfoGeneralContratacion other = (InfoGeneralContratacion) object;
        return !((this.idInfoGeneral == null && other.idInfoGeneral != null) || (this.idInfoGeneral != null && !this.idInfoGeneral.equals(other.idInfoGeneral)));
    }

    @Override
    public String toString() {
        return "sv.gob.mined.paquescolar.model.InfoGeneralContratacion[ idInfoGeneral=" + idInfoGeneral + " ]";
    }

    public LocalDateTime getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(LocalDateTime fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

}
