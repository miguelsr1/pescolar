/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Basic;
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
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;

/**
 *
 * @author Carlos Quintanilla
 */
@Table(name = "RUBRO_POR_CE")
@Entity
public class RubroPorCe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_RUBRO", nullable = false)
    @GeneratedValue(generator = "SEQ_RUBRO_POR_CE", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "SEQ_RUBRO_POR_CE", name = "SEQ_RUBRO_POR_CE", allocationSize = 1, initialValue = 1)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CODIGO_ENTIDAD")
    private VwCatalogoEntidadEducativa codigoEntidad;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_RUBRO_INTERES")
    private RubrosAmostrarInteres idRubroInteres;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PROCESO_ADQ", referencedColumnName = "ID_PROCESO_ADQ")
    private ProcesoAdquisicion idProcesoAdq;
    @Column(name = "USUARIO", length = 25)
    private String usuario;
    @Column(name = "FECHA")
    private LocalDate fecha;
    @Column(name = "ESTADO_ELIMINACION")
    private Boolean estadoEliminacion;

    public RubroPorCe() {
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the codigoEntidad
     */
    public VwCatalogoEntidadEducativa getCodigoEntidad() {
        return codigoEntidad;
    }

    /**
     * @param codigoEntidad the codigoEntidad to set
     */
    public void setCodigoEntidad(VwCatalogoEntidadEducativa codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    /**
     * @return the idRubroInteres
     */
    public RubrosAmostrarInteres getIdRubroInteres() {
        return idRubroInteres;
    }

    /**
     * @param idRubroInteres the idRubroInteres to set
     */
    public void setIdRubroInteres(RubrosAmostrarInteres idRubroInteres) {
        this.idRubroInteres = idRubroInteres;
    }

    /**
     * @return the idProcesoAdq
     */
    public ProcesoAdquisicion getIdProcesoAdq() {
        return idProcesoAdq;
    }

    /**
     * @param idProcesoAdq the idProcesoAdq to set
     */
    public void setIdProcesoAdq(ProcesoAdquisicion idProcesoAdq) {
        this.idProcesoAdq = idProcesoAdq;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the fecha
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the estadoEliminacion
     */
    public Boolean getEstadoEliminacion() {
        return estadoEliminacion;
    }

    /**
     * @param estadoEliminacion the estadoEliminacion to set
     */
    public void setEstadoEliminacion(Boolean estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RubroPorCe)) {
            return false;
        }
        RubroPorCe other = (RubroPorCe) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "sv.gob.mined.pescolar.model.RubroPorCe[ id=" + getId() + " ]";
    }

}
