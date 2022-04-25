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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import sv.gob.mined.pescolar.model.EstadoReserva;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.model.dto.OpcionMenuUsuarioDto;
import sv.gob.mined.pescolar.utils.Filtro;

/**
 *
 * @author misanchez
 */
@ApplicationScoped
public class CatalogoRepo {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    public Object findEntityByPk(Class clazz, Object pk) {
        return em.find(clazz, pk);
    }

    public List<OpcionMenuUsuarioDto> findAllOpcionMenuByUsuarioAndApp(Long idUsuario, Long idOpcMenu) {
        Query q = em.createNamedQuery("opcionMenuUsuarioDto", OpcionMenuUsuarioDto.class);
        q.setParameter(1, idUsuario);
        q.setParameter(2, idOpcMenu);

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
        CriteriaQuery cq = cb.createQuery(arg);
        Root root = cq.from(arg);
        List<Predicate> lstCondiciones = new ArrayList();

        for (String var : params.keySet()) {
            lstCondiciones.add(cb.equal(root.get(var), params.get(var)));
        }

        cq.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = em.createQuery(cq);

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
                    if (parametro.getClave().split("\\.").length > 1) {
                        Path path = root;
                        for (String clave : parametro.getClave().split("\\.")) {
                            path = path.get(clave);
                        }
                        cq.select(root).where(cb.equal(path, parametro.getValor()));
                    } else {
                        cq.select(root).where(cb.equal(root.get(parametro.getClave()), parametro.getValor()));
                    }
                    break;
                case 2://LIKE
                    cq.select(root).where(cb.like(root.get(parametro.getClave()), "%" + parametro.getValor() + "%"));
                    break;
            }
        }

        Query query = em.createQuery(cq);

        return query.getResultList();
    }

    @Transactional
    public List<?> findListByParam(Class<?> arg, List<Filtro> parametros, String orderBy, Boolean orderAsc) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(arg);
        Root root = cq.from(arg);

        for (Filtro parametro : parametros) {
            Path path = root;
            if (parametro.getValor() != null) {
                switch (parametro.getTipoOperacion()) {
                    case 1://EQUALS

                        if (parametro.getClave().split("\\.").length > 1) {
                            for (String clave : parametro.getClave().split("\\.")) {
                                path = path.get(clave);
                            }
                            cq.select(root).where(cb.equal(path, parametro.getValor()));
                        } else {
                            cq.select(root).where(cb.equal(path.get(parametro.getClave()), parametro.getValor()));
                        }
                        break;
                    case 2://LIKE
                        if (parametro.getClave().split("\\.").length > 1) {
                            for (String clave : parametro.getClave().split("\\.")) {
                                path = path.get(clave);
                            }
                            cq.select(root).where(cb.like(path, "%" + parametro.getValor() + "%"));
                        } else {
                            cq.select(root).where(cb.like(path, "%" + parametro.getValor() + "%"));
                        }

                        //cq.select(root).where(cb.like(root.get(parametro.getClave()), "%" + parametro.getValor() + "%"));
                        break;
                }
            }
        }

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
}
