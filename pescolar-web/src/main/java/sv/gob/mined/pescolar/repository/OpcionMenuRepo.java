/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.Modulo;
import sv.gob.mined.pescolar.model.OpcionMenu;

/**
 *
 * @author Carlos Quintanilla
 */
@Stateless
public class OpcionMenuRepo extends AbstractRepository<OpcionMenu, Long> {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    public OpcionMenuRepo() {
        super(OpcionMenu.class);
    }

    public List<OpcionMenu> listaropcionmenu() {
        Query q = em.createQuery("select om from OpcionMenu om order by om.app asc, om.opcIdOpcMenu asc, om.orden asc", OpcionMenu.class);
        return q.getResultList();
    }

    public List<OpcionMenu> listaropcionmenuexcluyente(Long id) {
        Query q = em.createQuery("select opc from OpcionMenu opc "
                + "where opc.idOpcMenu not in ("
                + "   select opc2.idOpcMenu.idOpcMenu "
                + "   from TipoUsuOpcMenu opc2 "
                + "   where opc2.idTipoUsuario.idTipoUsuario = " + id + " "
                + ") order by opc.orden ", OpcionMenu.class);
        return q.getResultList();
    }

    public List<OpcionMenu> listaropcionmenuhijosexcluyente(Long idopcion, Long idtipousuario) {
        Query q = em.createQuery("select opc from OpcionMenu opc "
                + "where opc.opcIdOpcMenu.idOpcMenu = " + idopcion + " "
                + "and opc.idOpcMenu not in ("
                + "   select opc2.idOpcMenu.idOpcMenu "
                + "   from TipoUsuOpcMenu opc2 "
                + "   where opc2.idTipoUsuario.idTipoUsuario = " + idtipousuario + " "
                + ") order by opc.orden ", OpcionMenu.class);
        return q.getResultList();
    }

    public List<OpcionMenu> listaropcionmenuporpadreynombreopcion(Long idpadre, String strnombreopcion) {
        Query q = em.createQuery("select op from OpcionMenu op where op.opcIdOpcMenu.idOpcMenu = " + idpadre + " and op.nombreOpcion = '" + strnombreopcion + "'", OpcionMenu.class);
        return q.getResultList();
    }

    public List<OpcionMenu> listaropcionmenuporpadreynombreopcionconotroid(Long idpadre, String strnombreopcion, Long id) {
        Query q = em.createQuery("select op from OpcionMenu op where op.opcIdOpcMenu.idOpcMenu = " + idpadre + " and op.nombreOpcion = '" + strnombreopcion + "' and op.idOpcMenu <> " + id, OpcionMenu.class);
        return q.getResultList();
    }
    
    public List<OpcionMenu> listaropcionmenuhijos(Long id) {
        Query q = em.createQuery("select op from OpcionMenu op where op.opcIdOpcMenu.idOpcMenu = " + id + " ", OpcionMenu.class);
        return q.getResultList();
    }

    public List<Modulo> listarmodulos() {
        Query q = em.createQuery("select mod from Modulo mod", Modulo.class);
        return q.getResultList();
    }

}
