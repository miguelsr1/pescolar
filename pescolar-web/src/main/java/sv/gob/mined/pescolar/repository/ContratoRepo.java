/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.repository;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import sv.gob.mined.pescolar.model.ContratosOrdenesCompra;

/**
 *
 * @author misanchez
 */
@ApplicationScoped
public class ContratoRepo {
    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;
    
    public ContratosOrdenesCompra findContratoByIdResAdj(Long idResAdj){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContratosOrdenesCompra> cr = cb.createQuery(ContratosOrdenesCompra.class);
        Root<ContratosOrdenesCompra> root = cr.from(ContratosOrdenesCompra.class);
        
        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("idResolucionAdj").get("id"), idResAdj));
        
        cr.select(root).where(lstCondiciones.toArray(new Predicate[]{}));
        
        Query query = em.createQuery(cr);

        return (ContratosOrdenesCompra) query.getSingleResult();
    }
}
