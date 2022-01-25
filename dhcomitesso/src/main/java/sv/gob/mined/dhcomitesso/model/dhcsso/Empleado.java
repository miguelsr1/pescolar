package sv.gob.mined.dhcomitesso.model.dhcsso;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Table(name = "EMPLEADO", schema = "SIECSSO")
@Entity
public class Empleado implements Serializable {

    @Id
    @Column(name = "ID_EMPLEADO", nullable = false)
    private Integer id;

    @Column(name = "CODIGO", length = 20)
    private String codigo;

    @Column(name = "DUI", length = 10)
    private String dui;

    @Column(name = "CORREO_ELECTRONICO", length = 250)
    private String correoElectronico;

    @Column(name = "CLAVE_ACCESO", length = 30)
    private String claveAcceso;

    @Column(name = "ACTIVO")
    private Boolean activo;

    @Column(name = "FECHA_ACTIVACION")
    private LocalDate fechaActivacion;

    public LocalDate getFechaActivacion() {
        return fechaActivacion;
    }

    public void setFechaActivacion(LocalDate fechaActivacion) {
        this.fechaActivacion = fechaActivacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
