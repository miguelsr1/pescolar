package sv.gob.mined.pescolar.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.ejb.Stateless;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.OrganizacionEducativa;
import sv.gob.mined.pescolar.model.view.VwCeClimaFrio;

/**
 *
 * @author cQuintanilla
 */
@Stateless
public class EntidadEducativaClimaFrio extends AbstractRepository<VwCeClimaFrio, String> {

    public EntidadEducativaClimaFrio() {
        super(VwCeClimaFrio.class);
    }

   
    public void guardarCeClimaFrio(OrganizacionEducativa organizacionEducativa) {
        if (organizacionEducativa.getIdOrganizacionEducativa() == null) {
            em.persist(organizacionEducativa);
        } else {
            organizacionEducativa.setFechaModificacion(LocalDateTime.now());
            em.merge(organizacionEducativa);
        }
    }

    
    public VwCeClimaFrio findCeClimaFrioByCodigoEntidad(String codigoEntidad) {
        Query q = em.createNativeQuery("select e.* from vw_ce_clima_frio e inner join ce_clima_frio c on e.codigo_entidad = c.codigo_entidad  where e.codigo_entidad = ?1", VwCeClimaFrio.class);
        q.setParameter(1, codigoEntidad);
        return q.getResultList().isEmpty() ? null : (VwCeClimaFrio) q.getSingleResult();
    }
}
