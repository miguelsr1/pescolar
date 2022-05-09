package sv.gob.mined.pescolar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author misanchez
 */
@Entity
@Table(name = "MUNICIPIO_ALEDANHO")
public class MunicipioAledanho implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODIGO")
    private String codigo;
    @Column(name = "CODIGOS_MUNICIPIOS")
    private String codigosMunicipios;
    @Column(name = "ID_MUNICIPIO")
    private Long idMunicipio;
    @Column(name = "ID_MUNICIPIOS")
    private String idMunicipios;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigosMunicipios() {
        return codigosMunicipios;
    }

    public void setCodigosMunicipios(String codigosMunicipios) {
        this.codigosMunicipios = codigosMunicipios;
    }

    public Long getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Long idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getIdMunicipios() {
        return idMunicipios;
    }

    public void setIdMunicipios(String idMunicipios) {
        this.idMunicipios = idMunicipios;
    }

}
