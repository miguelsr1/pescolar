package sv.gob.mined.pescolar.model.dto.contratacion;

import java.io.Serializable;
import java.math.BigDecimal;
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
@SqlResultSetMapping(name = "defaultPrecioReferenciaEmpresaDto",
        entities = @EntityResult(entityClass = PrecioReferenciaEmpresaDto.class))
public class PrecioReferenciaEmpresaDto implements Serializable {
    @Id
    private Long idRow;
    private String noItem;
    private String nombreProducto;
    private String descripcionNivel;
    private BigDecimal precioMaximo;
    private BigDecimal precioReferencia;

    public PrecioReferenciaEmpresaDto() {
    }

    public Long getIdRow() {
        return idRow;
    }

    public void setIdRow(Long idRow) {
        this.idRow = idRow;
    }

    public String getNoItem() {
        return noItem;
    }

    public void setNoItem(String noItem) {
        this.noItem = noItem;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionNivel() {
        return descripcionNivel;
    }

    public void setDescripcionNivel(String descripcionNivel) {
        this.descripcionNivel = descripcionNivel;
    }

    public BigDecimal getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(BigDecimal precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public BigDecimal getPrecioReferencia() {
        return precioReferencia;
    }

    public void setPrecioReferencia(BigDecimal precioReferencia) {
        this.precioReferencia = precioReferencia;
    }
}
