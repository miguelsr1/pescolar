package sv.gob.mined.pescolar.model.dto.contratacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author DesarrolloPc
 */
@Setter
@Getter
@Entity
@SqlResultSetMapping(name = "defaultActaRecomendacionDto",
        entities = @EntityResult(entityClass = ActaRecomendacionDto.class))
public class ActaRecomendacionDto implements Serializable {

    @Id
    private BigDecimal idRow;
    private String nombreCe;
    private String codigoEntidad;
    private String modalidadAdministrativa;
    private String fechaApertura;
    private String horaRegistro;
    private String minutoRegistro;
    private String descripcionRubro;
    private String justificacion;
    private String usuarioInsercion;
    
    @Transient
    private List<ParticipanteDto> lstParticipantes = new ArrayList();
    @Transient
    private List<ProveedorDisponibleDto> lstPorcentajeEval = new ArrayList();
    @Transient
    private List<DetalleItemDto> lstDetalleNew = new ArrayList();
    
    public List<DetalleItemDto> getLstDetalleItem() {
        if (lstDetalleNew == null) {
            lstDetalleNew = new ArrayList();
        }
        return lstDetalleNew;
    }

    public void setLstDetalleItem(List<DetalleItemDto> lstDetalleNew) {
        this.lstDetalleNew = lstDetalleNew;
    }
}
