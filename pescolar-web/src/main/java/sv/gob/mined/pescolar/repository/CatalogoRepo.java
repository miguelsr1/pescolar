package sv.gob.mined.pescolar.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
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
import sv.gob.mined.pescolar.model.ProcesoAdquisicion;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.model.dto.MunicipioDto;
import sv.gob.mined.pescolar.model.dto.OpcionMenuUsuarioDto;
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;
import sv.gob.mined.pescolar.utils.Constantes;
import sv.gob.mined.pescolar.utils.DescriptorDto;
import sv.gob.mined.pescolar.utils.db.Filtro;

/**
 *
 * @author misanchez
 */
@ApplicationScoped
public class CatalogoRepo {

    @PersistenceContext
    private EntityManager em;

    @Produces
    @RequestScoped
    public EntityManager entityManager() {
        return em;
    }

    private final List<SelectItem> lstDocumentosImp = new ArrayList<>();
    private final SelectItem garantiaUsoTela = new SelectItem(6, "Garantía  de buen uso y resguardo de la tela");

    public <T extends Object> T findEntityByPk(Class<T> clazz, Object pk) {
        return entityManager().find(clazz, pk);
    }

    public List<OpcionMenuUsuarioDto> findAllOpcionMenuByUsuarioAndApp(Long idUsuario, Long idOpcMenu) {
        Query q = entityManager().createNamedQuery("opcionMenuUsuarioDto", OpcionMenuUsuarioDto.class);
        q.setParameter(1, idUsuario);
        q.setParameter(2, idOpcMenu);

        return q.getResultList();
    }

    public List<BigDecimal> findAllModuloByUsuario(Long idUsuario) {
        Query q = entityManager().createNativeQuery("select distinct opc.app idModulo \n"
                + "from usuario usu inner join tipo_usu_opc_menu tuom on usu.id_tipo_usuario = tuom.id_tipo_usuario inner join opcion_menu opc on tuom.id_opc_menu = opc.id_opc_menu\n"
                + "where usu.id_usuario = ?1\n"
                + "order by opc.app");
        q.setParameter(1, idUsuario);
        return q.getResultList();
    }

    public List<RubrosAmostrarInteres> findAllRubrosByIdProceso(Long id) {
        Query q = entityManager().createQuery("SELECT d.idRubroAdq FROM DetalleProcesoAdq d WHERE d.idProcesoAdq.id=:id ORDER BY d.idRubroAdq.id", RubrosAmostrarInteres.class);
        q.setParameter("id", id);
        return q.getResultList();
    }

    public List<EstadoReserva> findAllEstadoReserva() {
        Query q = entityManager().createQuery("SELECT e FROM EstadoReserva e ORDER BY e.id", EstadoReserva.class);
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
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(arg);
        Root root = cq.from(arg);
        cq = createCriteriaQuery(cb, cq, root, parametros);
        Query query = entityManager().createQuery(cq);

        return query.getResultList();
    }

    @Transactional
    public List<?> findListByParam(Class<?> arg, List<Filtro> parametros, String... varIns) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root root = cq.from(arg);
        List<Selection<?>> lst = new ArrayList<>();
        for (String varIn : varIns) {
            lst.add(root.get(varIn));
        }
        cq.multiselect(lst);

        Query query = entityManager().createQuery(cq);

        return query.getResultList();
    }

    @Transactional
    public <T extends Object> T findByParam(Class<T> arg, List<Filtro> parametros) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(arg);
        Root root = cq.from(arg);
        cq = createCriteriaQuery(cb, cq, root, parametros);
        Query query = entityManager().createQuery(cq);

        return query.getResultList().isEmpty() ? null : (T) query.getSingleResult();
    }

    @Transactional
    public List<?> findListByParam(Class<?> arg, List<Filtro> parametros, String orderBy, Boolean orderAsc) {
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
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

        Query query = entityManager().createQuery(cq);

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

        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<EstadisticaCenso> root = cq.from(EstadisticaCenso.class);
        cq.select(root.get("idNivelEducativo").get("id"));

        lstCondiciones.add(cb.equal(root.get("idProcesoAdq").get("id"), idProcesoAdq));
        lstCondiciones.add(cb.equal(root.get("codigoEntidad"), codigoEntidad));

        cq.where(lstCondiciones.toArray(Predicate[]::new));

        return entityManager().createQuery(cq).getResultList();
    }

    public List<VwCatalogoEntidadEducativa> findAllEntidades() {
        CriteriaQuery<VwCatalogoEntidadEducativa> cq = entityManager().getCriteriaBuilder().createQuery(VwCatalogoEntidadEducativa.class);
        Root<VwCatalogoEntidadEducativa> root = cq.from(VwCatalogoEntidadEducativa.class);
        cq.select(root);
        return entityManager().createQuery(cq).getResultList();
    }

    public <T extends Object> T findDetProveedor(DetRubroMuestraIntere detRubro, Long idPro, Class clase) {
        Query q = entityManager().createQuery("SELECT d FROM " + clase.getSimpleName() + " d WHERE d.idMuestraInteres.idRubroInteres.id=:pIdRubro and d.idMuestraInteres.idAnho.id=:pIdAnho and d.idMuestraInteres.idEmpresa=:idEmpresa and d.estadoEliminacion=0 and d.idMuestraInteres.estadoEliminacion=0 " + (clase.equals(CapaInstPorRubro.class) ? " and d.idProcesoAdq.id=:pIdPro " : "") + " ORDER BY d.idMuestraInteres", clase);
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
            query = entityManager().createQuery("SELECT m FROM Municipio m WHERE m.codigoDepartamento.id=:departamento", Municipio.class);
            query.setParameter("departamento", codigoDepartamento);
        } else {
            switch (codigoDepartamento) {
                case "00":
                    query = entityManager().createQuery("SELECT m FROM Municipio m ORDER BY cast(m.codigoDepartamento.codigoDepartamento as integer"
                            + " ),  cast(m.codigoMunicipio as integer)", Municipio.class);
                    break;
                default:
                    query = entityManager().createQuery("SELECT m FROM Municipio m WHERE m.codigoDepartamento.id=:departamento ORDER BY cast(m.codigoDepartamento.id as integer),  cast(m.codigoMunicipio as integer)", Municipio.class);
                    query.setParameter("departamento", codigoDepartamento);
                    break;
            }
        }
        return query.getResultList();
    }

    public List<Canton> getLstCantonByMunicipio(Long idMunicipio) {
        Query q = entityManager().createQuery("SELECT c FROM Canton c WHERE c.idMunicipio= :id ORDER BY c.codigoCanton", Canton.class);
        q.setParameter("id", new BigInteger(idMunicipio.toString()));
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
        Query q = entityManager().createQuery("SELECT e FROM EntidadFinanciera e WHERE e.bandera=:tipoEntidad ORDER BY e.nombreEntFinan", EntidadFinanciera.class);
        q.setParameter("tipoEntidad", tipoEntidad);
        return q.getResultList();
    }

    public DetRubroMuestraIntere findDetRubroByAnhoAndRubro(Long idAnho, Long idEmpresa) {
        Query q = entityManager().createQuery("SELECT d FROM DetRubroMuestraIntere d WHERE d.idEmpresa.id=:idEmpresa and d.idAnho.id=:pIdAnho ORDER BY d.id", DetRubroMuestraIntere.class);
        q.setParameter("idEmpresa", idEmpresa);
        q.setParameter("pIdAnho", idAnho);
        if (q.getResultList().isEmpty()) {
            return null;
        } else {
            return (DetRubroMuestraIntere) q.getResultList().get(0);
        }
    }

    public List<MunicipioDto> getLstMunicipiosDisponiblesDeInteres(Long idCapaDistribucion, String codigoDepartamento) {
        String sql = Constantes.QUERY_PROVEEDOR_MUNICIPIOS_DISPONIBLES_DE_INTERES;
        sql = codigoDepartamento.equals("00") ? sql.replace("COMODIN_DEPARTAMENTO", "")
                : sql.replace("COMODIN_DEPARTAMENTO", "and depa.codigo_departamento = '" + codigoDepartamento + "'");
        Query q = entityManager().createNativeQuery(sql, MunicipioDto.class);
        q.setParameter(1, idCapaDistribucion);
        return q.getResultList();
    }

    public List<MunicipioDto> getLstMunicipiosDeInteres(Long idCapaDistribucion) {
        Query q = entityManager().createNamedQuery("Proveedor.MunicipiosDeIntegeres", MunicipioDto.class);
        q.setParameter(1, idCapaDistribucion);
        return q.getResultList();
    }

    public List<DescriptorDto> getLstTecnicosPaquete() {
        List<DescriptorDto> lst = new ArrayList();

        lst.add(new DescriptorDto("Carlos Enrique Villegas", "carlos.enrique.villegas@admin.mined.edu.sv"));
        lst.add(new DescriptorDto("Yasmin Floricelda Pineda", "yasmin.floricelda.pineda@admin.mined.edu.sv"));
        lst.add(new DescriptorDto("Misael Humberto Escobar", "misael.humberto.chavez@admin.mined.edu.sv"));
        lst.add(new DescriptorDto("Evelyn Lissette Zetino", "evelin.lissette.zetino@admin.mined.edu.sv"));
        lst.add(new DescriptorDto("Rudy Josue Acevedo", "rudy.josue.acevedo@admin.mined.edu.sv"));
        lst.add(new DescriptorDto("Angela Margarita Rosales", "angela.margarita.rosales@admin.mined.edu.sv"));
        lst.add(new DescriptorDto("Jorge Adalberto Henriquez Acosta", "jorge.adalberto.henriquez@admin.mined.edu.sv"));
        lst.add(new DescriptorDto("Jaime Roberto Gomez", "jaime.roberto.gomez@admin.mined.edu.sv"));
        lst.add(new DescriptorDto("Rene Oswaldo Hernandez Brizuela", "rene.oswaldo.hernandez@admin.mined.edu.sv"));
        lst.add(new DescriptorDto("Jose Ruben Renderos Giron", "jose.ruben.renderos@admin.mined.edu.sv"));
        lst.add(new DescriptorDto("Juan Alberto Rivera", "juan.alberto.rivera@admin.mined.edu.sv"));
        lst.add(new DescriptorDto("Ester Elizabeth Ayala", "ester.elizabeth.ayala@admin.mined.edu.sv"));
        lst.add(new DescriptorDto("RAFA ", "rafael.arias@mined.gob.sv"));

        return lst;
    }

    public List<RubrosAmostrarInteres> findRubrosByCe(String codigoEntidad, Long idProcesoAdq) {
        Query q = em.createQuery("select r.idRubroInteres from RubroPorCe r WHERE r.codigoEntidad.codigoEntidad=:pCodigo and r.idProcesoAdq.id=:pIdPro and r.estadoEliminacion=0", RubrosAmostrarInteres.class);
        q.setParameter("pCodigo", codigoEntidad);
        q.setParameter("pIdPro", idProcesoAdq);
        return q.getResultList();
    }

    public ProcesoAdquisicion getProcesoAnhoAnterior(Long idAnho) {
        Query q = em.createQuery("SELECT p FROM ProcesoAdquisicion p WHERE p.idAnho.id=:pIdAnho and p.padreIdProcesoAdq is null", ProcesoAdquisicion.class);
        q.setParameter("pIdAnho", idAnho - 1);
        return (ProcesoAdquisicion) q.getSingleResult();
    }

    public List<SelectItem> getLstDocumentosImp(Boolean uniforme, Integer idAnho) {
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
}
