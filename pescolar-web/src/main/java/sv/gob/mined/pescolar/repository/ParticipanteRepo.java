package sv.gob.mined.pescolar.repository;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import sv.gob.mined.pescolar.model.Participante;

/**
 *
 * @author misanchez
 */
@Stateless
public class ParticipanteRepo extends AbstractRepository<Participante, Long> {

    public ParticipanteRepo() {
        super(Participante.class);
    }

    public List<Participante> findParticipatesByCodEndByIdAnho(String codigoEntidad, Long idAnho) {
        Query q = em.createQuery("SELECT p FROM Participante p WHERE p.idOferta.codigoEntidad.codigoEntidad=:codEnt and p.idOferta.idDetProcesoAdq.idProcesoAdq.idAnho.id=:idAnho");
        q.setParameter("codEnt", codigoEntidad);
        q.setParameter("idAnho", idAnho);

        return q.getResultList();
    }

    public List<Participante> findParticipantesByParam(String codigoDepartamento, Long idMunicipio, String codigoEntidad, String nombreCe, Long idRubro) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Participante> cr = cb.createQuery(Participante.class);
        Root<Participante> root = cr.from(Participante.class);
        List<Predicate> lstCondiciones = new ArrayList();

        if (codigoDepartamento != null) {
            lstCondiciones.add(cb.equal(root.get("idOferta").get("codigoEntidad").get("codigoDepartamento"), codigoDepartamento));
        }
        if (idMunicipio != null) {
            lstCondiciones.add(cb.equal(root.get("idOferta").get("codigoEntidad").get("idMunicipio").get("id"), idMunicipio));
        }
        if (idRubro != null) {
            lstCondiciones.add(cb.equal(root.get("idOferta").get("idDetProcesoAdq").get("idRubroAdq").get("id"), idRubro));
        }
        if (codigoEntidad != null && !codigoEntidad.isEmpty()) {
            lstCondiciones.add(cb.like(root.get("idOferta").get("codigoEntidad").get("codigoEntidad"), "%" + codigoEntidad + "%"));
        }
        if (nombreCe != null && !nombreCe.isEmpty()) {
            lstCondiciones.add(cb.like(root.get("idOferta").get("codigoEntidad").get("nombre"), "%" + nombreCe.toUpperCase() + "%"));
        }

        lstCondiciones.add(cb.equal(root.get("idOferta").get("idDetProcesoAdq").get("idProcesoAdq").get("idAnho").get("id"), 10l));

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = em.createQuery(cr);

        return query.getResultList();
    }
}
