package sv.gob.mined.pescolar.repository;

import javax.ejb.Stateless;
import sv.gob.mined.pescolar.model.DetalleOferta;

/**
 *
 * @author misanchez
 */
@Stateless
public class DetalleOfertaRepo extends AbstractRepository<DetalleOferta, Long> {

    public DetalleOfertaRepo() {
        super(DetalleOferta.class);
    }
}
