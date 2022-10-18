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
import sv.gob.mined.pescolar.model.TipoUsuario;

/**
 *
 * @author Carlos Quintanilla
 */
@Stateless
public class TipoUsuarioRepo extends AbstractRepository<TipoUsuario, Long> {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    public TipoUsuarioRepo() {
        super(TipoUsuario.class);
    }

    public List<TipoUsuario> listartipousuario() {
        Query q = em.createQuery("select tu from TipoUsuario tu", TipoUsuario.class);
        return q.getResultList();
    }
    
    public List<TipoUsuario> listartipousuariopordescripcion(String strdescripcion) {
        Query q = em.createQuery("select tu from TipoUsuario tu where tu.descripcion = '" + strdescripcion + "' ", TipoUsuario.class);
        return q.getResultList();
    }
    
    public List<TipoUsuario> listartipousuariopordescripcionconotroid(String strdescripcion,Long id) {
        Query q = em.createQuery("select tu from TipoUsuario tu where tu.descripcion = '" + strdescripcion + "' and tu.idTipoUsuario <> " + id, TipoUsuario.class);
        return q.getResultList();
    }
    
    public List<TipoUsuario> listartipousuarioporrol(String strrol) {
        Query q = em.createQuery("select tu from TipoUsuario tu where tu.rol = '" + strrol + "' ", TipoUsuario.class);
        return q.getResultList();
    }
    
    public List<TipoUsuario> listartipousuarioporrolconotroid(String strrol,Long id) {
        Query q = em.createQuery("select tu from TipoUsuario tu where tu.rol = '" + strrol + "' and tu.idTipoUsuario <> " + id, TipoUsuario.class);
        return q.getResultList();
    }

}
