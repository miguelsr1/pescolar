package sv.gob.mined.pescolar.repository;

import javax.ejb.Stateless;
import sv.gob.mined.pescolar.model.DiasPlazoContrato;

/**
 *
 * @author misanchez
 */
@Stateless
public class DiasPlazoContratoRepo extends AbstractRepository<DiasPlazoContrato, Long> {

    public DiasPlazoContratoRepo() {
        super(DiasPlazoContrato.class);
    }

}
