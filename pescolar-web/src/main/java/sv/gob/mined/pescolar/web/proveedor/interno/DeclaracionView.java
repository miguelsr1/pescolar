/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.web.proveedor.interno;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import sv.gob.mined.pescolar.model.Anho;
import sv.gob.mined.pescolar.model.Canton;
import sv.gob.mined.pescolar.model.CapaInstPorRubro;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.PreciosRefRubroEmp;
import sv.gob.mined.pescolar.model.dto.NotificacionOfertaProvDto;
import sv.gob.mined.pescolar.model.dto.OfertaGlobal;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.EmpresaRepo;
import sv.gob.mined.pescolar.repository.MailRepo;
import sv.gob.mined.pescolar.repository.ParticipanteRepo;
import sv.gob.mined.pescolar.utils.Constantes;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.Reportes;
import sv.gob.mined.pescolar.web.SessionView;

/**
 *
 * @author MISanchez
 */
@Named
@ViewScoped
public class DeclaracionView implements Serializable {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("Bundle");

    private Boolean isUniforme = false;
    private Boolean aceptarCondiciones = false;
    private Boolean aceptarDeclaracion = false;
    private String idGestion = "";
    private String preCabecera;
    private String cabecera;
    private String detalle;
    private Anho anho;
    private Empresa empresa = new Empresa();
    //private DetalleProcesoAdq detalleProcesoAdq = new DetalleProcesoAdq();
    private CapaInstPorRubro capacidadInst = new CapaInstPorRubro();

    @Inject
    private EmpresaRepo empresaRepo;
    @Inject
    private ParticipanteRepo participanteRepo;
    /*@Inject
    private ReportesRepo reporteRepo;*/
    @Inject
    private CargaGeneralView cargaGeneralView;
    @Inject
    private SessionView sessionView;
    @Inject
    private Reportes reportes;
    @Inject
    private MailRepo mailRepo;
    @Inject
    private CatalogoRepo catalogoRepo;

    private static final ResourceBundle UTIL_CORREO = ResourceBundle.getBundle("Bundle");

    @PostConstruct
    public void ini() {
        if (cargaGeneralView.getEmpresa() != null && cargaGeneralView.getEmpresa().getId() != null) {
            empresa = cargaGeneralView.getEmpresa();
            capacidadInst = cargaGeneralView.getCapacidadInstPorRubro();
            anho = cargaGeneralView.getProcesoAdquisicion().getIdAnho();
            isUniforme = (cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getIdRubroUniforme() == 1l);
            if (capacidadInst != null) {
                aceptarCondiciones = (capacidadInst.getIdMuestraInteres().getDatosVerificados());
                aceptarDeclaracion = (capacidadInst.getIdMuestraInteres().getAceptacionTerminos());

                preCabecera = MessageFormat.format(UTIL_CORREO.getString("prov.declaracion.precabecera"),
                        empresa.getRazonSocial(),
                        empresa.getIdPersona().getNumeroDui(),
                        capacidadInst.getIdMuestraInteres().getIdAnho().getAnho());
                cabecera = MessageFormat.format(UTIL_CORREO.getString("prov.declaracion.cabecera"),
                        capacidadInst.getIdMuestraInteres().getIdRubroInteres().getDescripcionRubro(),
                        capacidadInst.getIdMuestraInteres().getIdAnho().getAnho());
                detalle = MessageFormat.format(UTIL_CORREO.getString("prov.declaracion.detalle"),
                        capacidadInst.getIdMuestraInteres().getIdRubroInteres().getDescripcionRubro(),
                        empresa.getIdPersona().getEmail(),
                        empresa.getDireccionCompleta().concat(", ").concat(empresa.getIdMunicipio().getNombreMunicipio()).concat(", ").concat(empresa.getIdMunicipio().getCodigoDepartamento().getNombreDepartamento()),
                        capacidadInst.getIdMuestraInteres().getIdAnho().getAnho());
            }
        }

    }

    public String getPreCabecera() {
        return preCabecera;
    }

    public String getCabecera() {
        return cabecera;
    }

    public String getDetalle() {
        return detalle;
    }

    public Boolean getAceptarDeclaracion() {
        return aceptarDeclaracion;
    }

    public void setAceptarDeclaracion(Boolean aceptarDeclaracion) {
        this.aceptarDeclaracion = aceptarDeclaracion;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Boolean getAceptarCondiciones() {
        return aceptarCondiciones;
    }

    public void setAceptarCondiciones(Boolean aceptarCondiciones) {
        this.aceptarCondiciones = aceptarCondiciones;
    }

    public void guardarAceptarCondiciones() {
        if (aceptarCondiciones && aceptarDeclaracion) {
            Boolean todoBien = true;
            if (anho.getId().intValue() > 8) { // anho mayor de 2020
                //validación de ingreso de todos los item para el rubro de uniforme
                if (isUniforme) {
                    List<PreciosRefRubroEmp> lstPreciosReferencia = participanteRepo.findPreciosRefRubroEmpRubro(getEmpresa(),
                            cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId(), anho.getId());
                    if (lstPreciosReferencia.size() < 12) {
                        todoBien = false;
                    }
                }
            }

            if (todoBien) {
                idGestion = empresaRepo.datosConfirmados(capacidadInst.getIdMuestraInteres().getId(),
                        empresa.getId(),
                        sessionView.getUsuario().getIdPersona().getUsuario());
                Logger.getLogger(DeclaracionView.class.getName()).log(Level.INFO, idGestion);
                generarNotificacion();
            } else {
                JsfUtil.mensajeAlerta("No se puede confirmar su Oferta Global debido a que no ha ingresado los precios de referencias de todos los ITEMS disponibles.");
            }
        } else {
            JsfUtil.mensajeAlerta("Debe de aceptar la Declaració Jurada y Aceptación de Presentación de Oferta.");
        }
    }

    public void impOfertaGlobal() {
        try {
            String lugar = empresa.getIdMunicipio().getNombreMunicipio().concat(", ").concat(empresa.getIdMunicipio().getCodigoDepartamento().getNombreDepartamento());
            if (idGestion.isEmpty()) {
                idGestion = empresaRepo.datosConfirmados(capacidadInst.getIdMuestraInteres().getId(),
                        empresa.getId(),
                        sessionView.getUsuario().getIdPersona().getUsuario());
            }

            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            HashMap param = new HashMap();
            param.put("ubicacionImagenes", ctx.getRealPath(Constantes.PATH_IMAGENES) + File.separator);
            param.put("pEscudo", ctx.getRealPath(Constantes.PATH_IMAGENES) + File.separator);
            param.put("usuarioInsercion", sessionView.getUsuario().getIdPersona().getUsuario());
            param.put("pLugar", lugar);
            param.put("pRubro", JsfUtil.getNombreRubroById(capacidadInst.getIdMuestraInteres().getIdRubroInteres().getId()));
            param.put("pIdRubro", capacidadInst.getIdMuestraInteres().getIdRubroInteres().getId().intValue());
            param.put("pCorreoPersona", capacidadInst.getIdMuestraInteres().getIdEmpresa().getIdPersona().getEmail());
            param.put("pIdGestion", idGestion);

            List<OfertaGlobal> lstDatos = reportes.getLstOfertaGlobal(empresa.getNumeroNit(), cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId(),
                    anho.getId());
            lstDatos.get(0).setRubro(JsfUtil.getNombreRubroById(capacidadInst.getIdMuestraInteres().getIdRubroInteres().getId()));
            if (lstDatos.get(0).getDepartamento().contains("TODO EL PAIS")) {
                param.put("productor", Boolean.TRUE);
            } else {
                param.put("productor", Boolean.FALSE);
            }

            List<JasperPrint> jasperPrintList = new ArrayList();

            jasperPrintList.add(JasperFillManager.fillReport(
                    reportes.getPathReporte("sv/gob/mined/apps/reportes/oferta" + File.separator + "rptOfertaGlobalProv" + anho.getAnho() + ".jasper"),
                    param, new JRBeanCollectionDataSource(lstDatos)));

            String muni = sessionView.getNombreMunicipio();

            param.put("pLugar", empresa.getIdMunicipio().getCodigoDepartamento().getNombreDepartamento());

            if (empresa.getIdPersoneria().getId().intValue() == 1) {
                jasperPrintList.add(reportes.getReporteAImprimir("sv/gob/mined/apps/reportes/declaracion" + File.separator + "rptDeclaracionJurAceptacionPerProvNat" + anho.getAnho(), param, new JRBeanCollectionDataSource(reportes.getDeclaracionJurada(empresa, cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId(), anho.getId(), muni))));
            } else {
                jasperPrintList.add(reportes.getReporteAImprimir("sv/gob/mined/apps/reportes/declaracion" + File.separator + "rptDeclaracionJurAceptacionPerProvJur" + anho.getAnho(), param, new JRBeanCollectionDataSource(reportes.getDeclaracionJurada(empresa, cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId(), anho.getId(), muni))));
            }

            Reportes.generarReporte(jasperPrintList, "oferta_global_" + getEmpresa().getNumeroNit());

        } catch (IOException | JRException ex) {
            Logger.getLogger(DeclaracionView.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*private void enviarNotificacionModProv() {
        String titulo;
        String mensaje;
        Date fecha = new Date();
        List<String> to = new ArrayList();
        List<String> cc = new ArrayList();
        List<String> bcc = new ArrayList();

        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");

        cc.add("carlos.enrique.villegas@admin.mined.edu.sv");
        cc.add("rene.brizuela@mined.gob.sv");

        bcc.add(JsfUtil.getValorFromBundleByKey("cuenta1.name"));
        bcc.add("miguel.sanchez@mined.gob.sv");
        to.add(empresa.getIdPersona().getEmail());

        titulo = MessageFormat.format(UTIL_CORREO.getString("prov_notif_inscripcion.email.titulo"), capacidadInst.getIdMuestraInteres().getIdAnho().getAnho());
        mensaje = MessageFormat.format(UTIL_CORREO.getString("prov_notif_inscripcion.email.mensaje"),
                sdfHora.format(fecha).split(":")[0], sdfHora.format(fecha).split(":")[1],
                Herramientas.getNumDia(fecha), Herramientas.getNomMes(fecha), Herramientas.getNumAnyo(fecha),
                empresa.getIdPersona().getEmail(), empresa.getIdPersona().getNombreCompleto(), empresa.getIdPersona().getNumeroDui(), idGestion,
                sdfHora.format(fecha).split(":")[0], sdfHora.format(fecha).split(":")[1],
                Herramientas.getNumDia(fecha), Herramientas.getNomMes(fecha), Herramientas.getNumAnyo(fecha));

        mailRepo.enviarMail(titulo, mensaje, to, cc, bcc);
    }*/
    public void generarNotificacion() {
        StringBuilder sb = new StringBuilder();
        String nombreCanton = "";

        if (isUniforme) {
            for (Canton canton : catalogoRepo.getLstCantonByMunicipio(empresa.getIdMunicipio().getId())) {
                if (canton.getCodigoCanton().equals(empresa.getCodigoCanton()) && canton.getIdMunicipio().intValue() == empresa.getIdMunicipio().getId().intValue()) {
                    nombreCanton = canton.getNombreCanton();
                    break;
                }
            }
        }

        NotificacionOfertaProvDto nopd = participanteRepo.getNotificacionOfertaProv(empresa.getId(), anho.getId(), cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId());

        sb.append(preCabecera);

        sb.append(MessageFormat.format(RESOURCE_BUNDLE.getString("prov_notif_inscripcion_uniforme.email.mensaje"),
                nopd.getRazonSocial(),
                nopd.getNumeroNit(),
                nopd.getDescripcionRubro(),
                nopd.getPrograma(),
                isUniforme ? nombreCanton.concat(",").concat(nopd.getUbicacionPer()) : nopd.getUbicacionPer(),
                nopd.getDomicilio(),
                nopd.getTelefonoPer(),
                nopd.getDireccionCompleta(),
                nopd.getTelefonoLocal(),
                isUniforme ? "Cantón, Municipio y Departamento" : "Municipio y Departamento"));

        sb.append(RESOURCE_BUNDLE.getString("prov_notif_inscripcion_uniforme.email.mensaje.tbl_precios.header"));
        nopd.getLstDetItemOfertaGlobal().forEach((det) -> {
            sb.append(MessageFormat.format(RESOURCE_BUNDLE.getString("prov_notif_inscripcion_uniforme.email.mensaje.tbl_precios.detalle"), det.getDescripcionItem(), det.getPrecioMaxReferencia(), (det.getPrecioUnitario() == null ? BigDecimal.ZERO : det.getPrecioUnitario())));
        });
        sb.append(RESOURCE_BUNDLE.getString("prov_notif_inscripcion_uniforme.email.mensaje.tbl_precios.fin"));

        sb.append(MessageFormat.format(RESOURCE_BUNDLE.getString("prov_notif_inscripcion_uniforme.email.mensaje.middle"), cargaGeneralView.getCapaDistribucionAcre().getCodigoDepartamento().getNombreDepartamento()));

        sb.append(RESOURCE_BUNDLE.getString("prov_notif_inscripcion_uniforme.email.mensaje.tbl_municipio.header"));
        nopd.getLstMunIntOfertaGlobal().forEach((det) -> {
            sb.append(MessageFormat.format(RESOURCE_BUNDLE.getString("prov_notif_inscripcion_uniforme.email.mensaje.tbl_municipio.detalle"), det.getNombreDepartamento(), det.getNombreMunicipio()));
        });
        sb.append(RESOURCE_BUNDLE.getString("prov_notif_inscripcion_uniforme.email.mensaje.tbl_municipio.fin"));

        List<String> to = new ArrayList();
        List<String> cc = new ArrayList();
        List<String> bcc = new ArrayList();

        to.add(empresa.getIdPersona().getEmail());

        cc.add(sessionView.getUsuario().getIdPersona().getEmail());
        cc.add("rafael.jose.arias@admin.mined.edu.sv");
        cc.add("carlos.enrique.villegas@admin.mined.edu.sv");

        mailRepo.enviarMail("Notificación de Recepción de Oferta Global " + anho.getAnho(),
                sb.toString(),
                to,
                cc,
                bcc);
    }
}
