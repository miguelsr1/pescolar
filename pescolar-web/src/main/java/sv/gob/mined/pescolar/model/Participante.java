package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.OrderBy;

@Table(name = "PARTICIPANTES")
@Entity
public class Participante implements Serializable {

    @Id
    @Column(name = "ID_PARTICIPANTE", nullable = false)
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
    private LocalDate fechaInsercion;

    @Column(name = "USUARIO_MODIFICACION", length = 25)
    private String usuarioModificacion;

    @Column(name = "FECHA_MODIFICACION")
    private LocalDate fechaModificacion;

    @Column(name = "FECHA_ELIMINACION")
    private LocalDate fechaEliminacion;

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

    @OneToMany(mappedBy = "idParticipante", fetch = FetchType.LAZY)
    @OrderBy(clause = "noItem asc")
    private List<DetalleOferta> detalleOfertasList;

    @Transient
    private BigInteger cantidad = BigInteger.ZERO;
    @Transient
    private BigDecimal monto = BigDecimal.ZERO;

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

    public LocalDate getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(LocalDate fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
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
        for (DetalleOferta detalleOfertas : detalleOfertasList) {
            if (detalleOfertas.getEstadoEliminacion().compareTo(0l) == 0) {
                cantidad = cantidad.add(detalleOfertas.getCantidad().toBigInteger());
            }
        }
        return cantidad;
    }

    public BigDecimal getMonto() {
        monto = BigDecimal.ZERO;
        for (DetalleOferta detalleOfertas : detalleOfertasList) {
            if (detalleOfertas.getEstadoEliminacion().compareTo(0l) == 0) {
                monto = monto.add(detalleOfertas.getPrecioUnitario().multiply(detalleOfertas.getCantidad()));
            }
        }
        return monto;
    }

}
