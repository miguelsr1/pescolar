/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.model.dto.contratacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author misanchez
 */
@Setter
@Getter
@Entity
@SqlResultSetMapping(name = "defaultDetalleItemDto",
        entities = @EntityResult(entityClass = DetalleItemDto.class))
public class DetalleItemDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private BigDecimal idRow;
    @Transient
    private String noItem;
    @Transient
    private String consolidadoEspTec;
    @Transient
    private String razonSocial;
    @Transient
    private String nombreProveedor;
    @Transient
    private BigInteger cantidad;
    @Transient
    private BigDecimal precioUnitario;
    @Transient
    private BigDecimal subTotal;

}
