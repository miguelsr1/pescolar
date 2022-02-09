/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sv.gob.mined.dhcomitesso.repository;

import java.time.LocalDate;
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
import sv.gob.mined.dhcomitesso.model.dhcsso.Candidato;
import sv.gob.mined.dhcomitesso.model.dhcsso.DetalleProceso;
import sv.gob.mined.dhcomitesso.model.dhcsso.Empleado;
import sv.gob.mined.dhcomitesso.model.dhcsso.ProcesoEleccion;
import sv.gob.mined.dhcomitesso.model.dhcsso.Votacion;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.CandidatoView;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.DataEmpleadoView;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.VotanteView;

/**
 *
 * @author misanchez
 */
@ApplicationScoped
public class CandidatoRepo {

    @PersistenceContext(unitName = "siecssoPU")
    private EntityManager em;

    @Transactional
    public Candidato guardar(Candidato candidato) {
        if (candidato.getId() == null) {
            em.persist(candidato);
        } else {
            em.merge(candidato);
        }

        return candidato;
    }

    @Transactional
    public ProcesoEleccion findProcesoByPk() {
        return em.find(ProcesoEleccion.class, 1);
    }

    @Transactional
    public List<DataEmpleadoView> findEmpleadosByOrg(String inuniorg) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DataEmpleadoView> cr = cb.createQuery(DataEmpleadoView.class);
        Root<DataEmpleadoView> root = cr.from(DataEmpleadoView.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("inuniorg"), inuniorg));

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = em.createQuery(cr);

        return query.getResultList();
    }

    @Transactional
    public List<CandidatoView> findCandidatos() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CandidatoView> cr = cb.createQuery(CandidatoView.class);
        Root<CandidatoView> root = cr.from(CandidatoView.class);

        cr.orderBy(cb.asc(root.get("codigoEmpleado")));
        cr.select(root);

        Query query = em.createQuery(cr);

        return query.getResultList();
    }

    @Transactional
    public void guardarVoto(Integer idProceso, Integer idCandidato, Empleado empleado) {
        ProcesoEleccion pe = em.find((ProcesoEleccion.class), idProceso);

        Votacion voto = new Votacion();
        voto.setFechaInsercion(LocalDate.now());
        voto.setIdCandidato(em.find(Candidato.class, idCandidato));
        voto.setIdProceso(pe);

        em.persist(voto);

        DetalleProceso detalleProceso = new DetalleProceso();
        detalleProceso.setFechaVotacion(LocalDate.now());
        detalleProceso.setIdEmpleado(empleado);
        detalleProceso.setIdProceso(pe);

        em.persist(detalleProceso);
    }

    public Boolean isVotoRealizado(Integer idProceso, Integer idEmpleado) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DetalleProceso> cr = cb.createQuery(DetalleProceso.class);
        Root<DetalleProceso> root = cr.from(DetalleProceso.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("idProceso").get("id"), idProceso));
        lstCondiciones.add(cb.equal(root.get("idEmpleado").get("id"), idEmpleado));

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));
        Query query = em.createQuery(cr);

        return !query.getResultList().isEmpty();
    }

    public List<VotanteView> findAllVotantes() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VotanteView> cr = cb.createQuery(VotanteView.class);
        Root<VotanteView> root = cr.from(VotanteView.class);

        cr.orderBy(cb.asc(root.get("codigoEmpleado")));
        cr.select(root);

        Query query = em.createQuery(cr);

        return query.getResultList();
    }
}
