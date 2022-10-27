/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.model.dto.contratacion;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.pescolar.model.DetalleOferta;

/**
 *
 * @author misanchez
 */
@Entity
@XmlRootElement
@SqlResultSetMapping(name = "defaultVwContizacion",
        entities = @EntityResult(entityClass = VwCotizacion.class))
public class VwCotizacion implements Serializable {

    @Id
    private Long idRow;
    private String modalidadAdministrativa;
    private String razonSocial;
    private String nombreCe;
    private String codigoEntidad;
    private String direccionCe;
    private String fechaApertura;
    private String nombreRepresentanteCe;
    private String nombreRespresenanteEmp;
    private String usuarioInsercion;
    
    @Transient
    private String lugarFecha;
    @Transient
    private List<DetalleOferta> lstDetalleOferta;
    @Transient
    private List<DetalleOferta> lstDetalleOfertaLibros;

    public VwCotizacion() {
    }

    public Long getIdRow() {
        return idRow;
    }

    public void setIdRow(Long idRow) {
        this.idRow = idRow;
    }

    public String getLugarFecha() {
        return lugarFecha;
    }

    public void setLugarFecha(String lugarFecha) {
        this.lugarFecha = lugarFecha;
    }

    public String getModalidadAdministrativa() {
        return modalidadAdministrativa;
    }

    public void setModalidadAdministrativa(String modalidadAdministrativa) {
        this.modalidadAdministrativa = modalidadAdministrativa;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreCe() {
        return nombreCe;
    }

    public void setNombreCe(String nombreCe) {
        this.nombreCe = nombreCe;
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public String getDireccionCe() {
        return direccionCe;
    }

    public void setDireccionCe(String direccionCe) {
        this.direccionCe = direccionCe;
    }

    public String getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(String fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getNombreRepresentanteCe() {
        return nombreRepresentanteCe;
    }

    public void setNombreRepresentanteCe(String nombreRepresentanteCe) {
        this.nombreRepresentanteCe = nombreRepresentanteCe;
    }

    public String getNombreRespresenanteEmp() {
        return nombreRespresenanteEmp;
    }

    public void setNombreRespresenanteEmp(String nombreRespresenanteEmp) {
        this.nombreRespresenanteEmp = nombreRespresenanteEmp;
    }

    public List<DetalleOferta> getLstDetalleOferta() {
        return lstDetalleOferta;
    }

    public void setLstDetalleOferta(List<DetalleOferta> lstDetalleOferta) {
        this.lstDetalleOferta = lstDetalleOferta;
    }

    public String getUsuarioInsercion() {
        return usuarioInsercion;
    }

    public void setUsuarioInsercion(String usuarioInsercion) {
        this.usuarioInsercion = usuarioInsercion;
    }

    public List<DetalleOferta> getLstDetalleOfertaLibros() {
        return lstDetalleOfertaLibros;
    }

    public void setLstDetalleOfertaLibros(List<DetalleOferta> lstDetalleOfertaLibros) {
        this.lstDetalleOfertaLibros = lstDetalleOfertaLibros;
    }
}
