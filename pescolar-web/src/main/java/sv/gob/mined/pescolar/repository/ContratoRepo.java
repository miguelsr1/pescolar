package sv.gob.mined.pescolar.repository;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import sv.gob.mined.pescolar.model.ContratosOrdenesCompra;

/**
 *
 * @author misanchez
 */
@Stateless
public class ContratoRepo extends AbstractRepository<ContratosOrdenesCompra, Long> {

    public ContratoRepo() {
        super(ContratosOrdenesCompra.class);
    }

    public ContratosOrdenesCompra findContratoByIdResAdj(Long idResAdj) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContratosOrdenesCompra> cr = cb.createQuery(ContratosOrdenesCompra.class);
        Root<ContratosOrdenesCompra> root = cr.from(ContratosOrdenesCompra.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("idResolucionAdj").get("id"), idResAdj));

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = em.createQuery(cr);

        return (ContratosOrdenesCompra) query.getSingleResult();
    }
}
