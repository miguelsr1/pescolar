package sv.gob.mined.dhcomitesso.model.dhcsso;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "VOTACION", schema = "SIECSSO")
@Entity
public class Votacion implements Serializable {

    @Id
    @Column(name = "ID_VOTACION", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_CANDIDATO")
    private Candidato idCandidato;

    @Column(name = "FECHA_INSERCION")
    private LocalDate fechaInsercion;

    public LocalDate getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(LocalDate fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public Candidato getIdCandidato() {
        return idCandidato;
    }

    public void setIdCandidato(Candidato idCandidato) {
        this.idCandidato = idCandidato;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
