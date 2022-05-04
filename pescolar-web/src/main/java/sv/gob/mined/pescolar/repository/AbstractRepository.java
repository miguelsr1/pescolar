package sv.gob.mined.pescolar.repository;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import sv.gob.mined.pescolar.utils.db.Filtro;

/**
 *
 * @author misanchez
 * @param <Entity>
 * @param <Primary>
 */
public abstract class AbstractRepository<Entity, Primary> {

    @PersistenceContext
    public EntityManager em;

    private final Class<Entity> entityClass;

    public AbstractRepository(Class<Entity> entityClass) {
        this.entityClass = entityClass;
    }

    public void save(Entity entity) {
        em.persist(entity);
    }

    public void update(Entity entity) {
        em.merge(entity);
    }

    public void delete(Entity entity) {
        em.remove(entity);
    }

    public Entity findByPk(Primary pk) {
        return em.find(entityClass, pk);
    }

    public List<Entity> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Entity> cq = cb.createQuery(entityClass);

        return em.createQuery(cq).getResultList();
    }

    public Entity findEntityByParam(List<Filtro> parametros) {
        List<Predicate> lstCondiciones = new ArrayList();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Entity> cq = cb.createQuery(entityClass);
        Root<Entity> root = cq.from(entityClass);

        for (Filtro parametro : parametros) {
            Path path = root;
            if (parametro.getValor() != null) {
                switch (parametro.getTipoOperacion()) {
                    case EQUALS://EQUALS
                        if (parametro.getClave().split("\\.").length > 1) {
                            for (String clave : parametro.getClave().split("\\.")) {
                                path = path.get(clave);
                            }
                            lstCondiciones.add(cb.equal(path, parametro.getValor()));
                        } else {
                            lstCondiciones.add(cb.equal(path.get(parametro.getClave()), parametro.getValor()));
                        }
                        break;
                    case LIKE://LIKE
                        if (parametro.getClave().split("\\.").length > 1) {
                            for (String clave : parametro.getClave().split("\\.")) {
                                path = path.get(clave);
                            }
                            lstCondiciones.add(cb.like(path, "%" + parametro.getValor() + "%"));
                        } else {
                            lstCondiciones.add(cb.like(path, "%" + parametro.getValor() + "%"));
                        }
                        break;
                    case IN:

                        if (parametro.getClave().split("\\.").length > 1) {
                            for (String clave : parametro.getClave().split("\\.")) {
                                path = path.get(clave);
                            }
                        }
                        

                        /*CriteriaBuilder.In<?> inClause = cb.in(path);

                        for (String idNivel : parametro.getValor().toString().split("\\,")) {
                            inClause.value(parametro.getClazz().cast(idNivel));
                        }*/

                        break;
                }
            }
        }
        cq.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        return em.createQuery(cq).getResultList().isEmpty() ? null : em.createQuery(cq).getResultList().get(0);
    }
    
    public void prueba(){
        //CriteriaBuilder.In<Long> inClause = cb.in(path);
    }
}
