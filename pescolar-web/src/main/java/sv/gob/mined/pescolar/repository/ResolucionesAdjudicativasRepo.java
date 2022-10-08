package sv.gob.mined.pescolar.repository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
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
public class ResolucionesAdjudicativasRepo extends AbstractRepository<ResolucionesAdjudicativa, Long> {

    public ResolucionesAdjudicativasRepo() {
        super(ResolucionesAdjudicativa.class);
    }

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
}
