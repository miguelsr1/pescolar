package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "ESTADO_REGISTRO")
@Entity
public class EstadoRegistro implements Serializable {

    @OneToMany(mappedBy = "idEstadoRegistro", fetch = FetchType.LAZY)
    private List<Empresa> empresaList;
    @Id
    @Column(name = "ID_ESTADO_REGISTRO", nullable = false)
    private Long id;

    @Column(name = "DESCRIPCION_ESTADO", length = 75)
    private String descripcionEstado;

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoRegistro() {
    }

    public List<Empresa> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<Empresa> empresaList) {
        this.empresaList = empresaList;
    }
}