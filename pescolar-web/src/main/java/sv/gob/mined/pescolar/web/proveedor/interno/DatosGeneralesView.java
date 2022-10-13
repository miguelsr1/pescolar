package sv.gob.mined.pescolar.web.proveedor.interno;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import sv.gob.mined.pescolar.model.Canton;
import sv.gob.mined.pescolar.model.CapaDistribucionAcre;
import sv.gob.mined.pescolar.model.CapaInstPorRubro;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.DetalleProcesoAdq;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.EntidadFinanciera;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.TecnicoProveedor;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.DiasPlazoContratoRepo;
import sv.gob.mined.pescolar.repository.EmpresaRepo;
import sv.gob.mined.pescolar.repository.MailRepo;
import sv.gob.mined.pescolar.utils.Constantes;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.db.Filtro;
import sv.gob.mined.pescolar.utils.enums.TipoOperador;
import sv.gob.mined.pescolar.web.SessionView;

/**
 *
 * @author misanchez
 */
@Named
@ViewScoped
public class DatosGeneralesView implements Serializable {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("Bundle");

    private Boolean deshabiliar = false;
    private Boolean showUpdateEmpresa;
    private Boolean personaNatural;
    private Boolean mismaDireccion;
    private Boolean rubroUniforme;
    private Boolean inscritoIva = false;
    private Boolean deseaInscribirseIva = false;
    private Boolean showFoto = false;

    private String diasPlazo;
    private String fotoProveedor;
    private String idCanton;
    private String codEntFinanciera;
    private String idCantonLocal;
    private String tapEmpresa;
    private String tapPersona;
    private String fileName = "fotoProveedores/profile.png";
    private String codigoDepartamento;
    private String codigoDepartamentoLocal;
    private String codigoDepartamentoCalificado;

    private Long idMunicipio;
    private Long idMunicipioLocal;

    private List<Filtro> params = new ArrayList();
    private List<Canton> lstCantones = new ArrayList<>();
    private List<Canton> lstCantonesLocal = new ArrayList<>();

    private DetalleProcesoAdq detalleProcesoAdq;
    private CapaDistribucionAcre departamentoCalif;
    private CapaInstPorRubro capacidadInst = new CapaInstPorRubro();

    @Inject
    private CatalogoRepo catalogoRepo;
    @Inject
    private EmpresaRepo empresaRepo;
    @Inject
    private MailRepo mailRepo;

    @Inject
    private SessionView sessionView;
    @Inject
    private CargaGeneralView cargaGeneralView;
    @Inject
    private DiasPlazoContratoRepo diasPlazoRepo;

    @PostConstruct
    public void init() {
        if (sessionView.getUsuario().getIdTipoUsuario().getIdTipoUsuario() == 9l) {
            //El usuario logeado es un proveedor
            sessionView.setAnhoProvedor(11l); //se setea el año proximo de contratación
            params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idPersona", sessionView.getUsuario().getIdPersona()).build());
            cargaGeneralView.setEmpresa(empresaRepo.findEntityByParam(params));
            cargarDatosEmpresa(cargaGeneralView.getEmpresa());
        }
        if (cargaGeneralView.getEmpresa().getId() != null) {
            cargarDatosEmpresa(cargaGeneralView.getEmpresa());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public String getTapEmpresa() {
        return tapEmpresa;
    }

    public String getTapPersona() {
        return tapPersona;
    }

    public CargaGeneralView getCargaGeneralView() {
        return cargaGeneralView;
    }

    public void setCargaGeneralView(CargaGeneralView cargaGeneralView) {
        this.cargaGeneralView = cargaGeneralView;
    }

    public Boolean getShowUpdateEmpresa() {
        return showUpdateEmpresa;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String filename) {
        this.fileName = filename;
    }

    public Boolean getMismaDireccion() {
        return mismaDireccion;
    }

    public void setMismaDireccion(Boolean mismaDireccion) {
        this.mismaDireccion = mismaDireccion;
    }

    public List<Municipio> getLstMunicipios() {
        return catalogoRepo.getLstMunicipiosByDepartamento(codigoDepartamento);
    }

    public List<Municipio> getLstMunicipiosLocal() {
        return catalogoRepo.getLstMunicipiosByDepartamento(codigoDepartamentoLocal);
    }

    public List<EntidadFinanciera> getLstEntidades() {
        return catalogoRepo.findEntidadFinancieraEntities((short) 1);
    }

    public String getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(String codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public String getCodigoDepartamentoLocal() {
        return codigoDepartamentoLocal;
    }

    public void setCodigoDepartamentoLocal(String codigoDepartamentoLocal) {
        this.codigoDepartamentoLocal = codigoDepartamentoLocal;
    }

    public String getCodigoDepartamentoCalificado() {
        return codigoDepartamentoCalificado;
    }

    public void setCodigoDepartamentoCalificado(String codigoDepartamentoCalificado) {
        this.codigoDepartamentoCalificado = codigoDepartamentoCalificado;
    }

    public String getIdCanton() {
        return idCanton;
    }

    public void setIdCanton(String idCanton) {
        this.idCanton = idCanton;
    }

    public Long getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Long idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public List<Canton> getLstCantones() {
        return catalogoRepo.getLstCantonByMunicipio(idMunicipio);
    }

    public List<Canton> getLstCantonesLocal() {
        return catalogoRepo.getLstCantonByMunicipio(idMunicipioLocal);
    }

    public String getCodEntFinanciera() {
        return codEntFinanciera;
    }

    public void setCodEntFinanciera(String entidadFinanciera) {
        this.codEntFinanciera = entidadFinanciera;
    }

    public Boolean getDeshabiliar() {
        return deshabiliar;
    }

    public CapaInstPorRubro getCapacidadInst() {
        if (capacidadInst == null) {
            capacidadInst = new CapaInstPorRubro();
        }
        return capacidadInst;
    }

    public Boolean getShowFoto() {
        return showFoto;
    }

    public void setShowFoto(Boolean showFoto) {
        this.showFoto = showFoto;
    }

    public Boolean getInscritoIva() {
        return inscritoIva;
    }

    public void setInscritoIva(Boolean inscritoIva) {
        this.inscritoIva = inscritoIva;
    }

    public Boolean getRubroUniforme() {
        return rubroUniforme;
    }

    public void setRubroUniforme(Boolean rubroUniforme) {
        this.rubroUniforme = rubroUniforme;
    }

    public Boolean getDeseaInscribirseIva() {
        return deseaInscribirseIva;
    }

    public void setDeseaInscribirseIva(Boolean deseaInscribirseIva) {
        this.deseaInscribirseIva = deseaInscribirseIva;
    }

    public DetalleProcesoAdq getDetalleProcesoAdq() {
        return detalleProcesoAdq;
    }

    public void setDetalleProcesoAdq(DetalleProcesoAdq detalleProcesoAdq) {
        this.detalleProcesoAdq = detalleProcesoAdq;
    }

    public String getIdCantonLocal() {
        return idCantonLocal;
    }

    public void setIdCantonLocal(String idCantonLocal) {
        if (idCantonLocal != null) {
            this.idCantonLocal = idCantonLocal;
        }
    }

    public Long getIdMunicipioLocal() {
        return idMunicipioLocal;
    }

    public void setIdMunicipioLocal(Long idMunicipioLocal) {
        this.idMunicipioLocal = idMunicipioLocal;
    }

    public Boolean getPersonaNatural() {
        return personaNatural;
    }

    public void setPersonaNatural(Boolean personaNatural) {
        this.personaNatural = personaNatural;
    }

    // </editor-fold>
    public String getNombrePieza() {
        if (detalleProcesoAdq != null && detalleProcesoAdq.getId() != null) {
            switch (detalleProcesoAdq.getIdRubroAdq().getId().intValue()) {
                case 4:
                case 5:
                    return "Piezas";
                case 2:
                    return "Paquetes";
                default:
                    return "Pares";
            }
        } else {
            return "";
        }
    }

    public void empresaSeleccionada(SelectEvent event) {
        if (event.getObject() != null) {
            if (event.getObject() instanceof Empresa) {
                cargarDatosEmpresa((Empresa) event.getObject());
            }
        } else {
            deshabiliar = false;
            JsfUtil.mensajeAlerta("Debe de seleccionar una empresa");
        }
    }

    public String getDiasPlazo() {
        return diasPlazo;
    }

    private void cargarDatosEmpresa(Empresa emp) {
        cargaGeneralView.setEmpresa(emp);

        if (cargaGeneralView.getEmpresa().getIdMunicipio() == null) {
            cargaGeneralView.getEmpresa().setIdMunicipio(catalogoRepo.findEntityByPk(Municipio.class, 1l));

            if (cargaGeneralView.getEmpresa().getIdPersona().getIdMunicipio() == null) {
                cargaGeneralView.getEmpresa().getIdPersona().setIdMunicipio(catalogoRepo.findEntityByPk(Municipio.class, BigDecimal.ONE));
            }
        }
        sessionView.setVariableSession("idEmpresa", cargaGeneralView.getEmpresa().getId());
        cargaGeneralView.cargarDetalleCalificacion();
        cargarDetalleCalificacion();

        showUpdateEmpresa = (sessionView.getUsuario().getIdTipoUsuario().getIdTipoUsuario() == 1l || sessionView.getUsuario().getIdTipoUsuario().getIdTipoUsuario() == 9l);

        params.clear();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idAnho.id", sessionView.getIdAnho()).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idRubroInteres.id", capacidadInst.getIdMuestraInteres().getIdRubroInteres().getId()).build());

        if (diasPlazoRepo.findEntityByParam(params) != null) {
            diasPlazo = diasPlazoRepo.findEntityByParam(params).getDiasPlazo().toString();
        } else {
            JsfUtil.mensajeAlerta("No se han regitrado los días de plazo del contrato para el rubro: " + getNombreRubroProveedor());
        }
    }

    private void cargarDetalleCalificacion() {
        capacidadInst = cargaGeneralView.getCapacidadInstPorRubro();
        if (capacidadInst == null) {
            JsfUtil.mensajeAlerta("No se han cargado los datos de este proveedor para el proceso de contratación del año " + cargaGeneralView.getProcesoAdquisicion().getIdAnho());
        } else {
            detalleProcesoAdq = JsfUtil.findDetalleByRubroAndAnho(cargaGeneralView.getProcesoAdquisicion(),
                    capacidadInst.getIdMuestraInteres().getIdRubroInteres().getId(),
                    capacidadInst.getIdMuestraInteres().getIdAnho().getId());
            departamentoCalif = catalogoRepo.findDetProveedor(capacidadInst.getIdMuestraInteres(), null, CapaDistribucionAcre.class);
            personaNatural = (cargaGeneralView.getEmpresa().getIdPersoneria().getId().intValue() == 1);
            if (personaNatural) {
                mismaDireccion = (cargaGeneralView.getEmpresa().getIdMunicipio().getId().intValue() == cargaGeneralView.getEmpresa().getIdPersona().getIdMunicipio().getId().intValue());
            }

            if (departamentoCalif == null || departamentoCalif.getCodigoDepartamento() == null) {
                JsfUtil.mensajeAlerta("Este proveedor no posee departamento de calificación " + cargaGeneralView.getProcesoAdquisicion().getIdAnho().getAnho());
            } else {

                codigoDepartamento = cargaGeneralView.getEmpresa().getIdPersona().getIdMunicipio().getCodigoDepartamento().getId();
                idMunicipio = cargaGeneralView.getEmpresa().getIdPersona().getIdMunicipio().getId();
                codigoDepartamentoCalificado = departamentoCalif.getCodigoDepartamento().getId();

                idMunicipioLocal = cargaGeneralView.getEmpresa().getIdMunicipio().getId();
                codigoDepartamentoLocal = cargaGeneralView.getEmpresa().getIdMunicipio().getCodigoDepartamento().getId();

                rubroUniforme = (departamentoCalif.getIdMuestraInteres().getIdRubroInteres().getIdRubroUniforme().intValue() == 1);

                if (rubroUniforme) {
                    idCanton = cargaGeneralView.getEmpresa().getIdPersona().getCodigoCanton();
                    idCantonLocal = cargaGeneralView.getEmpresa().getCodigoCanton();
                    lstCantones = catalogoRepo.getLstCantonByMunicipio(idMunicipio);
                    lstCantonesLocal = catalogoRepo.getLstCantonByMunicipio(idMunicipioLocal);
                }

                deshabiliar = false;
                if (cargaGeneralView.getEmpresa().getIdPersona().getUrlImagen() == null) {
                    fileName = "fotoProveedores/profile.png";
                } else {
                    fileName = "fotoProveedores/" + cargaGeneralView.getEmpresa().getIdPersona().getUrlImagen();
                }
            }
        }
        /*}
        }*/

        if (cargaGeneralView.getEmpresa() == null || cargaGeneralView.getEmpresa().getId() == null) {
            tapEmpresa = "";
            tapPersona = "";
        } else if (cargaGeneralView.getEmpresa().getIdPersoneria().getId() == null) {
            tapPersona = "";
            tapEmpresa = "";
        } else if (cargaGeneralView.getEmpresa().getIdPersoneria().getId().intValue() == 2) {
            tapEmpresa = "Empresa";
            tapPersona = "Representante Legal";
        } else {
            tapEmpresa = "";
            tapPersona = "Proveedor";
        }
    }

    public void guardarDatosGenerales() {
        if (showUpdateEmpresa || sessionView.getIsUsuarioAdministrador()) {
            if (cargaGeneralView.getEmpresa().getIdPersoneria().getId().intValue() == 1) {
                cargaGeneralView.getEmpresa().setRazonSocial(cargaGeneralView.getEmpresa().getIdPersona().getNombreCompletoProveedor());
                cargaGeneralView.getEmpresa().setTelefonos(cargaGeneralView.getEmpresa().getIdPersona().getNumeroTelefono());
                cargaGeneralView.getEmpresa().setNumeroCelular(cargaGeneralView.getEmpresa().getIdPersona().getNumeroCelular());
                cargaGeneralView.getEmpresa().setNumeroNit(cargaGeneralView.getEmpresa().getIdPersona().getNumeroNit());

                if (mismaDireccion) {
                    idMunicipioLocal = idMunicipio;
                    cargaGeneralView.getEmpresa().setDireccionCompleta(cargaGeneralView.getEmpresa().getIdPersona().getDomicilio());
                    cargaGeneralView.getEmpresa().getIdPersona().setIdMunicipio(catalogoRepo.findEntityByPk(Municipio.class, idMunicipio));
                    cargaGeneralView.getEmpresa().setIdMunicipio(catalogoRepo.findEntityByPk(Municipio.class, idMunicipio));
                    
                    if (rubroUniforme) {
                        idCantonLocal = idCanton;
                        cargaGeneralView.getEmpresa().setCodigoCanton(idCantonLocal);
                        cargaGeneralView.getEmpresa().getIdPersona().setCodigoCanton(idCanton);
                    } else {
                        cargaGeneralView.getEmpresa().setCodigoCanton(null);
                        cargaGeneralView.getEmpresa().getIdPersona().setCodigoCanton(null);
                    }
                } else {
                    cargaGeneralView.getEmpresa().setIdMunicipio(catalogoRepo.findEntityByPk(Municipio.class, idMunicipioLocal));
                    cargaGeneralView.getEmpresa().getIdPersona().setIdMunicipio(catalogoRepo.findEntityByPk(Municipio.class, idMunicipio));
                    //cargaGeneralView.getEmpresa().getIdPersona().setCodigoCanton(idCanton);
                }

            } else if (rubroUniforme) {
                cargaGeneralView.getEmpresa().setCodigoCanton(idCantonLocal);
                cargaGeneralView.getEmpresa().setIdMunicipio(catalogoRepo.findEntityByPk(Municipio.class, idMunicipioLocal));
                cargaGeneralView.getEmpresa().getIdPersona().setIdMunicipio(catalogoRepo.findEntityByPk(Municipio.class, idMunicipio));
                cargaGeneralView.getEmpresa().getIdPersona().setCodigoCanton(idCanton);
            }

            if (rubroUniforme) {
                cargaGeneralView.getEmpresa().setEsContribuyente(inscritoIva);
                cargaGeneralView.getEmpresa().setDeseaInscribirse(deseaInscribirseIva);
            }

            empresaRepo.update(cargaGeneralView.getEmpresa());
            empresaRepo.guardarCapaInst(departamentoCalif, capacidadInst);

            //Si el usuario es proveedor, enviar notificación a cuenta de técnico de paquete escolar
            if (sessionView.getUsuario().getIdTipoUsuario().getIdTipoUsuario() == 9l) {
                notificarTecnicoPaquete();
            }
            JsfUtil.mensajeUpdate();
        }

        if (sessionView.getIsUsuarioAdministrador()) {
            departamentoCalif.setCodigoDepartamento(catalogoRepo.findEntityByPk(Departamento.class, codigoDepartamentoCalificado));

            departamentoCalif.setFechaModificacion(LocalDate.now());
            departamentoCalif.setUsuarioModificacion(sessionView.getUsuario().getIdPersona().getUsuario());
            capacidadInst.setFechaModificacion(LocalDate.now());
            capacidadInst.setUsuarioModificacion(sessionView.getUsuario().getIdPersona().getUsuario());

            if (empresaRepo.guardarCapaInst(departamentoCalif, capacidadInst)) {
            }

            JsfUtil.mensajeUpdate();
        }

        cargaGeneralView.recargarInformacion();
        cargarDatosEmpresa(cargaGeneralView.getEmpresa());
    }

    public void forceRefreshPage() {
        FacesContext context = FacesContext.getCurrentInstance();
        String refreshpage = context.getViewRoot().getViewId();
        ViewHandler handler = context.getApplication().getViewHandler();
        UIViewRoot root = handler.createView(context, refreshpage);
        root.setViewId(refreshpage);
        context.setViewRoot(root);
    }

    /**
     * Luego de que el proveedor actualice los datos generales, se envia un
     * correo a un tecnico MINED para darle seguimiento al registro de la oferta
     */
    private void notificarTecnicoPaquete() {
        TecnicoProveedor tecnico = empresaRepo.getTecnicoProveedor(cargaGeneralView.getEmpresa().getId());
        if (tecnico == null) {
            JsfUtil.mensajeAlerta("Usted no tiene ténico asignado");
            return;
        }

        StringBuilder sb = new StringBuilder("");

        sb = sb.append(MessageFormat.format(RESOURCE_BUNDLE.getString("pagoprov.email.nofitecnico.message"), cargaGeneralView.getEmpresa().getRazonSocial()));
        sb = sb.append("<br/>").append("<br/>");
        sb = sb.append(RESOURCE_BUNDLE.getString("pagoprov.email.footer"));

        mailRepo.enviarMail(MessageFormat.format(RESOURCE_BUNDLE.getString("pagoprov.email.nofitecnico.titulo"), sessionView.getAnhoProceso()),
                sb.toString(),
                cargaGeneralView.getEmpresa().getIdPersona().getEmail(),
                empresaRepo.getTecnicoProveedor(cargaGeneralView.getEmpresa().getId()).getMailTecnico());
    }

    public void invitacionProveedorPaquete() {
        StringBuilder sb = new StringBuilder("");

        sb = sb.append(MessageFormat.format(RESOURCE_BUNDLE.getString("pagoprov.email.update.header"), cargaGeneralView.getEmpresa().getRazonSocial()));
        sb = sb.append("<br/>").append("<br/>");
        sb = sb.append(MessageFormat.format(RESOURCE_BUNDLE.getString("pagoprov.email.update.message"), JsfUtil.getNombreRubroById(capacidadInst.getIdMuestraInteres().getIdRubroInteres().getId()), sessionView.getProceso().getIdAnho().getAnho(), "<a href=\"".concat(RESOURCE_BUNDLE.getString("url")).concat("\">Clic acá</a>")));
        sb = sb.append("<br/>").append("<br/>");
        sb = sb.append(RESOURCE_BUNDLE.getString("pagoprov.email.footer"));

        mailRepo.enviarMail(MessageFormat.format(RESOURCE_BUNDLE.getString("pagoprov.email.update.titulo"), sessionView.getAnhoProceso()),
                sb.toString(),
                cargaGeneralView.getEmpresa().getIdPersona().getEmail(),
                "rafael.arias@mined.gob.sv"
        );
        JsfUtil.mensajeInformacion("Se ha enviado la notificación al proveedor.");
    }

    public void filtroProveedores() {
        cargaGeneralView.setEmpresa(new Empresa());
        capacidadInst = new CapaInstPorRubro();

        fileName = "fotoProveedores/profile.png";
        showFoto = true;
        deshabiliar = true;

        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 500);
        options.put("contentWidth", 800);
        options.put("hideEffect", "fade");
        options.put("showEffect", "fade");

        PrimeFaces.current().dialog().openDynamic(Constantes.DLG_BUSCAR_PROVEEDOR, options, null);
    }

    public String getNombreRubroProveedor() {
        if (capacidadInst != null && capacidadInst.getIdMuestraInteres() != null) {
            return JsfUtil.getNombreRubroById(capacidadInst.getIdMuestraInteres().getIdRubroInteres().getId());
        } else {
            return "";
        }
    }

    public void dlgFotografia() {
        if (cargaGeneralView.getEmpresa() == null || cargaGeneralView.getEmpresa().getId() == null) {
            JsfUtil.mensajeAlerta("Debe de seleccionar un empresa");
        } else {
            deshabiliar = true;
            Map<String, Object> options = new HashMap();
            options.put("modal", true);
            options.put("draggable", false);
            options.put("resizable", false);
            options.put("contentHeight", 400);
            options.put("contentWidth", 554);
            sessionView.setVariableSession("nitEmpresa", cargaGeneralView.getEmpresa().getNumeroNit());
            PrimeFaces.current().dialog().openDynamic("/app/comunes/filtroFotoProveedor", options, null);
        }
    }

    public void updateFrm(SelectEvent event) {
        fotoProveedor = (String) event.getObject();
        if (!fotoProveedor.contains("/")) {
            showFoto = false;
        } else {
            cargaGeneralView.getEmpresa().getIdPersona().setUrlImagen(fotoProveedor);
        }
    }

    public void generarItems() {
        if (departamentoCalif != null && departamentoCalif.getId() != null) {
            empresaRepo.calcularNoItems(departamentoCalif.getIdMuestraInteres().getId());
        } else {
            JsfUtil.mensajeAlerta("No posee oferta 2023");
        }
    }
}
