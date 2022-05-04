package sv.gob.mined.dhcomitesso.model.dhcsso;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name = "PROCESO_ELECCION", schema = "SIECSSO")
@Entity
public class ProcesoEleccion implements Serializable {

    @Id
    @Column(name = "ID_PROCESO", nullable = false)
    private Integer id;

    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;

    @Column(name = "HABILITAR_PROCESO")
    private Boolean habilitarProceso;

    @Column(name = "HORAS")
    private Integer horas;

    @OneToMany(mappedBy = "idProceso")
    private List<DetalleProceso> detalleProcesoList;

    @Column(name = "FECHA_INSERCION")
    private LocalDate fechaInsercion;

    public LocalDate getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(LocalDate fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public Boolean getHabilitarProceso() {
        return habilitarProceso;
    }

    public void setHabilitarProceso(Boolean habilitarProceso) {
        this.habilitarProceso = habilitarProceso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<DetalleProceso> getDetalleProcesoList() {
        return detalleProcesoList;
    }

    public void setDetalleProcesoList(List<DetalleProceso> detalleProcesoList) {
        this.detalleProcesoList = detalleProcesoList;
    }
}
