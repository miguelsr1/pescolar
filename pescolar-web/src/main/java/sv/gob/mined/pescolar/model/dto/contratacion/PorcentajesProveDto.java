package sv.gob.mined.pescolar.model.dto.contratacion;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author DesarrolloPc
 */
@Setter
@Getter
@Entity
@SqlResultSetMapping(name = "defaultPorcentajesProveDto",
        entities = @EntityResult(entityClass = PorcentajesProveDto.class))
public class PorcentajesProveDto implements Serializable {

    @Id
    private BigDecimal idRow;
    private String razonSocial;
    private BigDecimal porcentajePrecio = BigDecimal.ZERO;
    private BigDecimal porcentajeGeo = BigDecimal.ZERO;
    private BigDecimal porcentajeCapacidad = BigDecimal.ZERO;
    private BigDecimal porcentajeEvaluacion = BigDecimal.ZERO;
    private BigDecimal porcentajeCalificacion = BigDecimal.ZERO;
}
