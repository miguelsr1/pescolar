package sv.gob.mined.pescolar.web.proveedor.interno;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import sv.gob.mined.pescolar.model.CapaDistribucionAcre;
import sv.gob.mined.pescolar.model.CapaInstPorRubro;
import sv.gob.mined.pescolar.model.DisMunicipioIntere;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.dto.MunicipioDto;
import sv.gob.mined.pescolar.model.dto.OfertaGlobal;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.DisMunicipioInteresRepo;
import sv.gob.mined.pescolar.utils.Constantes;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.Reportes;
import sv.gob.mined.pescolar.web.SessionView;

/**
 *
 * @author misanchez
 */
@Named
@ViewScoped
public class MunicipioInteresView implements Serializable {

    private Boolean deshabiliar = false;

    private CapaInstPorRubro capacidadInst = new CapaInstPorRubro();
    private CapaDistribucionAcre departamentoCalif = new CapaDistribucionAcre();

    private List<MunicipioDto> lstMunSource = new ArrayList();
    private List<MunicipioDto> lstMunTarget = new ArrayList();
    private DualListModel<MunicipioDto> lstMunicipiosInteres = new DualListModel();

    @Inject
    private Reportes reportes;
    @Inject
    private SessionView sessionView;
    @Inject
    private CatalogoRepo catalogoRepo;
    @Inject
    private DisMunicipioInteresRepo disMuniRepo;
    @Inject
    private CargaGeneralView cargaGeneralView;

    @PostConstruct
    public void init() {
        if (cargaGeneralView.getEmpresa().getId() != null) {
            cargarMunInteres();
        }
    }

    public Boolean getDeshabiliar() {
        return deshabiliar;
    }

    public void setDeshabiliar(Boolean deshabiliar) {
        this.deshabiliar = deshabiliar;
    }

    public CargaGeneralView getCargaGeneralView() {
        return cargaGeneralView;
    }

    public void setCargaGeneralView(CargaGeneralView cargaGeneralView) {
        this.cargaGeneralView = cargaGeneralView;
    }

    public DualListModel<MunicipioDto> getLstMunicipiosInteres() {
        return lstMunicipiosInteres;
    }

    public void setLstMunicipiosInteres(DualListModel<MunicipioDto> lstMunicipiosInteres) {
        this.lstMunicipiosInteres = lstMunicipiosInteres;
    }

    public List<MunicipioDto> getLstMunSource() {
        return lstMunSource;
    }

    public void setLstMunSource(List<MunicipioDto> lstMunSource) {
        this.lstMunSource = lstMunSource;
    }

    public List<MunicipioDto> getLstMunTarget() {
        return lstMunTarget;
    }

    public void setLstMunTarget(List<MunicipioDto> lstMunTarget) {
        this.lstMunTarget = lstMunTarget;
    }

    public CapaDistribucionAcre getDepartamentoCalif() {
        return departamentoCalif;
    }

    public Municipio getMunicipioByConverter(Long id) {
        return catalogoRepo.findEntityByPk(Municipio.class, id);
    }

    public void onTransfer(TransferEvent event) {
        for (Object item : event.getItems()) {
            if (event.isAdd()) {
                DisMunicipioIntere disMunicipio = new DisMunicipioIntere();
                disMunicipio.setEstadoEliminacion(0l);
                disMunicipio.setFechaInsercion(LocalDate.now());
                disMunicipio.setIdCapaDistribucion(departamentoCalif);
                disMunicipio.setIdMunicipio(catalogoRepo.findEntityByPk(Municipio.class, ((MunicipioDto) item).getIdMunicipio()));
                disMunicipio.setUsuarioInsercion(sessionView.getUsuario().getIdPersona().getUsuario());

                disMuniRepo.save(disMunicipio);
            } else {
                disMuniRepo.remove(((MunicipioDto) item).getId(), departamentoCalif.getId());
            }
        }
    }

    public void guardarMunicipioInteres() {
        List<DisMunicipioIntere> lstMunicipioIntereses = disMuniRepo.findMunicipiosInteres(departamentoCalif);

        lstMunicipioIntereses.forEach((disMunicipioInteres) -> {
            disMunicipioInteres.setEstadoEliminacion(1l);
        });

        if (!(lstMunicipiosInteres.getSource().isEmpty() && lstMunicipiosInteres.getTarget().isEmpty())) {
            for (MunicipioDto mun : getLstMunicipiosInteres().getTarget()) {
                Boolean existe = false;
                for (DisMunicipioIntere disMunicipioInteres : lstMunicipioIntereses) {
                    if (disMunicipioInteres.getIdMunicipio().getId().compareTo(mun.getIdMunicipio()) == 0) {
                        disMunicipioInteres.setEstadoEliminacion(0l);
                        existe = true;
                        break;
                    }
                }

                if (!existe) {
                    DisMunicipioIntere disMunicipio = new DisMunicipioIntere();
                    disMunicipio.setEstadoEliminacion(0l);
                    disMunicipio.setFechaInsercion(LocalDate.now());
                    disMunicipio.setIdCapaDistribucion(departamentoCalif);
                    disMunicipio.setIdMunicipio(catalogoRepo.findEntityByPk(Municipio.class, mun.getIdMunicipio()));
                    disMunicipio.setUsuarioInsercion(sessionView.getUsuario().getIdPersona().getUsuario());
                    lstMunicipioIntereses.add(disMunicipio);
                }
            }

            try {
                for (DisMunicipioIntere disMunicipioInteres : lstMunicipioIntereses) {
                    if (disMunicipioInteres.getId() == null) {
                        disMuniRepo.save(disMunicipioInteres);
                    }
                }
                JsfUtil.mensajeUpdate();

            } catch (Exception e) {
                Logger.getLogger(MunicipioInteresView.class
                        .getName()).log(Level.SEVERE, "Error registrando municipios de interes", e);
                JsfUtil.mensajeError("Ha ocurrido un error en el registro de los datos.<br/>Reportar por favor al adminsitrador del sistema");
            }
        }
    }

    public void impOfertaGlobal() {
        try {
            String idGestion = "";
            String lugar = cargaGeneralView.getEmpresa().getIdMunicipio().getNombreMunicipio().concat(", ").concat(cargaGeneralView.getEmpresa().getIdMunicipio().getCodigoDepartamento().getNombreDepartamento());

            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            HashMap param = new HashMap();
            param.put("ubicacionImagenes", ctx.getRealPath(Reportes.PATH_IMAGENES) + File.separator);
            param.put("pEscudo", ctx.getRealPath(Reportes.PATH_IMAGENES) + File.separator);
            param.put("usuarioInsercion", sessionView.getUsuario().getIdPersona().getUsuario());
            param.put("pLugar", lugar);
            param.put("pRubro", JsfUtil.getNombreRubroById(capacidadInst.getIdMuestraInteres().getIdRubroInteres().getId()));
            param.put("pIdRubro", capacidadInst.getIdMuestraInteres().getIdRubroInteres().getId().intValue());
            param.put("pCorreoPersona", capacidadInst.getIdMuestraInteres().getIdEmpresa().getIdPersona().getEmail());
            param.put("pIdGestion", idGestion);

            List<OfertaGlobal> lstDatos = reportes.getLstOfertaGlobal(cargaGeneralView.getDetRubroMuestraInteres());
            lstDatos.get(0).setRubro(JsfUtil.getNombreRubroById(capacidadInst.getIdMuestraInteres().getIdRubroInteres().getId()));
            if (lstDatos.get(0).getDepartamento().contains("TODO EL PAIS")) {
                param.put("productor", Boolean.TRUE);
            } else {
                param.put("productor", Boolean.FALSE);
            }

            List<JasperPrint> jasperPrintList = new ArrayList();

            jasperPrintList.add(JasperFillManager.fillReport(
                    reportes.getPathReporte("sv/gob/mined/apps/reportes/oferta" + File.separator + "rptOfertaGlobalProv" + cargaGeneralView.getProcesoAdquisicion().getIdAnho().getAnho() + ".jasper"),
                    param, new JRBeanCollectionDataSource(lstDatos)));

            String muni = sessionView.getNombreMunicipio();

            param.put("pLugar", cargaGeneralView.getEmpresa().getIdMunicipio().getCodigoDepartamento().getNombreDepartamento());

            if (cargaGeneralView.getEmpresa().getIdPersoneria().getId().intValue() == 1) {
                jasperPrintList.add(reportes.getReporteAImprimir("sv/gob/mined/apps/reportes/declaracion" + File.separator + "rptDeclaracionJurAceptacionPerProvNat" + cargaGeneralView.getProcesoAdquisicion().getIdAnho().getAnho(), param, new JRBeanCollectionDataSource(reportes.getDeclaracionJurada(cargaGeneralView.getEmpresa(), cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId(), cargaGeneralView.getProcesoAdquisicion().getIdAnho().getId(), muni))));

            } else {
                jasperPrintList.add(reportes.getReporteAImprimir("sv/gob/mined/apps/reportes/declaracion" + File.separator + "rptDeclaracionJurAceptacionPerProvJur" + cargaGeneralView.getProcesoAdquisicion().getIdAnho().getAnho(), param, new JRBeanCollectionDataSource(reportes.getDeclaracionJurada(cargaGeneralView.getEmpresa(), cargaGeneralView.getDetRubroMuestraInteres().getIdRubroInteres().getId(), cargaGeneralView.getProcesoAdquisicion().getIdAnho().getId(), muni))));

            }

            if (!jasperPrintList.isEmpty()) {
                reportes.generarReporte(jasperPrintList, "oferta_global_" + cargaGeneralView.getEmpresa().getNumeroNit());

            }
        } catch (JRException | IOException ex) {
            Logger.getLogger(MunicipioInteresView.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void filtroProveedores() {
        cargaGeneralView.setEmpresa(new Empresa());
        capacidadInst = new CapaInstPorRubro();

        lstMunSource.clear();
        lstMunTarget.clear();
        cargaGeneralView.setFileName("fotoProveedores/profile.png");

        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 500);
        options.put("contentWidth", 750);

        PrimeFaces.current().dialog().openDynamic(Constantes.DLG_BUSCAR_PROVEEDOR, options, null);
    }

    public void empSelecMuniInteres(SelectEvent event) {
        if (event.getObject() != null) {
            if (event.getObject() instanceof Empresa) {
                cargaGeneralView.setEmpresa((Empresa) event.getObject());
                sessionView.setVariableSession("idEmpresa", cargaGeneralView.getEmpresa().getId());
                cargaGeneralView.cargarDetalleCalificacion();
                cargarMunInteres();

            } else {
                Logger.getLogger(MunicipioInteresView.class
                        .getName()).log(Level.INFO, "No se pudo convertir el objeto a la clase Empresa{0}", event.getObject());
            }
        } else {
            JsfUtil.mensajeAlerta("Debe de seleccionar una empresa");
        }
    }

    private void cargarMunInteres() {
        cargarDetalleCalificacion();

        if (capacidadInst != null && capacidadInst.getId() != null) {
            if (departamentoCalif != null && departamentoCalif.getCodigoDepartamento() != null) {
                lstMunSource = catalogoRepo.getLstMunicipiosDisponiblesDeInteres(departamentoCalif.getId(), departamentoCalif.getCodigoDepartamento().getId());
                lstMunTarget = catalogoRepo.getLstMunicipiosDeInteres(departamentoCalif.getId());
                lstMunicipiosInteres = new DualListModel(lstMunSource, lstMunTarget);
            }
        }
    }

    private void cargarDetalleCalificacion() {
        departamentoCalif = cargaGeneralView.getCapaDistribucionAcre();
        capacidadInst = cargaGeneralView.getCapacidadInstPorRubro();

        if (departamentoCalif == null || departamentoCalif.getCodigoDepartamento() == null) {
            JsfUtil.mensajeAlerta("Este proveedor no posee departamento de calificación " + cargaGeneralView.getProcesoAdquisicion().getIdAnho().getAnho());
        } else {
            if (cargaGeneralView.getEmpresa().getIdPersona().getUrlImagen() == null) {
                cargaGeneralView.setFileName("fotoProveedores/profile.png");
            } else {
                cargaGeneralView.setFileName("fotoProveedores/" + cargaGeneralView.getEmpresa().getIdPersona().getUrlImagen());

            }
        }
    }

    @FacesConverter(forClass = MunicipioDto.class, value = "muniConverter")
    public static class MunicipioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MunicipioInteresView controller = (MunicipioInteresView) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "municipioInteresView");
            MunicipioDto mun = new MunicipioDto();
            try {
                BeanUtils.copyProperties(mun, controller.getMunicipioByConverter(getKey(value)));
            } catch (IllegalAccessException | InvocationTargetException ex) {
                Logger.getLogger(MunicipioInteresView.class.getName()).log(Level.SEVERE, null, ex);
            }

            return mun;
        }

        Long getKey(String value) {
            return Long.parseLong(value);
        }

        String getStringKey(Long value) {
            return value.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof MunicipioDto) {
                MunicipioDto o = (MunicipioDto) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Municipio.class.getName());
            }
        }
    }
}
