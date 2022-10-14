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
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;

/**
 *
 * @author Carlos Quintanilla
 */
@Stateless
public class RubrosAmostrarInteresRepo extends AbstractRepository<RubrosAmostrarInteres, Long> {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    public RubrosAmostrarInteresRepo() {
        super(RubrosAmostrarInteres.class);
    }

    public RubrosAmostrarInteres listarrubroporid(String codRubro) {
        RubrosAmostrarInteres entrubro = null;
        if (codRubro != null && !codRubro.isBlank()) {
            Query q;
            q = em.createQuery("SELECT r "
                    + "FROM RubrosAmostrarInteres r "
                    + "WHERE r.id =:pCodRubro ", RubrosAmostrarInteres.class);
            q.setParameter("pCodRubro", Long.valueOf(codRubro));

            if (!q.getResultList().isEmpty()) {
                entrubro = (RubrosAmostrarInteres) q.getSingleResult();
            }
        }
        return entrubro;
    }

    public List<RubrosAmostrarInteres> getLstRubros(){
        Query q = em.createQuery("SELECT r FROM RubrosAmostrarInteres r WHERE r.id in (2,3,4,5) ORDER BY r.id", RubrosAmostrarInteres.class);
        return q.getResultList();
    }
}
