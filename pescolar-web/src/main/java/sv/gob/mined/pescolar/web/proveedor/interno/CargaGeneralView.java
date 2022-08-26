package sv.gob.mined.pescolar.web.proveedor.interno;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.AlterableContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import sv.gob.mined.pescolar.model.CapaDistribucionAcre;
import sv.gob.mined.pescolar.model.CapaInstPorRubro;
import sv.gob.mined.pescolar.model.DetCapaSegunRubro;
import sv.gob.mined.pescolar.model.DetRubroMuestraIntere;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.ProcesoAdquisicion;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.utils.Constantes;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.web.SessionView;

/**
 *
 * @author misanchez
 */
@Named
@SessionScoped
public class CargaGeneralView /*extends RecuperarProcesoUtil*/ implements Serializable {

    private Boolean deshabiliar = false;
    private Boolean showFoto;

    private String urlStr;
    private String fileName;
    private String rubroProveedor;
    private String numeroNit;
    private String municipioDepartamento;
    private String domicilio;

    private Long idAnho;

    private Empresa empresa = new Empresa();
    private ProcesoAdquisicion proceso;
    private DetRubroMuestraIntere detRubroMuestraInteres;

    @Inject
    private CatalogoRepo proveedorEJB;
    @Inject
    private SessionView sessionView;
    @Inject
    private BeanManager beanManager;

    @PostConstruct
    public void ini() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        urlStr = req.getRequestURL().toString();
    }

    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String url) {
        this.urlStr = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getIdAnho() {
        return idAnho;
    }

    public CapaInstPorRubro getCapacidadInstPorRubro() {
        return detRubroMuestraInteres != null ? proveedorEJB.findDetProveedor(detRubroMuestraInteres, proceso.getId(), CapaInstPorRubro.class) : null;
    }

    public CapaDistribucionAcre getCapaDistribucionAcre() {
        return detRubroMuestraInteres != null ? proveedorEJB.findDetProveedor(detRubroMuestraInteres, proceso.getId(), CapaDistribucionAcre.class) : null;
    }

    public DetCapaSegunRubro getDetCapaSegunRubro() {
        return detRubroMuestraInteres != null ? proveedorEJB.findDetProveedor(detRubroMuestraInteres, proceso.getId(), DetCapaSegunRubro.class) : null;
    }

    public DetRubroMuestraIntere getDetRubroMuestraInteres() {
        return detRubroMuestraInteres;
    }

    public Boolean getDeshabiliar() {
        return deshabiliar;
    }

    public void setDeshabiliar(Boolean deshabiliar) {
        this.deshabiliar = deshabiliar;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Boolean getShowFoto() {
        return showFoto;
    }

    public void setShowFoto(Boolean showFoto) {
        this.showFoto = showFoto;
    }

    public String getNombreRubroProveedor() {
        return rubroProveedor;
    }

    public ProcesoAdquisicion getProcesoAdquisicion() {
        return sessionView.getProceso();
    }

    public String getNumeroNit() {
        return numeroNit;
    }

    public String getMunicipioDepartamento() {
        return municipioDepartamento;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void dlgFotografia() {
        if (getEmpresa() == null || getEmpresa().getId() == null) {
            JsfUtil.mensajeAlerta("Debe de seleccionar un empresa");
        } else {
            deshabiliar = true;
            Map<String, Object> options = new HashMap();
            options.put("modal", true);
            options.put("draggable", false);
            options.put("resizable", false);
            options.put("contentHeight", 400);
            options.put("contentWidth", 554);
            sessionView.setVariableSession("nitEmpresa", getEmpresa().getNumeroNit());
            PrimeFaces.current().dialog().openDynamic("/app/comunes/filtroFotoProveedor", options, null);
        }
    }

    public void cargarDetalleCalificacion() {
        this.proceso = sessionView.getProceso();
        idAnho = proceso.getIdAnho().getId();
        if (proceso.getId() == null) {
            JsfUtil.mensajeAlerta("Debe seleccionar un proceso de contratación");
        } else {
            if (getEmpresa().getIdPersoneria().getId().compareTo(Constantes.PERSONA_NATURAL) == 0) {
                numeroNit = getEmpresa().getIdPersona().getNumeroDui();
                municipioDepartamento = getEmpresa().getIdPersona().getIdMunicipio().getNombreMunicipio() + ", " + getEmpresa().getIdPersona().getIdMunicipio().getCodigoDepartamento().getNombreDepartamento();
                domicilio = getEmpresa().getIdPersona().getDomicilio();
            } else {
                numeroNit = getEmpresa().getNumeroNit();
                municipioDepartamento = getEmpresa().getIdMunicipio().getNombreMunicipio() + ", " + getEmpresa().getIdMunicipio().getCodigoDepartamento().getNombreDepartamento();
                domicilio = getEmpresa().getDireccionCompleta();
            }
            detRubroMuestraInteres = proveedorEJB.findDetRubroByAnhoAndRubro(idAnho, getEmpresa().getId());
            if (detRubroMuestraInteres == null) {
                JsfUtil.mensajeAlerta("No se han cargado los datos de este proveedor para el proceso de contratación del año " + proceso.getIdAnho().getAnho());
            } else {
                rubroProveedor = JsfUtil.getNombreRubroById(detRubroMuestraInteres.getIdRubroInteres().getId());
            }
        }
    }

    /**
     * Destruye el bean en sesión y redirecciona a la página de inicio
     *
     * @return
     */
    public String gotoPrincipalPage() {
        AlterableContext ctxSession = (AlterableContext) beanManager.getContext(SessionScoped.class);
        for (Bean<?> bean : beanManager.getBeans(CargaGeneralView.class)) {
            Object instance = ctxSession.get(bean);
            if (instance != null) {
                ctxSession.destroy(bean);
            }
        }

        return Constantes.GO_TO_PRINCIPAL_PAGE;
    }
}
