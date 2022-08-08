package sv.gob.mined.pescolar.repository;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import sv.gob.mined.pescolar.model.EstadisticaCenso;
import sv.gob.mined.pescolar.model.NivelEducativo;

/**
 *
 * @author misanchez
 */
@Stateless
public class NivelEducativoRepo extends AbstractRepository<NivelEducativo, Long> {

    public NivelEducativoRepo() {
        super(NivelEducativo.class);
    }

    public Long getCantidadTotalByCodEntAndIdProcesoAdq(String niveles, String codigoEntidad, Long idProcesoAdq) {
        List<Predicate> lstCondiciones = new ArrayList();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<EstadisticaCenso> root = cq.from(EstadisticaCenso.class);

        lstCondiciones.add(cb.equal(root.get("codigoEntidad"), codigoEntidad));
        lstCondiciones.add(cb.equal(root.get("idProcesoAdq").get("id"), idProcesoAdq));

        In<Long> inClause = cb.in(root.get("idNivelEducativo").get("id"));

        for (String idNivel : niveles.split("\\,")) {
            inClause.value(Long.parseLong(idNivel));
        }

        lstCondiciones.add(inClause);

        cq.select(cb.sum(root.get("totalMatricula"))).where(lstCondiciones.toArray(Predicate[]::new));

        return em.createQuery(cq).getSingleResult();
    }
    
    public List<Long> findNivelesByCodigoEntAndProcesoAdq(String codigoEntidad, Long idProcesoAdq){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<EstadisticaCenso> root = cq.from(EstadisticaCenso.class);
        
        Predicate filtroMatMas = cb.greaterThan(root.get("masculino"), 0l);
        Predicate filtroMatFem = cb.greaterThan(root.get("femenimo"), 0l);
        
        Predicate filtroMat = cb.or(filtroMatMas, filtroMatFem);
        
        
        cq.distinct(true).multiselect(root.get("idNivelEducativo").get("id"));
        cq.where(cb.equal(root.get("codigoEntidad"),codigoEntidad), 
                cb.equal(root.get("idProcesoAdq").get("id"), idProcesoAdq),
                cb.and(filtroMat));
        
        Query query = em.createQuery(cq);
        
        return query.getResultList();
    }
}
