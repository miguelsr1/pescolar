package sv.gob.mined.pescolar.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.EstadoReserva;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.utils.Filtro;

/**
 *
 * @author misanchez
 */
@ApplicationScoped
public class CatalogoRepo {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    public List<Departamento> findAllDepartamento() {
        Query q = em.createQuery("SELECT d FROM Departamento d ORDER BY d.id", Departamento.class);
        return q.getResultList();
    }

    public List<Municipio> findMunicipiosByDepa(String codigoDepartamento) {
        Query q = em.createQuery("SELECT m FROM Municipio m WHERE m.codigoDepartamento.id=:codDepa ORDER BY m.id", Municipio.class);
        q.setParameter("codDepa", codigoDepartamento);
        return q.getResultList();
    }

    public List<RubrosAmostrarInteres> findAllRubrosByIdProceso(Integer id) {
        Query q = em.createQuery("SELECT d.idRubroAdq FROM DetalleProcesoAdq d WHERE d.idProcesoAdq.id=:id ORDER BY d.idRubroAdq.id", RubrosAmostrarInteres.class);
        q.setParameter("id", id);
        return q.getResultList();
    }

    public List<EstadoReserva> findAllEstadoReserva() {
        Query q = em.createQuery("SELECT e FROM EstadoReserva e ORDER BY e.id", EstadoReserva.class);
        return q.getResultList();
    }

    public List<SelectItem> getLstDocumentosImp(Boolean uniforme, Integer idAnho, List<SelectItem> lstDocumentosImp) {
        SelectItem garantiaUsoTela = new SelectItem(6, "Garantía  de buen uso y resguardo de la tela");

        if (lstDocumentosImp.isEmpty()) {
            //Id son los mismos que estan el la tabla TIPO_RPT
            lstDocumentosImp.add(new SelectItem(7, "Contrato"));
            lstDocumentosImp.add(new SelectItem(5, "Garantía Contrato"));
            lstDocumentosImp.add(new SelectItem(4, "Nota Adjudicación"));
            lstDocumentosImp.add(new SelectItem(3, "Acta Adjudicación"));
            if (idAnho != 10) {
                lstDocumentosImp.add(new SelectItem(12, "Orden de Inicio"));
            }
            lstDocumentosImp.add(new SelectItem(10, "Declaración Adjudicatorio"));
            lstDocumentosImp.add(new SelectItem(13, "Acta de Recomendación"));
            lstDocumentosImp.add(new SelectItem(2, "Cotización"));
            lstDocumentosImp.add(new SelectItem(11, "Oferta Global del Proveedor"));

        }
        if (uniforme) {
            if (!lstDocumentosImp.contains(garantiaUsoTela)) {
                lstDocumentosImp.add(garantiaUsoTela);
            }
        } else {
            lstDocumentosImp.remove(garantiaUsoTela);
        }

        return lstDocumentosImp;
    }

    @Transactional
    public List<?> findListByParam(Class<?> arg, Map<String, Object> params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cr = cb.createQuery(arg);
        Root root = cr.from(arg);
        List<Predicate> lstCondiciones = new ArrayList();

        for (String var : params.keySet()) {
            lstCondiciones.add(cb.equal(root.get(var), params.get(var)));
        }

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = em.createQuery(cr);

        return query.getResultList();
    }

    @Transactional
    public List<?> findListByParam(Class<?> arg, List<Filtro> parametros) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(arg);
        Root root = cq.from(arg);

        for (Filtro parametro : parametros) {
            switch (parametro.getTipoOperacion()) {
                case 1://EQUALS
                    if (parametro.getClave().split(",").length > 1) {
                        cq.select(root).where(cb.equal(root.get("idPersona").get("usuario"), parametro.getValor()));
                    } else {
                        cq.select(root).where(cb.equal(root.get(parametro.getClave()), parametro.getValor()));
                    }
                    break;
                case 2://LIKE
                    cq.select(root).where(cb.like(root.get(parametro.getClave()), "%" + parametro.getValor() + "%"));
                    break;
            }
        }

        return em.createQuery(cq).getResultList().isEmpty() ? null : em.createQuery(cq).getResultList();
    }
}
