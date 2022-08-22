package sv.gob.mined.pescolar.web.proveedor.interno;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Session;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.paradise.view.GuestPreferences;
import sv.gob.mined.pescolar.model.Canton;
import sv.gob.mined.pescolar.model.CapaDistribucionAcre;
import sv.gob.mined.pescolar.model.CapaInstPorRubro;
import sv.gob.mined.pescolar.model.Departamento;
import sv.gob.mined.pescolar.model.DetalleProcesoAdq;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.EntidadFinanciera;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
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
    private GuestPreferences guestPreferencesView;

    @Resource(mappedName = "java:/PaqueteEscolar")
    private Session mailSession;

    @PostConstruct
    public void init() {
        if (sessionView.getUsuario().getIdTipoUsuario().getIdTipoUsuario() == 9l) {
            //El usuario logeado es un proveedor
            params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idPersona", sessionView.getUsuario().getIdPersona()).build());
            cargaGeneralView.setEmpresa(empresaRepo.findEntityByParam(params));
            cargarDatosEmpresa(cargaGeneralView.getEmpresa());
            showUpdateEmpresa = true;
        } else {
            //El usuario logeado es un usuario MINED
            cargarDetalleCalificacion();
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

    private void cargarDatosEmpresa(Empresa emp) {
        cargaGeneralView.setEmpresa(emp);

        if (cargaGeneralView.getUrlStr().contains("DatosGenerales")) {
            idMunicipio = cargaGeneralView.getEmpresa().getIdPersona().getIdMunicipio().getId();
            codigoDepartamento = cargaGeneralView.getEmpresa().getIdPersona().getIdMunicipio().getCodigoDepartamento().getId();
        }

        if (cargaGeneralView.getEmpresa().getIdMunicipio() == null) {
            cargaGeneralView.getEmpresa().setIdMunicipio(catalogoRepo.findEntityByPk(Municipio.class, 1l));

            if (cargaGeneralView.getEmpresa().getIdPersona().getIdMunicipio() == null) {
                cargaGeneralView.getEmpresa().getIdPersona().setIdMunicipio(catalogoRepo.findEntityByPk(Municipio.class, BigDecimal.ONE));
            }
        }
        sessionView.setVariableSession("idEmpresa", cargaGeneralView.getEmpresa().getId());
        cargaGeneralView.cargarDetalleCalificacion();
        cargarDetalleCalificacion();
        showUpdateEmpresa = (sessionView.getUsuario().getIdTipoUsuario().getIdTipoUsuario() == 1l);
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
                codigoDepartamento = cargaGeneralView.getEmpresa().getIdMunicipio().getCodigoDepartamento().getId();
                idMunicipio = cargaGeneralView.getEmpresa().getIdMunicipio().getId();
                codigoDepartamentoCalificado = departamentoCalif.getCodigoDepartamento().getId();

                idMunicipioLocal = cargaGeneralView.getEmpresa().getIdMunicipio().getId();
                codigoDepartamentoLocal = cargaGeneralView.getEmpresa().getIdMunicipio().getCodigoDepartamento().getId();

                rubroUniforme = (departamentoCalif.getIdMuestraInteres().getIdRubroInteres().getIdRubroUniforme().intValue() == 1);

                if (rubroUniforme) {
                    idCanton = cargaGeneralView.getEmpresa().getIdPersona().getCodigoCanton();
                    idCantonLocal = cargaGeneralView.getEmpresa().getCodigoCanton();
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
        if (showUpdateEmpresa) {
            if (cargaGeneralView.getEmpresa().getIdPersoneria().getId().intValue() == 1) {
                cargaGeneralView.getEmpresa().setRazonSocial(cargaGeneralView.getEmpresa().getIdPersona().getNombreCompletoProveedor());
                cargaGeneralView.getEmpresa().setTelefonos(cargaGeneralView.getEmpresa().getIdPersona().getNumeroTelefono());
                cargaGeneralView.getEmpresa().setNumeroCelular(cargaGeneralView.getEmpresa().getIdPersona().getNumeroCelular());
                cargaGeneralView.getEmpresa().setNumeroNit(cargaGeneralView.getEmpresa().getIdPersona().getNumeroNit());

                if (mismaDireccion) {
                    idMunicipioLocal = idMunicipio;
                    idCantonLocal = idCanton;
                    cargaGeneralView.getEmpresa().setDireccionCompleta(cargaGeneralView.getEmpresa().getIdPersona().getDomicilio());

                    cargaGeneralView.getEmpresa().getIdPersona().setIdMunicipio(new Municipio(idMunicipio));
                    cargaGeneralView.getEmpresa().setIdMunicipio(new Municipio(idMunicipioLocal));
                    if (rubroUniforme) {
                        cargaGeneralView.getEmpresa().setCodigoCanton(idCantonLocal);
                        cargaGeneralView.getEmpresa().getIdPersona().setCodigoCanton(idCanton);
                    } else {
                        cargaGeneralView.getEmpresa().setCodigoCanton(null);
                        cargaGeneralView.getEmpresa().getIdPersona().setCodigoCanton(null);
                    }
                } else {
                    cargaGeneralView.getEmpresa().getIdPersona().setIdMunicipio(new Municipio(idMunicipio));
                    cargaGeneralView.getEmpresa().getIdPersona().setCodigoCanton(idCanton);
                }

            } else if (rubroUniforme) {
                cargaGeneralView.getEmpresa().setCodigoCanton(idCantonLocal);
                cargaGeneralView.getEmpresa().setIdMunicipio(new Municipio(idMunicipioLocal));
                cargaGeneralView.getEmpresa().getIdPersona().setIdMunicipio(new Municipio(idMunicipio));
                cargaGeneralView.getEmpresa().getIdPersona().setCodigoCanton(idCanton);
            }

            if (rubroUniforme) {
                cargaGeneralView.getEmpresa().setEsContribuyente(inscritoIva);
                cargaGeneralView.getEmpresa().setDeseaInscribirse(deseaInscribirseIva);
            }

            empresaRepo.update(cargaGeneralView.getEmpresa());

            //Si el usuario es proveedor, enviar notificación a cuenta de técnico de paquete escolar
            if (sessionView.getUsuario().getIdTipoUsuario().getIdTipoUsuario() == 9l) {
                notificarTecnicoPaquete();
            }
        } else {
            departamentoCalif.setCodigoDepartamento(catalogoRepo.findEntityByPk(Departamento.class, codigoDepartamentoCalificado));

            if (empresaRepo.guardarCapaInst(departamentoCalif, capacidadInst)) {
                JsfUtil.mensajeUpdate();
            }
        }
        //revisar este funcionamiento

        /*departamentoCalif.setCodigoDepartamento(catalogoRepo.findEntityByPk(Departamento.class, codigoDepartamentoCalificado));

        if (empresaRepo.guardarCapaInst(departamentoCalif, capacidadInst)) {
            JsfUtil.mensajeUpdate();
        }*/
    }

    /**
     * Luego de que el proveedor actualice los datos generales, se envia un
     * correo a un tecnico MINED para darle seguimiento al registro de la oferta
     */
    private void notificarTecnicoPaquete() {
        StringBuilder sb = new StringBuilder("");

        sb = sb.append(MessageFormat.format(RESOURCE_BUNDLE.getString("pagoprov.email.nofitecnico.message"), cargaGeneralView.getEmpresa().getRazonSocial()));
        sb = sb.append("<br/>").append("<br/>");
        sb = sb.append(RESOURCE_BUNDLE.getString("pagoprov.email.footer"));

        mailRepo.enviarMail(cargaGeneralView.getEmpresa().getIdPersona().getEmail(),
                "infopaquetes@mined.gob.sv",
                empresaRepo.getTecnicoProveedor(cargaGeneralView.getEmpresa().getId()).getMailTecnico(),
                MessageFormat.format(RESOURCE_BUNDLE.getString("pagoprov.email.nofitecnico.titulo"), sessionView.getAnhoProceso()),
                sb.toString(),
                mailSession);
    }

    private void invitacionProveedorPaquete() {
        StringBuilder sb = new StringBuilder("");

        sb = sb.append(MessageFormat.format(RESOURCE_BUNDLE.getString("pagoprov.email.update.header"), cargaGeneralView.getEmpresa().getRazonSocial()));
        sb = sb.append("<br/>").append("<br/>");
        sb = sb.append(MessageFormat.format(RESOURCE_BUNDLE.getString("pagoprov.email.update.message"), JsfUtil.getNombreRubroById(capacidadInst.getIdMuestraInteres().getIdRubroInteres().getId()), sessionView.getProceso().getIdAnho().getAnho(), RESOURCE_BUNDLE.getString("url")));
        sb = sb.append("<br/>").append("<br/>");
        sb = sb.append(RESOURCE_BUNDLE.getString("pagoprov.email.footer"));

        mailRepo.enviarMail(cargaGeneralView.getEmpresa().getIdPersona().getEmail(),
                "rafael.arias@mined.gob.sv",
                "",
                MessageFormat.format(RESOURCE_BUNDLE.getString("pagoprov.email.update.titulo"), sessionView.getAnhoProceso()),
                sb.toString(),
                mailSession);
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

    public void usuarioProveedor() {
        sessionView.setIdAnho(11l);
        guestPreferencesView.setMenuMode("layout-menu-overlay");
    }
}