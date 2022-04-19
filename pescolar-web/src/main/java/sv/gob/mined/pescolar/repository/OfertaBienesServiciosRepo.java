package sv.gob.mined.pescolar.repository;

import sv.gob.mined.pescolar.model.OfertaBienesServicio;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import javax.ejb.Stateless;

@Stateless
public class OfertaBienesServiciosRepo {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    public List<OfertaBienesServicio> findOfertaByCodEntAndIdDetPro(String codigoEntidad, Integer idDetProcesoAdq) {
        Query q = em.createQuery("SELECT o FROM OfertaBienesServicio o WHERE o.codigoEntidad=:codEnt and o.idDetProcesoAdq.id=:idDetPro");
        q.setParameter("codEnt", codigoEntidad);
        q.setParameter("idDetPro", idDetProcesoAdq);

        return q.getResultList();
    }
}
