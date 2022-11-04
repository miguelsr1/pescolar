/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.model.dto.contratacion;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author misanchez
 */
@Setter
@Getter
@Entity
@SqlResultSetMapping(name = "defaultProveedorDisponibleDto",
        entities = @EntityResult(entityClass = ProveedorDisponibleDto.class))
public class ProveedorDisponibleDto implements Serializable {

    @Id
    private Long idRow;
    private Long idEmpresa;
    private String razonSocial;
    private Long distribuidor;
    private Long capacidadAcreditada;
    private Long capacidadAdjudicada;
    private String nombreMunicipio;
    private String nombreDepartamento;
    private Double puAvg;
    private Double porcentajePrecio = 0d;
    private Double porcentajeGeo = 0d;
    private Double porcentajeCapacidad = 0d;
    private Double porcentajeCapacidadItem = 0d;
    private Double porcentajeEvaluacion;
    private Double porcentajeCalificacion;
    private Double porcentajeAdjudicacion;
    private Double porcentajeNota = 0d;

    public ProveedorDisponibleDto() {
    }
}
