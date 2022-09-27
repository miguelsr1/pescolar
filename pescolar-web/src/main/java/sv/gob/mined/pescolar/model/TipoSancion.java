/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author RAREcheverria
 */
@Entity
@Table(name = "TIPO_SANCION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoSancion.findAll", query = "SELECT t FROM TipoSancion t")})
public class TipoSancion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPO_SANCION")
    private Integer idTipoSancion;
    @Size(max = 200)
    @Column(name = "DESCRIPCION_SANCION")
    private String descripcionSancion;
    @Size(max = 25)
    @Column(name = "USUARIO")
    private String usuario;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "ESTADO_ELIMINACION")
    private Short estadoEliminacion;
    @OneToMany(mappedBy = "idTipoSancion")
    private Collection<ListaNegraEmpresa> listaNegraEmpresaCollection;

    public TipoSancion() {
    }

    public TipoSancion(Integer idTipoSancion) {
        this.idTipoSancion = idTipoSancion;
    }

    public Integer getIdTipoSancion() {
        return idTipoSancion;
    }

    public void setIdTipoSancion(Integer idTipoSancion) {
        this.idTipoSancion = idTipoSancion;
    }

    public String getDescripcionSancion() {
        return descripcionSancion;
    }

    public void setDescripcionSancion(String descripcionSancion) {
        this.descripcionSancion = descripcionSancion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Short getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(Short estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    @XmlTransient
    public Collection<ListaNegraEmpresa> getListaNegraEmpresaCollection() {
        return listaNegraEmpresaCollection;
    }

    public void setListaNegraEmpresaCollection(Collection<ListaNegraEmpresa> listaNegraEmpresaCollection) {
        this.listaNegraEmpresaCollection = listaNegraEmpresaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoSancion != null ? idTipoSancion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoSancion)) {
            return false;
        }
        TipoSancion other = (TipoSancion) object;
        if ((this.idTipoSancion == null && other.idTipoSancion != null) || (this.idTipoSancion != null && !this.idTipoSancion.equals(other.idTipoSancion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.pescolar.model.TipoSancion[ idTipoSancion=" + idTipoSancion + " ]";
    }
    
}
