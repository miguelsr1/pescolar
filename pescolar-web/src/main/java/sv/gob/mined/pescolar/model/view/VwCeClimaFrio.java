/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.model.view;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author RAREcheverria
 */
@Entity
@Table(name = "VW_CE_CLIMA_FRIO")
@XmlRootElement
public class VwCeClimaFrio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Id
    @Size(min = 1, max = 5)
    @Column(name = "CODIGO_ENTIDAD")
    private String codigoEntidad;
    @Size(max = 250)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 250)
    @Column(name = "DIRECCION")
    private String direccion;
    @Size(max = 10)
    @Column(name = "TELEFONO1")
    private String telefono1;
    @Size(max = 10)
    @Column(name = "TELEFONO2")
    private String telefono2;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 10)
    @Column(name = "FAX")
    private String fax;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "CODIGO_DEPARTAMENTO")
    private String codigoDepartamento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "CODIGO_MUNICIPIO")
    private String codigoMunicipio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_MUNICIPIO")
    private BigInteger idMunicipio;
    @Size(max = 5)
    @Column(name = "ENTIDAD_FRIO")
    private Integer entidadFrio;
    @Column(name = "ESTADO_ELIMINACION")
    private Integer estadoEliminacion;

    public VwCeClimaFrio() {
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(String codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public String getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(String codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public BigInteger getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(BigInteger idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public Integer getEntidadFrio() {
        return entidadFrio;
    }

    public void setEntidadFrio(Integer entidadFrio) {
        this.entidadFrio = entidadFrio;
    }

    public Integer getEstadoEliminacion() {
        return estadoEliminacion;
    }

    public void setEstadoEliminacion(Integer estadoEliminacion) {
        this.estadoEliminacion = estadoEliminacion;
    }
    
}
