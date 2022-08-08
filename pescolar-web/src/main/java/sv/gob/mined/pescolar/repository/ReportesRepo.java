/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.repository;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import sv.gob.mined.pescolar.model.Anho;
import sv.gob.mined.pescolar.model.DisMunicipioIntere;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.PreciosRefRubro;
import sv.gob.mined.pescolar.model.PreciosRefRubroEmp;
import sv.gob.mined.pescolar.model.dto.DeclaracionJurada;
import sv.gob.mined.pescolar.model.dto.DetItemOfertaGlobal;
import sv.gob.mined.pescolar.model.dto.DetMunIntOfertaGlobal;
import sv.gob.mined.pescolar.model.dto.OfertaGlobal;

/**
 *
 * @author misanchez
 */
@Stateless
public class ReportesRepo {

    @PersistenceContext(unitName = "paquetePU")
    private EntityManager em;

    /**
     * Llenado de un reporte que tiene como fuente de datos una consulta SQL
     *
     * @param input
     * @param map
     * @return
     */
    public JasperPrint getRpt(HashMap map, InputStream input) {
        try {
            try (input) {
                Connection conn = em.unwrap(java.sql.Connection.class);
                return JasperFillManager.fillReport(input, map, conn);
            }
        } catch (JRException | IOException ex) {
            Logger.getLogger(ReportesRepo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Llenado de un reporte a partir de un DataSource del tipo BeanCollection
     *
     * @param input
     * @param map
     * @param lst
     * @return
     */
    public JasperPrint getRpt(HashMap map, InputStream input, List lst) {
        try {
            try (input) {
                JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lst);

                return JasperFillManager.fillReport(input, map, ds);
            }
        } catch (JRException | IOException ex) {
            Logger.getLogger(ReportesRepo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<OfertaGlobal> getLstOfertaGlobal(String nit, Long idRubro, Long idAnho) {

        List<OfertaGlobal> lstRpt;
        Anho anho = em.find(Anho.class, idAnho);
        Query q = em.createNamedQuery("DatosProveDto.ofertaGlobal", OfertaGlobal.class);
        q.setParameter(1, nit);
        q.setParameter(2, idRubro);
        q.setParameter(3, idAnho);

        lstRpt = q.getResultList();

        lstRpt.get(0).setAnho(anho.getAnho());
        lstRpt.get(0).setLstDetItemOfertaGlobal(getLstItemOfertaGlobal(nit, idRubro, idAnho));
        lstRpt.get(0).setLstMunIntOfertaGlobal(getLstMunIntOfertaGlobal(nit, idRubro, idAnho));

        return lstRpt;
    }

    public List<DetItemOfertaGlobal> getLstItemOfertaGlobal(String nit, Long idRubro, Long idAnho) {
        PreciosRefRubroEmp preTem;
        List<DetItemOfertaGlobal> lst = new ArrayList();
        Query q = em.createQuery("SELECT p FROM PreciosRefRubroEmp p WHERE p.idMuestraInteres.idEmpresa.numeroNit=:nit and p.idMuestraInteres.idRubroInteres.id=:pIdRubro AND p.idMuestraInteres.idAnho.id=:pIdAnho and p.idProducto.id not in (1) ORDER BY cast(p.noItem as integer)", PreciosRefRubroEmp.class);
        q.setParameter("nit", nit);
        q.setParameter("pIdRubro", idRubro);
        q.setParameter("pIdAnho", idAnho);
        List<PreciosRefRubroEmp> lstPrecios = q.getResultList();

        q = em.createNativeQuery("select prr.* from PRECIOS_REF_RUBRO prr inner join NIVEL_EDUCATIVO niv on niv.ID_NIVEL_EDUCATIVO = prr.ID_NIVEL_EDUCATIVO where prr.id_rubro_interes = ?1 and prr.id_anho = ?2 order by niv.ORDEN2", PreciosRefRubro.class);
        q.setParameter(1, idRubro);
        q.setParameter(2, idAnho);
        List<PreciosRefRubro> lstPrecioMax = q.getResultList();

        switch (idRubro.intValue()) {
            case 1:
            case 4:
                for (int i = 0; i < 13; i++) {
                    DetItemOfertaGlobal det = new DetItemOfertaGlobal();

                    switch ((i + 1)) {
                        case 1:
                            det.setDescripcionItem("Confección Blusa Parvularia");
                            det.setPrecioMaxReferencia(new BigDecimal("4.25"));
                            break;
                        case 2:
                            det.setDescripcionItem("Confección Falda Parvularia");
                            det.setPrecioMaxReferencia(new BigDecimal("4.25"));
                            break;
                        case 3:
                            det.setDescripcionItem("Confección Camisa Parvularia");
                            det.setPrecioMaxReferencia(new BigDecimal("4.25"));
                            break;
                        case 4:
                            det.setDescripcionItem("Confección Pantalón Corto Parvularia");
                            det.setPrecioMaxReferencia(new BigDecimal("4.00"));
                            break;
                        case 5:
                            det.setDescripcionItem("Confección Pantalón niño y niña de Parvularia zona con clima templado arriba de los 1000 metros sobre el nivel del mar");
                            det.setPrecioMaxReferencia(new BigDecimal("6.00"));
                            break;
                        case 6:
                            det.setDescripcionItem("Confección Blusa Básica (I, II y III ciclo)");
                            det.setPrecioMaxReferencia(new BigDecimal("4.50"));
                            break;
                        case 7:
                            det.setDescripcionItem("Confección Falda Básica (I, II y III ciclo)");
                            det.setPrecioMaxReferencia(new BigDecimal("4.50"));
                            break;
                        case 8:
                            det.setDescripcionItem("Confección Camisa Básica (I, II y III ciclo)");
                            det.setPrecioMaxReferencia(new BigDecimal("4.50"));
                            break;
                        case 9:
                            det.setDescripcionItem("Confección Pantalón Básica (I, II y III ciclo)");
                            det.setPrecioMaxReferencia(new BigDecimal("6.00"));
                            break;
                        case 10:
                            det.setDescripcionItem("Confección Blusa Bachillerato");
                            det.setPrecioMaxReferencia(new BigDecimal("4.50"));
                            break;
                        case 11:
                            det.setDescripcionItem("Confección Falda Bachillerato");
                            det.setPrecioMaxReferencia(new BigDecimal("4.50"));
                            break;
                        case 12:
                            det.setDescripcionItem("Confección Camisa Bachillerato");
                            det.setPrecioMaxReferencia(new BigDecimal("4.50"));
                            break;
                        case 13:
                            det.setDescripcionItem("Confección Pantalón Bachillerato");
                            det.setPrecioMaxReferencia(new BigDecimal("6.00"));
                            break;
                    }

                    for (PreciosRefRubroEmp preciosRefRubroEmp : lstPrecios) {
                        if ((i + 1) == Integer.parseInt(preciosRefRubroEmp.getNoItem())) {
                            det.setPrecioUnitario(preciosRefRubroEmp.getPrecioReferencia());
                            break;
                        }
                    }
                    lst.add(det);
                }
                break;
            case 2:
                for (PreciosRefRubroEmp preciosRefRubroEmp : lstPrecios) {
                    DetItemOfertaGlobal det = new DetItemOfertaGlobal();

                    switch (preciosRefRubroEmp.getNoItem()) {
                        case "1":
                            det.setDescripcionItem("Paquete de útiles escolares para inicial y parvularia");
                            det.setPrecioMaxReferencia(lstPrecioMax.get(0).getPrecioMaxMas());
                            break;
                        case "2":
                            det.setDescripcionItem("Paquete de útiles escolares primer ciclo");
                            det.setPrecioMaxReferencia(lstPrecioMax.get(1).getPrecioMaxMas());
                            break;
                        case "3":
                            det.setDescripcionItem("Paquete de útiles escolares segundo ciclo");
                            det.setPrecioMaxReferencia(lstPrecioMax.get(2).getPrecioMaxMas());
                            break;
                        case "4":
                            det.setDescripcionItem("Paquete de útiles escolares tercer ciclo");
                            det.setPrecioMaxReferencia(lstPrecioMax.get(3).getPrecioMaxMas());
                            break;
                        case "4.4":
                            det.setDescripcionItem("Paquete de útiles escolares modalidad flexible - tercer ciclo");
                            det.setPrecioMaxReferencia(lstPrecioMax.get(4).getPrecioMaxMas());
                            break;
                        case "5":
                            det.setDescripcionItem("Paquete de útiles escolares bachillerato");
                            det.setPrecioMaxReferencia(lstPrecioMax.get(5).getPrecioMaxMas());
                            break;
                        case "5.1":
                            det.setDescripcionItem("Paquete de útiles escolares modalidad flexible - bachillerato");
                            det.setPrecioMaxReferencia(lstPrecioMax.get(6).getPrecioMaxMas());
                            break;
                    }

                    preTem = getPrecioMax(lstPrecios, preciosRefRubroEmp.getNoItem());
                    if (preTem != null) {
                        det.setPrecioUnitario(preTem.getPrecioReferencia());
                    }
                    lst.add(det);
                }
                break;
            case 3:
                for (int i = 0; i < 10; i++) {
                    DetItemOfertaGlobal det = new DetItemOfertaGlobal();

                    switch ((i + 1)) {
                        case 1:
                            det.setDescripcionItem("Zapatos para niño, incial y parvularia");
                            det.setPrecioMaxReferencia(new BigDecimal("14.60"));
                            break;
                        case 2:
                            det.setDescripcionItem("Zapatos para niña, incial y parvularia");
                            det.setPrecioMaxReferencia(new BigDecimal("14.60"));
                            break;
                        case 3:
                            det.setDescripcionItem("Zapatos para niño, primer ciclo");
                            det.setPrecioMaxReferencia(new BigDecimal("14.60"));
                            break;
                        case 4:
                            det.setDescripcionItem("Zapatos para niña, primer ciclo");
                            det.setPrecioMaxReferencia(new BigDecimal("14.60"));
                            break;
                        case 5:
                            det.setDescripcionItem("Zapatos para niño, segundo ciclo");
                            det.setPrecioMaxReferencia(new BigDecimal("14.60"));
                            break;
                        case 6:
                            det.setDescripcionItem("Zapatos para niña, segundo ciclo");
                            det.setPrecioMaxReferencia(new BigDecimal("14.60"));
                            break;
                        case 7:
                            det.setDescripcionItem("Zapatos para niño, tercer ciclo");
                            det.setPrecioMaxReferencia(new BigDecimal("14.60"));
                            break;
                        case 8:
                            det.setDescripcionItem("Zapatos para niña, tercer ciclo");
                            det.setPrecioMaxReferencia(new BigDecimal("14.60"));
                            break;
                        case 9:
                            det.setDescripcionItem("Zapatos para niño, bachillerato");
                            det.setPrecioMaxReferencia(new BigDecimal("16.00"));
                            break;
                        case 10:
                            det.setDescripcionItem("Zapatos para niña, bachillerato");
                            det.setPrecioMaxReferencia(new BigDecimal("16.00"));
                            break;
                    }

                    for (PreciosRefRubroEmp preciosRefRubroEmp : lstPrecios) {
                        if ((i + 1) == Integer.parseInt(preciosRefRubroEmp.getNoItem())) {
                            det.setPrecioUnitario(preciosRefRubroEmp.getPrecioReferencia());
                            break;
                        }
                    }
                    lst.add(det);
                }
                break;
        }
        return lst;
    }

    public List<DetMunIntOfertaGlobal> getLstMunIntOfertaGlobal(String nit, Long idRubro, Long idAnho) {
        List<DetMunIntOfertaGlobal> lst = new ArrayList();
        Query q = em.createQuery("SELECT d FROM DisMunicipioIntere d WHERE d.idCapaDistribucion.idMuestraInteres.idEmpresa.numeroNit=:nit and d.idCapaDistribucion.idMuestraInteres.idRubroInteres.id=:pIdRubro and d.idCapaDistribucion.idMuestraInteres.idAnho.id=:pIdAnho ORDER BY d.idMunicipio.codigoDepartamento.id, d.idMunicipio.codigoMunicipio ASC", DisMunicipioIntere.class);
        q.setParameter("nit", nit);
        q.setParameter("pIdRubro", idRubro);
        q.setParameter("pIdAnho", idAnho);
        List<DisMunicipioIntere> lstMunicipios = q.getResultList();

        for (DisMunicipioIntere disMunicipioInteres : lstMunicipios) {
            DetMunIntOfertaGlobal detMun = new DetMunIntOfertaGlobal();
            detMun.setCodigoDepartamento(disMunicipioInteres.getIdMunicipio().getCodigoDepartamento().getId());
            detMun.setNombreDepartamento(disMunicipioInteres.getIdMunicipio().getCodigoDepartamento().getNombreDepartamento());
            detMun.setCodigoMunicipio(disMunicipioInteres.getIdMunicipio().getCodigoMunicipio());
            detMun.setNombreMunicipio(disMunicipioInteres.getIdMunicipio().getNombreMunicipio());

            lst.add(detMun);
        }
        return lst;
    }

    public PreciosRefRubroEmp getPrecioMax(List<PreciosRefRubroEmp> lstPrecioMax, final String noItem) {
        return lstPrecioMax.stream().parallel().filter(precio -> precio.getNoItem().equals(noItem)).findAny().orElse(new PreciosRefRubroEmp());

        /*return ((PreciosRefRubroEmp) CollectionUtils.find(lstPrecioMax, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return (((PreciosRefRubroEmp) o).getNoItem().equals(noItem));
            }
        }));*/
    }

    public List<DeclaracionJurada> getDeclaracionJurada(Empresa empresa, Long idRubro, Long idAnho, String ciudad) {
        Query q = em.createNamedQuery("Proveedor.DeclaracionJurada", DeclaracionJurada.class);
        q.setParameter(1, empresa.getNumeroNit());
        q.setParameter(2, idRubro);
        q.setParameter(3, idAnho);

        List<DeclaracionJurada> lstDeclaracion = q.getResultList();
        if (!lstDeclaracion.isEmpty()) {
            lstDeclaracion.get(0).setCiudad(ciudad);
            lstDeclaracion.get(0).setFecha(new Date());
        }
        return lstDeclaracion.isEmpty() ? new ArrayList() : lstDeclaracion;
    }

}
