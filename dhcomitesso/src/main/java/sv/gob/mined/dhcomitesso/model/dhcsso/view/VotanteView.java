/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.dhcomitesso.model.dhcsso.view;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author misanchez
 */
@Table(name = "VW_VOTANTES", schema = "SIECSSO")
@Entity
public class VotanteView implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "CODIGO_EMPLEADO")
    private String codigoEmpleado;
    @Column(name = "NOMBRE_EMPLEADO")
    private String nombreEmpleado;
    @Column(name = "DUI")
    private String dui;
    @Column(name = "INUNIORG")
    private String inuniorg;
    @Column(name = "NOMBRE_ESTRUCTURA")
    private String nombreEstructura;
    @Column(name = "NUMERO_TELEFONO")
    private String numeroTelefono;

    public VotanteView() {
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getInuniorg() {
        return inuniorg;
    }

    public void setInuniorg(String inuniorg) {
        this.inuniorg = inuniorg;
    }

    public String getNombreEstructura() {
        return nombreEstructura;
    }

    public void setNombreEstructura(String nombreEstructura) {
        this.nombreEstructura = nombreEstructura;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

}
