package sv.gob.mined.pescolar.repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.CatalogoProducto;
import sv.gob.mined.pescolar.model.ContratosOrdenesCompra;
import sv.gob.mined.pescolar.model.DetalleOferta;
import sv.gob.mined.pescolar.model.HistorialCamEstResAdj;
import sv.gob.mined.pescolar.model.NivelEducativo;
import sv.gob.mined.pescolar.model.ResolucionesAdjudicativa;
import sv.gob.mined.pescolar.model.RptDocumentos;
import sv.gob.mined.pescolar.model.dto.DetalleItemDto;
import sv.gob.mined.pescolar.model.dto.contratacion.ContratoDto;
import sv.gob.mined.pescolar.model.dto.contratacion.ParticipanteDto;
import sv.gob.mined.pescolar.model.dto.contratacion.ResguardoDto;
import sv.gob.mined.pescolar.model.dto.contratacion.VwRptContratoJurCabecera;
import sv.gob.mined.pescolar.model.dto.contratacion.VwRptPagare;

/**
 *
 * @author misanchez
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ResolucionesAdjudicativasRepo extends AbstractRepository<ResolucionesAdjudicativa, Long> {

    public ResolucionesAdjudicativasRepo() {
        super(ResolucionesAdjudicativa.class);
    }

    @EJB
    private SaldosFacade saldoFacade;
    @Inject
    private ParticipanteRepo participanteRepo;

    /*@Transactional
    public ResolucionesAdjudicativa findResolucionAdjudicativa(Long idParticipante) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ResolucionesAdjudicativa> cr = cb.createQuery(ResolucionesAdjudicativa.class);
        Root<ResolucionesAdjudicativa> root = cr.from(ResolucionesAdjudicativa.class);

        List<Predicate> lstCondiciones = new ArrayList();
        lstCondiciones.add(cb.equal(root.get("idParticipante").get("id"), idParticipante));

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = em.createQuery(cr);

        return (ResolucionesAdjudicativa) query.getSingleResult();
    }*/
    public List<DetalleOferta> findItemsEnResguardo(String codigoEntidad, Long idAnho, String usuario) {
        List<DetalleOferta> lstDetalleResguardo = new ArrayList<>();
        List<ResguardoDto> lstResguardo;
        Query q = em.createNamedQuery("Contratacion.ResguardoTotal", ResguardoDto.class);
        q.setParameter(1, codigoEntidad);
        q.setParameter(2, idAnho);
        lstResguardo = q.getResultList();
        for (ResguardoDto resguardoDto : lstResguardo) {
            DetalleOferta det = new DetalleOferta();
            det.setCantidad(BigInteger.ZERO);
            det.setCantidadResguardo(resguardoDto.getCantidad().toBigInteger());
            det.setConsolidadoEspTec(codigoEntidad);
            CatalogoProducto cp = em.find(CatalogoProducto.class, resguardoDto.getIdProducto());
            NivelEducativo ne = em.find(NivelEducativo.class, resguardoDto.getIdNivelEducativo());
            det.setConsolidadoEspTec(cp.toString() + ", " + ne.toString());
            det.setEstadoEliminacion(0l);
            det.setFechaInsercion(LocalDateTime.now());
            det.setIdNivelEducativo(ne);
            det.setIdProducto(cp);
            det.setNoItem(resguardoDto.getNoItem());
            det.setUsuarioInsercion(usuario);

            lstDetalleResguardo.add(det);
        }
        return lstDetalleResguardo;
    }

    public Boolean validarCambioEstado(ResolucionesAdjudicativa resAdj, Long estadoReserva) {
        Boolean resultado = false;
        if ((resAdj.getIdEstadoReserva().getId().intValue() == 1 && estadoReserva.intValue() == 2) || (resAdj.getIdEstadoReserva().getId().intValue() == 3 && estadoReserva.intValue() == 2)) {
            resultado = true;
        } else //DIGITADA -> ANULADA y REVERTIDA -> ANULADA 
        if ((resAdj.getIdEstadoReserva().getId().intValue() == 1 && estadoReserva.intValue() == 4) || (resAdj.getId().intValue() == 3 && estadoReserva.intValue() == 4)) {
            resultado = true;
        } else //APLICADA -> REVERTIDA
        if (resAdj.getIdEstadoReserva().getId().intValue() == 2 && estadoReserva.intValue() == 3) {
            resultado = true;
        }
        return resultado;
    }

    @Lock(LockType.WRITE)
    public HashMap<String, Object> aplicarReservaDeFondos(ResolucionesAdjudicativa resAdj,
            Long estadoReserva, String codigoEntidad, String comentarioReversion, String usuario) {
        Logger.getLogger(ResolucionesAdjudicativasRepo.class.getName()).log(Level.INFO, "Usuario que aplica reserva: {0} del CE: {1}", new Object[]{usuario, codigoEntidad});
        HashMap<String, Object> param = new HashMap();
        try {
            param = saldoFacade.aplicarReservaDeFondos(resAdj, estadoReserva, codigoEntidad, comentarioReversion, usuario);
        } catch (Exception e) {
            Logger.getLogger(ResolucionesAdjudicativasRepo.class.getName()).log(Level.WARNING, "Es necesario volver aplicar la reserva: {0} del CE: {1}", new Object[]{usuario, codigoEntidad});
            param.put("error", "Por favor, intenten nuevamente APLICAR la reserva.");
        }
        return param;
    }

    public ResolucionesAdjudicativa findResolucionesAdjudicativasByIdParticipante(Long participante) {
        Query q = em.createQuery("SELECT r FROM ResolucionesAdjudicativas r WHERE r.idParticipante.id=:participante and r.estadoEliminacion=0", ResolucionesAdjudicativa.class);
        q.setParameter("participante", participante);
        List<ResolucionesAdjudicativa> lst = q.getResultList();
        if (lst.isEmpty()) {
            return null;
        } else {
            return lst.get(0);
        }
    }

    public List<ContratosOrdenesCompra> findContratoByResolucion(ResolucionesAdjudicativa resolucion) {
        Query q = em.createQuery("SELECT c FROM ContratosOrdenesCompra c WHERE c.estadoEliminacion = 0 and c.idResolucionAdj=:resolucion", ContratosOrdenesCompra.class);
        q.setParameter("resolucion", resolucion);
        return q.getResultList();
    }

    public ContratosOrdenesCompra editContrato(ContratosOrdenesCompra contratosOrdenesCompras) {
        return em.merge(contratosOrdenesCompras);
    }

    public List<ContratoDto> generarRptActaAdjudicacion(Long idResolucion) {
        List<ContratoDto> lst;

        ResolucionesAdjudicativa res = findByPk(idResolucion);

        Query query = em.createNamedQuery("Contratacion.RptActaAdjudicacion", ContratoDto.class);
        query.setParameter(1, res.getIdParticipante().getIdOferta().getId());
        lst = query.getResultList();
        if (lst.isEmpty()) {
            return new ArrayList();
        } else {
            query = em.createNamedQuery("Contratacion.RptActaAdjudicacionParticipantes", ParticipanteDto.class);
            query.setParameter(1, res.getIdParticipante().getIdOferta().getId());
            lst.get(0).setLstParticipantes(query.getResultList());

            query = em.createNamedQuery("Contratacion.RptActaAdjudicacionItems", DetalleItemDto.class);
            query.setParameter(1, res.getIdParticipante().getIdOferta().getId());
            lst.get(0).setLstDetalleItem(query.getResultList());
        }
        return lst;
    }

    public List<ContratoDto> generarRptNotaAdjudicacion(Long idResolucion) {
        Query query = em.createNamedQuery("Contratacion.RptNotaAdjudicacionBean", ContratoDto.class);
        query.setParameter(1, idResolucion);

        List<ContratoDto> lstNotaAdj = query.getResultList();
        if (!lstNotaAdj.isEmpty()) {
            query = em.createQuery("SELECT r.idParticipante.idEmpresa.idPersoneria.id, r.idParticipante.idEmpresa.distribuidor FROM ResolucionesAdjudicativa r WHERE r.id=:idResolucion");
            query.setParameter("idResolucion", idResolucion);

            Object idPersoneria = query.getSingleResult();
            Object lstD[] = (Object[]) idPersoneria;

            if (lstD[1].toString().equals("0")) {
                //FABRICANTES
                query = em.createNamedQuery("Contratacion.RptNotaAdjudicacionBeanDetalleItemFab", DetalleItemDto.class);
            } else {
                //DISTRIBUIDORES
                query = em.createNamedQuery("Contratacion.RptNotaAdjudicacionBeanDetalleItemDist", DetalleItemDto.class);
            }
            query.setParameter(1, idResolucion);
            lstNotaAdj.get(0).setLstDetalleItem(query.getResultList());
        }

        return lstNotaAdj;
    }

    public List<VwRptPagare> generarRptGarantia(Long idResolucion, Long idContrato) {
        try {
            Query query = em.createNamedQuery("Contratacion.VwRptPagare", VwRptPagare.class);
            query.setParameter(1, idResolucion);
            query.setParameter(2, idContrato);

            return query.getResultList();
        } finally {
        }
    }

    public List<RptDocumentos> getDocumentosAImprimir(Integer idDetProcesoAdq, List<Integer> lstNumDoc) {
        Query q = em.createQuery("SELECT r FROM RptDocumentos r WHERE r.idDetProcesoAdq.id=:idDet and r.idTipoRpt.idTipoRpt in :lst ORDER BY r.orden", RptDocumentos.class);
        q.setParameter("idDet", idDetProcesoAdq);
        q.setParameter("lst", lstNumDoc);

        return q.getResultList();
    }

    public List<HistorialCamEstResAdj> findHistorialByIdResolucionAdj(Long idResolucionAdj) {
        Query q = em.createQuery("SELECT h FROM HistorialCamEstResAdj h WHERE h.idResolucionAdj.id=:idResolucionAdj ORDER BY h.idHistorialCam", HistorialCamEstResAdj.class);
        q.setParameter("idResolucionAdj", idResolucionAdj);
        return q.getResultList();
    }

    public List<VwRptContratoJurCabecera> generarContrato(ContratosOrdenesCompra idContrato, Long idRubro) {
        List<VwRptContratoJurCabecera> lstContrato;
        List<DetalleItemDto> lst;
        try {
            Query query = em.createNamedQuery("Contratacion.VwRptContratoJurCabecera", VwRptContratoJurCabecera.class);
            query.setParameter(1, idContrato.getId());

            lstContrato = query.getResultList();

            if (!lstContrato.isEmpty()) {
                query = em.createQuery("SELECT DISTINCT c.idResolucionAdj.idParticipante.idEmpresa.idPersoneria.idPersoneria, c.idResolucionAdj.idParticipante.idEmpresa.distribuidor FROM ContratosOrdenesCompras c WHERE c.idResolucionAdj=:idResolucion and c.estadoEliminacion=0");
                query.setParameter("idResolucion", idContrato.getIdResolucionAdj());

                Object idPersoneria = query.getSingleResult();
                Object[] datos = (Object[]) idPersoneria;

                if (datos[1].toString().equals("1")) {
                    //DISTRIBUIDOR
                    query = em.createNamedQuery("Contratacion.RptNotaAdjudicacionBeanDetalleItemDist", DetalleItemDto.class);
                } else {
                    //FABRICANTE
                    query = em.createNamedQuery("Contratacion.RptNotaAdjudicacionBeanDetalleItemFab", DetalleItemDto.class);
                }

                query.setParameter(1, idContrato.getIdResolucionAdj().getId());
                lst = query.getResultList();

                List<DetalleItemDto> lstDetalle = new ArrayList();
                List<DetalleItemDto> lstDetalleBac = new ArrayList();

                lstDetalleBac.addAll(lst.stream().filter(d -> idRubro.compareTo(1l) == 0 && d.getConsolidadoEspTec().contains("BACHILLERATO")).collect(Collectors.toList()));
                lstDetalle.addAll(lst.stream().filter(d -> idRubro.compareTo(1l) != 0 || !d.getConsolidadoEspTec().contains("BACHILLERATO")).collect(Collectors.toList()));

                lstContrato.get(0).setLstDetalleItems(lstDetalle);
                lstContrato.get(0).setLstDetalleItemsBac(lstDetalleBac);
            }

            return lstContrato;
        } finally {
        }
    }

    public List<ContratoDto> generarRptActaRecomendacion(Long idResolucion) {
        List<ContratoDto> lst;

        ResolucionesAdjudicativa res = findByPk(idResolucion);

        Query query = em.createNamedQuery("Contratacion.RptActaAdjudicacion", ContratoDto.class);
        query.setParameter(1, res.getIdParticipante().getIdOferta().getId());
        lst = query.getResultList();
        if (lst.isEmpty()) {
            return new ArrayList();
        } else {
            query = em.createNamedQuery("Contratacion.RptActaAdjudicacionParticipantes", ParticipanteDto.class);
            query.setParameter(1, res.getIdParticipante().getIdOferta().getId());
            lst.get(0).setLstParticipantes(query.getResultList());

            query = em.createNamedQuery("Contratacion.RptActaAdjudicacionItems", DetalleItemDto.class);
            query.setParameter(1, res.getIdParticipante().getIdOferta().getId());
            lst.get(0).setLstDetalleItem(query.getResultList());

            lst.get(0).setLstPorcentajeEval(participanteRepo.getLstProveedorPorcentajeEval(res.getIdParticipante().getIdOferta()));
        }
        return lst;
    }
}
