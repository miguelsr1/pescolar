package sv.gob.mined.pescolar.repository;

import javax.ejb.Stateless;
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

    /*@Transactional
    public ResolucionesAdjudicativa findResolucionAdjudicativa(Long idParticipante) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResolucionesAdjudicativa> cr = cb.createQuery(ResolucionesAdjudicativa.class);
        Root<ResolucionesAdjudicativa> root = cr.from(ResolucionesAdjudicativa.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("idParticipante").get("id"), idParticipante));

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = em.createQuery(cr);

        return (ResolucionesAdjudicativa) query.getSingleResult();
    }*/
}
