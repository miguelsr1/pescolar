package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import org.hibernate.annotations.Where;

@Table(name = "DET_RUBRO_MUESTRA_INTERES")
@Entity
@Where(clause = "ESTADO_ELIMINACION = 0")
public class DetRubroMuestraIntere implements Serializable {

    @OneToMany(mappedBy = "idMuestraInteres", fetch = FetchType.LAZY)
    private List<PreciosRefRubroEmp> preciosRefRubroEmpList;

    @Id
    @Column(name = "ID_MUESTRA_INTERES", nullable = false)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_DET_PROCESO_ADQ")
    private DetalleProcesoAdq idDetProcesoAdq;

    @Column(name = "DATOS_VERIFICADOS")
    private Boolean datosVerificados;

    @Column(name = "ID_GESTION", length = 20)
    private String idGestion;

    @Column(name = "ACEPTACION_TERMINOS")
    private Boolean aceptacionTerminos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_RUBRO_INTERES")
    private RubrosAmostrarInteres idRubroInteres;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ANHO")
    private Anho idAnho;

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

    public Boolean getAceptacionTerminos() {
        return aceptacionTerminos;
    }

    public void setAceptacionTerminos(Boolean aceptacionTerminos) {
        this.aceptacionTerminos = aceptacionTerminos;
    }

    public String getIdGestion() {
        return idGestion;
    }

    public void setIdGestion(String idGestion) {
        this.idGestion = idGestion;
    }

    public Boolean getDatosVerificados() {
        return datosVerificados;
    }

    public void setDatosVerificados(Boolean datosVerificados) {
        this.datosVerificados = datosVerificados;
    }

    public DetalleProcesoAdq getIdDetProcesoAdq() {
        return idDetProcesoAdq;
    }

    public void setIdDetProcesoAdq(DetalleProcesoAdq idDetProcesoAdq) {
        this.idDetProcesoAdq = idDetProcesoAdq;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PreciosRefRubroEmp> getPreciosRefRubroEmpList() {
        return preciosRefRubroEmpList;
    }

    public void setPreciosRefRubroEmpList(List<PreciosRefRubroEmp> preciosRefRubroEmpList) {
        this.preciosRefRubroEmpList = preciosRefRubroEmpList;
    }
}
