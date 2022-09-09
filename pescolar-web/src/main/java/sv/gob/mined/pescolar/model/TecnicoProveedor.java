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
import org.hibernate.annotations.Where;

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "TECNICO_PROVEEDOR")
@Where(clause = "ESTADO_ELIMINACION = 0")
public class TecnicoProveedor implements Serializable {

    @Id
    @Column(name = "ID_TECNICO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTecnicoProveedor")
    @SequenceGenerator(name = "seqTecnicoProveedor", sequenceName = "SEQ_TECNICO_PROVEEDOR", allocationSize = 1, initialValue = 1)
    private Long idTecnico;
    @Column(name = "MAIL_TECNICO")
    private String mailTecnico;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_EMPRESA", nullable = false)
    private Empresa idEmpresa;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ANHO")
    private Anho idAnho;
    @Column(name = "FECHA_INSERCION", nullable = false)
    private LocalDate fechaInsercion;
    @Column(name = "USUARIO_INSERCION", length = 25)
    private String usuarioInsercion;
    @Column(name = "FECHA_ELIMINACION", nullable = false)
    private LocalDate fechaEliminacion;
    @Column(name = "ESTADO_ELIMINACION")
    private Boolean estadoEliminacion;

    public TecnicoProveedor() {
    }

    public TecnicoProveedor(String mailTecnico) {
        this.mailTecnico = mailTecnico;
    }

    public Long getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Long idTecnico) {
        this.idTecnico = idTecnico;
    }

    public String getMailTecnico() {
        return mailTecnico;
    }

    public void setMailTecnico(String mailTecnico) {
        this.mailTecnico = mailTecnico;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Anho getIdAnho() {
        return idAnho;
    }

    public void setIdAnho(Anho idAnho) {
        this.idAnho = idAnho;
    }

    public LocalDate getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(LocalDate fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public String getUsuarioInsercion() {
        return usuarioInsercion;
    }

    public void setUsuarioInsercion(String usuarioInsercion) {
        this.usuarioInsercion = usuarioInsercion;
    }

    public LocalDate getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(LocalDate fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public Boolean getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(Boolean estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    @Override
    public String toString() {
        return "TecnicoProveedor{" + "idTecnico=" + idTecnico + '}';
    }

}
