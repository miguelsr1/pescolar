package sv.gob.mined.dhcomitesso.model.dhcsso;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.OneToMany;

@Table(name = "PROCESO_ELECCION", schema = "SIECSSO")
@Entity
public class ProcesoEleccion implements Serializable {

    @Id
    @Column(name = "ID_PROCESO", nullable = false)
    private Integer id;

    @Column(name = "FECHA_INICIO")
    private LocalDate fechaInicio;

    @Column(name = "HABILITAR_PROCESO")
    private Boolean habilitarProceso;

    @Column(name = "HORAS")
    private Boolean horas;

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

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Boolean getHoras() {
        return horas;
    }

    public void setHoras(Boolean horas) {
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
