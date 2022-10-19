package sv.gob.mined.pescolar.repository;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.OfertaResguardo;
import sv.gob.mined.pescolar.model.dto.contratacion.ResguardoDto;

/**
 *
 * @author DesarrolloPc
 */
@Stateless
public class OfertaResguardoRepo extends AbstractRepository<OfertaResguardo, Long> {

    public OfertaResguardoRepo() {
        super(OfertaResguardo.class);
    }

    public List<ResguardoDto> getLstResguardoADisminuir(String codigoEntidad, Long idProcesoAdq, Long idRubroInteres) {
        String queryName;

        switch (idRubroInteres.intValue()) {
            case 2:
                queryName = "Contratacion.resguardoUtiles";
                break;
            case 3:
                queryName = "Contratacion.resguardoZapatos";
                break;
            default:
                queryName = "Contratacion.resguardoUniformes";
                break;
        }

        Query q = em.createNamedQuery(queryName, ResguardoDto.class);
        q.setParameter(1, codigoEntidad);
        q.setParameter(2, idProcesoAdq);
        return q.getResultList();
    }
}
