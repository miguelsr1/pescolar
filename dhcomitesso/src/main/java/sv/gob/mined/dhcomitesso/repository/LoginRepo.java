/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.dhcomitesso.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import sv.gob.mined.dhcomitesso.model.dhcsso.Empleado;

/**
 *
 * @author misanchez
 */
@ApplicationScoped
public class LoginRepo implements Serializable {

    @PersistenceContext(unitName = "siecssoPU")
    private EntityManager em;

    @Transactional
    public Empleado findEmpleadoByCodigo(String codigoEmpleado, String dui) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Empleado> cr = cb.createQuery(Empleado.class);
        Root<Empleado> root = cr.from(Empleado.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("codigo"), codigoEmpleado));
        lstCondiciones.add(cb.equal(root.get("dui"), dui));

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = em.createQuery(cr);
        return query.getResultList().isEmpty() ? null : (Empleado) query.getResultList().get(0);
    }
}
