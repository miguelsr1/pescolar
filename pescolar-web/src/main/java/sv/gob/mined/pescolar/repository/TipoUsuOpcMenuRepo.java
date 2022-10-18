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
import sv.gob.mined.pescolar.model.TipoUsuOpcMenu;

/**
 *
 * @author Carlos Quintanilla
 */
@Stateless
public class TipoUsuOpcMenuRepo extends AbstractRepository<TipoUsuOpcMenu, Long> {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    public TipoUsuOpcMenuRepo() {
        super(TipoUsuOpcMenu.class);
    }

    public List<TipoUsuOpcMenu> listartipousuopcmenu() {
        Query q = em.createQuery("select tu from TipoUsuOpcMenu tu order by tu.idTipoUsuario", TipoUsuOpcMenu.class);
        return q.getResultList();
    }

    public List<TipoUsuOpcMenu> listartipousuopcmenuportipousuario(Long id) {
        Query q = em.createQuery("select tuom from TipoUsuOpcMenu tuom where tuom.idTipoUsuario.idTipoUsuario = " + id + " ", TipoUsuOpcMenu.class);
        return q.getResultList();
    }

    public List<TipoUsuOpcMenu> listartipousuopcmenuporopcionmenuytipousuario(Long idopcion, Long idtipousuario) {
        Query q = em.createQuery("select tu from TipoUsuOpcMenu tu where tu.idOpcMenu.idOpcMenu = " + idopcion + " and tu.idTipoUsuario.idTipoUsuario = " + idtipousuario + " ", TipoUsuOpcMenu.class);
        return q.getResultList();
    }

    public List<TipoUsuOpcMenu> listartipousuopcmenuporopcionmenuytipousuarioconotroid(Long idopcion, Long idtipousuario, Long id) {
        Query q = em.createQuery("select tu from TipoUsuOpcMenu tu where tu.idOpcMenu.idOpcMenu = " + idopcion + " and tu.idTipoUsuario.idTipoUsuario = " + idtipousuario + " and tu.id <> " + id, TipoUsuOpcMenu.class);
        return q.getResultList();
    }
    
    public List<TipoUsuOpcMenu> listartipousuopcmenuporopcionmenu(Long id) {
        Query q = em.createQuery("select tuom from TipoUsuOpcMenu tuom where tuom.idOpcMenu.idOpcMenu = " + id + " ", TipoUsuOpcMenu.class);
        return q.getResultList();
    }

}
