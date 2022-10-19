package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author DesarrolloPc
 */
@Table(name = "OFERTA_RESGUARDO")
@Entity
public class OfertaResguardo implements Serializable {

    @Id
    @Column(name = "ID_OFERTA_RESGUARDO", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ofertaResguardo")
    @SequenceGenerator(name = "ofertaResguardo", sequenceName = "SEQ_OFERTA_RESGUARDO", allocationSize = 1, initialValue = 1)
    private Long idOfertaResguardo;
    @Column(name = "ID_OFERTA", nullable = false)
    private Long idOferta;
    @Column(name = "ID_DET_RESGUARDO", nullable = false)
    private Long idDetResguardo;
    @Column(name = "ID_CANTIDAD", nullable = false)
    private Integer cantidad;
    @Column(name = "NO_ITEM", nullable = false)
    private String noItem;
    @Column(name = "ID_PRODUCTO", nullable = false)
    private Long idProducto;
    @Column(name = "ID_NIVEL_EDUCATIVO", nullable = false)
    private Long idNivelEducativo;
    @Column(name = "USUARIO_INSERCION", nullable = false)
    private String usuarioInsercion;
    @Column(name = "FECHA_INSERCION", nullable = false)
    private LocalDateTime fechaInsercion;
    @Column(name = "ESTADO_ELIMINACION", nullable = false)
    private Short estadoEliminacion;
    @Column(name = "RESTADO", nullable = false)
    private Short restado;

    public Long getIdOfertaResguardo() {
        return idOfertaResguardo;
    }

    public void setIdOfertaResguardo(Long idOfertaResguardo) {
        this.idOfertaResguardo = idOfertaResguardo;
    }

    public Long getIdOferta() {
        return idOferta;
    }

    public void setIdOferta(Long idOferta) {
        this.idOferta = idOferta;
    }

    public Long getIdDetResguardo() {
        return idDetResguardo;
    }

    public void setIdDetResguardo(Long idDetResguardo) {
        this.idDetResguardo = idDetResguardo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getNoItem() {
        return noItem;
    }

    public void setNoItem(String noItem) {
        this.noItem = noItem;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Long getIdNivelEducativo() {
        return idNivelEducativo;
    }

    public void setIdNivelEducativo(Long idNivelEducativo) {
        this.idNivelEducativo = idNivelEducativo;
    }

    public String getUsuarioInsercion() {
        return usuarioInsercion;
    }

    public void setUsuarioInsercion(String usuarioInsercion) {
        this.usuarioInsercion = usuarioInsercion;
    }

    public LocalDateTime getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(LocalDateTime fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public Short getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(Short estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }

}
