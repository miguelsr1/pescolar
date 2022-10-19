package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "RESOLUCIONES_ADJUDICATIVAS")
@Entity
public class ResolucionesAdjudicativa implements Serializable {

    @Id
    @Column(name = "ID_RESOLUCION_ADJ", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PARTICIPANTE")
    private Participante idParticipante;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ESTADO_RESERVA")
    private EstadoReserva idEstadoReserva;

    @Column(name = "VALOR", precision = 18, scale = 2)
    private BigDecimal valor;

    @Column(name = "USUARIO_INSERCION", nullable = false, length = 25)
    private String usuarioInsercion;

    @Column(name = "FECHA_INSERCION", nullable = false)
    private LocalDateTime fechaInsercion;

    @Column(name = "USUARIO_MODIFICACION", length = 25)
    private String usuarioModificacion;

    @Column(name = "FECHA_MODIFICACION")
    private LocalDateTime fechaModificacion;

    @Column(name = "FECHA_ELIMINACION")
    private LocalDateTime fechaEliminacion;

    @Column(name = "ESTADO_ELIMINACION", nullable = false)
    private Long estadoEliminacion;

    @OneToMany(mappedBy = "idResolucionAdj", fetch = FetchType.LAZY)
    private List<ContratosOrdenesCompra> contratosOrdenesComprasList;

    public Long getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(Long estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

    public LocalDateTime getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(LocalDateTime fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public LocalDateTime getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(LocalDateTime fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public String getUsuarioInsercion() {
        return usuarioInsercion;
    }

    public void setUsuarioInsercion(String usuarioInsercion) {
        this.usuarioInsercion = usuarioInsercion;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public EstadoReserva getIdEstadoReserva() {
        return idEstadoReserva;
    }

    public void setIdEstadoReserva(EstadoReserva idEstadoReserva) {
        this.idEstadoReserva = idEstadoReserva;
    }

    public Participante getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(Participante idParticipante) {
        this.idParticipante = idParticipante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ContratosOrdenesCompra> getContratosOrdenesComprasList() {
        return contratosOrdenesComprasList;
    }

    public void setContratosOrdenesComprasList(List<ContratosOrdenesCompra> contratosOrdenesComprasList) {
        this.contratosOrdenesComprasList = contratosOrdenesComprasList;
    }
}
