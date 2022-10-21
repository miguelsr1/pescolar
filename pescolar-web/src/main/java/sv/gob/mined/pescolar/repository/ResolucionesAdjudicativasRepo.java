package sv.gob.mined.pescolar.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.CatalogoProducto;
import sv.gob.mined.pescolar.model.DetalleOferta;
import sv.gob.mined.pescolar.model.NivelEducativo;
import sv.gob.mined.pescolar.model.ResolucionesAdjudicativa;
import sv.gob.mined.pescolar.model.dto.contratacion.ResguardoDto;

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
}
