/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.model.dto.contratacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.pescolar.model.dto.DetalleItemDto;

/**
 *
 * @author DesarrolloPc
 */
@Entity
@XmlRootElement
@SqlResultSetMapping(name = "defaultRptContrato",
        entities = @EntityResult(entityClass = ContratoDto.class))
public class ContratoDto implements Serializable {

    @Id
    private Long idRow;
    private BigDecimal montoContratoModif;
    private String descripcionRubro;
    private String numeroContrato;
    private String codigoEntidad;
    private String nombreCe;
    private String modalidadAdministrativa;
    private String plazoContrato;
    private String nombreMiembro;
    private String razonSocial;
    private String nitEmpresa;
    private String nombreRepresentante;
    private String nitRepresentante;
    private String anhoContrato;
    private String ciudadFirma;
    private String horaRegistro;
    private String minutoRegistro;
    private String usuarioInsercion;
    private String fechaApertura;
    private String justificacion;
    private String nombreDepartamento;
    private String nombreMunicipio;
    private String nombreRepresentanteCe;
    private String numeroDui;

    private BigInteger idResolucionAdj;
    private BigInteger distribuidor;

    private LocalDateTime fechaOrdenInicio;
    private LocalDateTime fechaCreacionModif;
    private LocalDateTime fechaContratoModif;
    private LocalDateTime fechaNota;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaResolucion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    @Transient
    private List<DetalleItemDto> lstDetalleOld = new ArrayList();
    @Transient
    private List<DetalleItemDto> lstDetalleNew = new ArrayList();
    @Transient
    private List<ParticipanteDto> lstParticipantes = new ArrayList();
    @Transient
    private List<ProveedorDisponibleDto> lstPorcentajeEval = new ArrayList();

    public ContratoDto() {
    }

    public Long getIdRow() {
        return idRow;
    }

    public void setIdRow(Long idRow) {
        this.idRow = idRow;
    }

    public String getDescripcionRubro() {
        return descripcionRubro;
    }

    public void setDescripcionRubro(String DescripcionRubro) {
        this.descripcionRubro = DescripcionRubro;
    }

    public LocalDateTime getFechaCreacionModif() {
        return fechaCreacionModif;
    }

    public void setFechaCreacionModif(LocalDateTime fechaCreacionModif) {
        this.fechaCreacionModif = fechaCreacionModif;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public String getNombreCe() {
        return nombreCe;
    }

    public void setNombreCe(String nombreCe) {
        this.nombreCe = nombreCe;
    }

    public String getModalidadAdministrativa() {
        return modalidadAdministrativa;
    }

    public void setModalidadAdministrativa(String modalidadAdministrativa) {
        this.modalidadAdministrativa = modalidadAdministrativa;
    }

    public LocalDateTime getFechaContratoModif() {
        return fechaContratoModif;
    }

    public void setFechaContratoModif(LocalDateTime fechaContratoModif) {
        this.fechaContratoModif = fechaContratoModif;
    }

    public BigDecimal getMontoContratoModif() {
        return montoContratoModif;
    }

    public void setMontoContratoModif(BigDecimal montoContratoModif) {
        this.montoContratoModif = montoContratoModif;
    }

    public String getPlazoContrato() {
        return plazoContrato;
    }

    public void setPlazoContrato(String plazoContrato) {
        this.plazoContrato = plazoContrato;
    }

    public LocalDateTime getFechaOrdenInicio() {
        return fechaOrdenInicio;
    }

    public void setFechaOrdenInicio(LocalDateTime fechaOrdenInicio) {
        this.fechaOrdenInicio = fechaOrdenInicio;
    }

    public String getNombreMiembro() {
        return nombreMiembro;
    }

    public void setNombreMiembro(String nombreMiembro) {
        this.nombreMiembro = nombreMiembro;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNitEmpresa() {
        return nitEmpresa;
    }

    public void setNitEmpresa(String nitEmpresa) {
        this.nitEmpresa = nitEmpresa;
    }

    public String getNombreRepresentante() {
        return nombreRepresentante;
    }

    public void setNombreRepresentante(String nombreRepresentante) {
        this.nombreRepresentante = nombreRepresentante;
    }

    public String getNitRepresentante() {
        return nitRepresentante;
    }

    public void setNitRepresentante(String nitRepresentante) {
        this.nitRepresentante = nitRepresentante;
    }

    public String getAnhoContrato() {
        return anhoContrato;
    }

    public void setAnhoContrato(String anhoContrato) {
        this.anhoContrato = anhoContrato;
    }

    public List<DetalleItemDto> getLstDetalleOld() {
        if (lstDetalleOld == null) {
            lstDetalleOld = new ArrayList();
        }
        return lstDetalleOld;
    }

    public void setLstDetalleOld(List<DetalleItemDto> lstDetalleOld) {
        this.lstDetalleOld = lstDetalleOld;
    }

    public List<DetalleItemDto> getLstDetalleNew() {
        if (lstDetalleNew == null) {
            lstDetalleNew = new ArrayList();
        }
        return lstDetalleNew;
    }

    public void setLstDetalleNew(List<DetalleItemDto> lstDetalleNew) {
        this.lstDetalleNew = lstDetalleNew;
    }

    public List<DetalleItemDto> getLstDetalleItem() {
        if (lstDetalleNew == null) {
            lstDetalleNew = new ArrayList();
        }
        return lstDetalleNew;
    }

    public void setLstDetalleItem(List<DetalleItemDto> lstDetalleNew) {
        this.lstDetalleNew = lstDetalleNew;
    }

    public String getCiudadFirma() {
        return ciudadFirma;
    }

    public void setCiudadFirma(String ciudadFirma) {
        this.ciudadFirma = ciudadFirma;
    }

    public String getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(String horaRegistro) {
        this.horaRegistro = horaRegistro;
    }

    public String getMinutoRegistro() {
        return minutoRegistro;
    }

    public void setMinutoRegistro(String minutoRegistro) {
        this.minutoRegistro = minutoRegistro;
    }

    public LocalDateTime getFechaNota() {
        return fechaNota;
    }

    public void setFechaNota(LocalDateTime fechaNota) {
        this.fechaNota = fechaNota;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getUsuarioInsercion() {
        return usuarioInsercion;
    }

    public void setUsuarioInsercion(String usuarioInsercion) {
        this.usuarioInsercion = usuarioInsercion;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(String fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public List<ParticipanteDto> getLstParticipantes() {
        if (lstParticipantes == null) {
            lstParticipantes = new ArrayList();
        }
        return lstParticipantes;
    }

    public void setLstParticipantes(List<ParticipanteDto> lstParticipantes) {
        this.lstParticipantes = lstParticipantes;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    public String getNombreRepresentanteCe() {
        return nombreRepresentanteCe;
    }

    public void setNombreRepresentanteCe(String nombreRepresentanteCe) {
        this.nombreRepresentanteCe = nombreRepresentanteCe;
    }

    public String getNumeroDui() {
        return numeroDui;
    }

    public void setNumeroDui(String numeroDui) {
        this.numeroDui = numeroDui;
    }

    public BigInteger getIdResolucionAdj() {
        return idResolucionAdj;
    }

    public void setIdResolucionAdj(BigInteger idResolucionAdj) {
        this.idResolucionAdj = idResolucionAdj;
    }

    public BigInteger getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(BigInteger distribuidor) {
        this.distribuidor = distribuidor;
    }

    public List<ProveedorDisponibleDto> getLstPorcentajeEval() {
        return lstPorcentajeEval;
    }

    public void setLstPorcentajeEval(List<ProveedorDisponibleDto> lstPorcentajeEval) {
        this.lstPorcentajeEval = lstPorcentajeEval;
    }
}
