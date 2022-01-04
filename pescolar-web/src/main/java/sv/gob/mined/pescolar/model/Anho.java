package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ANHO")
@Entity
public class Anho implements Serializable {
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
}