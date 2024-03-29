package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

@Table(name = "PARTICIPANTES")
@Entity
@Where(clause = "ESTADO_ELIMINACION = 0")
public class Participante implements Serializable {

    @Id
    @Column(name = "ID_PARTICIPANTE", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participantes")
    @SequenceGenerator(name = "participantes", sequenceName = "SEQ_PARTICIPANTES", allocationSize = 1, initialValue = 1)
    private Long id;

    @JoinColumn(name = "ID_OFERTA", referencedColumnName = "ID_OFERTA")
    @ManyToOne(fetch = FetchType.EAGER)
    private OfertaBienesServicio idOferta;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ID_EMPRESA", nullable = false)
    private Empresa idEmpresa;

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

    @Column(name = "MODIFICATIVA", nullable = false)
    private Long modificativa;

    @Column(name = "PORCENTAJE_CAPACIDAD", precision = 10, scale = 2)
    private BigDecimal porcentajeCapacidad;

    @Column(name = "PORCENTAJE_GEO", precision = 10, scale = 2)
    private BigDecimal porcentajeGeo;

    @Column(name = "PORCENTAJE_PRECIO", precision = 10, scale = 2)
    private BigDecimal porcentajePrecio;

    @OneToMany(mappedBy = "idParticipante", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy(clause = "to_number(noItem) asc")
    private List<DetalleOferta> detalleOfertasList;

    @OneToMany(mappedBy = "idParticipante", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ResolucionesAdjudicativa> resolucionesAdjudicativaList;

    @Transient
    private BigInteger cantidad = BigInteger.ZERO;
    @Transient
    private BigDecimal monto = BigDecimal.ZERO;
    @Transient
    private Boolean eliminar = false;
    @Transient
    private Boolean tieneReserva = false;

    public Participante() {
    }

    public Participante(Long id) {
        this.id = id;
    }

    public BigDecimal getPorcentajePrecio() {
        return porcentajePrecio;
    }

    public void setPorcentajePrecio(BigDecimal porcentajePrecio) {
        this.porcentajePrecio = porcentajePrecio;
    }

    public BigDecimal getPorcentajeGeo() {
        return porcentajeGeo;
    }

    public void setPorcentajeGeo(BigDecimal porcentajeGeo) {
        this.porcentajeGeo = porcentajeGeo;
    }

    public BigDecimal getPorcentajeCapacidad() {
        return porcentajeCapacidad;
    }

    public void setPorcentajeCapacidad(BigDecimal porcentajeCapacidad) {
        this.porcentajeCapacidad = porcentajeCapacidad;
    }

    public Long getModificativa() {
        return modificativa;
    }

    public void setModificativa(Long modificativa) {
        this.modificativa = modificativa;
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

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public OfertaBienesServicio getIdOferta() {
        return idOferta;
    }

    public void setIdOferta(OfertaBienesServicio idOferta) {
        this.idOferta = idOferta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DetalleOferta> getDetalleOfertasList() {
        if (detalleOfertasList == null) {
            detalleOfertasList = new ArrayList();
        }
        return detalleOfertasList;
    }

    public void setDetalleOfertasList(List<DetalleOferta> detalleOfertasList) {
        this.detalleOfertasList = detalleOfertasList;
    }

    public BigInteger getCantidad() {
        cantidad = BigInteger.ZERO;
        for (DetalleOferta detalleOfertas : getDetalleOfertasList()) {
            if (detalleOfertas.getEstadoEliminacion().compareTo(0l) == 0) {
                cantidad = cantidad.add(detalleOfertas.getCantidad());
            }
        }
        return cantidad;
    }

    public BigDecimal getMonto() {
        monto = BigDecimal.ZERO;
        for (DetalleOferta detalleOfertas : getDetalleOfertasList()) {
            if (detalleOfertas.getEstadoEliminacion().compareTo(0l) == 0) {
                monto = monto.add(detalleOfertas.getPrecioUnitario().multiply(new BigDecimal(detalleOfertas.getCantidad())));
            }
        }
        return monto;
    }

    public void setEliminar(Boolean eliminar) {
        this.eliminar = eliminar;
        if (this.eliminar) {
            estadoEliminacion = 1l;
        } else {
            estadoEliminacion = 0l;
        }
    }

    public Boolean getEliminar() {
        return estadoEliminacion.compareTo(1l) == 0;
    }

    public Boolean getTieneReserva() {
        tieneReserva = getResolucionesAdjudicativaList() == null;
        return tieneReserva;
    }

    public void setTieneReserva(Boolean tieneReserva) {
        this.tieneReserva = tieneReserva;
    }

    public List<ResolucionesAdjudicativa> getResolucionesAdjudicativaList() {
        return resolucionesAdjudicativaList;
    }

    public void setResolucionesAdjudicativaList(List<ResolucionesAdjudicativa> resolucionesAdjudicativaList) {
        this.resolucionesAdjudicativaList = resolucionesAdjudicativaList;
    }
}
