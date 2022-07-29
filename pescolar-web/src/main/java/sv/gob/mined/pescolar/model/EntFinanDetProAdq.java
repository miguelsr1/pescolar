/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "ENT_FINAN_DET_PRO_ADQ")
@NamedQueries({
    @NamedQuery(name = "EntFinanDetProAdq.findAll", query = "SELECT e FROM EntFinanDetProAdq e")})
public class EntFinanDetProAdq implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_ENT_FINAN")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ENT_FINAN_DET_PRO")
    @SequenceGenerator(name = "SEQ_ENT_FINAN_DET_PRO", sequenceName = "SEQ_ENT_FINAN_DET_PRO", allocationSize = 1, initialValue = 1)
    private Long idEntFinan;
    @JoinColumn(name = "COD_ENT_FINANCIERA", referencedColumnName = "COD_ENT_FINANCIERA")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private EntidadFinanciera codEntFinanciera;
    @Column(name = "ID_DET_PROCESO_ADQ")
    private Long idDetProcesoAdq;
    @Column(name = "ESTADO_ELIMINACION")
    private Short estadoEliminacion;
    @Column(name = "FECHA_INSERCION")
    private LocalDate fechaInsercion;
    @Column(name = "USUARIO_INSERCION")
    private String usuarioInsercion;
    @Transient
    private Boolean eliminar = false;

    public EntFinanDetProAdq() {
    }

    public void setEliminar(Boolean eliminar) {
        this.eliminar = eliminar;
        if (this.eliminar) {
            estadoEliminacion = (short) 1;
        } else {
            estadoEliminacion = (short) 0;
        }
    }

    public Boolean getEliminar() {
        return (estadoEliminacion.compareTo((short) 1) == 0);
    }

    public EntFinanDetProAdq(Long idEntFinan) {
        this.idEntFinan = idEntFinan;
    }

    public Long getIdEntFinan() {
        return idEntFinan;
    }

    public void setIdEntFinan(Long idEntFinan) {
        this.idEntFinan = idEntFinan;
    }

    public EntidadFinanciera getCodEntFinanciera() {
        return codEntFinanciera;
    }

    public void setCodEntFinanciera(EntidadFinanciera codEntFinanciera) {
        this.codEntFinanciera = codEntFinanciera;
    }

    public Long getIdDetProcesoAdq() {
        return idDetProcesoAdq;
    }

    public void setIdDetProcesoAdq(Long idDetProcesoAdq) {
        this.idDetProcesoAdq = idDetProcesoAdq;
    }

    public Short getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(Short estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    public LocalDate getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(LocalDate fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public String getUsuarioInsercion() {
        return usuarioInsercion;
    }

    public void setUsuarioInsercion(String usuarioInsercion) {
        this.usuarioInsercion = usuarioInsercion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEntFinan != null ? idEntFinan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntFinanDetProAdq)) {
            return false;
        }
        EntFinanDetProAdq other = (EntFinanDetProAdq) object;
        return !((this.idEntFinan == null && other.idEntFinan != null) || (this.idEntFinan != null && !this.idEntFinan.equals(other.idEntFinan)));
    }

    @Override
    public String toString() {
        return "sv.gob.mined.paquescolar.model.EntFinanDetProAdq[ idEntFinan=" + idEntFinan + " ]";
    }

}
