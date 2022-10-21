package sv.gob.mined.pescolar.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.DetalleProcesoAdq;
import sv.gob.mined.pescolar.model.OfertaBienesServicio;

/**
 *
 * @author misanchez
 */
@Stateless
public class OfertaRepo extends AbstractRepository<OfertaBienesServicio, Long> {


    public OfertaRepo() {
        super(OfertaBienesServicio.class);
    }

    public HashMap<String, String> getNoItemsByCodigoEntidadAndIdProcesoAdq(String codigoEntidad, DetalleProcesoAdq detProcesoAdq, String niveles, boolean isUniforme) {
        String noItemSeparados = "";
        String noItems = "";
        String idNivelesCe = "";
        String sql;
        HashMap<String, String> mapItems = new HashMap();
        List lst;
        BigDecimal mas;
        BigDecimal fem;

        if (isUniforme) {
            sql = "select id_nivel, sum(masculino) mas, sum(femenimo) fem\n"
                    + "from (select case when id_nivel_educativo = 1 then 1 when id_nivel_educativo in (3,4,5) then 2 when id_nivel_educativo = 6 then 6 end id_nivel,\n"
                    + "        masculino, femenimo\n"
                    + "    from estadistica_censo \n"
                    + "    where id_proceso_adq = ?1 and id_nivel_educativo in (1,3,4,5,6) and codigo_entidad = ?2 and (masculino <> 0 or femenimo <> 0))\n"
                    + "group by id_nivel";
        } else {
            sql = "select "
                    + "id_nivel_educativo, "
                    + "masculino, femenimo from estadistica_censo \n"
                    + "where id_proceso_adq = ?1 and \n"
                    + "    id_nivel_educativo in (" + niveles + ") and \n"
                    + "    codigo_entidad = ?2 and \n"
                    + "    (masculino <> 0 or femenimo <> 0)";
        }

        Query q = em.createNativeQuery(sql);
        q.setParameter(1, detProcesoAdq.getIdProcesoAdq().getId());
        q.setParameter(2, codigoEntidad);

        lst = q.getResultList();

        //item_8 = '8' and item_9 = '9'
        //'8','9'
        for (Object object : lst) {
            Object datos[] = (Object[]) object;

            mas = (BigDecimal) datos[1];
            fem = (BigDecimal) datos[2];

            switch (((BigDecimal) datos[0]).intValue()) {
                case 23:
                    idNivelesCe += idNivelesCe.isEmpty() ? "23" : ",23";
                    switch (detProcesoAdq.getIdRubroAdq().getId().intValue()) {
                        case 2:
                            if (fem.intValue() > 0 || mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_4_4 = '44'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'4.4'";
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case 24:
                    idNivelesCe += idNivelesCe.isEmpty() ? "24" : ",24";
                    switch (detProcesoAdq.getIdRubroAdq().getId().intValue()) {
                        case 2:
                            if (fem.intValue() > 0 || mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_5_1 = '51'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'5.1'";
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case 22:
                case 1: //PARVULARIA
                    idNivelesCe += idNivelesCe.isEmpty() ? "" + ((BigDecimal) datos[0]).intValue() : "," + ((BigDecimal) datos[0]).intValue() + "";
                    switch (detProcesoAdq.getIdRubroAdq().getId().intValue()) {
                        case 1:
                        case 4:
                        case 5://uniformes
                            if (fem.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_" + ((BigDecimal) datos[0]).intValue() + " = '" + ((BigDecimal) datos[0]).intValue() + "' and item_2 = '2'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'" + ((BigDecimal) datos[0]).intValue() + "','2'";
                            }
                            if (mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_3 = '3' and item_4 = '4'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'3','4'";
                            }
                            break;
                        case 2://utiles
                            //case 6://mascarillas
                            if (fem.intValue() > 0 || mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_" + ((BigDecimal) datos[0]).intValue() + " = '" + ((BigDecimal) datos[0]).intValue() + "'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'1'";
                            }
                            break;
                        case 3://zapatos
                            if (fem.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_" + ((BigDecimal) datos[0]).intValue() + " = '" + ((BigDecimal) datos[0]).intValue() + "'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'" + ((BigDecimal) datos[0]).intValue() + "'";
                            }
                            if (mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_2 = '2'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'2'";
                            }
                            break;
                    }

                    break;
                case 2: //BASICA - UNICAMENTE APLICA PARA UNIFORME
                    idNivelesCe += idNivelesCe.isEmpty() ? "2" : ",2";
                    if (fem.intValue() > 0) {
                        noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_6 = '6' and item_7 = '7'";
                        noItems += (noItems.isEmpty() ? "" : " , ") + "'6','7'";
                    }
                    if (mas.intValue() > 0) {
                        noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_8 = '8' and item_9 = '9'";
                        noItems += (noItems.isEmpty() ? "" : " , ") + "'8','9'";
                    }
                    break;
                case 3: //PRIMER CICLO
                    idNivelesCe += idNivelesCe.isEmpty() ? "3" : ",3";
                    switch (detProcesoAdq.getIdRubroAdq().getId().intValue()) {
                        case 2://utiles
                        case 6://mascarilla
                            if (fem.intValue() > 0 || mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_2 = '2'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'2'";
                            }
                            break;
                        case 3://zapatos
                            if (fem.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_3 = '3'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'3'";
                            }
                            if (mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_4 = '4'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'4'";
                            }
                            break;
                    }
                    break;
                case 4: //SEGUNDO CICLO
                    idNivelesCe += idNivelesCe.isEmpty() ? "4" : ",4";
                    switch (detProcesoAdq.getIdRubroAdq().getId().intValue()) {
                        case 2://utiles
                        case 6://mascarilla
                            if (fem.intValue() > 0 || mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_3 = '3'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'3'";
                            }
                            break;
                        case 3://zapatos
                            if (fem.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_5 = '5'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'5'";
                            }
                            if (mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_6 = '6'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'6'";
                            }
                            break;
                    }
                    break;
                case 5: //TERCER CICLO
                    idNivelesCe += idNivelesCe.isEmpty() ? "5" : ",5";
                    switch (detProcesoAdq.getIdRubroAdq().getId().intValue()) {
                        case 2://utiles
                        case 6://mascarilla
                            if (fem.intValue() > 0 || mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_4 = '4'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'4'";
                            }
                            break;
                        case 3://zapatos
                            if (fem.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_7 = '7'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'7'";
                            }
                            if (mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_8 = '8'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'8'";
                            }
                            break;
                    }
                    break;
                case 6: //MEDIA
                    idNivelesCe += idNivelesCe.isEmpty() ? "6" : ",6";
                    switch (detProcesoAdq.getIdRubroAdq().getId().intValue()) {
                        case 1:
                        case 4:
                        case 5://uniformes
                            if (fem.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_10 = '10' and item_11 = '11'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'10','11'";
                            }
                            if (mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_12 = '12' and item_13 = '13'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'13','13'";
                            }
                            break;
                        case 2://utiles
                        case 6://mascarilla
                            if (fem.intValue() > 0 || mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_5 = '5'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'5'";
                            }
                            break;
                        case 3://zapatos
                            if (fem.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_9 = '9'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'9'";
                            }
                            if (mas.intValue() > 0) {
                                noItemSeparados += (noItemSeparados.isEmpty() ? "" : " and ") + "item_10 = '10'";
                                noItems += (noItems.isEmpty() ? "" : " , ") + "'10'";
                            }
                            break;
                    }
                    break;
            }
        }

        mapItems.put("noItemSeparados", noItemSeparados);
        mapItems.put("noItems", noItems);
        mapItems.put("idNivelesCe", idNivelesCe);

        return mapItems;
    }

    public List<Long> getLstNivelesConMatriculaReportadaByIdProcesoAdqAndCodigoEntidad(Long idProcesoAdq, String codigoEntidad) {
        Query q = em.createQuery("SELECT DISTINCT e.idNivelEducativo.id FROM EstadisticaCenso e WHERE e.idProcesoAdq.id=:idProAdq AND e.codigoEntidad=:codEnt AND (e.masculino >0 OR e.femenimo > 0) order by e.idNivelEducativo.id");
        q.setParameter("idProAdq", idProcesoAdq);
        q.setParameter("codEnt", codigoEntidad);
        return q.getResultList();
    }

}
