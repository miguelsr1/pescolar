/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.model.dto.contratacion;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author misanchez
 */
@Entity
@XmlRootElement
@SqlResultSetMapping(name = "defaultProveedorDisponibleDto",
        entities = @EntityResult(entityClass = ProveedorDisponibleDto.class))
public class ProveedorDisponibleDto implements Serializable {

    @Id
    private Long idRow;
    private Long idEmpresa;
    private String razonSocial;
    private Long distribuidor;
    private Long capacidadAcreditada;
    private Long capacidadAdjudicada;
    private String nombreMunicipio;
    private String nombreDepartamento;
    private Double puAvg;
    private Double porcentajePrecio = 0d;
    private Double porcentajeGeo = 0d;
    private Double porcentajeCapacidad = 0d;
    private Double porcentajeCapacidadItem = 0d;
    private Double porcentajeEvaluacion;
    private Double porcentajeCalificacion;
    private Double porcentajeAdjudicacion;
    private Double porcentajeNota = 0d;

    public ProveedorDisponibleDto() {
    }

    public Double getPorcentajeCapacidadItem() {
        return porcentajeCapacidadItem;
    }

    public void setPorcentajeCapacidadItem(Double porcentajeCapacidadItem) {
        this.porcentajeCapacidadItem = porcentajeCapacidadItem;
    }

    public Double getPorcentajeCapacidad() {
        return porcentajeCapacidad;
    }

    public void setPorcentajeCapacidad(Double porcentajeCapacidad) {
        this.porcentajeCapacidad = porcentajeCapacidad;
    }

    public Double getPorcentajeGeo() {
        return porcentajeGeo;
    }

    public void setPorcentajeGeo(Double porcentajeGeo) {
        this.porcentajeGeo = porcentajeGeo;
    }

    public Long getIdRow() {
        return idRow;
    }

    public void setIdRow(Long idRow) {
        this.idRow = idRow;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Long getDistribuidor() {
        return distribuidor;
    }

    public void setDistribuidor(Long distribuidor) {
        this.distribuidor = distribuidor;
    }

    public Long getCapacidadAcreditada() {
        return capacidadAcreditada;
    }

    public void setCapacidadAcreditada(Long capacidadAcreditada) {
        this.capacidadAcreditada = capacidadAcreditada;
    }

    public Long getCapacidadAdjudicada() {
        return capacidadAdjudicada;
    }

    public void setCapacidadAdjudicada(Long capacidadAdjudicada) {
        this.capacidadAdjudicada = capacidadAdjudicada;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public Double getPuAvg() {
        return puAvg;
    }

    public void setPuAvg(Double puAvg) {
        this.puAvg = puAvg;
    }

    public Double getPorcentajePrecio() {
        return porcentajePrecio;
    }

    public void setPorcentajePrecio(Double porcentajePrecio) {
        this.porcentajePrecio = porcentajePrecio;
    }

    public Double getPorcentajeEvaluacion() {
        return porcentajeEvaluacion;
    }

    public void setPorcentajeEvaluacion(Double porcentajeEvaluacion) {
        this.porcentajeEvaluacion = porcentajeEvaluacion;
    }

    public Double getPorcentajeAdjudicacion() {
        return porcentajeAdjudicacion;
    }

    public void setPorcentajeAdjudicacion(Double porcentajeAdjudicacion) {
        this.porcentajeAdjudicacion = porcentajeAdjudicacion;
    }

    public Double getPorcentajeNota() {
        return porcentajeNota;
    }

    public void setPorcentajeNota(Double porcentajeNota) {
        this.porcentajeNota = porcentajeNota;
    }

    public Double getPorcentajeCalificacion() {
        return porcentajeCalificacion;
    }

    public void setPorcentajeCalificacion(Double porcentajeCalificacion) {
        this.porcentajeCalificacion = porcentajeCalificacion;
    }

}
