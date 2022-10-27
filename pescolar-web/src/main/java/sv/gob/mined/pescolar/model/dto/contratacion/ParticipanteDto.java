/*
 * To change this template, choose Tools | Templates
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
@SqlResultSetMapping(name = "defaultParticipanteDto",
        entities = @EntityResult(entityClass = ParticipanteDto.class))
public class ParticipanteDto implements Serializable {

    @Id
    private Long idRow;
    private String razonSocial;
    private int id;

    public ParticipanteDto() {
    }

    public ParticipanteDto(int i, String p) {
    }

    public Long getIdRow() {
        return idRow;
    }

    public void setIdRow(Long idRow) {
        this.idRow = idRow;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public int getId() {
        return idRow.intValue();
    }
}
