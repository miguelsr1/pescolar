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
import sv.gob.mined.dhcomitesso.model.dhcsso.view.CandidatoDto;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.EmpleadoDto;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.ResultadosDto;
import sv.gob.mined.dhcomitesso.model.dhcsso.view.VotanteDto;

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
    public List<EmpleadoDto> findEmpleadosByOrg(String inuniorg) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EmpleadoDto> cr = cb.createQuery(EmpleadoDto.class);
        Root<EmpleadoDto> root = cr.from(EmpleadoDto.class);

        /*List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("inuniorg"), inuniorg));*/

        cr.select(root).where(cb.equal(root.get("inuniorg"), inuniorg));

        Query query = em.createQuery(cr);

        return query.getResultList();
    }

    @Transactional
    public List<CandidatoDto> findCandidatosByIdProceso(Integer idProceso) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CandidatoDto> cr = cb.createQuery(CandidatoDto.class);
        Root<CandidatoDto> root = cr.from(CandidatoDto.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("idProceso"), idProceso));

        cr.orderBy(cb.asc(root.get("codigoEmpleado")));
        cr.select(root).where(cb.equal(root.get("idProceso"), idProceso));

        Query query = em.createQuery(cr);

        return query.getResultList();
    }

    @Transactional
    public List<ResultadosDto> findResultadosByIdProceso(Integer idProceso) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResultadosDto> cr = cb.createQuery(ResultadosDto.class);
        Root<ResultadosDto> root = cr.from(ResultadosDto.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("idProceso"), idProceso));

        cr.select(root).where(cb.equal(root.get("idProceso"), idProceso));

        Query query = em.createQuery(cr);

        return query.getResultList();
    }

    @Transactional
    public List<ResultadosDto> findCandidatosElectosByIdProceso(Integer idProceso) {
        Query query = em.createNamedQuery("candidatosElectos", ResultadosDto.class);
        query.setParameter(1, idProceso);
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

        cr.select(root).where(cb.equal(root.get("idProceso").get("id"), idProceso), cb.equal(root.get("idEmpleado").get("id"), idEmpleado));
        Query query = em.createQuery(cr);

        return !query.getResultList().isEmpty();
    }

    public List<VotanteDto> findAllVotantesByIdProceso(Integer idProceso) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<VotanteDto> cr = cb.createQuery(VotanteDto.class);
        Root<VotanteDto> root = cr.from(VotanteDto.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("idProceso"), idProceso));

        cr.orderBy(cb.asc(root.get("codigoEmpleado")));
        cr.select(root).where(cb.equal(root.get("idProceso"), idProceso));

        Query query = em.createQuery(cr);

        return query.getResultList();
    }

    public List<Empleado> findAllEmpleados() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Empleado> cr = cb.createQuery(Empleado.class);
        Root<Empleado> root = cr.from(Empleado.class);

        cr.select(root);

        Query query = em.createQuery(cr);

        return query.getResultList();
    }
}
