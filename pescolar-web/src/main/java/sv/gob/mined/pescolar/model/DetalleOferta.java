package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.Where;

@Table(name = "DETALLE_OFERTAS")
@Entity
@Where(clause = "ESTADO_ELIMINACION = 0")
public class DetalleOferta implements Serializable {

    @Id
    @Column(name = "ID_DETALLE_OFE", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalle_oferta")
    @SequenceGenerator(name = "detalle_oferta", sequenceName = "SEQ_DET_OFERTA", allocationSize = 1, initialValue = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_NIVEL_EDUCATIVO")
    private NivelEducativo idNivelEducativo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PRODUCTO")
    private CatalogoProducto idProducto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PARTICIPANTE")
    private Participante idParticipante;

    @Column(name = "CANTIDAD", nullable = false, precision = 18, scale = 0)
    private BigInteger cantidad;

    @Column(name = "PRECIO_UNITARIO", nullable = false, precision = 18, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "CONSOLIDADO_ESP_TEC", nullable = false, length = 200)
    private String consolidadoEspTec;

    @Column(name = "NO_ITEM", nullable = false, length = 6)
    private String noItem;

    @Column(name = "USUARIO_INSERCION", nullable = false, length = 25)
    private String usuarioInsercion;

    @Column(name = "FECHA_INSERCION", nullable = false)
    private LocalDateTime fechaInsercion;

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

    @Column(name = "CANTIDAD_RESGUARDO", nullable = false, precision = 18, scale = 2)
    private BigInteger cantidadResguardo;

    @Column(name = "ESTILO_ZAPATO", length = 20)
    private String estiloZapato;

    public String getEstiloZapato() {
        return estiloZapato;
    }

    public void setEstiloZapato(String estiloZapato) {
        this.estiloZapato = estiloZapato;
    }

    public BigInteger getCantidadResguardo() {
        return cantidadResguardo;
    }

    public void setCantidadResguardo(BigInteger cantidadResguardo) {
        this.cantidadResguardo = cantidadResguardo;
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

    public String getNoItem() {
        return noItem;
    }

    public void setNoItem(String noItem) {
        this.noItem = noItem;
    }

    public String getConsolidadoEspTec() {
        return consolidadoEspTec;
    }

    public void setConsolidadoEspTec(String consolidadoEspTec) {
        this.consolidadoEspTec = consolidadoEspTec;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigInteger getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigInteger cantidad) {
        this.cantidad = cantidad;
    }

    public Participante getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(Participante idParticipante) {
        this.idParticipante = idParticipante;
    }

    public CatalogoProducto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(CatalogoProducto idProducto) {
        this.idProducto = idProducto;
    }

    public NivelEducativo getIdNivelEducativo() {
        return idNivelEducativo;
    }

    public void setIdNivelEducativo(NivelEducativo idNivelEducativo) {
        this.idNivelEducativo = idNivelEducativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
