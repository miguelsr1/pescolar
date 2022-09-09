/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import org.hibernate.envers.Audited;
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;

/**
 *
 * @author CQuintanilla
 */
@Entity
@Table(name = "CE_CLIMA_FRIO")
public class CeClimaFrio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID_CE", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CE_CLIMA_FRIO")
    @SequenceGenerator(name = "SEQ_CE_CLIMA_FRIO", sequenceName = "SEQ_CE_CLIMA_FRIO", allocationSize = 1, initialValue = 1)
    private Long idCe;
    /*@Size(max = 5)
    @Column(name = "CODIGO_ENTIDAD")
    private String codigoEntidad;*/

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CODIGO_ENTIDAD")
    private VwCatalogoEntidadEducativa codigoEntidad;

    @Size(max = 25)
    @Column(name = "USUARIO")
    private String usuario;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "ESTADO_ELIMINACION")
    private Short estadoEliminacion;

    public CeClimaFrio() {
    }

    public CeClimaFrio(Long idCe) {
        this.idCe = idCe;
    }

    public Long getIdCe() {
        return idCe;
    }

    public void setIdCe(Long idCe) {
        this.idCe = idCe;
    }

    public VwCatalogoEntidadEducativa getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(VwCatalogoEntidadEducativa codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCe != null ? idCe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CeClimaFrio)) {
            return false;
        }
        CeClimaFrio other = (CeClimaFrio) object;
        if ((this.idCe == null && other.idCe != null) || (this.idCe != null && !this.idCe.equals(other.idCe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.pescolar.model.CeClimaFrio[ idCe=" + idCe + " ]";
    }

}
