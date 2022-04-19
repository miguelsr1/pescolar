/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.pescolar.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.gob.mined.pescolar.utils.Filtro;

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
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Entity> cq = cb.createQuery(entityClass);
        Root<Entity> root = cq.from(entityClass);
        for (Filtro parametro : parametros) {
            switch (parametro.getTipoOperacion()) {
                case 1://EQUALS
                    cq.select(root).where(cb.equal(root.get(parametro.getClave()), parametro.getValor()));
                    break;
                case 2://LIKE
                    cq.select(root).where(cb.like(root.get(parametro.getClave()), "%" + parametro.getValor() + "%"));
                    break;
            }
        }

        return em.createQuery(cq).getResultList().isEmpty() ? null : em.createQuery(cq).getResultList().get(0);
    }
}
