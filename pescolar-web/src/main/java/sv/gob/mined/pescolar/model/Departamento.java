package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "DEPARTAMENTO")
@Entity
public class Departamento implements Serializable {

    @OneToMany(mappedBy = "codigoDepartamento", fetch = FetchType.LAZY)
    private List<Municipio> municipioList;
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

    public Departamento() {
    }

    public List<Municipio> getMunicipioList() {
        return municipioList;
    }

    public void setMunicipioList(List<Municipio> municipioList) {
        this.municipioList = municipioList;
    }
}