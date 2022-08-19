package sv.gob.mined.pescolar.repository;

import javax.ejb.Stateless;
import sv.gob.mined.pescolar.model.TecnicoProveedor;

/**
 *
 * @author misanchez
 */
@Stateless
public class TecnicoProveedorRepo extends AbstractRepository<TecnicoProveedor, Long> {

    public TecnicoProveedorRepo() {
        super(TecnicoProveedor.class);
    }
}
