package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name = "DIAS_PLAZO_CONTRATO")
@Entity
public class DiasPlazoContrato implements Serializable {

    @Id
    @Column(name = "ID_DIA", nullable = false)
    @GeneratedValue(generator = "seqDias", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seqDias", sequenceName = "SEQ_DIAS_PLAZO", allocationSize = 1, initialValue = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ANHO")
    private Anho idAnho;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_RUBRO_INTERES")
    private RubrosAmostrarInteres idRubroInteres;

    @Column(name = "DIAS_PLAZO")
    private Integer diasPlazo;

    @Column(name = "USUARIO", length = 25)
    private String usuario;

    @Column(name = "FECHA")
    private LocalDate fecha;

    @Column(name = "ESTADO_ELIMINACION")
    private Boolean estadoEliminacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDiasPlazo() {
        return diasPlazo;
    }

    public void setDiasPlazo(Integer diasPlazo) {
        this.diasPlazo = diasPlazo;
    }

    public Anho getIdAnho() {
        return idAnho;
    }

    public void setIdAnho(Anho idAnho) {
        this.idAnho = idAnho;
    }

    public RubrosAmostrarInteres getIdRubroInteres() {
        return idRubroInteres;
    }

    public void setIdRubroInteres(RubrosAmostrarInteres idRubroInteres) {
        this.idRubroInteres = idRubroInteres;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Boolean getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(Boolean estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    @Override
    public String toString() {
        return "DiasPlazoContrato{" + "id=" + id + '}';
    }

}
