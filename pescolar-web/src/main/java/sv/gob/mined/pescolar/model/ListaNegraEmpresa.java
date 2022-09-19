/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author RAREcheverria
 */
@Entity
@Table(name = "LISTA_NEGRA_EMPRESA")
@XmlRootElement

public class ListaNegraEmpresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_LISTA_NEGRA")
    private BigDecimal idListaNegra;
    @Column(name = "TIEMPO_SANCION")
    private Integer tiempoSancion;
    @Size(max = 500)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Size(max = 25)
    @Column(name = "USUARIO")
    private String usuario;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "ESTADO_ELIMINACION")
    private Short estadoEliminacion;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID_EMPRESA")
    @ManyToOne
    private Empresa idEmpresa;

    public ListaNegraEmpresa() {
    }

    public ListaNegraEmpresa(BigDecimal idListaNegra) {
        this.idListaNegra = idListaNegra;
    }

    public BigDecimal getIdListaNegra() {
        return idListaNegra;
    }

    public void setIdListaNegra(BigDecimal idListaNegra) {
        this.idListaNegra = idListaNegra;
    }

    public Integer getTiempoSancion() {
        return tiempoSancion;
    }

    public void setTiempoSancion(Integer tiempoSancion) {
        this.tiempoSancion = tiempoSancion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idListaNegra != null ? idListaNegra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ListaNegraEmpresa)) {
            return false;
        }
        ListaNegraEmpresa other = (ListaNegraEmpresa) object;
        if ((this.idListaNegra == null && other.idListaNegra != null) || (this.idListaNegra != null && !this.idListaNegra.equals(other.idListaNegra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.pescolar.model.ListaNegraEmpresa[ idListaNegra=" + idListaNegra + " ]";
    }
    
}
