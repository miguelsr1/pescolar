/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.dhcomitesso.model.dhcsso;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DesarrolloPc
 */
@Entity
@Table(name = "NOMINA_VOTACION", schema = "SIECSSO")
@NamedQueries({
    @NamedQuery(name = "NominaVotacion.findAll", query = "SELECT n FROM NominaVotacion n")})
public class NominaVotacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Size(max = 68)
    @Column(name = "UNIDAD_ADMINISTRATIVA")
    private String unidadAdministrativa;
    @Size(max = 11)
    @Column(name = "COD_ADVO")
    private String codAdvo;
    @Size(max = 48)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 2)
    @Column(name = "SEXO")
    private String sexo;
    @Size(max = 30)
    @Column(name = "CARGO_NOMINAL")
    private String cargoNominal;
    @Size(max = 14)
    @Column(name = "DUI")
    private String dui;
    @Size(max = 29)
    @Column(name = "EDIFICIO_NIVEL")
    private String edificioNivel;

    public NominaVotacion() {
    }

    public NominaVotacion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(String unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public String getCodAdvo() {
        return codAdvo;
    }

    public void setCodAdvo(String codAdvo) {
        this.codAdvo = codAdvo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCargoNominal() {
        return cargoNominal;
    }

    public void setCargoNominal(String cargoNominal) {
        this.cargoNominal = cargoNominal;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getEdificioNivel() {
        return edificioNivel;
    }

    public void setEdificioNivel(String edificioNivel) {
        this.edificioNivel = edificioNivel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NominaVotacion)) {
            return false;
        }
        NominaVotacion other = (NominaVotacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.dhcomitesso.model.dhcsso.NominaVotacion[ id=" + id + " ]";
    }
    
}
