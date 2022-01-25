/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.dhcomitesso.repository;

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
import sv.gob.mined.dhcomitesso.model.dhcsso.view.DataEmpleadoView;
import sv.gob.mined.dhcomitesso.model.dhevaluacion.EstOrganizativa;

/**
 *
 * @author misanchez
 */
@ApplicationScoped
public class EstructuraOrganizativaRepo {

    @PersistenceContext(unitName = "evaluacionPU")
    private EntityManager emEva;
    @PersistenceContext(unitName = "siecssoPU")
    private EntityManager emCsso;

    @Transactional
    public List<EstOrganizativa> findDirecciones() {
        CriteriaBuilder cb = emEva.getCriteriaBuilder();
        CriteriaQuery<EstOrganizativa> cr = cb.createQuery(EstOrganizativa.class);
        Root<EstOrganizativa> root = cr.from(EstOrganizativa.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.notLike(root.get("inuniorg"), "122%"));
        lstCondiciones.add(cb.like(root.get("nombreEstructura"), "DIRECCION%"));

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = emEva.createQuery(cr);

        return query.getResultList();
    }

    @Transactional
    public List<EstOrganizativa> findDependencia(String codigoPadre) {
        CriteriaBuilder cb = emEva.getCriteriaBuilder();
        CriteriaQuery<EstOrganizativa> cr = cb.createQuery(EstOrganizativa.class);
        Root<EstOrganizativa> root = cr.from(EstOrganizativa.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("codigoSup"), codigoPadre));

        cr.select(root).where(lstCondiciones.toArray(new Predicate[]{}));

        Query query = emEva.createQuery(cr);

        return query.getResultList();
    }

    @Transactional
    public List<DataEmpleadoView> findEmpleadosByOrg(String inuniorg) {
        CriteriaBuilder cb = emCsso.getCriteriaBuilder();
        CriteriaQuery<DataEmpleadoView> cr = cb.createQuery(DataEmpleadoView.class);
        Root<DataEmpleadoView> root = cr.from(DataEmpleadoView.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("inuniorg"), inuniorg));

        cr.select(root).where(lstCondiciones.toArray(new Predicate[]{}));

        Query query = emCsso.createQuery(cr);

        return query.getResultList();
    }

}
