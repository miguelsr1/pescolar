package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "DET_CAPA_SEGUN_RUBRO")
@Entity
public class DetCapaSegunRubro implements Serializable {
    @Id
    @Column(name = "ID_DETALLE_RUBRO", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_NIVEL_EDUCATIVO")
    private NivelEducativo idNivelEducativo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PRODUCTO")
    private CatalogoProducto idProducto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_MUESTRA_INTERES")
    private DetRubroMuestraIntere idMuestraInteres;

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

    public DetRubroMuestraIntere getIdMuestraInteres() {
        return idMuestraInteres;
    }

    public void setIdMuestraInteres(DetRubroMuestraIntere idMuestraInteres) {
        this.idMuestraInteres = idMuestraInteres;
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