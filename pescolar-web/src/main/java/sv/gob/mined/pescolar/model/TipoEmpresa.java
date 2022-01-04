package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "TIPO_EMPRESA")
@Entity
public class TipoEmpresa implements Serializable {
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
}