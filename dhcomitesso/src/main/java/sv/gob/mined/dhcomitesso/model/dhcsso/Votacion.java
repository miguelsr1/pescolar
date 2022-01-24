package sv.gob.mined.dhcomitesso.model.dhcsso;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "VOTACION", schema = "SIECSSO")
@Entity
public class Votacion implements Serializable {

    @Id
    @Column(name = "ID_VOTACION", nullable = false)
    @GeneratedValue(generator = "SEQ_VOTO", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_VOTO", sequenceName = "SEQ_VOTO", allocationSize = 1, initialValue = 1, schema = "SIECSSO")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PROCESO")
    private ProcesoEleccion idProceso;

    @ManyToOne(fetch = FetchType.EAGER)
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

    public ProcesoEleccion getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(ProcesoEleccion idProceso) {
        this.idProceso = idProceso;
    }
}
