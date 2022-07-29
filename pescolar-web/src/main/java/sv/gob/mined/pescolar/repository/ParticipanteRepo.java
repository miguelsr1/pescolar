package sv.gob.mined.pescolar.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.DetalleProcesoAdq;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.Participante;
import sv.gob.mined.pescolar.model.PorcentajeEvaluacion;
import sv.gob.mined.pescolar.model.dto.contratacion.PrecioReferenciaEmpresaDto;
import sv.gob.mined.pescolar.model.dto.contratacion.ProveedorDisponibleDto;
import sv.gob.mined.pescolar.utils.db.Filtro;
import sv.gob.mined.pescolar.utils.enums.TipoOperador;

/**
 *
 * @author misanchez
 */
@Stateless
public class ParticipanteRepo extends AbstractRepository<Participante, Long> {

    @Inject
    private CatalogoRepo catalogoRepo;

    public ParticipanteRepo() {
        super(Participante.class);
    }

    public List<Participante> findParticipatesByCodEndByIdAnho(String codigoEntidad, Long idAnho) {
        Query q = em.createQuery("SELECT p FROM Participante p WHERE p.idOferta.codigoEntidad.codigoEntidad=:codEnt and p.idOferta.idDetProcesoAdq.idProcesoAdq.idAnho.id=:idAnho");
        q.setParameter("codEnt", codigoEntidad);
        q.setParameter("idAnho", idAnho);

        return q.getResultList();
    }

    public List<Participante> findParticipantesByParam(String codigoDepartamento, Long idMunicipio, String codigoEntidad, String nombreCe, Long idRubro) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Participante> cr = cb.createQuery(Participante.class);
        Root<Participante> root = cr.from(Participante.class);
        List<Predicate> lstCondiciones = new ArrayList();

        if (codigoDepartamento != null) {
            lstCondiciones.add(cb.equal(root.get("idOferta").get("codigoEntidad").get("codigoDepartamento"), codigoDepartamento));
        }
        if (idMunicipio != null) {
            lstCondiciones.add(cb.equal(root.get("idOferta").get("codigoEntidad").get("idMunicipio").get("id"), idMunicipio));
        }
        if (idRubro != null) {
            lstCondiciones.add(cb.equal(root.get("idOferta").get("idDetProcesoAdq").get("idRubroAdq").get("id"), idRubro));
        }
        if (codigoEntidad != null && !codigoEntidad.isEmpty()) {
            lstCondiciones.add(cb.like(root.get("idOferta").get("codigoEntidad").get("codigoEntidad"), "%" + codigoEntidad + "%"));
        }
        if (nombreCe != null && !nombreCe.isEmpty()) {
            lstCondiciones.add(cb.like(root.get("idOferta").get("codigoEntidad").get("nombre"), "%" + nombreCe.toUpperCase() + "%"));
        }

        lstCondiciones.add(cb.equal(root.get("idOferta").get("idDetProcesoAdq").get("idProcesoAdq").get("idAnho").get("id"), 10l));

        cr.select(root).where(lstCondiciones.toArray(Predicate[]::new));

        Query query = em.createQuery(cr);

        return query.getResultList();
    }

    @Transactional
    public List<ProveedorDisponibleDto> findLstProveedoresDisponibles(DetalleProcesoAdq detProcesoAdq,
            String codigoEntidad, String codDepartamento, String codMunicipio, String codCanton,
            Long idMunicipio, String idMunicipios, Boolean municipioIgual, Long cantidad, HashMap<String, String> mapItems) {

        Query q = em.createNativeQuery(getQueryLstIdEmpresa(codDepartamento, codMunicipio, codCanton, idMunicipio, idMunicipios, detProcesoAdq.getIdRubroAdq().getId(), detProcesoAdq,
                municipioIgual, cantidad, mapItems.get("noItemSeparados"), mapItems.get("noItems")), ProveedorDisponibleDto.class);
        return q.getResultList();
    }

    private String getQueryLstIdEmpresa(String codDep, String codMun, String codCanton, Long idMunicipio, String idMunicipios, Long idRubro, DetalleProcesoAdq idDetProcesoAdq,
            Boolean municipioIgual, Long cantidad, String noItemSeparados, String noItems) {
        Long idAnho = idDetProcesoAdq.getIdProcesoAdq().getIdAnho().getId();

        List<PorcentajeEvaluacion> lstPorcentajes = getPorcentajesEvaluacionByAnho(idAnho, idRubro);
        BigDecimal porPrecio = BigDecimal.ZERO;
        BigDecimal porUbicacionLocal = BigDecimal.ZERO;
        BigDecimal porMunAledanhos = BigDecimal.ZERO;
        BigDecimal porUbicacionOtros = BigDecimal.ZERO;

        BigDecimal porCapacidadCompleta = BigDecimal.ZERO;
        BigDecimal porCapacidadParcial = BigDecimal.ZERO;

        for (PorcentajeEvaluacion lstPorcentaje : lstPorcentajes) {
            switch (idRubro.intValue()) {
                case 4:
                case 5:
                    break;
                case 2:
                    switch (lstPorcentaje.getIdCriterio().intValue()) {
                        case 1:
                            porPrecio = lstPorcentaje.getPorcentaje();
                        case 3:
                            porUbicacionLocal = lstPorcentaje.getPorcentaje();
                            break;
                        case 4:
                            porMunAledanhos = lstPorcentaje.getPorcentaje();
                            break;
                        case 5:
                            porUbicacionOtros = lstPorcentaje.getPorcentaje();
                            break;
                        case 7:
                            porCapacidadCompleta = lstPorcentaje.getPorcentaje();
                            break;
                        case 8:
                            porCapacidadParcial = lstPorcentaje.getPorcentaje();
                            break;
                    }
                    break;
                case 3:
                    switch (lstPorcentaje.getIdCriterio().intValue()) {
                        case 1:
                            porPrecio = lstPorcentaje.getPorcentaje();
                        case 3:
                            porUbicacionLocal = lstPorcentaje.getPorcentaje();
                            break;
                        case 4:
                            porUbicacionOtros = lstPorcentaje.getPorcentaje();
                            break;
                        case 6:
                            porCapacidadCompleta = lstPorcentaje.getPorcentaje();
                            break;
                        case 7:
                            porCapacidadParcial = lstPorcentaje.getPorcentaje();
                            break;
                    }
                    break;
            }
        }

        String sql = "select \n"
                + "    rownum                  as idRow,\n"
                + "    tb1.id_empresa          as idEmpresa,\n"
                + "    razon_social            as razonSocial,\n"
                + "    distribuidor,\n"
                + "    capacidad_acreditada    as capacidadAcreditada,\n"
                + "    capacidad_adjudicada    as capacidadAdjudicada,\n"
                + "    nombre_municipio        as nombreMunicipio,\n"
                + "    nombre_departamento     as nombreDepartamento,\n"
                + "    precio_promedio         as puAvg,\n"
                + "    porcentaje_precio       as porcentajePrecio,\n"
                + "    porcentaje_geo          as porcentajeGeo,\n"
                + "    porcentaje_capacidad    as porcentajeCapacidad,\n"
                + "    porcentaje_capacidad_i  as porcentajeCapacidadItem,\n"
                + "    porcentaje_precio+porcentaje_geo+porcentaje_capacidad_i+porcentaje_capacidad" + ((idAnho.intValue() == 10 && idRubro == 3) ? "+porcentaje_nota" : "") + " as porcentajeEvaluacion,\n"
                + "    0                       as porcentajeCalificacion,\n"
                + "    ((capacidad_adjudicada*100)/capacidad_acreditada) porcentajeAdjudicacion,\n"
                + ((idAnho.intValue() == 10 && idRubro == 3) ? " porcentaje_nota as porcentajeNota\n" : " 0 as porcentajeNota\n")
                + "from (select \n"
                + "        emp.id_empresa,\n"
                + "        det.id_muestra_interes,\n"
                + "        emp.razon_social,\n"
                + "        nvl(emp.distribuidor,0)      as distribuidor,\n"
                + "        mun_e.nombre_municipio,\n"
                + "        dep_e.nombre_departamento,\n"
                + "        tbl.precio_promedio,\n"
                + "        round((((min( distinct tbl.precio_promedio) OVER (order by tbl.precio_promedio))*100)/tbl.precio_promedio)*" + (idAnho.intValue() > 9 ? "0.4" : (porPrecio.intValue() / 100)) + ",2) as porcentaje_precio,\n"
                + "        mun_e.id_municipio,\n"
                + "        mun_e.codigo_departamento,\n"
                + "        emp.codigo_canton,\n"
                + "        " + (idAnho.intValue() > 8 ? "0" : "tbl.porcentaje_capacidad") + " as porcentaje_capacidad_i,\n"
                + "        " + getParteSelectUbicacion(idRubro, codCanton, idMunicipio, codDep, idMunicipios, porUbicacionLocal, porMunAledanhos, porUbicacionOtros) + "\n"
                + ((idAnho.intValue() == 10 && idRubro == 3) ? " ,(nota.nota_zapato_nino+nota.nota_zapato_nina) porcentaje_nota " : "")
                + "    from det_rubro_muestra_interes det\n"
                + "        inner join empresa emp                  on emp.id_empresa = det.id_empresa\n"
                + ((idAnho.intValue() == 10 && idRubro == 3) ? " inner join nota_pruebas_zapatero nota on nota.id_muestra_interes = det.id_muestra_interes " : "")
                + "        inner join municipio mun_e              on mun_e.id_municipio = emp.id_municipio\n"
                + "        inner join departamento dep_e           on mun_e.codigo_departamento = dep_e.codigo_departamento\n"
                + "        inner join (select pre.id_muestra_interes,pre.id_empresa, round(avg(precio_referencia),3) precio_promedio,((count(pre.id_empresa)*100)/" + noItems.split(",").length + ")*" + getPorcentajePorItems(idRubro) + " porcentaje_capacidad\n"
                + "                    from precios_ref_rubro_emp pre\n"
                + "                        inner join empresa_no_item emp on emp.id_muestra_interes = pre.id_muestra_interes\n"
                + "                        inner join det_rubro_muestra_interes det on emp.id_muestra_interes = det.id_muestra_interes\n"
                + "                    where " + (municipioIgual ? " (" + noItemSeparados + ") and " : "") + "\n"
                + "                        no_item in (" + noItems + ") and pre.estado_eliminacion = 0 and\n"
                + "                        det.id_rubro_interes =  " + idRubro + " and det.id_anho = " + idAnho + " and pre.precio_referencia > 0 \n"
                + "                    group by pre.id_muestra_interes, pre.id_empresa) tbl on det.id_muestra_interes = tbl.id_muestra_interes\n"
                + "    where \n"
                + "        det.estado_eliminacion = 0) tb1\n"
                + "    inner join (select \n"
                + "                    cip.id_muestra_interes,\n"
                + "                    cip.capacidad_acreditada,\n"
                + "                    cip.capacidad_adjudicada,\n"
                + "                    case when (cip.capacidad_acreditada-cip.capacidad_adjudicada) >= " + cantidad + " then " + (idRubro == 4 ? "12.50" : (idRubro == 5 ? "12.50" : porCapacidadCompleta.toString())) + " \n"
                + "                    else (((cip.capacidad_acreditada-cip.capacidad_adjudicada)*100)/" + cantidad + ") * " + (idRubro == 4 ? "0.1250" : (idRubro == 5 ? "0.1250" : porCapacidadParcial.divide(BigDecimal.valueOf(100)).toString())) + " \n"
                + "                    end porcentaje_capacidad\n"
                + "                from \n"
                + "                    capa_inst_por_rubro cip\n"
                + "                    inner join capa_distribucion_acre cda on cda.id_muestra_interes = cip.id_muestra_interes and cda.id_capa_distribucion in (select id_capa_distribucion from dis_municipio_interes dis inner join municipio mun on mun.id_municipio = dis.id_municipio where dis.estado_eliminacion = 0 and dis.id_municipio = " + idMunicipio + " and  dis.id_capa_distribucion = cda.id_capa_distribucion and " + (municipioIgual ? "mun.codigo_municipio ='" + codMun + "' and mun.codigo_departamento = '" + codDep + "'" : " mun.id_municipio in (" + (idMunicipios.isEmpty() ? idMunicipio : idMunicipios + "," + idMunicipio) + ")") + ")\n"
                + "                    inner join dis_municipio_interes mun on mun.id_capa_distribucion = cda.id_capa_distribucion and mun.id_municipio =  " + idMunicipio + "\n"
                + "                where \n"
                + "                    cip.capacidad_acreditada>0 and cip.id_proceso_adq = " + idDetProcesoAdq.getIdProcesoAdq().getId() + ") tb2 on tb1.id_muestra_interes = tb2.id_muestra_interes\n"
                + "where \n"
                + "    id_municipio " + (municipioIgual ? "=" : "<>") + " (select id_municipio from municipio where codigo_municipio = '" + codMun + "' and codigo_departamento = '" + codDep + "') \n"
                + "    " + (idRubro == 2 ? " and codigo_departamento = '" + codDep + "' " : "")
                + "order by\n"
                + "    (porcentaje_precio+porcentaje_geo+porcentaje_capacidad_i+porcentaje_capacidad" + ((idAnho.intValue() == 10 && idRubro == 3) ? "+porcentaje_nota" : "") + ") desc,((capacidad_adjudicada*100)/capacidad_acreditada) asc";

        return sql;
    }

    private List<PorcentajeEvaluacion> getPorcentajesEvaluacionByAnho(Long idAnho, Long idRubro) {
        List<Filtro> params = new ArrayList();
        params.add(new Filtro(TipoOperador.EQUALS, "idAnho", idAnho));
        params.add(new Filtro(TipoOperador.EQUALS, "idRubroInteres", idRubro));

        return (List<PorcentajeEvaluacion>) catalogoRepo.findListByParam(PorcentajeEvaluacion.class, params);
    }

    private String getPorcentajePorItems(Long idRubro) {
        switch (idRubro.intValue()) {
            case 1:
            case 4:
            case 5:
                return "0.125 ";
            default:
                return "0.175 ";
        }
    }

    private String getParteSelectUbicacion(Long idRubro, String codigoCanton, Long idMunicipio, String codigosDepartamento, String idMunicipios,
            BigDecimal porUbicacionLocal, BigDecimal porMunAledanhos, BigDecimal porUbicacionOtros) {
        switch (idRubro.intValue()) {
            case 1:
            case 4:
            case 5:
                if (codigoCanton != null && !codigoCanton.isEmpty()) {
                    return " case when mun_e.id_municipio = " + idMunicipio + " and nvl(emp.codigo_canton,'00') = '" + codigoCanton + "' then 35.00 when mun_e.id_municipio = " + idMunicipio + " and nvl(emp.codigo_canton,'00') != '" + codigoCanton + "' then 26.25 when mun_e.id_municipio in (" + idMunicipios + ") then 17.50 else 8.75 end porcentaje_geo ";
                } else {
                    return " case when mun_e.id_municipio = " + idMunicipio + " then 35.00 when mun_e.id_municipio in (" + idMunicipios + ") then 23.00 else 12.00 end porcentaje_geo ";
                }
            case 2:
                return " case when mun_e.id_municipio = " + idMunicipio + " then " + porUbicacionLocal.toString() + " when mun_e.id_municipio in (" + idMunicipios + ") then " + porMunAledanhos.toString() + " else " + porUbicacionOtros.toString() + " end porcentaje_geo";
            default:
                return " case when mun_e.codigo_departamento in (" + codigosDepartamento + ") then " + porUbicacionLocal.toString() + " else " + porUbicacionOtros.toString() + " end porcentaje_geo";
        }
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

    public Boolean isDepaCalificado(Empresa empresa, String codigoDepartamento, DetalleProcesoAdq detalleProceso) {
        Boolean valor = false;
        Query q;
        q = em.createQuery("SELECT c.codigoDepartamento FROM CapaDistribucionAcre c WHERE c.idMuestraInteres.idEmpresa=:idEmpresa and (c.idMuestraInteres.idRubroInteres.idRubroInteres=:pIdRubro and c.idMuestraInteres.idAnho.idAnho=:pIdAnho) and c.estadoEliminacion=0");
        q.setParameter("idEmpresa", empresa);
        q.setParameter("pIdRubro", detalleProceso.getIdRubroAdq().getId());
        q.setParameter("pIdAnho", detalleProceso.getIdProcesoAdq().getIdAnho().getId());
        /*if (detalleProceso.getIdRubroAdq().getDescripcionRubro().contains("2do")) {
            q.setParameter("idRubro", new BigDecimal(4));
        } else {
            q.setParameter("idRubro", detalleProceso.getIdRubroAdq().getIdRubroInteres());
        }*/

        List lst = q.getResultList();
        if (!lst.isEmpty()) {
            Departamento depaCalificado = (Departamento) lst.get(0);
            if (!depaCalificado.getId().equals("00")) {
                valor = depaCalificado.getId().equals(codigoDepartamento);
            } else {
                valor = depaCalificado.getId().equals("00");
            }
        }
        return valor;
    }
}
