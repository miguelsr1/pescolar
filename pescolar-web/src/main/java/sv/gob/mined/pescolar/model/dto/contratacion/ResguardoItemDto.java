package sv.gob.mined.pescolar.model.dto.contratacion;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author DesarrolloPc
 */
@Setter
@Getter
@Entity
@XmlRootElement
@SqlResultSetMapping(name = "defaultResguardoItemDto",
        entities = @EntityResult(entityClass = ResguardoItemDto.class))
public class ResguardoItemDto implements Serializable {

    @Id
    private Long idRow;
    private Long idNivelEducativo;
    private Long idProducto;
    private String descripcionNivel;
    private String nombreProducto;
    private Long total;
    private Long cantidad;
    private Long totalComprar;

}
