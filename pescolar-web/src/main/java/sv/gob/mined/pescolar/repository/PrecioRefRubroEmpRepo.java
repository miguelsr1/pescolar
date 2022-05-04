/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.gob.mined.pescolar.model.PreciosRefRubroEmp;

/**
 *
 * @author misanchez
 */
@Stateless
public class PrecioRefRubroEmpRepo extends AbstractRepository<PreciosRefRubroEmp, Long> {

    public PrecioRefRubroEmpRepo() {
        super(PreciosRefRubroEmp.class);
    }

    public List<PreciosRefRubroEmp> findPreciosByEmp(Long idEmpresa, Long idRubro, Long idAnho){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PreciosRefRubroEmp> cq = cb.createQuery(PreciosRefRubroEmp.class);
        Root<PreciosRefRubroEmp> root = cq.from(PreciosRefRubroEmp.class);
        
        cq.where(cb.equal(root.get("idMuestraInteres").get("idEmpresa").get("id"), idEmpresa),
                cb.equal(root.get("idMuestraInteres").get("idRubroInteres").get("id"), idRubro),
                cb.equal(root.get("idMuestraInteres").get("idAnho").get("id"), idAnho));
        
        cq.orderBy(cb.asc(root.get("noItem").as(Integer.class)));
        Query query = em.createQuery(cq);
        return query.getResultList();
    }
    
    /*public PreciosRefRubroEmp findPrecioByEmpresaAndProductoAndNivel(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PreciosRefRubroEmp>
    }*/
}
