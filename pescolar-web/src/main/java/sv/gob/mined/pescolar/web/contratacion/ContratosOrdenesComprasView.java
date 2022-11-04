/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.web.contratacion;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.primefaces.PrimeFaces;
import sv.gob.mined.apps.utilitario.Herramientas;
import sv.gob.mined.pescolar.model.CapaInstPorRubro;
import sv.gob.mined.pescolar.model.ContratosOrdenesCompra;
import sv.gob.mined.pescolar.model.DetRubroMuestraIntere;
import sv.gob.mined.pescolar.model.DetalleOferta;
import sv.gob.mined.pescolar.model.DetalleProcesoAdq;
import sv.gob.mined.pescolar.model.DiasPlazoContrato;
import sv.gob.mined.pescolar.model.Empresa;
import sv.gob.mined.pescolar.model.HistorialCamEstResAdj;
import sv.gob.mined.pescolar.model.Municipio;
import sv.gob.mined.pescolar.model.OfertaBienesServicio;
import sv.gob.mined.pescolar.model.OrganizacionEducativa;
import sv.gob.mined.pescolar.model.Participante;
import sv.gob.mined.pescolar.model.ResolucionesAdjudicativa;
import sv.gob.mined.pescolar.model.RptDocumentos;
import sv.gob.mined.pescolar.model.dto.contratacion.Bean2Excel;
import sv.gob.mined.pescolar.model.dto.contratacion.VwCotizacion;
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.ContratoRepo;
import sv.gob.mined.pescolar.repository.EntidadEducativaRepo;
import sv.gob.mined.pescolar.repository.OfertaRepo;
import sv.gob.mined.pescolar.repository.ParticipanteRepo;
import sv.gob.mined.pescolar.repository.ReportesRepo;
import sv.gob.mined.pescolar.repository.ResolucionesAdjudicativasRepo;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.RecuperarProcesoUtil;
import sv.gob.mined.pescolar.utils.Reportes;
import sv.gob.mined.pescolar.web.SessionView;

/**
 *
 * @author misanchez
 */
@Named
@ViewScoped
public class ContratosOrdenesComprasView extends RecuperarProcesoUtil implements Serializable {

    private int estadoEdicion = 0;
    private int tipoRpt = 1;
    private Boolean soloLectura = false;
    private Boolean showFechaOrdenInicio = true;
    private Boolean deshabilitado = true;
    private Boolean continuar = true;
    private Boolean noEditableRepCe = true;
    private Boolean filtroCE = true;
    private Boolean cambiarRepreCe = false;
    private Boolean cambiarCiudadFirma = false;
    private Boolean crearRepresentante = false;
    private Boolean actaAdj = false;
    private Boolean notaAdj = false;
    private Boolean contrato = false;
    private Boolean garantiaContrato = false;
    private Boolean garantiaAnticipo = false;
    private Boolean garantiaUsoTela = false;
    private Boolean showGarantiaUsoTela = false;
    private Boolean analisisTecEco = false;
    private String codigoEntidad;
    private String razonSocial;
    private String representanteLegal;
    private String nombreEncargadoCompra;
    private Long rubro = 0l;
    private Long idParticipante = 0l;
    private Long idMunicipio;

    private DetalleProcesoAdq detalleProceso = new DetalleProcesoAdq();
    private DiasPlazoContrato diasPlazo = new DiasPlazoContrato();
    private OfertaBienesServicio oferta;
    private Participante participante;
    private OrganizacionEducativa representanteCe;
    private ContratosOrdenesCompra current = new ContratosOrdenesCompra();
    private ResolucionesAdjudicativa resolucionAdj = new ResolucionesAdjudicativa();
    private VwCatalogoEntidadEducativa entidadEducativa = new VwCatalogoEntidadEducativa();
    private List<Integer> lstSelectDocumentosImp = new ArrayList();
    private List<SelectItem> lstDocumentosImp = new ArrayList();
    private List<HistorialCamEstResAdj> lstHistorialCambios = new ArrayList();

    @Inject
    private CatalogoRepo catalogoRepo;
    @Inject
    private ResolucionesAdjudicativasRepo resoRepo;
    @Inject
    private ContratoRepo contratoRepo;
    @Inject
    private ParticipanteRepo participanteRepo;
    @Inject
    private EntidadEducativaRepo entidadRepo;
    @Inject
    private OfertaRepo ofertaRepo;
    @Inject
    private ReportesRepo reporteRepo;
    @Inject
    private Reportes reportes;

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("Bundle");

    @Inject
    private SessionView sessionView;

    /**
     * Creates a new instance of ContratosOrdenesComprasController
     */
    public ContratosOrdenesComprasView() {
    }

    @PostConstruct
    public void ini() {
        rubro = sessionView.getIdRubro();
        idMunicipio = sessionView.getIdMunicipio();

        if (rubro != null) {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            detalleProceso = JsfUtil.findDetalleByRubroAndAnho(sessionView.getProceso(), rubro, sessionView.getIdAnho());
            idMunicipio = sessionView.getIdMunicipio();
            if (params.containsKey("txtCodigoEntidad")) {
                if (detalleProceso != null) {
                    cargaInicialDeDatos(params);
                    lstDocumentosImp = null; //utilEJB.getLstDocumentosImp(rubro.intValueExact() == 1 || rubro.intValueExact() == 4 || rubro.intValueExact() == 5, detalleProceso.getIdProcesoAdq().getIdAnho().getIdAnho().intValue());
                    seleccionarDocumentosAImprimir();
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public List<HistorialCamEstResAdj> getLstHistorialCambios() {
        return lstHistorialCambios;
    }

    public DiasPlazoContrato getDiasPlazo() {
        return diasPlazo;
    }

    public void setLstHistorialCambios(List<HistorialCamEstResAdj> lstHistorialCambios) {
        this.lstHistorialCambios = lstHistorialCambios;
    }

    public Boolean getAnalisisTecEco() {
        return analisisTecEco;
    }

    public void setAnalisisTecEco(Boolean analisisTecEco) {
        this.analisisTecEco = analisisTecEco;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public Long getRubro() {
        return rubro;
    }

    public void setRubro(Long rubro) {
        this.rubro = rubro;
    }

    public Boolean getShowFechaOrdenInicio() {
        return showFechaOrdenInicio;
    }

    public void setShowFechaOrdenInicio(Boolean showFechaOrdenInicio) {
        this.showFechaOrdenInicio = showFechaOrdenInicio;
    }

    public ContratosOrdenesCompra getSelected() {
        return current;
    }

    public Boolean getNoEditableRepCe() {
        return noEditableRepCe;
    }

    public void setNoEditableRepCe(Boolean noEditableRepCe) {
        this.noEditableRepCe = noEditableRepCe;
    }

    public Boolean getCrearRepresentante() {
        return crearRepresentante;
    }

    public void setCrearRepresentante(Boolean crearRepresentante) {
        this.crearRepresentante = crearRepresentante;
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public OfertaBienesServicio getOferta() {
        return oferta;
    }

    public void setOferta(OfertaBienesServicio oferta) {
        this.oferta = oferta;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public Long getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(Long idParticipante) {
        this.idParticipante = idParticipante;
    }

    public OrganizacionEducativa getRepresentanteCe() {
        return representanteCe;
    }

    public void setRepresentanteCe(OrganizacionEducativa representanteCe) {
        this.representanteCe = representanteCe;
    }

    public VwCatalogoEntidadEducativa getEntidadEducativa() {
        return entidadEducativa;
    }

    public void setEntidadEducativa(VwCatalogoEntidadEducativa entidadEducativa) {
        this.entidadEducativa = entidadEducativa;
    }

    public Boolean getDeshabilitado() {
        return deshabilitado;
    }

    public void setDeshabilitado(Boolean deshabilitado) {
        this.deshabilitado = deshabilitado;
    }

    public Boolean getContinuar() {
        return continuar;
    }

    public void setContinuar(Boolean continuar) {
        this.continuar = continuar;
    }

    public Boolean getFiltroCE() {
        return filtroCE;
    }

    public void setFiltroCE(Boolean filtroCE) {
        this.filtroCE = filtroCE;
    }

    public Boolean getCambiarCiudadFirma() {
        return cambiarCiudadFirma;
    }

    public void setCambiarCiudadFirma(Boolean cambiarCiudadFirma) {
        this.cambiarCiudadFirma = cambiarCiudadFirma;
    }

    public Boolean getCambiarRepreCe() {
        return cambiarRepreCe;
    }

    public void setCambiarRepreCe(Boolean cambiarRepreCe) {
        this.cambiarRepreCe = cambiarRepreCe;
    }

    public Boolean getActaAdj() {
        return actaAdj;
    }

    public void setActaAdj(Boolean actaAdj) {
        this.actaAdj = actaAdj;
    }

    public Boolean getNotaAdj() {
        return notaAdj;
    }

    public void setNotaAdj(Boolean notaAdj) {
        this.notaAdj = notaAdj;
    }

    public Boolean getContrato() {
        return contrato;
    }

    public void setContrato(Boolean contrato) {
        this.contrato = contrato;
    }

    public Boolean getGarantiaContrato() {
        return garantiaContrato;
    }

    public void setGarantiaContrato(Boolean garantiaContrato) {
        this.garantiaContrato = garantiaContrato;
    }

    public Boolean getGarantiaAnticipo() {
        return garantiaAnticipo;
    }

    public void setGarantiaAnticipo(Boolean garantiaAnticipo) {
        this.garantiaAnticipo = garantiaAnticipo;
    }

    public Boolean getGarantiaUsoTela() {
        return garantiaUsoTela;
    }

    public void setGarantiaUsoTela(Boolean garantiaUsoTela) {
        this.garantiaUsoTela = garantiaUsoTela;
    }

    public DetalleProcesoAdq getDetalleProceso() {
        return detalleProceso;
    }

    public void setDetalleProceso(DetalleProcesoAdq detalleProceso) {
        this.detalleProceso = detalleProceso;
    }

    public int getTipoRpt() {
        return tipoRpt;
    }

    public void setTipoRpt(int tipoRpt) {
        this.tipoRpt = tipoRpt;
        sessionView.setVariableSession("tipoRpt", tipoRpt);
    }

    public Boolean getShowGarantiaUsoTela() {
        return showGarantiaUsoTela;
    }

    public void setShowGarantiaUsoTela(Boolean showGarantiaUsoTela) {
        this.showGarantiaUsoTela = showGarantiaUsoTela;
    }

    public List<SelectItem> getLstDocumentosImp() {
        return lstDocumentosImp;
    }

    public List<Integer> getLstSelectDocumentosImp() {
        return lstSelectDocumentosImp;
    }

    public void setLstSelectDocumentosImp(List<Integer> lstSelectDocumentosImp) {
        this.lstSelectDocumentosImp = lstSelectDocumentosImp;
    }
    // </editor-fold>

    private void cargaInicialDeDatos(Map<String, String> params) {
        if (params.containsKey("txtCodigoEntidad")) {
            codigoEntidad = params.get("txtCodigoEntidad");
            entidadEducativa = catalogoRepo.findEntityByPk(VwCatalogoEntidadEducativa.class, codigoEntidad);
            resolucionAdj = resoRepo.findByPk((Long) sessionView.getVariableSession("idRes"));
            sessionView.removeVariableSession("idRes");
            continuar = false;
            deshabilitado = false;
            diasPlazo = contratoRepo.findDiasPlazoPorRubro(rubro, sessionView.getIdAnho());
            if (resolucionAdj.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdRubroAdq().getDescripcionRubro().contains("UNIFORME")) {
                showGarantiaUsoTela = true;
                showFechaOrdenInicio = false;
            }
            cargarDocumentoLegal();
        } else {
            JsfUtil.mensajeAlerta("Se ha perdido el valor del codigo del centro escolar. Por favor inicie nuevamente su proceso");
            Logger.getLogger(ContratosOrdenesComprasView.class.getName()).log(Level.INFO, null, "=============================================================");
            Logger.getLogger(ContratosOrdenesComprasView.class.getName()).log(Level.INFO, null, "Error: Se ha perdido el valor de la variable codigoEntidad");
            Logger.getLogger(ContratosOrdenesComprasView.class.getName()).log(Level.INFO, null, "ContratosOrdenesComprasController.cargaInicialDeDatos()");
            Logger.getLogger(ContratosOrdenesComprasView.class.getName()).log(Level.INFO, null, "=============================================================");
        }
    }

    private void cargarDocumentoLegal() {
        List<ContratosOrdenesCompra> lst = resoRepo.findContratoByResolucion(resolucionAdj);

        if (lst.size() > 1) {
            JsfUtil.mensajeError("Existe un problema con el contrato seleccionado, por favor reportarlo al administrador del sistema.");
            //resolucionAdjudicativaEJB.enviarCorreoDeError(resolucionAdj.getId(), JsfUtil.getSessionMailG("2"));
        } else {
            ContratosOrdenesCompra contratoOrd = null;

            if (lst.size() == 1) {
                contratoOrd = lst.get(0);
                contratoOrd.setPlazoPrevistoEntrega(diasPlazo.getDiasPlazo().longValue());
            }

            razonSocial = resolucionAdj.getIdParticipante().getIdEmpresa().getRazonSocial();
            representanteLegal = participanteRepo.getRespresentanteLegalEmp(resolucionAdj.getIdParticipante().getIdEmpresa().getIdPersona().getId());
            representanteCe = entidadRepo.getPresidenteOrganismoEscolar(codigoEntidad);
            nombreEncargadoCompra = entidadRepo.getMiembro(codigoEntidad, "ENCARGADO DE COMPRA").getNombreMiembro();

            soloLectura = (resolucionAdj.getIdEstadoReserva().getId().intValue() == 2);

            if (contratoOrd == null) { //CREAR NUEVA INSTACIA DE UN CONTRATO
                current = new ContratosOrdenesCompra();

                if (idMunicipio != null) {
                    current.setCiudadFirma(catalogoRepo.findEntityByPk(Municipio.class, sessionView.getIdMunicipio()).getNombreMunicipio());
                    current.setAnhoContrato(resolucionAdj.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getIdAnho().getAnho());

                    current.setPlazoPrevistoEntrega(diasPlazo.getDiasPlazo().longValue());

                    if (representanteCe != null) {
                        current.setMiembroFirma(representanteCe.getNombreMiembro());
                        noEditableRepCe = false;
                    }
                }
            } else { //CARGAR CONTRATO EXISTENTE
                current = contratoOrd;
                /**
                 * Fecha: 05/09/2018 Comentario: Validar que el contrato
                 * seleccionado tenga asignado el plazo previsto de entrega
                 */
                if (current.getPlazoPrevistoEntrega() == null) {
                    current.setPlazoPrevistoEntrega(diasPlazo.getDiasPlazo().longValue());
                    resoRepo.editContrato(current);
                }
            }
        }
    }

    public boolean getEENuevo() {
        return false;
    }

    public boolean getEEModificar() {
        return false;
    }

    public String prepareCreate() {
        if (idMunicipio != null) {
            limpiarCampos();
            codigoEntidad = "";
            showGarantiaUsoTela = false;
            current = new ContratosOrdenesCompra();
            current.setCiudadFirma(sessionView.getNombreMunicipio());
            oferta = null;
            deshabilitado = false;
            estadoEdicion = 1;
            continuar = true;
        } else {
            JsfUtil.mensajeAlerta("Debe de seleccionar un departamento y municipio.");
        }
        return null;
    }

    public void limpiarCampos() {
        oferta = null;
        entidadEducativa = null;
        participante = null;
        continuar = true;
        idParticipante = 0l;
        soloLectura = false;
        lstSelectDocumentosImp.clear();
    }

    public void guardar() {
        if (current != null) {
            if (resolucionAdj != null && resolucionAdj.getId() != null) {
                if (current.getCiudadFirma() == null || current.getCiudadFirma().isEmpty()) {
                    JsfUtil.mensajeError("Ocurrio un error inesperado. Debe de reasignar el municipio.");
                } else {
                    if (current.getId() == null) {
                        create();
                    } else {
                        update();
                    }
                }
            } else {
                JsfUtil.mensajeError("Ocurrio un error inesperado. Por favor iniciar nuevamente el proceso de inserción/modificación del contrato.");
            }
        }
    }

    public void create() {
        try {
            if (current.getAnhoContrato() == null) {
            } else if (current.getFechaEmision() == null) {
            } else {
                if (!noEditableRepCe) {
                    if (crearRepresentante) {
                        representanteCe = new OrganizacionEducativa();
                        representanteCe.setCargo("Presidente Propietario, Director");
                        representanteCe.setCodigoEntidad(codigoEntidad);
                        representanteCe.setEstadoEliminacion(0l);
                        representanteCe.setFechaInsercion(LocalDateTime.now());
                        representanteCe.setFirmaContrato(1l);
                        representanteCe.setNombreMiembro(current.getMiembroFirma());
                        representanteCe.setUsuarioInsercion(sessionView.getUsuario().getIdPersona().getUsuario());

                        entidadRepo.create(representanteCe);
                    }
                }
                current.setModificativa(false);
                current.setMiembroFirma(representanteCe.getNombreMiembro());
                current.setUsuarioInsercion(sessionView.getUsuario().getIdPersona().getUsuario());
                current.setFechaInsercion(LocalDateTime.now());
                current.setEstadoEliminacion(0l);
                current.setIdResolucionAdj(resolucionAdj);

                current = contratoRepo.createContrato(current);
                JsfUtil.mensajeInsert();
            }
        } catch (Exception e) {
            JsfUtil.mensajeError("Error en el registro del contrato del C.E.");
        }
    }

    public String prepareEdit() {
        idMunicipio = sessionView.getIdMunicipio();
        if (idMunicipio != null) {
            //actualizar anho y detalle proceso adquisicion
            limpiarCampos();
            showGarantiaUsoTela = false;
            codigoEntidad = "";
            current = new ContratosOrdenesCompra();
            oferta = null;
            deshabilitado = false;
            estadoEdicion = 2;
            continuar = true;
            garantiaAnticipo = false;
            garantiaContrato = false;
            garantiaUsoTela = false;
            actaAdj = false;
            notaAdj = false;
        } else {
            JsfUtil.mensajeAlerta("Debe de seleccionar un departamento y municipio.");
        }

        return null;
    }

    public void update() {
        try {
            if (noEditableRepCe) {
                representanteCe.setNombreMiembro(current.getMiembroFirma());
                representanteCe.setFechaModificacion(LocalDateTime.now());
                representanteCe.setUsuarioModificacion(sessionView.getUsuario().getIdPersona().getUsuario());

                entidadRepo.edit(representanteCe);
            }

            current.setUsuarioModificacion(sessionView.getUsuario().getIdPersona().getUsuario());
            current.setFechaModificacion(LocalDateTime.now());
            current = contratoRepo.editContrato(current);

            JsfUtil.mensajeUpdate();
        } catch (Exception e) {
            Logger.getLogger(ContratosOrdenesComprasView.class.getName()).log(Level.SEVERE, null, e);
            JsfUtil.mensajeError("Error en la actualización del contrato del C.E.");
        }
    }

    public void buscarEntidadEducativa() {
        diasPlazo = contratoRepo.findDiasPlazoPorRubro(rubro, sessionView.getIdAnho());
        limpiarCampos();
        if (codigoEntidad.length() == 5) {
            /**
             * Fecha: 30/08/2018 Comentario: Validación de seleccion del año y
             * proceso de adquisición
             */
            if (getRecuperarProceso().getProcesoAdquisicion() == null) {
                JsfUtil.mensajeAlerta("Debe de seleccionar un año y proceso de contratación.");
            } else {
                detalleProceso = JsfUtil.findDetalleByRubroAndAnho(sessionView.getProceso(), rubro, sessionView.getIdAnho());

                oferta = ofertaRepo.getOfertaByProcesoCodigoEntidad(codigoEntidad, detalleProceso);

                if (oferta == null) {
                    entidadEducativa = entidadRepo.findByPk(codigoEntidad);

                    if (entidadEducativa == null) {
                        JsfUtil.mensajeAlerta("No se ha encontrado el centro escolar con código: " + codigoEntidad);
                    } else {
                        JsfUtil.mensajeError("No existe un proceso de contratación para este centro escolar.");
                    }
                } else {
                    if (sessionView.getUsuario().getCodigoDepartamento().getId() != null) {
                        String dep = getRecuperarProceso().getDepartamento();
                        entidadEducativa = oferta.getCodigoEntidad();

                        if (entidadEducativa.getCodigoDepartamento().getId().equals(dep) || sessionView.getUsuario().getIdTipoUsuario().getIdTipoUsuario() == 1l) {
                            oferta = ofertaRepo.getOfertaByProcesoCodigoEntidad(codigoEntidad, detalleProceso);

                            if (oferta == null) {
                                JsfUtil.mensajeError("No existe un proceso de contratación para este centro escolar.");
                            } else {
                                List<Participante> lst = oferta.getParticipantesList();

                                for (int i = lst.size() - 1; i >= 0; i--) {
                                    if (lst.get(i).getEstadoEliminacion().compareTo(1l) == 0) {
                                        oferta.getParticipantesList().remove(lst.get(i));
                                    }
                                }

                                nombreEncargadoCompra = entidadRepo.getMiembro(codigoEntidad, "ENCARGADO DE COMPRA").getNombreMiembro();
                                //BUSCAR REPRESENTANTE DEL ORGANISMO DE ADMINISTRACION ESCOLAR
                                representanteCe = entidadRepo.getPresidenteOrganismoEscolar(codigoEntidad);
                                if (representanteCe == null) {
                                    JsfUtil.mensajeInformacion("No esta registrado el representante del organismo de administración escolar, pero lo puede registrar aqui.");
                                    crearRepresentante = true;
                                    noEditableRepCe = false;
                                } else {
                                    crearRepresentante = false;
                                }

                                showFechaOrdenInicio = !detalleProceso.getIdRubroAdq().getDescripcionRubro().contains("UNIFORME");
                            }
                        } else {
                            JsfUtil.mensajeAlerta("El codigo del centro escolar no pertenece al departamento " + JsfUtil.getNombreDepartamentoByCodigo(dep) + "<br/>"
                                    + "Departamento del CE: " + entidadEducativa.getCodigoEntidad() + " es " + entidadEducativa.getCodigoDepartamento().getNombreDepartamento());
                        }
                    }
                }
            }
        } else {
            entidadEducativa = null;
        }
    }

    public void buscarDocumentoLegal() {
        if (idParticipante != null && idParticipante.compareTo(0l) != 0) {
            continuar = true;
            participante = participanteRepo.findByPk(idParticipante);
            razonSocial = participante.getIdEmpresa().getRazonSocial();
            representanteLegal = participanteRepo.getRespresentanteLegalEmp(participante.getIdEmpresa().getIdPersona().getId());
            resolucionAdj = resoRepo.findResolucionesAdjudicativasByIdParticipante(idParticipante);

            if (resolucionAdj == null) {
                JsfUtil.mensajeAlerta("Este proveedor no posee adjudicaciones para este centro escolar.");
            } else {
                List<ContratosOrdenesCompra> lst = resoRepo.findContratoByResolucion(resolucionAdj);

                if (lst.size() > 1) {
                    JsfUtil.mensajeError("Existe un problema con el contrato seleccionado, por favor reportarlo al administrador del sistema.");
                    //resolucionAdjudicativaEJB.enviarCorreoDeError(resolucionAdj.getId(), JsfUtil.getSessionMailG("2"));
                } else {
                    ContratosOrdenesCompra contratoOrd = null;

                    if (lst.size() == 1) {
                        contratoOrd = lst.get(0);
                    }

                    showGarantiaUsoTela = (rubro.intValue() == 1 || rubro.intValue() == 4 || rubro.intValue() == 5);
                    lstDocumentosImp = catalogoRepo.getLstDocumentosImp(showGarantiaUsoTela, detalleProceso.getIdProcesoAdq().getIdAnho().getId().intValue());
                    showFechaOrdenInicio = !showGarantiaUsoTela;

                    switch (estadoEdicion) {
                        case 1: //BUSCAR RESOLUCION ADJUDICATIVA
                            if (contratoOrd == null) {
                                if (resolucionAdj.getIdEstadoReserva().getId().compareTo(2l) == 0) {
                                    continuar = false;
                                    if (current != null) {
                                        if (representanteCe != null) {
                                            current.setAnhoContrato(resolucionAdj.getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getIdAnho().getAnho());
                                            current.setPlazoPrevistoEntrega(diasPlazo.getDiasPlazo().longValue());

                                            current.setMiembroFirma(representanteCe.getNombreMiembro());
                                            seleccionarDocumentosAImprimir();
                                        }
                                    }
                                } else {
                                    limpiarCampos();
                                    JsfUtil.mensajeAlerta("Esta reserva de fondo se encuentra en estado de " + resolucionAdj.getIdEstadoReserva().getDescripcionReserva()
                                            + ".<br/>Ver historico de cambios. <a onclick=\"PF('dlgHistorialCambiosReserva').show();\">Ver</a>");
                                    resolucionAdj = null;
                                }
                            } else {
                                JsfUtil.mensajeAlerta("Ya existe el contrato para el proveedor y centro escolar seleccionados.");
                            }
                            break;
                        case 2: //BUSCAR CONTRATO
                            if (contratoOrd == null) {
                                JsfUtil.mensajeAlerta("No hay contrato para el proveedor y centro escolar seleccionados.");
                                resolucionAdj = null;

                            } else {
                                if ((sessionView.getUsuario().getIdPersona().getUsuario().equals("MISANCHEZ")
                                        || sessionView.getUsuario().getIdPersona().getUsuario().equals("RAFAARIAS"))
                                        && resolucionAdj.getIdEstadoReserva().getId().compareTo(5l) == 0) {
                                    buscarHistorialCambios();
                                    PrimeFaces.current().ajax().update("dlgHistorialCambiosReserva");
                                    JsfUtil.mensajeAlerta("Esta reserva de fondo se encuentra en estado de " + resolucionAdj.getIdEstadoReserva().getDescripcionReserva()
                                            + ".<br/>Ver historico de cambios. <a onclick=\"PF('dlgHistorialCambiosReserva').show();\">Ver</a>");
                                    current = contratoOrd;
                                    continuar = true;
                                    deshabilitado = true;
                                } else if (resolucionAdj.getIdEstadoReserva().getId().compareTo(2l) == 0
                                        || resolucionAdj.getIdEstadoReserva().getId().compareTo(5l) == 0) {
                                    switch (resolucionAdj.getIdEstadoReserva().getId().intValue()) {
                                        case 2:
                                        case 5:
                                            current = contratoOrd;
                                            soloLectura = (resolucionAdj.getIdEstadoReserva().getId().intValue() == 5);
                                            continuar = false;
                                            if (current.getPlazoPrevistoEntrega() == null) {
                                                current.setPlazoPrevistoEntrega(diasPlazo.getDiasPlazo().longValue());
                                            }
                                            seleccionarDocumentosAImprimir();
                                            break;
                                        default:
                                            JsfUtil.mensajeAlerta("La reserva de fondos NO ESTA APLICADA.");
                                            break;
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        }
    }

    private void seleccionarDocumentosAImprimir() {
        if (detalleProceso.getIdProcesoAdq().getIdAnho().getId().intValue() > 8) {
            lstSelectDocumentosImp.add(12);
            lstSelectDocumentosImp.add(11);
        }

        lstSelectDocumentosImp.add(10);
        lstSelectDocumentosImp.add(7);
        lstSelectDocumentosImp.add(5);
        lstSelectDocumentosImp.add(4);
        lstSelectDocumentosImp.add(3);
        lstSelectDocumentosImp.add(13);
        lstSelectDocumentosImp.add(2);

        if (showGarantiaUsoTela) {
            lstSelectDocumentosImp.add(6);
        }
        PrimeFaces.current().ajax().update("dvDocumentos");
    }

//    private void setPlazoPrevistoEntrega() {
//        switch (detalleProceso.getIdRubroAdq().getId().intValue()) {
//            case 1:
//            case 4:
//            case 5:
//                current.setPlazoPrevistoEntrega(60l);
//                break;
//            case 2:
//                if (detalleProceso.getIdProcesoAdq().getDescripcionProcesoAdq().contains("MODALIDADES FLEXIBLES")) {
//                    current.setPlazoPrevistoEntrega(15l);
//
//                } else {
//                    current.setPlazoPrevistoEntrega(30l);
//                }
//                break;
//            case 3:
//                current.setPlazoPrevistoEntrega(60l);
//                break;
//        }
//    }

    public List<JasperPrint> imprimirDesdeModificativa(List<RptDocumentos> lstRptDocumentos, Boolean perNatural, ContratosOrdenesCompra resolucionAdj, String codigoEnt) {
        representanteCe = entidadRepo.getPresidenteOrganismoEscolar(codigoEnt);
        current = resolucionAdj;
        this.resolucionAdj = resolucionAdj.getIdResolucionAdj();
        return imprimir(lstRptDocumentos, perNatural);
    }

    public List<JasperPrint> imprimir(List<RptDocumentos> lstRptDocumentos, Boolean perNatural) {
        String nombreRpt;
        Boolean sobredemanda;
        ServletContext ctx;
        JasperPrint rptTemp;
        HashMap param = new HashMap();
        List<JasperPrint> lstRptAImprimir = new ArrayList();

        ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

        param.put("ubicacionImagenes", ctx.getRealPath(Reportes.PATH_IMAGENES) + File.separator);
        param.put("pEncargadoDeCompras", nombreEncargadoCompra);
        param.put("pMiembro", representanteCe.getNombreMiembro());
        param.put("pEmail", current.getIdResolucionAdj().getIdParticipante().getIdEmpresa().getIdPersona().getEmail());
        param.put("pNumContrato", "ME-" + current.getNumeroContrato() + "/" + detalleProceso.getIdProcesoAdq().getIdAnho().getAnho() + "/COD:" + codigoEntidad);
        sobredemanda = detalleProceso.getIdProcesoAdq().getDescripcionProcesoAdq().contains("SOBREDEMANDA");

        for (RptDocumentos rptDoc : lstRptDocumentos) {

            param = JsfUtil.getNombreRubroRpt(detalleProceso.getIdRubroAdq().getId(), param, sobredemanda, detalleProceso.getIdProcesoAdq().getDescripcionProcesoAdq().contains("FLEXIBLE"));

            switch (rptDoc.getDs()) {
                case 0://JRBeanColletions
                    switch (rptDoc.getIdTipoRpt().getIdTipoRpt()) {
                        case 2://Solicitud de Cotizacion

                            String anho = "";
                            /**
                             * modificacion 01/sep2021 apartir de proceso 2022,
                             * se evaluará el monto del contrato para determinar
                             * si supera los $7,300 se imprimira las
                             * cotizaciones de todos los proveedores Si el
                             * contrato no supera el monto, solo se imprimirá la
                             * cotización del proveedor actual.
                             */

                            if (detalleProceso.getIdProcesoAdq().getIdAnho().getId().intValue() > 9) {
                                if (current.getIdResolucionAdj().getIdParticipante().getMonto().compareTo(new BigDecimal("7300")) == 1) {
                                    for (Participante par : current.getIdResolucionAdj().getIdParticipante().getIdOferta().getParticipantesList()) {
                                        List<VwCotizacion> lst = ofertaRepo.getLstCotizacion(sessionView.getNombreMunicipio(), codigoEntidad, detalleProceso, par);

                                        //Para contratos antes de 2016, se tomara los formatos de rpt que no incluyen el año en el nombre del archivo jasper
                                        if (Integer.parseInt(detalleProceso.getIdProcesoAdq().getIdAnho().getAnho()) > 2016) {
                                            anho = detalleProceso.getIdProcesoAdq().getIdAnho().getAnho();
                                        }

                                        //param = JsfUtil.getNombreRubroRpt(detalleProceso.getIdRubroAdq().getIdRubroInteres().toBigInteger().intValue(), param, sobredemanda, detalleProceso.getIdProcesoAdq().getDescripcionProcesoAdq().contains("FLEXIBLE"));
                                        rptTemp = reporteRepo.getRpt(param, reportes.getPathReporte(rptDoc.getNombreRpt() + anho + ".jasper"), lst);
                                        lstRptAImprimir.add(rptTemp);
                                    }
                                } else {
                                    Participante par = current.getIdResolucionAdj().getIdParticipante();
                                    List<VwCotizacion> lst = ofertaRepo.getLstCotizacion(sessionView.getNombreMunicipio(), codigoEntidad, detalleProceso, par);

                                    //Para contratos antes de 2016, se tomara los formatos de rpt que no incluyen el año en el nombre del archivo jasper
                                    if (Integer.parseInt(detalleProceso.getIdProcesoAdq().getIdAnho().getAnho()) > 2016) {
                                        anho = detalleProceso.getIdProcesoAdq().getIdAnho().getAnho();
                                    }

                                    //param = JsfUtil.getNombreRubroRpt(detalleProceso.getIdRubroAdq().getIdRubroInteres().toBigInteger().intValue(), param, sobredemanda, detalleProceso.getIdProcesoAdq().getDescripcionProcesoAdq().contains("FLEXIBLE"));
                                    rptTemp = reporteRepo.getRpt(param, reportes.getPathReporte(rptDoc.getNombreRpt() + anho + ".jasper"), lst);
                                    lstRptAImprimir.add(rptTemp);
                                }
                            } else {
                                for (Participante par : current.getIdResolucionAdj().getIdParticipante().getIdOferta().getParticipantesList()) {
                                    List<VwCotizacion> lst = ofertaRepo.getLstCotizacion(sessionView.getNombreMunicipio(), codigoEntidad, detalleProceso, par);

                                    //Para contratos antes de 2016, se tomara los formatos de rpt que no incluyen el año en el nombre del archivo jasper
                                    if (Integer.parseInt(detalleProceso.getIdProcesoAdq().getIdAnho().getAnho()) > 2016) {
                                        anho = detalleProceso.getIdProcesoAdq().getIdAnho().getAnho();
                                    }

                                    //param = JsfUtil.getNombreRubroRpt(detalleProceso.getIdRubroAdq().getIdRubroInteres().toBigInteger().intValue(), param, sobredemanda, detalleProceso.getIdProcesoAdq().getDescripcionProcesoAdq().contains("FLEXIBLE"));
                                    rptTemp = reporteRepo.getRpt(param, reportes.getPathReporte(rptDoc.getNombreRpt() + anho + ".jasper"), lst);
                                    lstRptAImprimir.add(rptTemp);
                                }
                            }

                            break;
                        
                        case 5://Garantia Cumplimiento
                        case 6://Garantia Uso Tela
                            lstRptAImprimir.add(reporteRepo.getRpt(param, reportes.getPathReporte(rptDoc.getNombreRpt() + ".jasper"), resoRepo.generarRptGarantia(current.getIdResolucionAdj().getId(), current.getId())));
                            break;
                        case 7://Contrato

                            param.put("SUBREPORT_DIR", JsfUtil.getPathReportes().concat(Reportes.PATH_REPORTES + "contratos") + File.separator);
                            param.put("idContrato", new BigDecimal(current.getId()));
                            param.put("ubicacionImagenes", ctx.getRealPath(Reportes.PATH_IMAGENES) + File.separator);
                            param.put("telDirector", (representanteCe.getTelDirector() == null ? "" : representanteCe.getTelDirector()));
                            if (!getSelected().getIdResolucionAdj().getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdRubroAdq().getDescripcionRubro().contains("UNIFORME")) {
                                if (getSelected().getFechaOrdenInicio() != null) {
                                    param.put("P_FECHA_INICIO", Herramientas.getNumDia(getSelected().getFechaOrdenInicio()) + " días del mes de " + Herramientas.getNomMes(getSelected().getFechaOrdenInicio()) + " del año  " + Herramientas.getNumAnyo(getSelected().getFechaOrdenInicio()));
                                } else {
                                    param.put("P_FECHA_INICIO", "SIN DEFINIR");
                                }
                            }

                            nombreRpt = rptDoc.getNombreRpt().concat(perNatural ? "Nat" : "Jur");
                            rptTemp = reporteRepo.getRpt(param, reportes.getPathReporte(nombreRpt + ".jasper"), resoRepo.generarContrato(current, current.getIdResolucionAdj().getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdRubroAdq().getId()));
                            lstRptAImprimir.add(rptTemp);
                            lstRptAImprimir.add(rptTemp);
                            break;
                        case 11: //oferta Global
                            Empresa emp = resolucionAdj.getIdParticipante().getIdEmpresa();
                            DetRubroMuestraIntere detRubro = participanteRepo.findDetByNitAndIdAnho(emp.getNumeroNit(), detalleProceso.getIdProcesoAdq().getIdAnho().getAnho());
                            CapaInstPorRubro capacidadInst = participanteRepo.findDetProveedor(detRubro, detalleProceso.getIdProcesoAdq().getId(), CapaInstPorRubro.class);

                            lstRptAImprimir.addAll(reportes.getReporteOfertaDeProveedor(capacidadInst, resolucionAdj.getIdParticipante().getIdEmpresa(), detalleProceso,
                                    reporteRepo.getLstOfertaGlobal(resolucionAdj.getIdParticipante().getIdEmpresa().getNumeroNit(), detalleProceso.getIdRubroAdq().getId(), detalleProceso.getIdProcesoAdq().getIdAnho().getId()),
                                    reporteRepo.getDeclaracionJurada(resolucionAdj.getIdParticipante().getIdEmpresa(), detalleProceso.getIdRubroAdq().getId(), detalleProceso.getIdProcesoAdq().getIdAnho().getId(), sessionView.getNombreMunicipio())));
                            break;
                        case 13://Acta de recomendacion
                            param.put("pPorcentajeCapa", detalleProceso.getIdRubroAdq().getIdRubroUniforme().intValue() == 1 ? "25" : "35");
                            param.put("pPorcentajeGeo", detalleProceso.getIdRubroAdq().getIdRubroUniforme().intValue() == 1 ? "35" : "25");
                            param.put("pPorcentajePrecio", detalleProceso.getIdRubroAdq().getId().intValue() == 2 ? "45" : "40");
                            param.put("pAnho", detalleProceso.getIdProcesoAdq().getIdAnho().getAnho());
                            param.put("SUBREPORT_DIR", JsfUtil.getPathReportes().concat(Reportes.PATH_REPORTES + "notasactas") + File.separator);

                            if (detalleProceso.getIdProcesoAdq().getIdAnho().getId().intValue() >= 10
                                    && detalleProceso.getIdRubroAdq().getId().intValue() == 3) {
                                rptTemp = reporteRepo.getRpt(param, reportes.getPathReporte(rptDoc.getNombreRpt() + "Zap.jasper"), resoRepo.generarRptActaRecomendacion(current.getIdResolucionAdj().getId()));
                            } else {
                                rptTemp = reporteRepo.getRpt(param, reportes.getPathReporte(rptDoc.getNombreRpt() + ".jasper"), resoRepo.generarRptActaRecomendacion(current.getIdResolucionAdj().getId()));
                            }

                            lstRptAImprimir.add(rptTemp);
                            break;
                    }
                    break;
                case 1://DSConnection SQL
                    switch (rptDoc.getIdTipoRpt().getIdTipoRpt()) {
                        case 3://Acta Adjudicacion

                            param.put("SUBREPORT_DIR", JsfUtil.getPathReportes().concat(Reportes.PATH_REPORTES + "notasactas") + File.separator);
                            param.put("pPorcentajeCapa", detalleProceso.getIdRubroAdq().getIdRubroUniforme().intValue() == 1 ? "25" : "35");
                            param.put("pPorcentajeGeo", detalleProceso.getIdRubroAdq().getIdRubroUniforme().intValue() == 1 ? "35" : "25");
                            param.put("pPorcentajePrecio", detalleProceso.getIdRubroAdq().getId().intValue() == 2 ? "45" : "40");
                            param.put("pIdOferta", current.getIdResolucionAdj().getIdParticipante().getIdOferta().getId());
                            rptTemp = reporteRepo.getRpt(param, reportes.getPathReporte(rptDoc.getNombreRpt() + ".jasper"));

                            lstRptAImprimir.add(rptTemp);
                            break;
                        case 4://Nota Adjudicacion

                            param.put("SUBREPORT_DIR", JsfUtil.getPathReportes().concat(Reportes.PATH_REPORTES + "notasactas") + File.separator);
                            param.put("pIdResolucionAdj", current.getIdResolucionAdj().getId());
                            rptTemp = reporteRepo.getRpt(param, reportes.getPathReporte(rptDoc.getNombreRpt() + ".jasper"));

                            lstRptAImprimir.add(rptTemp);
                            lstRptAImprimir.add(rptTemp);
                            break;
                        case 10://Declaracion adjudicatorio
                            param.put("idContrato", new BigDecimal(current.getId()));
                            param.put("ubicacionImagenes", ctx.getRealPath(Reportes.PATH_IMAGENES) + File.separator);
                            param.put("pAnho", detalleProceso.getIdProcesoAdq().getIdAnho().getAnho());

                            nombreRpt = rptDoc.getNombreRpt().concat(perNatural ? "PerNat" : "PerJur").concat(param.get("pAnho").toString());
                            rptTemp = reporteRepo.getRpt(param, reportes.getPathReporte(nombreRpt + ".jasper"));
                            lstRptAImprimir.add(rptTemp);
                            break;
                        case 7://Contrato
                            Boolean bachillerato = false;
                            Boolean libros = false;

                            if (detalleProceso.getIdRubroAdq().getDescripcionRubro().contains("UNIFORME")) {
                                for (DetalleOferta detalleOfertas : resolucionAdj.getIdParticipante().getDetalleOfertasList()) {
                                    if (detalleOfertas.getEstadoEliminacion().intValue() == 0) {
                                        if (detalleOfertas.getConsolidadoEspTec().contains("BACHILLERATO")) {
                                            bachillerato = true;
                                            break;
                                        }
                                    }
                                }
                            } else if (detalleProceso.getIdRubroAdq().getId().intValue() == 2) {
                                for (DetalleOferta detalleOfertas : resolucionAdj.getIdParticipante().getDetalleOfertasList()) {
                                    if (detalleOfertas.getEstadoEliminacion().intValue() == 0) {
                                        if (detalleOfertas.getConsolidadoEspTec().contains("Libro")) {
                                            libros = true;
                                            break;
                                        }
                                    }
                                }
                            }

                            //adición de aclaracion al contrato de 2do uniforme para el año 2019 
                            if (detalleProceso.getIdProcesoAdq().getIdAnho().getId().intValue() > 6) {
                                if (detalleProceso.getId() == 41) {
                                    param.put("pAclaracion", RESOURCE_BUNDLE.getString("aclaracionContrato2019") + " ");
                                } else {
                                    param.put("pAclaracion", ", conforme a las cláusulas que a continuación se especifican. ");
                                }
                            }

                            param.put("SUBREPORT_DIR", JsfUtil.getPathReportes().concat(Reportes.PATH_REPORTES + "contratos") + File.separator);
                            param.put("idContrato", new BigDecimal(current.getId()));
                            param.put("ubicacionImagenes", ctx.getRealPath(Reportes.PATH_IMAGENES) + File.separator);
                            param.put("telDirector", (representanteCe.getTelDirector() == null ? "" : representanteCe.getTelDirector()));
                            param.put("bachillerato", bachillerato);
                            param.put("libros", libros);
                            param.put("idResolucion", new BigDecimal(resolucionAdj.getId()));
                            if (!getSelected().getIdResolucionAdj().getIdParticipante().getIdOferta().getIdDetProcesoAdq().getIdRubroAdq().getDescripcionRubro().contains("UNIFORME")) {
                                if (getSelected().getFechaOrdenInicio() != null) {
                                    param.put("P_FECHA_INICIO", Herramientas.getNumDia(getSelected().getFechaOrdenInicio()) + " días del mes de " + Herramientas.getNomMes(getSelected().getFechaOrdenInicio()) + " del año  " + Herramientas.getNumAnyo(getSelected().getFechaOrdenInicio()));
                                } else {
                                    param.put("P_FECHA_INICIO", "SIN DEFINIR");
                                }
                            }
                            
                            nombreRpt = rptDoc.getNombreRpt().concat(perNatural ? "Nat" : "Jur");
                            rptTemp = reporteRepo.getRpt(param, reportes.getPathReporte(nombreRpt + ".jasper"));

                            lstRptAImprimir.add(rptTemp);
                            lstRptAImprimir.add(rptTemp);
                            break;
                        case 12:
                            param.put("pIdContrato", new BigDecimal(current.getId()));
                            rptTemp = reporteRepo.getRpt(param, reportes.getPathReporte(rptDoc.getNombreRpt() + ".jasper"));

                            lstRptAImprimir.add(rptTemp);
                            break;
                    }
                    break;
            }
        }

        return lstRptAImprimir;
    }

    public void impDocumentos() {
        List<RptDocumentos> lstRptDocumentos;
        Boolean isPersonaNat;

        if (getSelected() != null && getSelected().getId() != null) {
            if (lstSelectDocumentosImp.isEmpty()) {
                JsfUtil.mensajeAlerta("Debe de seleccionar un documento para poder ser impreso.");
            } else {
                lstRptDocumentos = resoRepo.getDocumentosAImprimir(detalleProceso.getId(), lstSelectDocumentosImp);

                if (lstRptDocumentos.isEmpty() && lstSelectDocumentosImp.isEmpty()) {
                    JsfUtil.mensajeAlerta("No se han definidos los documentos a imprimir para este proceso.");
                } else {
                    try {
                        isPersonaNat = (current.getIdResolucionAdj().getIdParticipante().getIdEmpresa().getIdPersoneria().getId().intValue() == 1);
                        try {
                            reportes.generarReporte(imprimir(lstRptDocumentos, isPersonaNat), "documentos_" + codigoEntidad);
                        } catch (IOException ex) {
                            Logger.getLogger(ContratosOrdenesComprasView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (JRException ex) {
                        Logger.getLogger(ContratosOrdenesComprasView.class.getName()).log(Level.WARNING, "Error en generacion del reporte id_resolucion: {0}", current.getIdResolucionAdj().getId());
                    }
                }
            }
        } else {
            JsfUtil.mensajeAlerta("Primero debe de guardar el contrato antes de imprimirlo");
        }
    }

    public void imprimirAnalisisEconomico() {
        if (current.getId() != null) {
            OfertaBienesServicio ofe = getSelected().getIdResolucionAdj().getIdParticipante().getIdOferta();
            if (ofe == null) {
                JsfUtil.mensajeAlerta("Primero debe de guardar la oferta!!!");
            } else {
                SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
                List lst = ofertaRepo.getDatosRptAnalisisEconomico(ofe.getCodigoEntidad().getCodigoEntidad(), ofe.getIdDetProcesoAdq());
                Bean2Excel oReport = new Bean2Excel(lst, detalleProceso.getIdRubroAdq().getDescripcionRubro(), entidadEducativa.getNombre(), entidadEducativa.getCodigoEntidad(), "", sd.format(ofe.getFechaApertura()), getSelected().getUsuarioInsercion());
                oReport.createFile(ofe.getCodigoEntidad().getCodigoEntidad(), representanteCe.getNombreMiembro(), nombreEncargadoCompra);
            }
        } else {
            JsfUtil.mensajeAlerta("Primero debe de guardar la oferta!!!");
        }
    }

    public void buscarHistorialCambios() {
        lstHistorialCambios = resoRepo.findHistorialByIdResolucionAdj(resolucionAdj.getId());
    }

    public JasperPrint rptCotizacion(Participante par) {
        String anho = "";
        String nombreRpt = "";
        HashMap param = new HashMap();
        List<VwCotizacion> lst = ofertaRepo.getLstCotizacion(sessionView.getNombreMunicipio(), codigoEntidad, detalleProceso, par);
        Boolean sobredemanda = getRecuperarProceso().getProcesoAdquisicion().getDescripcionProcesoAdq().contains("SOBREDEMANDA");

        //Para contratos antes de 2016, se tomara los formatos de rpt que no incluyen el año en el nombre del archivo jasper
        if (Integer.parseInt(detalleProceso.getIdProcesoAdq().getIdAnho().getAnho()) > 2016) {
            anho = detalleProceso.getIdProcesoAdq().getIdAnho().getAnho();
        }

        switch (detalleProceso.getIdRubroAdq().getId().intValue()) {
            case 1:
            case 4:
            case 5:
                nombreRpt = "rptCotizacionUni" + anho + ".jasper";
                break;
            case 2:
                nombreRpt = "rptCotizacionUti" + anho + ".jasper";
                break;
            case 3:
                if (getRecuperarProceso().getProcesoAdquisicion().getDescripcionProcesoAdq().contains("MINI")) {
                    nombreRpt = "rptCotizacionZap" + anho + "_mini.jasper";
                } else {
                    nombreRpt = "rptCotizacionZap" + anho + ".jasper";
                }
        }
        param = JsfUtil.getNombreRubroRpt(detalleProceso.getIdRubroAdq().getId(), param, sobredemanda, detalleProceso.getIdProcesoAdq().getDescripcionProcesoAdq().contains("FLEXIBLE"));
        param.put("ubicacionImagenes", ContratosOrdenesComprasView.class.getClassLoader().getResource(("sv/gob/mined/apps/reportes/cotizacion" + File.separator + nombreRpt)).getPath().replace(nombreRpt, ""));

        return reportes.generarRptBeanConnection(lst, param, "sv/gob/mined/apps/reportes/cotizacion", nombreRpt);
    }
}
