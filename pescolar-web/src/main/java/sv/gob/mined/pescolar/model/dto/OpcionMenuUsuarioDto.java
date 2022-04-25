/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.model.dto;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author misanchez
 */
@Entity
@NamedNativeQuery(
        name = "opcionMenuUsuarioDto",
        query = "select opc.id_opc_menu idOpcMenu, opc.nombre_opcion nombreOpcion, opc.nombre_panel nombrePanel, opc.icono, opc.orden, uom.id_usuario idUsuario, opc.opc_id_opc_menu opcIdOpcMenu, opc.app idModulo \n"
        + "from opcion_menu opc\n"
        + "    inner join usuario_opc_menu uom on uom.id_opc_menu = opc.id_opc_menu\n"
        + "where uom.id_usuario = ?1 and\n"
        + "    opc.opc_id_opc_menu = ?2\n"
        + "order by opc.orden",
        resultClass = OpcionMenuUsuarioDto.class)
@Getter
@Setter
public class OpcionMenuUsuarioDto implements Serializable {

    @Id
    private Long idOpcMenu;
    private Long opcIdOpcMenu;
    private String nombreOpcion;
    private String nombrePanel;
    private String icono;
    private String orden;
    private Long idUsuario;
    private Long idModulo;
}
