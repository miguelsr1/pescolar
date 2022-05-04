package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Table(name = "ANHO")
@Entity
public class Anho implements Serializable {

    @OneToMany(mappedBy = "idAnho", fetch = FetchType.LAZY)
    private List<DetRubroMuestraIntere> detRubroMuestraIntereList;

    @OneToMany(mappedBy = "idAnho", fetch = FetchType.LAZY)
    private List<ProcesoAdquisicion> procesoAdquisicionList;
    @Id
    @Column(name = "ID_ANHO", nullable = false)
    private Long id;

    @Column(name = "ANHO", nullable = false, length = 4)
    private String anho;

    public String getAnho() {
        return anho;
    }

    public void setAnho(String anho) {
        this.anho = anho;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Anho() {
    }

    @XmlTransient
    public List<ProcesoAdquisicion> getProcesoAdquisicionList() {
        return procesoAdquisicionList;
    }

    public void setProcesoAdquisicionList(List<ProcesoAdquisicion> procesoAdquisicionList) {
        this.procesoAdquisicionList = procesoAdquisicionList;
    }

    public List<DetRubroMuestraIntere> getDetRubroMuestraIntereList() {
        return detRubroMuestraIntereList;
    }

    public void setDetRubroMuestraIntereList(List<DetRubroMuestraIntere> detRubroMuestraIntereList) {
        this.detRubroMuestraIntereList = detRubroMuestraIntereList;
    }
}