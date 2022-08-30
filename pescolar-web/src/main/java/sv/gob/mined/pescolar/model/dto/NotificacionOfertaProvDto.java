/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Transient;

/**
 *
 * @author misanchez
 */
@Entity
@SqlResultSetMapping(name = "defaultNotificacionOfertaProvDto",
        entities = @EntityResult(entityClass = NotificacionOfertaProvDto.class))
public class NotificacionOfertaProvDto implements Serializable {

    @Id
    private Long idEmpresa;
    private String razonSocial;
    private String numeroNit;
    private String descripcionRubro;
    private String programa;
    private String ubicacionPer;
    private String domicilio;
    private String telefonoPer;
    private String direccionCompleta;
    private String telefonoLocal;
    private String nombreDepartamento;
    private Long idAnho;

    @Transient
    private List<DetItemOfertaGlobal> lstDetItemOfertaGlobal = new ArrayList();
    @Transient
    private List<DetMunIntOfertaGlobal> lstMunIntOfertaGlobal = new ArrayList();

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

    public String getNumeroNit() {
        return numeroNit;
    }

    public void setNumeroNit(String numeroNit) {
        this.numeroNit = numeroNit;
    }

    public String getDescripcionRubro() {
        return descripcionRubro;
    }

    public void setDescripcionRubro(String descripcionRubro) {
        this.descripcionRubro = descripcionRubro;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getUbicacionPer() {
        return ubicacionPer;
    }

    public void setUbicacionPer(String ubicacionPer) {
        this.ubicacionPer = ubicacionPer;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefonoPer() {
        return telefonoPer;
    }

    public void setTelefonoPer(String telefonoPer) {
        this.telefonoPer = telefonoPer;
    }

    public String getDireccionCompleta() {
        return direccionCompleta;
    }

    public void setDireccionCompleta(String direccionCompleta) {
        this.direccionCompleta = direccionCompleta;
    }

    public String getTelefonoLocal() {
        return telefonoLocal;
    }

    public void setTelefonoLocal(String telefonoLocal) {
        this.telefonoLocal = telefonoLocal;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public Long getIdAnho() {
        return idAnho;
    }

    public void setIdAnho(Long idAnho) {
        this.idAnho = idAnho;
    }

    public List<DetItemOfertaGlobal> getLstDetItemOfertaGlobal() {
        return lstDetItemOfertaGlobal;
    }

    public void setLstDetItemOfertaGlobal(List<DetItemOfertaGlobal> lstDetItemOfertaGlobal) {
        this.lstDetItemOfertaGlobal = lstDetItemOfertaGlobal;
    }

    public List<DetMunIntOfertaGlobal> getLstMunIntOfertaGlobal() {
        return lstMunIntOfertaGlobal;
    }

    public void setLstMunIntOfertaGlobal(List<DetMunIntOfertaGlobal> lstMunIntOfertaGlobal) {
        this.lstMunIntOfertaGlobal = lstMunIntOfertaGlobal;
    }

}
