package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang.math.NumberUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import sv.gob.mined.pescolar.model.CatalogoProducto;
import sv.gob.mined.pescolar.model.DetalleOferta;
import sv.gob.mined.pescolar.model.DetalleProcesoAdq;
import sv.gob.mined.pescolar.model.EstadoReserva;
import sv.gob.mined.pescolar.model.NivelEducativo;
import sv.gob.mined.pescolar.model.OfertaBienesServicio;
import sv.gob.mined.pescolar.model.Participante;
import sv.gob.mined.pescolar.model.PreciosRefRubroEmp;
import sv.gob.mined.pescolar.model.ResolucionesAdjudicativa;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.model.TechoRubroEntEdu;
import sv.gob.mined.pescolar.model.view.VwCatalogoEntidadEducativa;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.NivelEducativoRepo;
import sv.gob.mined.pescolar.repository.OfertaRepo;
import sv.gob.mined.pescolar.repository.ParticipanteRepo;
import sv.gob.mined.pescolar.repository.PrecioRefRubroEmpRepo;
import sv.gob.mined.pescolar.repository.ResolucionesAdjudicativasRepo;
import sv.gob.mined.pescolar.utils.JsfUtil;
import sv.gob.mined.pescolar.utils.db.Filtro;
import sv.gob.mined.pescolar.utils.enums.TipoOperador;

/**
 *
 * @author misanchez
 */
@SuppressWarnings("serial")
@Named
@ViewScoped
public class DetalleOfertaView implements Serializable {

    private int rowEdit = 0;
    private Long tmpIdNivel = 0l;
    private Long idParticipante;
    private Long idRubro = 0l;
    private Long idEstadoReserva = 0l;
    private String codigoEntidad;
    private String numItem;
    private String comentarioReversion;
    private String msjError = "";
    private Boolean positivo = false;
    private Boolean editable = true;
    private Boolean mostrarMsjPrecio = false;
    private Boolean activarFiltro = false;
    private Boolean mostrarMsj = false;

    private BigInteger cantidadTotal = BigInteger.ZERO;
    private BigInteger cantidadTotalResguardo = BigInteger.ZERO;

    private BigDecimal montoTotal = BigDecimal.ZERO;
    private BigDecimal saldoActual = BigDecimal.ZERO;

    private List<Long> lstNiveles = new ArrayList();

    private List<Filtro> params = new ArrayList();

    private CatalogoProducto item;
    private DetalleProcesoAdq detalleProceso;
    private DetalleOferta detalleSeleccionado;
    private NivelEducativo nivel;
    private Participante participante;
    private PreciosRefRubroEmp precio;
    private ResolucionesAdjudicativa resAdj;
    private TechoRubroEntEdu techo;
    private VwCatalogoEntidadEducativa entidadEducativa;

    private List<PreciosRefRubroEmp> lstPreciosEmp = new ArrayList<>();
    private List<Participante> lstParticipantes = new ArrayList<>();
    //private List<DetalleOferta> lstDetalle = new ArrayList<>();

    @Inject
    private OfertaRepo ofertaRepo;
    @Inject
    private ParticipanteRepo participanteRepo;
    @Inject
    private ResolucionesAdjudicativasRepo resolucionRepo;
    @Inject
    private CatalogoRepo catalogoRepo;
    @Inject
    private NivelEducativoRepo NivelEducativoRepo;
    @Inject
    private SessionView sessionView;
    @Inject
    private PrecioRefRubroEmpRepo precioRefRubroEmpRepo;
    @Inject
    private RepositorioAplicacionView repositorioAplicacionView;

    public DetalleOfertaView() {
    }

    @PostConstruct
    public void init() {
        Map<String, String> parametros = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parametros.containsKey("idParticipante")) {
            //recuperación de techo presupuestario del ce
            activarFiltro = true;
            participante = catalogoRepo.findEntityByPk(Participante.class, Long.parseLong(parametros.get("idParticipante")));
            codigoEntidad = participante.getIdOferta().getCodigoEntidad().getCodigoEntidad();
            detalleProceso = participante.getIdOferta().getIdDetProcesoAdq();

            lstNiveles = ofertaRepo.getLstNivelesConMatriculaReportadaByIdProcesoAdqAndCodigoEntidad(detalleProceso.getIdProcesoAdq().getId(), codigoEntidad);

            if (!lstNiveles.isEmpty()) {
                params.clear();
                params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "codigoEntidad", participante.getIdOferta().getCodigoEntidad().getCodigoEntidad()).build());
                params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idDetProcesoAdq", participante.getIdOferta().getIdDetProcesoAdq()).build());

                List<TechoRubroEntEdu> lstTecho = (List<TechoRubroEntEdu>) catalogoRepo.findListByParam(TechoRubroEntEdu.class, params);
                if (lstTecho.isEmpty()) {
                    JsfUtil.mensajeAlerta("No hay presupuesto para este Centro Educativo");
                    return;
                }
                techo = (TechoRubroEntEdu) catalogoRepo.findListByParam(TechoRubroEntEdu.class, params).get(0);

                if (techo != null && techo.getId() != null) {
                    //recuperando reserva de fondos
                    params.clear();
                    params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idParticipante.id", Long.parseLong(parametros.get("idParticipante"))).build());

                    resAdj = catalogoRepo.findByParam(ResolucionesAdjudicativa.class, params);
                    lstPreciosEmp = precioRefRubroEmpRepo.findPreciosByEmp(participante.getIdEmpresa().getId(), participante.getIdOferta().getIdDetProcesoAdq().getIdRubroAdq().getId(), participante.getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getIdAnho().getId());

                    if (resAdj == null) {
                        crearReserva();
                    } else {

                        idEstadoReserva = resAdj.getIdEstadoReserva().getId();

                        switch (resAdj.getIdEstadoReserva().getId().intValue()) {
                            case 1:
                            case 3:
                                editable = true;
                                if (participante.getDetalleOfertasList().isEmpty()) {
                                    agregarTodosLosItems();
                                }
                                break;
                            default:
                                editable = false;
                                break;
                        }
                    }
                } else {
                    msjError = "Este centro no tiene prespuesto asignado";
                    mostrarMsj = true;
                }
            } else {
                msjError = "Este centro no tiene matricula registrada";
                mostrarMsj = true;
            }
        }
    }

    public void editarOferta() {
        //activarFiltro = true;
        codigoEntidad = "";
        entidadEducativa = new VwCatalogoEntidadEducativa();
        participante = new Participante();
    }

    private void crearReserva() {
        mostrarMsjPrecio = false;
        mostrarMsj = false;
        editable = true;
        idEstadoReserva = 1l; //digitada
        resAdj = new ResolucionesAdjudicativa();
        resAdj.setIdParticipante(participante);
        resAdj.setValor(BigDecimal.ZERO);
        resAdj.setUsuarioInsercion(sessionView.getUsuario().getIdPersona().getUsuario());
        resAdj.setFechaInsercion(LocalDateTime.now());
        resAdj.setEstadoEliminacion(0l);
        resAdj.setIdEstadoReserva(catalogoRepo.findEntityByPk(EstadoReserva.class, idEstadoReserva));

        //en el momento de creación del detalle de oferta, se agregaran todos los items calificados del proveedor
        //seleccionado con el objetivo de facilitar el ingreso de esta información
        if (!lstPreciosEmp.isEmpty()) {
            if (participante.getDetalleOfertasList().isEmpty()) {
                agregarTodosLosItems();
            }
            participante.getResolucionesAdjudicativaList().add(resAdj);
            participanteRepo.update(participante);
        } else {
            //bandera para monstrar mensaje que el proveedor no tiene precios de referencia ingresados
            mostrarMsjPrecio = true;
        }
    }

    private void agregarTodosLosItems() {
        for (PreciosRefRubroEmp preRefEmp : lstPreciosEmp) {
            if (preRefEmp.getIdProducto().getId().intValue() != 1) {
                for (Long idNivel : lstNiveles) {
                    Long temIdNivel = idNivel;

                    if (preRefEmp.getIdNivelEducativo().getId().compareTo(temIdNivel) == 0) {
                        DetalleOferta det = new DetalleOferta();
                        det.setNoItem(preRefEmp.getNoItem());
                        det.setIdNivelEducativo(preRefEmp.getIdNivelEducativo());
                        det.setIdProducto(preRefEmp.getIdProducto());
                        det.setConsolidadoEspTec(preRefEmp.getIdProducto().toString() + ", " + preRefEmp.getIdNivelEducativo().toString());
                        det.setCantidad(BigInteger.ZERO);
                        det.setPrecioUnitario(preRefEmp.getPrecioReferencia());
                        det.setEstadoEliminacion(0l);
                        det.setUsuarioInsercion(sessionView.getUsuario().getIdPersona().getUsuario());
                        det.setFechaInsercion(LocalDateTime.now());
                        det.setModificativa(0l);
                        det.setIdParticipante(participante);

                        participante.getDetalleOfertasList().add(det);
                        break;
                    }
                }
            }
        }
    }

    public String getMsjError() {
        return msjError;
    }

    public Boolean getMostrarMsj() {
        return mostrarMsj;
    }

    public Long getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(Long idParticipante) {
        this.idParticipante = idParticipante;
    }

    public String getComentarioReversion() {
        return comentarioReversion;
    }

    public void setComentarioReversion(String comentarioReversion) {
        this.comentarioReversion = comentarioReversion;
    }

    public String getCodigoEntidad() {
        return codigoEntidad;
    }

    public void setCodigoEntidad(String codigoEntidad) {
        this.codigoEntidad = codigoEntidad;
    }

    public List<Participante> getLstParticipantes() {
        return lstParticipantes;
    }

    /*public List<DetalleOferta> getLstDetalle() {
        return lstDetalle;
    }*/
    public void setLstParticipantes(List<Participante> lstParticipantes) {
        this.lstParticipantes = lstParticipantes;
    }

    public Long getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(Long idRubro) {
        this.idRubro = idRubro;
    }

    public VwCatalogoEntidadEducativa getEntidadEducativa() {
        return entidadEducativa;
    }

    public void setEntidadEducativa(VwCatalogoEntidadEducativa entidadEducativa) {
        this.entidadEducativa = entidadEducativa;
    }

    public Boolean getActivarFiltro() {
        return activarFiltro;
    }

    public void setActivarFiltro(Boolean activarFiltro) {
        this.activarFiltro = activarFiltro;
    }

    public Long getIdEstadoReserva() {
        return idEstadoReserva;
    }

    public void setIdEstadoReserva(Long idEstadoReserva) {
        this.idEstadoReserva = idEstadoReserva;
    }

    public Boolean getPositivo() {
        return positivo;
    }

    public Boolean getMostrarMsjPrecio() {
        return mostrarMsjPrecio;
    }

    public Boolean getEditable() {
        return editable;
    }

    public int getRowEdit() {
        return rowEdit;
    }

    public void setRowEdit(int rowEdit) {
        this.rowEdit = rowEdit;
    }

    public DetalleOferta getDetalleSeleccionado() {
        return detalleSeleccionado;
    }

    public void setDetalleSeleccionado(DetalleOferta detalleSeleccionado) {
        this.detalleSeleccionado = detalleSeleccionado;
    }

    public ResolucionesAdjudicativa getResAdj() {
        return resAdj;
    }

    public void setResAdj(ResolucionesAdjudicativa resAdj) {
        this.resAdj = resAdj;
    }

    public List<RubrosAmostrarInteres> getLstRubros() {
        return catalogoRepo.findAllRubrosByIdProceso(sessionView.getIdProcesoAdq());
    }

    public List<EstadoReserva> getLstEstadoReserva() {
        return catalogoRepo.findAllEstadoReserva();
    }

    public TechoRubroEntEdu getTecho() {
        return techo;
    }

    public Participante getParticipante() {
        return participante;
    }

    public BigDecimal getMontoContrato() {
        montoTotal = BigDecimal.ZERO;

        if (participante != null) {
            participante.getDetalleOfertasList().forEach(detalleOferta -> {
                montoTotal = montoTotal.add(detalleOferta.getPrecioUnitario().multiply(new BigDecimal(detalleOferta.getCantidad())));
            });
        }

        return montoTotal;
    }

    public BigInteger getCantidadContrato() {
        cantidadTotal = BigInteger.ZERO;
        if (participante != null) {
            participante.getDetalleOfertasList().forEach(detalleOferta -> {
                cantidadTotal = cantidadTotal.add(detalleOferta.getCantidad());
            });
        }

        return cantidadTotal;
    }

    public BigDecimal getMontoSaldo() {
        if (participante == null || participante.getId() == null) {
            return BigDecimal.ZERO;
        }
        if (techo != null) {
            if (resAdj == null || resAdj.getId() == null || resAdj.getIdEstadoReserva().getId().compareTo(2l) == 0) {
                saldoActual = techo.getMontoDisponible();
            } else {
                saldoActual = techo.getMontoDisponible().add(getMontoContrato().negate());
            }

            positivo = saldoActual.compareTo(BigDecimal.ZERO) == 1;
            return saldoActual;
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getMontoTotal() {
        BigDecimal total = BigDecimal.ZERO;
        List<DetalleOferta> tmplista = participante.getDetalleOfertasList();
        if (tmplista != null) {
            for (DetalleOferta detalle : tmplista) {
                if (detalle.getEstadoEliminacion().compareTo(0l) == 0) {
                    if (detalle.getCantidad() != null && detalle.getPrecioUnitario() != null) {
                        total = total.add(new BigDecimal(detalle.getCantidad()).multiply(detalle.getPrecioUnitario()));
                    }
                }
            }
        }
        return total;
    }

    public String getMontoAdjudicado() {
        return "0.00";
    }

    public String getMontoAdjActual() {
        return "0.00";
    }

    public String getMontoPresupuestado() {
        return "0.00";
    }

    public void onItemSelect(SelectEvent event) {
        codigoEntidad = entidadEducativa.getCodigoEntidad();
    }

    public List<VwCatalogoEntidadEducativa> completeCe(String query) {
        return repositorioAplicacionView.findAllEntidadesByCodigoEntidad(query);
    }

    public void agregarDetalle() {
        DetalleOferta det = new DetalleOferta();
        det.setEstadoEliminacion(0l);
        det.setCantidad(BigInteger.ZERO);
        det.setPrecioUnitario(BigDecimal.ZERO);
        det.setFechaInsercion(LocalDateTime.now());
        det.setIdParticipante(participante);
        det.setUsuarioInsercion("ADMIN");
        det.setModificativa(0l);

        participante.getDetalleOfertasList().add(det);
    }

    public void onCellEdit(CellEditEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        DetalleOferta det = context.getApplication().evaluateExpressionGet(context, "#{detalle}", DetalleOferta.class);
        if (det != null) {
            edicionCellItem(det, event);
        }
    }

    private void edicionCellItem(DetalleOferta det, CellEditEvent event) {
        if (event.getNewValue() != null) {
            numItem = event.getNewValue().toString();
            if (NumberUtils.isNumber(numItem.trim())) {
                nivel = null;
                item = null;

                if (det.getEstadoEliminacion().compareTo(1l) == 0) {
                    JsfUtil.mensajeError("El detalle seleccionado no se puede modificar.");
                } else {
                    rowEdit = event.getRowIndex();

                    if (event.getColumn().getColumnKey().contains("item")) {
                        numItem = event.getNewValue().toString();
                        editarNumeroDeItem(det, event.getRowIndex());
                        det.setUsuarioModificacion(sessionView.getUsuario().getIdPersona().getUsuario());
                        det.setFechaModificacion(LocalDateTime.now());
                    }
                }
            } else {
                limpiarDetalleOferta(det);
                msjError = "Debe de ingresar un número válido.";
            }
        } else {
            limpiarDetalleOferta(det);
            msjError = "Debe de ingresar un número válido.";
        }
    }

    private void editarNumeroDeItem(DetalleOferta det, int rowEdit) {
        Boolean error = true;
        Boolean isNivel = true;
        tmpIdNivel = 0l;
        msjError = "";

        Optional<PreciosRefRubroEmp> precioItemEdit = lstPreciosEmp.stream().parallel().filter(p -> p.getNoItem().equals(numItem)).findAny();

        if (precioItemEdit.isPresent()) {
            precio = precioItemEdit.get();

            item = precio.getIdProducto();
            nivel = precio.getIdNivelEducativo();

            for (Long idNivel : lstNiveles) {
                switch (idNivel.intValue()) {
                    case 1:
                    case 22:
                        tmpIdNivel = 22l;
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                        if (detalleProceso.getIdRubroAdq().getIdRubroUniforme().intValue() == 1) {
                            tmpIdNivel = 2l;
                        } else {
                            tmpIdNivel = idNivel.longValue();
                        }
                        break;
                    case 6:
                    case 16:
                    case 17:
                    case 18:
                        tmpIdNivel = 6l;
                        break;
                    case 24:
                        tmpIdNivel = 24l;
                        break;
                    case 23:
                        tmpIdNivel = 23l;
                        break;
                }

                if (nivel.getId().intValue() == tmpIdNivel) {
                    isNivel = false;
                    break;
                }
            }

            error = isNivel;

            if (error) {
                msjError = "El item ingresado no es válido.";
                if (isNivel) {
                    msjError += "<br/>No se ha ingresado estadisticas en este nivel educativo.";
                }
                det.setConsolidadoEspTec("");
            } else {
                if (item != null && nivel != null && !validarItemDuplicado(det, rowEdit)) {
                    det.setConsolidadoEspTec(item.toString() + ", " + nivel.toString());
                    det.setIdProducto(item);
                    det.setIdNivelEducativo(nivel);
                    det.setPrecioUnitario(precio.getPrecioReferencia());
                } else {
                    limpiarDetalleOferta(det);
                }
            }
        } else {
            msjError = "El proveedor seleccionado no posee precios de referencia para el producto y nivel educativo seleccionado.";
            limpiarDetalleOferta(det);
        }
    }

    private void limpiarDetalleOferta(DetalleOferta det) {
        det.setNoItem("");
        det.setConsolidadoEspTec("");
        det.setPrecioUnitario(BigDecimal.ZERO);
        det.setCantidad(BigInteger.ZERO);
    }

    private boolean validarItemDuplicado(DetalleOferta detalleNew, int rowEdit) {
        List<DetalleOferta> tmplista = participante.getDetalleOfertasList();

        for (int i = 0; i < tmplista.size(); i++) {
            if (detalleNew.getId() != null && tmplista.get(i).getId() != null && tmplista.get(i).getId().compareTo(detalleNew.getId()) == 0 && i != rowEdit) {
            } else if (tmplista.get(i).getNoItem() != null && tmplista.get(i).getNoItem().equals(detalleNew.getNoItem()) && i != rowEdit) {
                msjError = "Este item ya fue agregado!";
                return true;
            }
        }
        return false;
    }

    public void eliminarDetalle() {
        if (detalleSeleccionado != null) {
            if (detalleSeleccionado.getEstadoEliminacion().compareTo(0l) == 0) {
                if (detalleSeleccionado.getId() != null) {
                    detalleSeleccionado.setEstadoEliminacion(1l);
                } else {
                    participante.getDetalleOfertasList().remove(rowEdit);
                }
            } else {
                detalleSeleccionado.setEstadoEliminacion(0l);
            }

            //detalleSeleccionado = null;
        } else {
            JsfUtil.mensajeAlerta("Debe seleccionar un detalle para poder eliminarlo.");
        }
    }

    public void cargarParticipantes() {
        sessionView.setIdRubro(idRubro);
        mostrarMsj = false;

        params.clear();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "codigoEntidad.codigoEntidad", entidadEducativa.getCodigoEntidad()).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idDetProcesoAdq.idProcesoAdq.id", sessionView.getIdProcesoAdq()).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idDetProcesoAdq.idRubroAdq.id", idRubro).build());

        OfertaBienesServicio oferta = catalogoRepo.findByParam(OfertaBienesServicio.class, params);

        if (oferta != null) {
            lstParticipantes = oferta.getParticipantesList();

            /*params.clear();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idProcesoAdq.id", sessionView.getIdProcesoAdq()).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idRubroAdq.id", idRubro).build());

        detalleProceso = catalogoRepo.findByParam(DetalleProcesoAdq.class, params);*/
            detalleProceso = JsfUtil.findDetalleByRubroAndAnho(sessionView.getProceso(), idRubro, sessionView.getIdAnho());

            lstNiveles = ofertaRepo.getLstNivelesConMatriculaReportadaByIdProcesoAdqAndCodigoEntidad(detalleProceso.getIdProcesoAdq().getId(), codigoEntidad);

            if (lstNiveles.isEmpty()) {
                msjError = "Este centro no tiene matricula registrada";
                mostrarMsj = true;
                return;
            }

            params.clear();
            params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "codigoEntidad", entidadEducativa.getCodigoEntidad()).build());
            params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idDetProcesoAdq", detalleProceso.getId()).build());

            techo = (TechoRubroEntEdu) catalogoRepo.findListByParam(TechoRubroEntEdu.class, params).get(0);
        }else{
            JsfUtil.mensajeAlerta("No hay proveedores agregados para este centro escolar.");
        }
    }

    public String guardarDetalleOferta() {
        String urlRed = "";
        Boolean isError = false;
        Boolean isUtiles;

        isUtiles = (detalleProceso.getIdRubroAdq().getId().intValue() == 2);

        isError = (participante.getDetalleOfertasList() == null || participante.getDetalleOfertasList().isEmpty());

        if (isError) {
            JsfUtil.mensajeAlerta("Debe de agregar al menos un detalle a la oferta.");
            return "";
        }

        for (DetalleOferta det : participante.getDetalleOfertasList()) {
            if (det.getEstadoEliminacion().compareTo(0l) == 0) {
                det.setUsuarioModificacion(sessionView.getUsuario().getIdPersona().getUsuario());
                det.setFechaEliminacion(LocalDateTime.now());

                if (det.getCantidad().compareTo(BigInteger.ZERO) == 0) {
                    JsfUtil.mensajeAlerta("Al menos un detalle de la oferta tiene cantidad de ITEMS con valor de CERO.");
                    return "";
                }

                if (det.getPrecioUnitario().compareTo(BigDecimal.ZERO) == 0) {
                    JsfUtil.mensajeAlerta("Al menos un detalle de la oferta tiene precio unitario de con valor de CERO.");
                    return "";
                }
            }
        }

        if (!resolucionRepo.validarCambioEstado(resAdj, idEstadoReserva)) {
            JsfUtil.mensajeAlerta("Cambio de estado inválido");
            return "";
        } else {
            switch (idEstadoReserva.intValue()) {
                case 2:
                    participanteRepo.update(participante);
                    if (aplicarCambiosReserva()) {
                        sessionView.setVariableSession("idRes", resAdj.getId());
                        urlRed = "reg02Contrato?includeViewParams=true&codigoEntidad=" + codigoEntidad + "&idParticipante=" + idParticipante + "&idResAdj=" + resAdj.getId();
                    } else {
                        urlRed = null;
                    }
                    break;
                case 3:
                    PrimeFaces.current().executeScript("PF('dlgReversion').show();");
                    break;
            }
        }

        return urlRed;
    }

    private boolean aplicarCambiosReserva() {
        HashMap<String, Object> param = resolucionRepo.aplicarReservaDeFondos(resAdj, idEstadoReserva, codigoEntidad, "Aplicación inicial", sessionView.getUsuario().getIdPersona().getUsuario());
        Boolean exito = !param.containsKey("error");
        if (sessionView.getUsuario().getIdPersona().getUsuario().equals("RMINERO")
                || sessionView.getUsuario().getIdPersona().getUsuario().equals("RAFAARIAS")
                || sessionView.getUsuario().getIdPersona().getUsuario().equals("CVILLEGAS") || exito) {
        } else {
            PrimeFaces.current().ajax().update("frmPrincipal");
            JsfUtil.mensajeAlerta(param.get("error").toString());
        }
        return exito;
    }

    public void buscarItemsProveedor() {
        participante = lstParticipantes.stream().filter(par -> par.getId().compareTo(idParticipante) == 0l).findAny().orElse(null);
        mostrarMsj = false;

        if (idParticipante != null && idParticipante.compareTo(0l) != 0) {
            try {
                //verificar si el proveedor seleccionado posee precios de referencia
                lstPreciosEmp = precioRefRubroEmpRepo.findPreciosByEmp(participante.getIdEmpresa().getId(), participante.getIdOferta().getIdDetProcesoAdq().getIdRubroAdq().getId(), participante.getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getIdAnho().getId());

                if (!lstPreciosEmp.isEmpty()) {

                    //verificar el estado de la resersolucion adjudicativa
                    resAdj = resolucionRepo.findResolucionesAdjudicativasByIdParticipante(idParticipante);

                    if (resAdj == null) {
                        crearReserva();
                        idEstadoReserva = 1l;
                    } else {
                        idEstadoReserva = resAdj.getIdEstadoReserva().getId();
                    }

                    switch (idEstadoReserva.intValue()) {
                        case 1://digitacion
                        case 3://revertida
                            //en el momento de creación del detalle de oferta, se agregaran todos los items calificados del proveedor
                            //seleccionado con el objetivo de facilitar el ingreso de esta información
                            if (participante.getDetalleOfertasList().isEmpty()) {
                                agregarTodosLosItems();
                            }
                            break;
                        case 2:
                            JsfUtil.mensajeInformacion("Reserva de fondos APLICADA. Primero debe REVERTIR la reserva para realizar cambios.");
                            break;
                        case 4:
                        case 5:
                            JsfUtil.mensajeInformacion("La reserva de fondos se encuentra ANULADA/MODIFICADA, No se pueden realizar cambios.");
                            break;
                    }
                } else {
                    //bandera para monstrar mensaje que el proveedor no tiene precios de referencia ingresados
                    mostrarMsjPrecio = true;
                }
            } catch (Exception e) {
                Logger.getLogger(DetalleOfertaView.class.getName()).log(Level.INFO, "Error obteniendo el participante {0}", idParticipante);
                JsfUtil.mensajeError("Ah ocurrido un error");
            }

        }
    }

    public void updateFilaDetalle() {
        int numRow = participante.getDetalleOfertasList().size();
        if (numRow > 1) {
            numRow--;
        }
        try {
            PrimeFaces.current().ajax().update("tblDetalleOferta:" + rowEdit + ":descripcionItem");
            PrimeFaces.current().ajax().update("tblDetalleOferta:" + rowEdit + ":precioUnitario");
            PrimeFaces.current().ajax().update("tblDetalleOferta:" + rowEdit + ":subTotal");
            PrimeFaces.current().ajax().update("tblDetalleOferta:lblTotalCantidad");
            PrimeFaces.current().ajax().update("tblDetalleOferta:lblTotalMonto");
            PrimeFaces.current().ajax().update("lblMontoAdj");
            PrimeFaces.current().ajax().update("lblMontoContrato");
            PrimeFaces.current().ajax().update("lblMontoSaldo");
            if (!msjError.isEmpty()) {
                JsfUtil.mensajeAlerta(msjError);
            }
        } catch (Exception ex) {
            Logger.getLogger(DetalleOfertaView.class.getName()).log(Level.WARNING, "No se encontro el componenete {0}", new Object[]{numRow});
        }
    }

    public String revertirReserva() {
        if (sessionView.getUsuario().getIdPersona().getUsuario().equals("RMINERO")
                || sessionView.getUsuario().getIdPersona().getUsuario().equals("RAFAARIAS")
                || sessionView.getUsuario().getIdPersona().getUsuario().equals("CVILLEGAS")
                || sessionView.getUsuario().getIdPersona().getUsuario().equals("MISANCHEZ")) {
            if (aplicarCambiosReserva()) {
                return "reg02DetalleOferta.xhtml"; //enviar parametros
            }
            return null;
        } else {
            JsfUtil.mensajeError("No posee privilegios para revertir reservas de fondos.");
            return "";
        }
    }
}
