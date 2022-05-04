package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_USUARIO")
    private Long idUsuario;
    @Size(max = 25)
    @Column(name = "USUARIO_INSERCION")
    private String usuarioInsercion;
    @Column(name = "FECHA_INSERCION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInsercion;
    @Size(max = 25)
    @Column(name = "USUARIO_MODIFICACION")
    private String usuarioModificacion;
    @Column(name = "FECHA_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "FECHA_ELIMINACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEliminacion;
    @Column(name = "ESTADO_ELIMINACION")
    private Long estadoEliminacion;
    @JoinColumn(name = "CODIGO_DEPARTAMENTO", referencedColumnName = "CODIGO_DEPARTAMENTO")
    @ManyToOne(fetch = FetchType.EAGER)
    private Departamento codigoDepartamento;
    @Column(name = "FECHA_VENCIMIENTO_CLAVE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimientoClave;
    @Column(name = "FECHA_INICIO_LOGIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicioLogin;
    @Column(name = "FECHA_FIN_LOGIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinLogin;
    @Column(name = "RANGO_FECHA_LOGIN")
    private Short rangoFechaLogin;
    @Column(name = "ACTIVO")
    private Short activo;
    @Size(max = 100)
    @Column(name = "TOKEN_CAMBIO_CLAVE")
    private String tokenCambioClave;
    @Column(name = "CAMBIAR_CLAVE")
    private Short cambiarClave;

    @JoinColumn(name = "ID_TIPO_USUARIO", referencedColumnName = "ID_TIPO_USUARIO")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoUsuario idTipoUsuario;

    @JoinColumn(name = "ID_PERSONA", referencedColumnName = "ID_PERSONA")
    @ManyToOne(fetch = FetchType.EAGER)
    private Persona idPersona;

    public Usuario() {
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuarioInsercion() {
        return usuarioInsercion;
    }

    public void setUsuarioInsercion(String usuarioInsercion) {
        this.usuarioInsercion = usuarioInsercion;
    }

    public Date getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(Date fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Date getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(Date fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public Long getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(Long estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    public Departamento getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(Departamento codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public Date getFechaVencimientoClave() {
        return fechaVencimientoClave;
    }

    public void setFechaVencimientoClave(Date fechaVencimientoClave) {
        this.fechaVencimientoClave = fechaVencimientoClave;
    }

    public Date getFechaInicioLogin() {
        return fechaInicioLogin;
    }

    public void setFechaInicioLogin(Date fechaInicioLogin) {
        this.fechaInicioLogin = fechaInicioLogin;
    }

    public Date getFechaFinLogin() {
        return fechaFinLogin;
    }

    public void setFechaFinLogin(Date fechaFinLogin) {
        this.fechaFinLogin = fechaFinLogin;
    }

    public Short getRangoFechaLogin() {
        return rangoFechaLogin;
    }

    public void setRangoFechaLogin(Short rangoFechaLogin) {
        this.rangoFechaLogin = rangoFechaLogin;
    }

    public Short getActivo() {
        return activo;
    }

    public void setActivo(Short activo) {
        this.activo = activo;
    }

    public String getTokenCambioClave() {
        return tokenCambioClave;
    }

    public void setTokenCambioClave(String tokenCambioClave) {
        this.tokenCambioClave = tokenCambioClave;
    }

    public Short getCambiarClave() {
        return cambiarClave;
    }

    public void setCambiarClave(Short cambiarClave) {
        this.cambiarClave = cambiarClave;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        return !((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario)));
    }

    @Override
    public String toString() {
        return "sv.gob.mined.pescolar.model.Usuario_1[ idUsuario=" + idUsuario + " ]";
    }

    public TipoUsuario getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(TipoUsuario idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public Persona getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Persona idPersona) {
        this.idPersona = idPersona;
    }

}
