package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Where;

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "ORGANIZACION_EDUCATIVA")
@Where(clause = "ESTADO_ELIMINACION = 0")
/*@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
            name = "SP_UPD_NOMBRE_DIRECTOR",
            procedureName = "SP_UPD_NOMBRE_DIRECTOR",
            returnsResultSet = false,
            parameters = {
                @StoredProcedureParameter(queryParameter = "P_COD_ENT", name = "P_COD_ENT", direction = Direction.IN, type = Integer.class),
                @StoredProcedureParameter(queryParameter = "P_ID_ANHO", name = "P_ID_ANHO", direction = Direction.IN, type = Integer.class),
                @StoredProcedureParameter(queryParameter = "P_NOMBRE", name = "P_NOMBRE", direction = Direction.IN, type = String.class)
            }
    )})*/
public class OrganizacionEducativa implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_ORGANIZACION_EDUCATIVA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_edu")
    @SequenceGenerator(name="org_edu", sequenceName = "SEQ_ORG_EDU", allocationSize=1, initialValue=1)
    private Long idOrganizacionEducativa;
    @Column(name = "CODIGO_ENTIDAD")
    private String codigoEntidad;
    @Column(name = "NOMBRE_MIEMBRO")
    private String nombreMiembro;
    @Column(name = "CARGO")
    private String cargo;
    @Column(name = "FIRMA_CONTRATO")
    private Long firmaContrato;
    @Column(name = "USUARIO_INSERCION")
    private String usuarioInsercion;
    @Column(name = "FECHA_INSERCION")
    private LocalDateTime fechaInsercion;
    @Column(name = "USUARIO_MODIFICACION")
    private String usuarioModificacion;
    @Column(name = "TEL_DIRECTOR")
    private String telDirector;
    @Column(name = "TEL_DIRECTOR2")
    private String telDirector2;
    @Column(name = "NUMERO_DUI")
    private String numeroDui;
    @Column(name = "FECHA_MODIFICACION")
    private LocalDateTime fechaModificacion;
    @Column(name = "FECHA_ELIMINACION")
    private LocalDateTime fechaEliminacion;
    @Column(name = "ESTADO_ELIMINACION")
    private Long estadoEliminacion;
    @Column(name = "CORREO_ELECTRONICO")
    private String correoElectronico;

    public OrganizacionEducativa() {
        estadoEliminacion = 0l;
        fechaInsercion = LocalDateTime.now();
    }

    public OrganizacionEducativa(Long idOrganizacionEducativa) {
        this.idOrganizacionEducativa = idOrganizacionEducativa;
    }

    public Long getIdOrganizacionEducativa() {
        return idOrganizacionEducativa;
    }

    public void setIdOrganizacionEducativa(Long idOrganizacionEducativa) {
        this.idOrganizacionEducativa = idOrganizacionEducativa;
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public String getNombreMiembro() {
        return nombreMiembro;
    }

    public void setNombreMiembro(String nombreMiembro) {
        this.nombreMiembro = nombreMiembro;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Long getFirmaContrato() {
        return firmaContrato;
    }

    public void setFirmaContrato(Long firmaContrato) {
        this.firmaContrato = firmaContrato;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOrganizacionEducativa != null ? idOrganizacionEducativa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrganizacionEducativa)) {
            return false;
        }
        OrganizacionEducativa other = (OrganizacionEducativa) object;
        return !((this.idOrganizacionEducativa == null && other.idOrganizacionEducativa != null) || (this.idOrganizacionEducativa != null && !this.idOrganizacionEducativa.equals(other.idOrganizacionEducativa)));
    }

    @Override
    public String toString() {
        return "sv.gob.mined.paquescolar.model.OrganizacionEducativa[ idOrganizacionEducativa=" + idOrganizacionEducativa + " ]";
    }

    public String getTelDirector() {
        return telDirector;
    }

    public void setTelDirector(String telDirector) {
        this.telDirector = telDirector;
    }

    public String getTelDirector2() {
        return telDirector2;
    }

    public void setTelDirector2(String telDirector2) {
        this.telDirector2 = telDirector2;
    }

    public String getNumeroDui() {
        return numeroDui;
    }

    public void setNumeroDui(String numeroDui) {
        this.numeroDui = numeroDui;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
}