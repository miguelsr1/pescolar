package sv.gob.mined.pescolar.repository;

import java.time.LocalDate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.CapaDistribucionAcre;
import sv.gob.mined.pescolar.model.DisMunicipioIntere;

/**
 *
 * @author DesarrolloPc
 */
@Stateless
public class DisMunicipioInteresRepo extends AbstractRepository<DisMunicipioIntere, Long> {

    public DisMunicipioInteresRepo() {
        super(DisMunicipioIntere.class);
    }

    public List<DisMunicipioIntere> findMunicipiosInteres(CapaDistribucionAcre depa) {
        Query query = em.createQuery("SELECT d FROM DisMunicipioIntere d WHERE d.idCapaDistribucion =:capa ORDER BY d.idMunicipio.id, d.idMunicipio.codigoDepartamento.id", DisMunicipioIntere.class);
        query.setParameter("capa", depa);

        return query.getResultList();
    }

    public void remove(Long idMunicipio, Long idCapaDistribucion) {
        Query q = em.createQuery("SELECT d FROM DisMunicipioIntere d WHERE d.idMunicipio.id=:pIdMunicipio and d.idCapaDistribucion.id=:pIdCapaDis", DisMunicipioIntere.class);
        q.setParameter("pIdMunicipio", idMunicipio);
        q.setParameter("pIdCapaDis", idCapaDistribucion);
        if (!q.getResultList().isEmpty()) {
            DisMunicipioIntere dis = (DisMunicipioIntere) q.getResultList().get(0);
            dis.setEstadoEliminacion(1l);
            dis.setFechaEliminacion(LocalDate.now());

            em.merge(dis);
        }
    }
}
