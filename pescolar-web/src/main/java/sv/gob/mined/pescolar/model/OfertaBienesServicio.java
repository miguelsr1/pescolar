package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.Where;
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;

@Table(name = "OFERTA_BIENES_SERVICIOS")
@Entity
@Where(clause = "ESTADO_ELIMINACION = 0")
public class OfertaBienesServicio implements Serializable {
    @Id
    @Column(name = "ID_OFERTA", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oferta")
    @SequenceGenerator(name = "oferta", sequenceName = "SEQ_OFERTA_BS", allocationSize = 1, initialValue = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_DET_PROCESO_ADQ", nullable = false)
    private DetalleProcesoAdq idDetProcesoAdq;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CODIGO_ENTIDAD", nullable = false)
    private VwCatalogoEntidadEducativa codigoEntidad;

    @Column(name = "FECHA_APERTURA")
    private LocalDate fechaApertura;

    @Column(name = "HORA_APERTURA")
    private Long horaApertura;

    @Column(name = "MINUTO_APERTURA")
    private Long minutoApertura;

    @Column(name = "JUSTIFICACION", length = 500)
    private String justificacion;

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

    @Column(name = "FECHA_SOLICITUD")
    private LocalDate fechaSolicitud;

    @Column(name = "DESCRIPCION_OFERTA", length = 20)
    private String descripcionOferta;

    @Column(name = "FECHA_OFERTA")
    private LocalDate fechaOferta;
    
    @OneToMany(mappedBy = "idOferta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private List<Participante> participantesList;

    public LocalDate getFechaOferta() {
        return fechaOferta;
    }

    public void setFechaOferta(LocalDate fechaOferta) {
        this.fechaOferta = fechaOferta;
    }

    public String getDescripcionOferta() {
        return descripcionOferta;
    }

    public void setDescripcionOferta(String descripcionOferta) {
        this.descripcionOferta = descripcionOferta;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

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

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public Long getMinutoApertura() {
        return minutoApertura;
    }

    public void setMinutoApertura(Long minutoApertura) {
        this.minutoApertura = minutoApertura;
    }

    public Long getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(Long horaApertura) {
        this.horaApertura = horaApertura;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public VwCatalogoEntidadEducativa getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(VwCatalogoEntidadEducativa codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public DetalleProcesoAdq getIdDetProcesoAdq() {
        return idDetProcesoAdq;
    }

    public void setIdDetProcesoAdq(DetalleProcesoAdq idDetProcesoAdq) {
        this.idDetProcesoAdq = idDetProcesoAdq;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Participante> getParticipantesList() {
        return participantesList;
    }

    public void setParticipantesList(List<Participante> participantesList) {
        this.participantesList = participantesList;
    }

    @Override
    public String toString() {
        return "OfertaBienesServicio{" + "id=" + id + '}';
    }

}