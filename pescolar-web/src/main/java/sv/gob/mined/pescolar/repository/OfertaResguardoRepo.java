package sv.gob.mined.pescolar.repository;

import javax.ejb.Stateless;
import sv.gob.mined.pescolar.model.OfertaResguardo;

/**
 *
 * @author DesarrolloPc
 */
@Stateless
public class OfertaResguardoRepo extends AbstractRepository<OfertaResguardo, Long> {

    public OfertaResguardoRepo() {
        super(OfertaResguardo.class);
    }

}
