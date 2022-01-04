package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "DEPARTAMENTO")
@Entity
public class Departamento implements Serializable {
    @Id
    @Column(name = "CODIGO_DEPARTAMENTO", nullable = false, length = 2)
    private String id;

    @Column(name = "NOMBRE_DEPARTAMENTO", length = 50)
    private String nombreDepartamento;

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}