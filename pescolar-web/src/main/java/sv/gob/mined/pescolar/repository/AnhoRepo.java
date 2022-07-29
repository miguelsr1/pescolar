package sv.gob.mined.pescolar.repository;

import javax.ejb.Stateless;
import sv.gob.mined.pescolar.model.Anho;

/**
 *
 * @author misanchez
 */
@Stateless
public class AnhoRepo extends AbstractRepository<Anho, Long>{

    public AnhoRepo() {
        super(Anho.class);
    }
    
}
