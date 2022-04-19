package sv.gob.mined.pescolar.repository;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import sv.gob.mined.pescolar.model.ResolucionesAdjudicativa;

/**
 *
 * @author misanchez
 */
@Stateless
public class ResolucionesAdjudicativasRepo extends AbstractRepository<ResolucionesAdjudicativa, Long> {

    public ResolucionesAdjudicativasRepo() {
        super(ResolucionesAdjudicativa.class);
    }

    @Transactional
    public ResolucionesAdjudicativa findResolucionAdjudicativa(Long idParticipante) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResolucionesAdjudicativa> cr = cb.createQuery(ResolucionesAdjudicativa.class);
        Root<ResolucionesAdjudicativa> root = cr.from(ResolucionesAdjudicativa.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("idParticipante").get("id"), idParticipante));

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = em.createQuery(cr);

        return (ResolucionesAdjudicativa) query.getSingleResult();
    }
}
