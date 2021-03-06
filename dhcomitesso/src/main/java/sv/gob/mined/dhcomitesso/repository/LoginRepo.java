/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.dhcomitesso.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;
import sv.gob.mined.dhcomitesso.model.dhcsso.Empleado;

/**
 *
 * @author misanchez
 */
@ApplicationScoped
public class LoginRepo implements Serializable {

    @Inject
    private Pbkdf2PasswordHash passwordHash;

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

    @Transactional
    public Empleado findEmpleadoByCodigo(String codigoEmpleado) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Empleado> cr = cb.createQuery(Empleado.class);
        Root<Empleado> root = cr.from(Empleado.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("codigo"), codigoEmpleado));

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = em.createQuery(cr);
        return query.getResultList().isEmpty() ? null : (Empleado) query.getResultList().get(0);
    }

    public Empleado findEmpleadoByCodigo(Integer idEmpleado, String codigoEmpleado) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Empleado> cr = cb.createQuery(Empleado.class);
        Root<Empleado> root = cr.from(Empleado.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("codigo"), codigoEmpleado));
        lstCondiciones.add(cb.equal(root.get("id"), idEmpleado));

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = em.createQuery(cr);
        return query.getResultList().isEmpty() ? null : (Empleado) query.getResultList().get(0);
    }

    public Empleado findEmpleadoByPk(Integer id) {
        return em.find(Empleado.class, id);
    }

    @Transactional
    public void guardar(Empleado emp) {
        em.merge(emp);
    }

    @Transactional
    public void guardar(String usuario, String password) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(parameters);

        Query q = em.createNativeQuery("Insert into SIECSSO.USERS (USERNAME,USER_PASSWORD) values ('"+usuario+"','" + passwordHash.generate(password.toCharArray()) + "')");
        q.executeUpdate();

        q = em.createNativeQuery("Insert into SIECSSO.GRUPO (ID_GRUPO,USER_GROUP,USERNAME) values (1,'" + usuario + "','" + passwordHash.generate(password.toCharArray()) + "')");
        q.executeUpdate();

    }

}
