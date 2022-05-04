package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Table(name = "MUNICIPIO")
@Entity
public class Municipio implements Serializable {

    @OneToMany(mappedBy = "idMunicipio", fetch = FetchType.LAZY)
    private List<Empresa> empresaList;
    @OneToMany(mappedBy = "idMunicipio", fetch = FetchType.LAZY)
    private List<Persona> personaList;
    @Id
    @Column(name = "ID_MUNICIPIO", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CODIGO_DEPARTAMENTO")
    private Departamento codigoDepartamento;

    @Column(name = "NOMBRE_MUNICIPIO", length = 150)
    private String nombreMunicipio;

    @Column(name = "CODIGO_MUNICIPIO", length = 2)
    private String codigoMunicipio;

    public String getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(String codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    public Departamento getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(Departamento codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Municipio() {
    }

    public List<Empresa> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<Empresa> empresaList) {
        this.empresaList = empresaList;
    }

    public List<Persona> getPersonaList() {
        return personaList;
    }

    public void setPersonaList(List<Persona> personaList) {
        this.personaList = personaList;
    }
}