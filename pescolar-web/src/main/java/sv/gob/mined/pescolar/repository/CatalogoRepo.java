package sv.gob.mined.pescolar.repository;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.transaction.Transactional;
import sv.gob.mined.pescolar.model.EstadisticaCenso;
import sv.gob.mined.pescolar.model.EstadoReserva;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.model.dto.OpcionMenuUsuarioDto;
import sv.gob.mined.pescolar.utils.db.Filtro;

/**
 *
 * @author misanchez
 */
@ApplicationScoped
public class CatalogoRepo {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    public <T extends Object> T findEntityByPk(Class<T> clazz, Object pk) {
        return em.find(clazz, pk);
    }

    public List<OpcionMenuUsuarioDto> findAllOpcionMenuByUsuarioAndApp(Long idUsuario, Long idOpcMenu) {
        Query q = em.createNamedQuery("opcionMenuUsuarioDto", OpcionMenuUsuarioDto.class);
        q.setParameter(1, idUsuario);
        q.setParameter(2, idOpcMenu);

        return q.getResultList();
    }

    public List<RubrosAmostrarInteres> findAllRubrosByIdProceso(Long id) {
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
    public List<?> findListByParam(Class<?> arg, List<Filtro> parametros) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(arg);
        Root root = cq.from(arg);
        cq = createCriteriaQuery(cb, cq, root, arg, parametros);
        Query query = em.createQuery(cq);

        return query.getResultList();
    }

    @Transactional
    public List<?> findListByParam(Class<?> arg, List<Filtro> parametros, String... varIns) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root root = cq.from(arg);
        List<Selection<?>> lst = new ArrayList<>();
        for (String varIn : varIns) {
            lst.add(root.get(varIn));
        }
        cq.multiselect(lst);
        
        Query query = em.createQuery(cq);

        return query.getResultList();
    }

    @Transactional
    public <T extends Object> T findByParam(Class<T> arg, List<Filtro> parametros) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(arg);
        Root root = cq.from(arg);
        cq = createCriteriaQuery(cb, cq, root, arg, parametros);
        Query query = em.createQuery(cq);

        return query.getResultList().isEmpty() ? null : (T) query.getSingleResult();
    }

    @Transactional
    public List<?> findListByParam(Class<?> arg, List<Filtro> parametros, String orderBy, Boolean orderAsc) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(arg);
        Root root = cq.from(arg);
        cq = createCriteriaQuery(cb, cq, root, arg, parametros);

        if (orderBy != null && orderAsc != null) {
            Path path = root;

            //Adición de campos de ordenamiento
            if (orderBy.split("\\.").length > 1) {
                for (String clave : orderBy.split("\\.")) {
                    path = path.get(clave);
                }
            }

            if (orderAsc) {
                cq.orderBy(cb.asc(path));
            } else {
                cq.orderBy(cb.desc(path));
            }
        }

        Query query = em.createQuery(cq);

        return query.getResultList();
    }

    private CriteriaQuery createCriteriaQuery(CriteriaBuilder cb, CriteriaQuery cq, Root root, Class<?> arg, List<Filtro> parametros) {
        List<Predicate> lstCondiciones = new ArrayList();

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
                }
            }
        }
        cq.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        return cq;
    }

    @Transactional
    public List<Long> getLstNivelesConMatriculaReportadaByIdProcesoAdqAndCodigoEntidad(Long idProcesoAdq, String codigoEntidad) {
        List<Predicate> lstCondiciones = new ArrayList();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<EstadisticaCenso> root = cq.from(EstadisticaCenso.class);
        cq.select(root.get("idNivelEducativo").get("id"));

        lstCondiciones.add(cb.equal(root.get("idProcesoAdq").get("id"), idProcesoAdq));
        lstCondiciones.add(cb.equal(root.get("codigoEntidad"), codigoEntidad));

        cq.where(lstCondiciones.toArray(Predicate[]::new));

        return em.createQuery(cq).getResultList();
    }
}
