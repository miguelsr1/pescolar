package sv.gob.mined.pescolar.repository;

import sv.gob.mined.pescolar.model.RecepcionBienesServicios;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import sv.gob.mined.pescolar.model.ContratosOrdenesCompra;
import sv.gob.mined.pescolar.model.DiasPlazoContrato;
import sv.gob.mined.pescolar.model.InfoGeneralContratacion;
import sv.gob.mined.pescolar.model.ResolucionesAdjudicativa;

/**
 *
 * @author misanchez
 */
@Stateless
public class ContratoRepo extends AbstractRepository<ContratosOrdenesCompra, Long> {

    public ContratoRepo() {
        super(ContratosOrdenesCompra.class);
    }

    public ContratosOrdenesCompra findContratoByIdResAdj(Long idResAdj) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContratosOrdenesCompra> cr = cb.createQuery(ContratosOrdenesCompra.class);
        Root<ContratosOrdenesCompra> root = cr.from(ContratosOrdenesCompra.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("idResolucionAdj").get("id"), idResAdj));

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = em.createQuery(cr);

        return (ContratosOrdenesCompra) query.getSingleResult();
    }

    public ContratosOrdenesCompra createContrato(ContratosOrdenesCompra contratosOrdenesCompras) {
        String numeroContrato = getNumContrato(contratosOrdenesCompras.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoEntidad(), contratosOrdenesCompras.getIdResolucionAdj().getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getIdAnho().getId());
        contratosOrdenesCompras.setNumeroContrato(numeroContrato.length() == 1 ? "0" + numeroContrato : numeroContrato);
        em.persist(contratosOrdenesCompras);

        InfoGeneralContratacion info = new InfoGeneralContratacion();

        info.setCantidadContrato(contratosOrdenesCompras.getIdResolucionAdj().getIdParticipante().getCantidad().longValue());
        info.setCodigoDepartamento(contratosOrdenesCompras.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoDepartamento().getId());
        info.setCodigoEntidad(contratosOrdenesCompras.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoEntidad());
        info.setCodigoMunicipio(contratosOrdenesCompras.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoMunicipio());
        info.setFechaInsercion(contratosOrdenesCompras.getFechaInsercion());
        info.setIdContrato(contratosOrdenesCompras.getId());
        info.setIdDetProcesoAdq(contratosOrdenesCompras.getIdResolucionAdj().getIdParticipante().getIdOferta().getIdDetProcesoAdq().getId().longValue());
        info.setIdEmpresa(contratosOrdenesCompras.getIdResolucionAdj().getIdParticipante().getIdEmpresa().getId());
        info.setMontoContrato(contratosOrdenesCompras.getIdResolucionAdj().getIdParticipante().getMonto());
        info.setNombreDepartamento(contratosOrdenesCompras.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoDepartamento().getNombreDepartamento());

        em.persist(info);

        //agregarDatosAResumen(contratosOrdenesCompras);

        return contratosOrdenesCompras;
    }

    private void agregarDatosAResumen(ContratosOrdenesCompra contrato) {
        Query q = em.createNamedQuery("SP_ADD_RESUMEN_CE_PROCESADO");
        q.setParameter("P_ID_DET_PROCESO_ADQ", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getIdDetProcesoAdq().getId());
        q.setParameter("P_CODIGO_ENTIDAD", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoEntidad());
        q.setParameter("P_CODIGO_DEPARTAMENTO", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoDepartamento().getId());
        q.setParameter("P_CODIGO_MUNICIPIO", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoMunicipio());
        q.getResultList();

        q = em.createNamedQuery("SP_ADD_RESUMEN_ADJ_EMP");
        q.setParameter("P_ID_DET_PROCESO_ADQ", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getIdDetProcesoAdq().getId());
        q.setParameter("P_ID_EMPRESA", contrato.getIdResolucionAdj().getIdParticipante().getIdEmpresa().getId());
        q.setParameter("P_ID_TIPO_EMPRESA", contrato.getIdResolucionAdj().getIdParticipante().getIdEmpresa().getIdTipoEmpresa().getId());
        q.setParameter("P_CODIGO_DEPARTAMENTO", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoDepartamento().getId());
        q.setParameter("P_CODIGO_ENTIDAD", contrato.getIdResolucionAdj().getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoEntidad());
        q.setParameter("P_SUBTOTAL", contrato.getIdResolucionAdj().getIdParticipante().getMonto());
        q.setParameter("P_CANTIDAD", contrato.getIdResolucionAdj().getIdParticipante().getCantidad());
        q.getResultList();
    }

    /**
     * Obtiene el numero del contrato y es incremental en base al proceso de
     * adquisición y codigo del centro educativo
     *
     * @param codigoEntidad
     * @param idDetProcesoAdq
     * @return
     */
    private synchronized String getNumContrato(String codigoEntidad, Long idAnho) {
        Query q = em.createQuery("SELECT COUNT(c.id)+1 FROM ContratosOrdenesCompra c WHERE c.idResolucionAdj.idParticipante.idOferta.codigoEntidad.codigoEntidad=:codigoEntidad AND c.idResolucionAdj.idParticipante.idOferta.idDetProcesoAdq.idProcesoAdq.idAnho.id=:idAnho AND c.estadoEliminacion=0 AND c.idResolucionAdj.estadoEliminacion=0 AND c.idResolucionAdj.idParticipante.estadoEliminacion=0 AND c.idResolucionAdj.idParticipante.idOferta.estadoEliminacion=0");
        q.setParameter("codigoEntidad", codigoEntidad);
        q.setParameter("idAnho", idAnho);
        return ((Long) q.getSingleResult()).toString();
    }

    public List<ContratosOrdenesCompra> findContratoByResolucion(ResolucionesAdjudicativa resolucion) {
        Query q = em.createQuery("SELECT c FROM ContratosOrdenesCompras c WHERE c.estadoEliminacion = 0 and c.idResolucionAdj=:resolucion", ContratosOrdenesCompra.class);
        q.setParameter("resolucion", resolucion);
        return q.getResultList();
    }

    public ContratosOrdenesCompra editContrato(ContratosOrdenesCompra contratosOrdenesCompras) {
        Query q = em.createQuery("SELECT r FROM RecepcionBienesServicio r WHERE r.idContrato=:pContrato and r.estadoEliminacion=0", RecepcionBienesServicios.class);
        q.setParameter("pContrato", contratosOrdenesCompras);
        if (!q.getResultList().isEmpty()) {
            RecepcionBienesServicios recep = (RecepcionBienesServicios) q.getResultList().get(0);
            recep.setFechaOrdenInicioEntrega1(contratosOrdenesCompras.getFechaOrdenInicio());

            em.merge(recep);
        }

        return em.merge(contratosOrdenesCompras);
    }

    public DiasPlazoContrato findDiasPlazoPorRubro(Long idRubro, Long idAnho) {
        Query q = em.createQuery("SELECT d FROM DiasPlazoContrato d wHERE d.idAnho.id=:pIdAnho and d.idRubroInteres.id=:pIdRubro", DiasPlazoContrato.class);
        q.setParameter("pIdAnho", idAnho);
        q.setParameter("pIdRubro", idRubro);
        return q.getResultList().isEmpty() ? null : (DiasPlazoContrato) q.getResultList().get(0);
    }
}
