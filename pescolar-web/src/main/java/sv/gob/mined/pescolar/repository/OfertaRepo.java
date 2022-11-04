package sv.gob.mined.pescolar.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Query;
import sv.gob.mined.pescolar.model.DetalleOferta;
import sv.gob.mined.pescolar.model.DetalleProcesoAdq;
import sv.gob.mined.pescolar.model.OfertaBienesServicio;
import sv.gob.mined.pescolar.model.Participante;
import sv.gob.mined.pescolar.model.dto.contratacion.Bean;
import sv.gob.mined.pescolar.model.dto.contratacion.ReportPOIBean;
import sv.gob.mined.pescolar.model.dto.contratacion.VwCotizacion;
import sv.gob.mined.pescolar.utils.Constantes;

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
                    + "from (select case when id_nivel_educativo in (25,26,28,29,30) then 22 when id_nivel_educativo in (10,11,12,13,14,15,7,8,9) then 2 when id_nivel_educativo in (16,17,18) then 6 end id_nivel,\n"
                    + "        masculino, femenimo\n"
                    + "    from estadistica_censo \n"
                    + "    where id_proceso_adq = ?1 and codigo_entidad = ?2 and (masculino <> 0 or femenimo <> 0))\n"
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
                case 10:
                case 11:
                case 12:
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
                case 13:
                case 14:
                case 15:
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
                case 7:
                case 8:
                case 9:
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
                case 16: 
                case 17: 
                case 18: 
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
    
    public List<BigDecimal> getIdNivelesItem(String codigoEntidad, Long idProcesoAdq, boolean isUniforme){
        String sql = isUniforme?Constantes.QUERY_NIVEL_UNI:Constantes.QUERY_NIVEL_UTI_ZAP;
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, codigoEntidad);
        q.setParameter(2, idProcesoAdq);
        return q.getResultList();
    }

    public OfertaBienesServicio getOfertaByProcesoCodigoEntidad(String codigoEntidad, DetalleProcesoAdq proceso) {
        Query q = em.createQuery("SELECT o FROM OfertaBienesServicio o WHERE o.codigoEntidad.codigoEntidad=:codigoEntidad and o.idDetProcesoAdq=:proceso and o.estadoEliminacion = 0", OfertaBienesServicio.class);
        q.setParameter("codigoEntidad", codigoEntidad);
        q.setParameter("proceso", proceso);
        if (!q.getResultList().isEmpty()) {
            return (OfertaBienesServicio) q.getSingleResult();
        } else {
            return null;
        }
    }
    
    
    public List<VwCotizacion> getLstCotizacion(String municipio, String codigoEntidad, DetalleProcesoAdq idProceso, Participante participante) {
        List<VwCotizacion> lstCotizacion= new ArrayList();
        try {
            Query q = em.createNamedQuery("Contratacion.VwCotizacion", VwCotizacion.class);
            q.setParameter(1, codigoEntidad);
            q.setParameter(2, idProceso.getId());
            q.setParameter(3, participante.getId());

            lstCotizacion = q.getResultList();

            VwCotizacion v = lstCotizacion.get(0);

            v.setLstDetalleOferta(new ArrayList());
            v.setLstDetalleOfertaLibros(new ArrayList());
            v.setLugarFecha(municipio.concat(",").concat(v.getFechaApertura()));

            String nombreVista = "";

            switch (idProceso.getIdRubroAdq().getId().intValue()) {
                case 1:
                case 4:
                case 5:
                    nombreVista = "VW_COTIZACION_UNIFORME";
                    break;
                case 2:
                    nombreVista = "VW_COTIZACION_UTILES";
                    break;
                case 3:
                    nombreVista = "VW_COTIZACION_ZAPATOS";
                    break;
            }

            q = em.createNativeQuery("select distinct * FROM " + nombreVista + " WHERE id_empresa=?1 and codigo_entidad=?2 and id_proceso_estadistica=?3 and (id_proceso_precio=?4 or id_proceso_precio=?5) and num_alumno is not null order by to_number(no_item), id_nivel_educativo");
            q.setParameter(1, participante.getIdEmpresa().getId());
            q.setParameter(2, codigoEntidad);
            q.setParameter(3, idProceso.getIdProcesoAdq().getId());
            q.setParameter(4, idProceso.getIdProcesoAdq().getId());
            q.setParameter(5, idProceso.getIdProcesoAdq().getPadreIdProcesoAdq() == null ? null : idProceso.getIdProcesoAdq().getPadreIdProcesoAdq().getId());

            List lst2 = q.getResultList();

            lst2.forEach((object1) -> {
                Object[] datos1 = (Object[]) object1;
                DetalleOferta det = new DetalleOferta();
                det.setNoItem(datos1[9].toString());
                det.setConsolidadoEspTec(datos1[0].toString().concat(", ").concat(datos1[1].toString()));
                det.setCantidad(new BigInteger(datos1[2].toString()));
                det.setPrecioUnitario(new BigDecimal(datos1[3].toString()));

                if (det.getConsolidadoEspTec().contains("Libro")) {
                    v.getLstDetalleOfertaLibros().add(det);
                } else {
                    v.getLstDetalleOferta().add(det);
                }
            });
        } catch (Exception e) {
            Logger.getLogger(OfertaRepo.class.getName()).log(Level.WARNING, "Error en la generacion de la cotizacion {0} {1} {2}", new Object[]{codigoEntidad, participante, idProceso});
        }

        return lstCotizacion;
    }
    
    public List<Object> getDatosRptAnalisisEconomico(String codigoEntidad, DetalleProcesoAdq idDetProceso) {
        String query = "";
        ReportPOIBean reportPOIBean = null;
        List<Object> listadoAExportar = new LinkedList<>();
        try {
            Connection conn = em.unwrap(java.sql.Connection.class);

            switch (idDetProceso.getIdRubroAdq().getId().intValue()) {
                case 1:
                case 4:
                case 5:
                    String idDetProcesos = getIdDetalleProcesoPadre(idDetProceso);

                    query = String.format(Constantes.QUERY_CONTRATACION_ANALISIS_ECONOMICO_UNIFORME, idDetProcesos, codigoEntidad, idDetProceso.getId());
                    break;
                case 2:
                    query = String.format(Constantes.QUERY_CONTRATACION_ANALISIS_ECONOMICO_UTILES, codigoEntidad, idDetProceso.getId());
                    break;
                case 3:
                    query = String.format(Constantes.QUERY_CONTRATACION_ANALISIS_ECONOMICO_ZAPATOS, codigoEntidad, idDetProceso.getId());
                    break;
            }
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet rs = stmt.executeQuery(query);

            LinkedHashMap<String, Integer> mapaItems = new LinkedHashMap<>();
            LinkedHashMap<String, String> mapaItemsIndex = new LinkedHashMap<>();
            LinkedHashMap<String, Integer> mapaRazonSocial = new LinkedHashMap<>();
            LinkedHashMap<String, Integer> mapaCantidades = new LinkedHashMap<>();

            Integer itemIndex = 0;
            Integer razonSocialIndex = 0;
            while (rs.next()) {
                if (!mapaItems.containsKey(rs.getString("descripcion_item"))) {
                    mapaItems.put(rs.getString("descripcion_item"), itemIndex);
                    mapaItemsIndex.put(rs.getString("descripcion_item"), rs.getString("no_item"));
                    mapaCantidades.put(rs.getString("descripcion_item"), rs.getInt("num_alumno"));
                    itemIndex++;
                }
                if (!mapaRazonSocial.containsKey(rs.getString("razon_social"))) {
                    mapaRazonSocial.put(rs.getString("razon_social"), razonSocialIndex);
                    razonSocialIndex++;
                }
            }
            Bean[][] datos = new Bean[mapaItems.size()][mapaRazonSocial.size()];
            rs.beforeFirst();
            while (rs.next()) {
                int x = mapaItems.get(rs.getString("descripcion_item"));
                int y = mapaRazonSocial.get(rs.getString("razon_social"));
                Bean bean = new Bean();
                bean.setCantidadOfertada(rs.getInt("num_alumno"));
                bean.setPrecioUnitario(rs.getDouble("precio_referencia"));
                bean.setCantidadAdjudicada(rs.getInt("adjudicada"));
                datos[x][y] = bean;
            }
            listadoAExportar.add(mapaItems);
            listadoAExportar.add(mapaRazonSocial);
            listadoAExportar.add(datos);
            listadoAExportar.add(mapaItemsIndex);
            listadoAExportar.add(mapaCantidades);

            listadoAExportar.add(reportPOIBean);

            return listadoAExportar;
        } catch (SQLException ex) {
            Logger.getLogger(OfertaResguardoRepo.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listadoAExportar;
    }

    private String getIdDetalleProcesoPadre(DetalleProcesoAdq idDetProceso) {
        String ids = "";
        Query q = em.createNativeQuery("select det.id_det_proceso_adq \n"
                + "from detalle_proceso_Adq det\n"
                + "    inner join proceso_adquisicion pro on pro.id_proceso_adq = det.id_proceso_adq\n"
                + "    inner join anho     on pro.id_anho = anho.id_anho\n"
                + "where anho.anho = ?1 and det.id_rubro_adq = ?2");
        q.setParameter(1, idDetProceso.getIdProcesoAdq().getIdAnho().getAnho());
        q.setParameter(2, idDetProceso.getIdRubroAdq().getId());
        List<Integer> lst = q.getResultList();
        for (Integer id : lst) {
            if (ids.isEmpty()) {
                ids = id.toString();
            } else {
                ids = ids + "," + id.toString();
            }
        }

        return ids;
    }
}
