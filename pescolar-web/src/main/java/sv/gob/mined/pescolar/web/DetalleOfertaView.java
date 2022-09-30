package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang.math.NumberUtils;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import sv.gob.mined.pescolar.model.CatalogoProducto;
import sv.gob.mined.pescolar.model.DetalleOferta;
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
    private String msjError = "";
    private Boolean positivo = false;
    private Boolean editable = true;
    private Boolean mostrarMsjPrecio = false;
    private Boolean activarFiltro = false;

    private BigInteger cantidadTotal = BigInteger.ZERO;
    private BigInteger cantidadTotalResguardo = BigInteger.ZERO;

    private BigDecimal montoTotal = BigDecimal.ZERO;
    private BigDecimal saldoActual = BigDecimal.ZERO;

    private List<Long> lstNiveles = new ArrayList();

    private List<Filtro> params = new ArrayList();

    private CatalogoProducto item;
    private DetalleOferta detalleSeleccionado;
    private NivelEducativo nivel;
    private Participante participante;
    private PreciosRefRubroEmp precio;
    private ResolucionesAdjudicativa resAdj;
    private TechoRubroEntEdu techo;
    private VwCatalogoEntidadEducativa entidadEducativa;

    private List<PreciosRefRubroEmp> lstPreciosEmp = new ArrayList<>();
    private List<Participante> lstParticipantes = new ArrayList<>();
    private List<DetalleOferta> lstDetalleResguaro = new ArrayList<>();

    @Inject
    private OfertaRepo ofertaRepo;
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

            params.clear();
            
            params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "codigoEntidad", participante.getIdOferta().getCodigoEntidad().getCodigoEntidad()).build());
            params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idDetProcesoAdq", participante.getIdOferta().getIdDetProcesoAdq()).build());

            techo = (TechoRubroEntEdu) catalogoRepo.findListByParam(TechoRubroEntEdu.class, params).get(0);

            //recuperando reserva de fondos
            params.clear();
            params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idParticipante.id", Long.parseLong(parametros.get("idParticipante"))).build());

            resAdj = catalogoRepo.findByParam(ResolucionesAdjudicativa.class, params);
            if (resAdj == null) {
                crearReserva();
            } else {
                idEstadoReserva = resAdj.getIdEstadoReserva().getId();

                switch (resAdj.getIdEstadoReserva().getId().intValue()) {
                    case 1:
                    case 3:
                        editable = true;
                        break;
                    default:
                        editable = false;
                        break;
                }
            }
        }
    }
    
    public void editarOferta(){
        activarFiltro = true;
    }

    private void crearReserva() {
        idEstadoReserva = 1l;
        resAdj = new ResolucionesAdjudicativa();
        resAdj.setIdParticipante(new Participante(idParticipante));
        resAdj.setIdEstadoReserva(new EstadoReserva(idEstadoReserva));

        /*params.clear();
        params.add(new Filtro(TipoOperador.EQUALS, "idEmpresa.id", participante.getIdEmpresa().getId()));
        params.add(new Filtro(TipoOperador.EQUALS, "idMuestraInteres.idRubroInteres.id", participante.getIdOferta().getIdDetProcesoAdq().getIdRubroAdq().getId()));
        params.add(new Filtro(TipoOperador.EQUALS, "idMuestraInteres.idAnho.id", participante.getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getIdAnho().getId()));*/

        //lstPreciosEmp = (List<PreciosRefRubroEmp>) catalogoRepo.findListByParam(PreciosRefRubroEmp.class, params);
        lstPreciosEmp = precioRefRubroEmpRepo.findPreciosByEmp(participante.getIdEmpresa().getId(), participante.getIdOferta().getIdDetProcesoAdq().getIdRubroAdq().getId(), participante.getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getIdAnho().getId());

        lstNiveles = NivelEducativoRepo.findNivelesByCodigoEntAndProcesoAdq(participante.getIdOferta().getCodigoEntidad().getCodigoEntidad(), participante.getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getId());

        //en el momento de creación del detalle de oferta, se agregaran todos los items calificados del proveedor
        //seleccionado con el objetivo de facilitar el ingreso de esta información
        if (!lstPreciosEmp.isEmpty()) {
            for (PreciosRefRubroEmp preRefEmp : lstPreciosEmp) {
                if (preRefEmp.getIdProducto().getId().intValue() != 1) {
                    for (Long idNivel : lstNiveles) {
                        Long temIdNivel = 0l;
                        if (participante.getIdOferta().getIdDetProcesoAdq().getIdRubroAdq().getId().compareTo(1l) == 0) { //rubro uniforme
                            switch (idNivel.intValue()) {
                                case 1:
                                case 6:
                                case 16:
                                case 18:
                                    //temIdNivel = idNivel;
                                    break;
                                case 3://primer ciclo
                                case 4://segundo ciclo
                                case 5://tercer ciclo
                                case 7://7o grado
                                case 8://8o grado
                                case 9://9o grado
                                case 10://1o grado
                                case 11://2o grado
                                case 12://3o grado
                                case 13://4o grado
                                case 14://5o grado
                                case 15://6o grado
                                    temIdNivel = 2l;
                                    break;
                            }
                        } else {//rubro de utiles o zapatos
                            temIdNivel = idNivel;
                        }

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
            verificarItemsEnResguardo();
        } else {
            //bandera para monstrar mensaje que el proveedor no tiene precios de referencia ingresados
            mostrarMsjPrecio = true;
        }
    }
    
    private void verificarItemsEnResguardo(){
        lstDetalleResguaro = resolucionRepo.findItemsEnResguardo(codigoEntidad, sessionView.getIdAnho(), sessionView.getUsuario().getIdPersona().getUsuario());
    }

    private void cargarReserva() {

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

    public List<DetalleOferta> getLstDetalleResguaro() {
        return lstDetalleResguaro;
    }

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
        if (resAdj != null && resAdj.getIdParticipante() != null) {
            for (DetalleOferta detalleOferta : resAdj.getIdParticipante().getDetalleOfertasList()) {
                montoTotal = montoTotal.add(detalleOferta.getPrecioUnitario().multiply(new BigDecimal(detalleOferta.getCantidad())));
            }
        }

        return montoTotal;
    }

    public BigInteger getCantidadContrato() {
        cantidadTotal = BigInteger.ZERO;
        if (resAdj != null && resAdj.getIdParticipante() != null) {
            for (DetalleOferta detalleOferta : resAdj.getIdParticipante().getDetalleOfertasList()) {
                cantidadTotal = cantidadTotal.add(detalleOferta.getCantidad());
            }
        }

        return cantidadTotal;
    }
    
    public BigInteger getCantidadResguardo() {
        cantidadTotalResguardo = BigInteger.ZERO;
        if (resAdj != null && resAdj.getIdParticipante() != null) {
            for (DetalleOferta detalleOferta : lstDetalleResguaro) {
                cantidadTotalResguardo = cantidadTotalResguardo.add(detalleOferta.getCantidad());
            }
        }

        return cantidadTotal;
    }

    public BigDecimal getMontoSaldo() {
        if (idParticipante == null || idParticipante.compareTo(0l) == 0) {
            return BigDecimal.ZERO;
        }
        if (techo != null) {
            if (resAdj == null || resAdj.getId() == null || resAdj.getIdEstadoReserva().getId().compareTo(2l) == 0) {
                saldoActual = techo.getMontoDisponible();
            } else {
                saldoActual = techo.getMontoDisponible().add(resAdj.getValor().negate());
            }

            positivo = saldoActual.compareTo(BigDecimal.ZERO) == 1;
            return saldoActual;
        } else {
            return BigDecimal.ZERO;
        }
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
        det.setIdParticipante(resAdj.getIdParticipante());
        det.setUsuarioInsercion("ADMIN");

        resAdj.getIdParticipante().getDetalleOfertasList().add(det);
    }

    public void onCellEdit(CellEditEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        DetalleOferta det = context.getApplication().evaluateExpressionGet(context, "#{detalle}", DetalleOferta.class);
        if (det != null) {
            if (event.getColumn().getColumnKey().contains("item")) {
                edicionCellItem(det, event);
            } else if (event.getColumn().getColumnKey().contains("cantidad")) {

            }
        }

        //edicionCellItem(det, event);
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
                    numItem = event.getNewValue().toString();

                    editarNumeroDeItem(det, event.getRowIndex());

                    det.setUsuarioModificacion("");
                    det.setFechaModificacion(LocalDate.now());
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

            for (int i = 0; i < participante.getDetalleOfertasList().size(); i++) {
                if (det.getId() != null && participante.getDetalleOfertasList().get(i).getId() != null && participante.getDetalleOfertasList().get(i).getId().compareTo(det.getId()) == 0 && i != rowEdit) {
                    det.setConsolidadoEspTec(item.toString() + ", " + nivel.toString());
                    det.setIdProducto(item);
                    det.setIdNivelEducativo(nivel);
                    det.setPrecioUnitario(precio.getPrecioReferencia());
                    error = false;
                    break;
                } else if (participante.getDetalleOfertasList().get(i).getNoItem() != null && participante.getDetalleOfertasList().get(i).getNoItem().equals(det.getNoItem()) && i != rowEdit) {
                    msjError = "Este item ya fue agregado!";
                    error = true;
                }
            }

            if (!error) {
                item = precio.getIdProducto();
                nivel = precio.getIdNivelEducativo();

                /*for (Long idNivel : lstNiveles) {
                    switch (idNivel.intValue()) {
                        case 1:
                        case 22:
                            if (participante.getIdOferta().getIdDetProcesoAdq().getIdProcesoAdq().getIdAnho().getId() > 8) {
                                tmpIdNivel = 22l;
                            } else {
                                tmpIdNivel = 1l;
                            }
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
                            if (participante.getIdOferta().getIdDetProcesoAdq().getIdRubroAdq().getIdRubroUniforme() == 1) {
                                tmpIdNivel = 2l;
                            } else {
                                tmpIdNivel = idNivel;
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

                    if (nivel.getId().compareTo(tmpIdNivel) == 0) {
                        isNivel = false;
                        break;
                    }
                }*/
            } else {
                msjError = "El item ingresado no es válido.";
                if (isNivel) {
                    msjError += "<br/>No se ha ingresado estadisticas en este nivel educativo.";
                }
                det.setConsolidadoEspTec("");
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

            detalleSeleccionado = null;
        } else {
            JsfUtil.mensajeAlerta("Debe seleccionar un detalle para poder eliminarlo.");
        }
    }

    public void cargarParticipantes() {
        params.clear();
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "codigoEntidad.codigoEntidad", entidadEducativa.getCodigoEntidad()).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idDetProcesoAdq.idProcesoAdq.id", sessionView.getIdProcesoAdq()).build());
        params.add(Filtro.builder().crearFiltro(TipoOperador.EQUALS, "idDetProcesoAdq.idRubroAdq.id", idRubro).build());

        lstParticipantes = catalogoRepo.findByParam(OfertaBienesServicio.class, params).getParticipantesList();
    }
}
