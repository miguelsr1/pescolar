package sv.gob.mined.pescolar.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import sv.gob.mined.pescolar.model.Participante;
import sv.gob.mined.pescolar.model.ResolucionesAdjudicativa;

/**
 *
 * @author misanchez
 */
@ApplicationScoped
public class DetalleOfertaRepo {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    /*@Transactional
    public Participante findParticipanteByPk(Long id) {
        Query q = em.createQuery("SELECT p FROM Participante p WHERE p.id=:id", Participante.class);
        q.setParameter("id", id);
        return (Participante) q.getResultList().get(0);
    }*/
    @Transactional
    public ResolucionesAdjudicativa findResolucionAdjudicativa(Long idParticipante) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResolucionesAdjudicativa> cr = cb.createQuery(ResolucionesAdjudicativa.class);
        Root<ResolucionesAdjudicativa> root = cr.from(ResolucionesAdjudicativa.class);
        
        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("idParticipante").get("id"), idParticipante));
        
        cr.select(root).where(lstCondiciones.toArray(new Predicate[]{}));
        
        Query query = em.createQuery(cr);

        return (ResolucionesAdjudicativa) query.getResultList().get(0);
    }

    @Transactional
    public List<?> findListByParam(Class<?> arg, Map<String, Object> params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cb.createQuery(arg);
        Root root = cr.from(arg);
        List<Predicate> lstCondiciones = new ArrayList();

        for (String var : params.keySet()) {
            lstCondiciones.add(cb.equal(root.get(var), params.get(var)));
        }

        cr.select(root).where(lstCondiciones.toArray(new Predicate[]{}));

        Query query = em.createQuery(cr);

        return query.getResultList();
    }
}
