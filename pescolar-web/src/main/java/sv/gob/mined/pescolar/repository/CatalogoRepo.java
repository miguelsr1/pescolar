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
import sv.gob.mined.pescolar.model.Canton;
import sv.gob.mined.pescolar.model.CapaInstPorRubro;
import sv.gob.mined.pescolar.model.DetRubroMuestraIntere;
import sv.gob.mined.pescolar.model.EntidadFinanciera;
import sv.gob.mined.pescolar.model.EstadisticaCenso;
import sv.gob.mined.pescolar.model.EstadoReserva;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.model.dto.OpcionMenuUsuarioDto;
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;
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
        cq = createCriteriaQuery(cb, cq, root, parametros);
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
        cq = createCriteriaQuery(cb, cq, root, parametros);
        Query query = em.createQuery(cq);

        return query.getResultList().isEmpty() ? null : (T) query.getSingleResult();
    }

    @Transactional
    public List<?> findListByParam(Class<?> arg, List<Filtro> parametros, String orderBy, Boolean orderAsc) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(arg);
        Root root = cq.from(arg);
        cq = createCriteriaQuery(cb, cq, root, parametros);

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

    private CriteriaQuery createCriteriaQuery(CriteriaBuilder cb, CriteriaQuery cq, Root root, List<Filtro> parametros) {
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

    public List<VwCatalogoEntidadEducativa> findAllEntidades() {
        CriteriaQuery<VwCatalogoEntidadEducativa> cq = em.getCriteriaBuilder().createQuery(VwCatalogoEntidadEducativa.class);
        Root<VwCatalogoEntidadEducativa> root = cq.from(VwCatalogoEntidadEducativa.class);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    public <T extends Object> T findDetProveedor(DetRubroMuestraIntere detRubro, Long idPro, Class clase) {
        Query q = em.createQuery("SELECT d FROM " + clase.getSimpleName() + " d WHERE d.idMuestraInteres.idRubroInteres.idRubroInteres=:pIdRubro and d.idMuestraInteres.idAnho.idAnho=:pIdAnho and d.idMuestraInteres.idEmpresa=:idEmpresa and d.estadoEliminacion=0 and d.idMuestraInteres.estadoEliminacion=0 " + (clase.equals(CapaInstPorRubro.class) ? " and d.idProcesoAdq.idProcesoAdq=:pIdPro " : "") + " ORDER BY d.idMuestraInteres", clase);
        q.setParameter("pIdRubro", detRubro.getIdRubroInteres().getId());
        q.setParameter("pIdAnho", detRubro.getIdAnho().getId());
        q.setParameter("idEmpresa", detRubro.getIdEmpresa());
        if (clase.equals(CapaInstPorRubro.class)) {
            q.setParameter("pIdPro", idPro);
        }

        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (T) q.getResultList().get(0);
        }
    }

    public List<Municipio> getLstMunicipiosByDepartamento(String codigoDepartamento) {
        Query query;
        if (null == codigoDepartamento) {
            query = em.createQuery("SELECT m FROM Municipio m WHERE m.codigoDepartamento.codigoDepartamento=:departamento", Municipio.class);
            query.setParameter("departamento", codigoDepartamento);
        } else {
            switch (codigoDepartamento) {
                case "00":
                    query = em.createQuery("SELECT m FROM Municipio m ORDER BY FUNC('TO_NUMBER',m.codigoDepartamento.codigoDepartamento),  FUNC('TO_NUMBER',m.codigoMunicipio)", Municipio.class);
                    break;
                default:
                    query = em.createQuery("SELECT m FROM Municipio m WHERE m.codigoDepartamento.codigoDepartamento=:departamento ORDER BY FUNC('TO_NUMBER',m.codigoDepartamento.codigoDepartamento),  FUNC('TO_NUMBER',m.codigoMunicipio)", Municipio.class);
                    query.setParameter("departamento", codigoDepartamento);
                    break;
            }
        }
        return query.getResultList();
    }

    public List<Canton> getLstCantonByMunicipio(Long idMunicipio) {
        Query q = em.createQuery("SELECT c FROM Canton c WHERE c.idMunicipio=:id ORDER BY c.codigoCanton", Canton.class);
        q.setParameter("id", idMunicipio);
        return q.getResultList();
    }

    /**
     * Devuelve un listado de entidades financieras (BANCOS o CAJAS DE CREDITO O
     * PRESTAMO) dependiendo del parametro que reciba 0 - Modulo de créditos 1 -
     * Bancos asociados a cuentas de los proveedores 2 - Las 2 anteriores
     *
     * @param tipoEntidad
     * @return
     */
    public List<EntidadFinanciera> findEntidadFinancieraEntities(Short tipoEntidad) {
        Query q = em.createQuery("SELECT e FROM EntidadFinanciera e WHERE e.estadoEliminacion=0 AND e.bandera=:tipoEntidad ORDER BY e.nombreEntFinan", EntidadFinanciera.class);
        q.setParameter("tipoEntidad", tipoEntidad);
        return q.getResultList();
    }

    public DetRubroMuestraIntere findDetRubroByAnhoAndRubro(Long idAnho, Long idEmpresa) {
        Query q = em.createQuery("SELECT d FROM DetRubroMuestraInteres d WHERE d.idEmpresa.idEmpresa=:idEmpresa and d.idAnho.idAnho=:pIdAnho and d.estadoEliminacion=0 ORDER BY d.idMuestraInteres", DetRubroMuestraIntere.class);
        q.setParameter("idEmpresa", idEmpresa);
        q.setParameter("pIdAnho", idAnho);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (DetRubroMuestraIntere) q.getResultList().get(0);
        }
    }
}
