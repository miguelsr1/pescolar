package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "CONTRATOS_ORDENES_COMPRAS")
@Entity

@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
            name = "SP_ADD_RESUMEN_CE_PROCESADO",
            procedureName = "SP_ADD_RESUMEN_CE_PROCESADO",
            parameters = {
                @StoredProcedureParameter(name = "P_ID_DET_PROCESO_ADQ", mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(name = "P_CODIGO_ENTIDAD", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "P_CODIGO_DEPARTAMENTO", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "P_CODIGO_MUNICIPIO", mode = ParameterMode.IN, type = String.class)
            }
    ),
    @NamedStoredProcedureQuery(
            name = "SP_ADD_RESUMEN_ADJ_EMP",
            procedureName = "SP_ADD_RESUMEN_ADJ_EMP",
            parameters = {
                @StoredProcedureParameter(name = "P_ID_DET_PROCESO_ADQ", mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(name = "P_ID_EMPRESA", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "P_ID_TIPO_EMPRESA", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "P_CODIGO_DEPARTAMENTO", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "P_CODIGO_ENTIDAD", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "P_SUBTOTAL", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "P_CANTIDAD", mode = ParameterMode.IN, type = String.class)
            }
    )
})
public class ContratosOrdenesCompra implements Serializable {
    @Id
    @Column(name = "ID_CONTRATO", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contrato")
    @SequenceGenerator(name = "contrato", sequenceName = "SEQ_CONTRATO", allocationSize = 1, initialValue = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_RESOLUCION_ADJ")
    private ResolucionesAdjudicativa idResolucionAdj;

    @Column(name = "ID_CONTRATO_PADRE")
    private Long idContratoPadre;

    @Column(name = "ACTIVO")
    private Long activo;

    @Column(name = "NUMERO_CONTRATO", length = 18)
    private String numeroContrato;

    @Column(name = "VALOR", precision = 18, scale = 2)
    private BigDecimal valor;

    @Column(name = "TIENE_ANTICIPO")
    private Long tieneAnticipo;

    @Column(name = "TIENE_RETENCION")
    private Long tieneRetencion;

    @Column(name = "PLAZO_PREVISTO_ENTREGA")
    private Long plazoPrevistoEntrega;

    @Column(name = "CIUDAD_FIRMA", length = 100)
    private String ciudadFirma;

    @Column(name = "FECHA_EMISION")
    private LocalDate fechaEmision;

    @Column(name = "ANHO_CONTRATO", length = 4)
    private String anhoContrato;

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

    @Column(name = "FECHA_ORDEN_INICIO")
    private LocalDateTime fechaOrdenInicio;

    @Column(name = "MIEMBRO_FIRMA", length = 200)
    private String miembroFirma;

    @Column(name = "MODIFICATIVA")
    private Boolean modificativa;

    @Column(name = "PRIMERO")
    private Boolean primero;

    public Boolean getPrimero() {
        return primero;
    }

    public void setPrimero(Boolean primero) {
        this.primero = primero;
    }

    public Boolean getModificativa() {
        return modificativa;
    }

    public void setModificativa(Boolean modificativa) {
        this.modificativa = modificativa;
    }

    public String getMiembroFirma() {
        return miembroFirma;
    }

    public void setMiembroFirma(String miembroFirma) {
        this.miembroFirma = miembroFirma;
    }

    public LocalDateTime getFechaOrdenInicio() {
        return fechaOrdenInicio;
    }

    public void setFechaOrdenInicio(LocalDateTime fechaOrdenInicio) {
        this.fechaOrdenInicio = fechaOrdenInicio;
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

    public String getAnhoContrato() {
        return anhoContrato;
    }

    public void setAnhoContrato(String anhoContrato) {
        this.anhoContrato = anhoContrato;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getCiudadFirma() {
        return ciudadFirma;
    }

    public void setCiudadFirma(String ciudadFirma) {
        this.ciudadFirma = ciudadFirma;
    }

    public Long getPlazoPrevistoEntrega() {
        return plazoPrevistoEntrega;
    }

    public void setPlazoPrevistoEntrega(Long plazoPrevistoEntrega) {
        this.plazoPrevistoEntrega = plazoPrevistoEntrega;
    }

    public Long getTieneRetencion() {
        return tieneRetencion;
    }

    public void setTieneRetencion(Long tieneRetencion) {
        this.tieneRetencion = tieneRetencion;
    }

    public Long getTieneAnticipo() {
        return tieneAnticipo;
    }

    public void setTieneAnticipo(Long tieneAnticipo) {
        this.tieneAnticipo = tieneAnticipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public Long getActivo() {
        return activo;
    }

    public void setActivo(Long activo) {
        this.activo = activo;
    }

    public Long getIdContratoPadre() {
        return idContratoPadre;
    }

    public void setIdContratoPadre(Long idContratoPadre) {
        this.idContratoPadre = idContratoPadre;
    }

    public ResolucionesAdjudicativa getIdResolucionAdj() {
        return idResolucionAdj;
    }

    public void setIdResolucionAdj(ResolucionesAdjudicativa idResolucionAdj) {
        this.idResolucionAdj = idResolucionAdj;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}