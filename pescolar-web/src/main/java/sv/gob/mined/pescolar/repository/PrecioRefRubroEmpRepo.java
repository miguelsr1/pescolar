package sv.gob.mined.pescolar.repository;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sv.gob.mined.pescolar.model.DetalleProcesoAdq;
import sv.gob.mined.pescolar.model.NivelEducativo;
import sv.gob.mined.pescolar.model.PreciosRefRubro;
import sv.gob.mined.pescolar.model.PreciosRefRubroEmp;
import sv.gob.mined.pescolar.model.dto.contratacion.PrecioReferenciaEmpresaDto;

/**
 *
 * @author misanchez
 */
@Stateless
public class PrecioRefRubroEmpRepo extends AbstractRepository<PreciosRefRubroEmp, Long> {

    public PrecioRefRubroEmpRepo() {
        super(PreciosRefRubroEmp.class);
    }

    public List<PreciosRefRubroEmp> findPreciosByEmp(Long idEmpresa, Long idRubro, Long idAnho) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PreciosRefRubroEmp> cq = cb.createQuery(PreciosRefRubroEmp.class);
        Root<PreciosRefRubroEmp> root = cq.from(PreciosRefRubroEmp.class);

        cq.where(cb.equal(root.get("idMuestraInteres").get("idEmpresa").get("id"), idEmpresa),
                cb.equal(root.get("idMuestraInteres").get("idRubroInteres").get("id"), idRubro),
                cb.equal(root.get("idMuestraInteres").get("idAnho").get("id"), idAnho));

        cq.orderBy(cb.asc(root.get("noItem").as(Integer.class)));
        Query query = em.createQuery(cq);
        return query.getResultList();
    }

    public List<PrecioReferenciaEmpresaDto> getLstPreciosByIdEmpresaAndIdProcesoAdq(Long idEmpresa, Long idAnho, String idNivelesCe) {
        Query q = em.createNativeQuery("select \n"
                + "                rownum                  idRow,\n"
                + "                pemp.no_item            noItem,\n"
                + "                cat.nombre_producto     nombreProducto,\n"
                + "                niv.descripcion_nivel   descripcionNivel,\n"
                + "                pmax.precio_maximo      precioMaximo,\n"
                + "                pemp.precio_referencia  precioReferencia\n"
                + "            from precio_maximo_referencia pmax\n"
                + "                inner join det_rubro_muestra_interes det on pmax.id_rubro_interes = det.id_rubro_interes and\n"
                + "                                                            pmax.id_anho = det.id_anho                                                            \n"
                + "                inner join precios_ref_rubro_emp pemp on pmax.id_producto = pemp.id_producto and \n"
                + "                                                        pmax.no_item = pemp.no_item and\n"
                + "                                                        pemp.id_muestra_interes = det.id_muestra_interes\n"
                + "                inner join catalogo_producto cat      on pemp.id_producto = cat.id_producto\n"
                + "                inner join nivel_educativo niv        on pemp.id_nivel_educativo = niv.id_nivel_educativo\n"
                + "            where \n"
                + "                det.id_empresa = " + idEmpresa + " and \n"
                + "                det.id_anho = " + idAnho + " and\n"
                + "                pemp.estado_eliminacion = 0 and\n"
                + (idAnho.intValue() > 8 ? " pemp.id_nivel_educativo in (" + (idNivelesCe.replace("1", "22").replace("5", "5,23").replace("6", "6,24")) + ")\n" : "                pemp.id_nivel_educativo in (" + (idNivelesCe) + ")\n")
                + "            order by to_number(pemp.no_item)", PrecioReferenciaEmpresaDto.class);
        return q.getResultList();
    }

    public PreciosRefRubro findPreciosRefRubroByNivelEduAndRubro(Long nivelEdu, DetalleProcesoAdq rubro) {
        PreciosRefRubro pr = new PreciosRefRubro();
        NivelEducativo n = em.find(NivelEducativo.class, nivelEdu);
        pr.setIdNivelEducativo(n);
        pr.setPrecioMaxFem(BigDecimal.ZERO);
        pr.setPrecioMaxMas(BigDecimal.ZERO);

        if (rubro == null || nivelEdu == null) {
            return pr;
        }
        Query q = em.createQuery("SELECT p FROM PreciosRefRubro p WHERE p.idNivelEducativo.id=:nivelEdu and p.idRubroInteres.id=:pIdRubro and p.idAnho.id=:pIdAnho", PreciosRefRubro.class);
        q.setParameter("nivelEdu", nivelEdu);
        q.setParameter("pIdRubro", rubro.getIdRubroAdq().getId());
        q.setParameter("pIdAnho", rubro.getIdProcesoAdq().getIdAnho().getId());

        if (q.getResultList().isEmpty()) {
            return pr;
        } else {
            return (PreciosRefRubro) q.getSingleResult();
        }
    }

    public List<PreciosRefRubro> getLstPreciosRefRubroByRubro(DetalleProcesoAdq rubro, Boolean ceClimaFrio) {
        Query q;

        if (rubro.getIdRubroAdq().getIdRubroUniforme() == 1) {
            if (ceClimaFrio) {
                q = em.createQuery("SELECT p FROM PreciosRefRubro p WHERE p.idAnho.id = :pIdAnho and p.idRubroInteres.id = :pIdRubro and ((p.idNivelEducativo.id = 22 and p.climaFrio=1) or (p.idNivelEducativo.id in (2,6) and p.climaFrio=0)) ORDER BY p.idNivelEducativo.orden2", PreciosRefRubro.class);
            } else {
                q = em.createQuery("SELECT p FROM PreciosRefRubro p WHERE p.idAnho.id = :pIdAnho and p.idRubroInteres.id = :pIdRubro and p.idNivelEducativo.id in (22, 2, 6) and p.climaFrio=0 ORDER BY p.idNivelEducativo.orden2", PreciosRefRubro.class);
            }
        } else {
            q = em.createQuery("SELECT p FROM PreciosRefRubro p WHERE p.idAnho.id = :pIdAnho and p.idRubroInteres.id = :pIdRubro ORDER BY p.idNivelEducativo.orden2", PreciosRefRubro.class);
        }

        q.setParameter("pIdAnho", rubro.getIdProcesoAdq().getIdAnho().getId());
        q.setParameter("pIdRubro", rubro.getIdRubroAdq().getId());

        return q.getResultList();
    }
}
