package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Table(name = "TIPO_EMPRESA")
@Entity
public class TipoEmpresa implements Serializable {

    @OneToMany(mappedBy = "idTipoEmpresa", fetch = FetchType.LAZY)
    private List<Empresa> empresaList;
    @OneToMany(mappedBy = "tipIdTipoEmp", fetch = FetchType.LAZY)
    private List<TipoEmpresa> tipoEmpresaList;
    @Id
    @Column(name = "ID_TIPO_EMP", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TIP_ID_TIPO_EMP")
    private TipoEmpresa tipIdTipoEmp;

    @Column(name = "DESCRIPCION_TIPO_EMP", length = 50)
    private String descripcionTipoEmp;

    public String getDescripcionTipoEmp() {
        return descripcionTipoEmp;
    }

    public void setDescripcionTipoEmp(String descripcionTipoEmp) {
        this.descripcionTipoEmp = descripcionTipoEmp;
    }

    public TipoEmpresa getTipIdTipoEmp() {
        return tipIdTipoEmp;
    }

    public void setTipIdTipoEmp(TipoEmpresa tipIdTipoEmp) {
        this.tipIdTipoEmp = tipIdTipoEmp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoEmpresa() {
    }

    public List<Empresa> getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List<Empresa> empresaList) {
        this.empresaList = empresaList;
    }

    public List<TipoEmpresa> getTipoEmpresaList() {
        return tipoEmpresaList;
    }

    public void setTipoEmpresaList(List<TipoEmpresa> tipoEmpresaList) {
        this.tipoEmpresaList = tipoEmpresaList;
    }
}