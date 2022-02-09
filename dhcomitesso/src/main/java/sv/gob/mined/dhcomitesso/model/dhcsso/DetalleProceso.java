/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.dhcomitesso.model.dhcsso;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "DETALLE_PROCESO")
@NamedQueries({
    @NamedQuery(name = "DetalleProceso.findAll", query = "SELECT d FROM DetalleProceso d")})
public class DetalleProceso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)    
    @Column(name = "ID_DETALLE_PROCESO")
    @GeneratedValue(generator = "SEQ_DETALLE_PROCESO", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_DETALLE_PROCESO", sequenceName = "SEQ_DETALLE_PROCESO", allocationSize = 1, initialValue = 1, schema = "SIECSSO")
    private Long idDetalleProceso;
    @Column(name = "FECHA_VOTACION")
    private LocalDate fechaVotacion;
    @JoinColumn(name = "ID_EMPLEADO", referencedColumnName = "ID_EMPLEADO")
    @ManyToOne
    private Empleado idEmpleado;
    @JoinColumn(name = "ID_PROCESO", referencedColumnName = "ID_PROCESO")
    @ManyToOne
    private ProcesoEleccion idProceso;

    public DetalleProceso() {
    }

    public DetalleProceso(Long idDetalleProceso) {
        this.idDetalleProceso = idDetalleProceso;
    }

    public Long getIdDetalleProceso() {
        return idDetalleProceso;
    }

    public void setIdDetalleProceso(Long idDetalleProceso) {
        this.idDetalleProceso = idDetalleProceso;
    }

    public LocalDate getFechaVotacion() {
        return fechaVotacion;
    }

    public void setFechaVotacion(LocalDate fechaVotacion) {
        this.fechaVotacion = fechaVotacion;
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public ProcesoEleccion getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(ProcesoEleccion idProceso) {
        this.idProceso = idProceso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleProceso != null ? idDetalleProceso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleProceso)) {
            return false;
        }
        DetalleProceso other = (DetalleProceso) object;
        if ((this.idDetalleProceso == null && other.idDetalleProceso != null) || (this.idDetalleProceso != null && !this.idDetalleProceso.equals(other.idDetalleProceso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.dhcomitesso.model.dhcsso.DetalleProceso[ idDetalleProceso=" + idDetalleProceso + " ]";
    }
    
}
