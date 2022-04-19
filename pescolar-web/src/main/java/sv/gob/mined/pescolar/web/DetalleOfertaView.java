package sv.gob.mined.pescolar.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang.math.NumberUtils;
import org.primefaces.event.CellEditEvent;
import sv.gob.mined.pescolar.model.DetalleOferta;
import sv.gob.mined.pescolar.model.EstadoReserva;
import sv.gob.mined.pescolar.model.ResolucionesAdjudicativa;
import sv.gob.mined.pescolar.model.RubrosAmostrarInteres;
import sv.gob.mined.pescolar.model.TechoRubroEntEdu;
import sv.gob.mined.pescolar.repository.CatalogoRepo;
import sv.gob.mined.pescolar.repository.ResolucionesAdjudicativasRepo;
import sv.gob.mined.pescolar.utils.JsfUtil;

/**
 *
 * @author misanchez
 */
@SuppressWarnings("serial")
@Named
@ViewScoped
public class DetalleOfertaView implements Serializable {

    private int rowEdit = 0;
    private String numItem;
    private Boolean editable = false;

    private BigDecimal montoTotal = BigDecimal.ZERO;
    private BigDecimal cantidadTotal = BigDecimal.ZERO;

    private BigDecimal saldoActual = BigDecimal.ZERO;
    private BigDecimal idParticipante = BigDecimal.ZERO;
    private BigDecimal idEstadoReserva = BigDecimal.ZERO;

    private DetalleOferta detalleSeleccionado;
    private ResolucionesAdjudicativa resAdj;
    private TechoRubroEntEdu techo;

    @Inject
    private ResolucionesAdjudicativasRepo resolucionRepo;
    @Inject
    private CatalogoRepo catalogoRepo;

    public DetalleOfertaView() {
    }

    @PostConstruct
    public void init() {
        resAdj = resolucionRepo.findResolucionAdjudicativa(414136l);
        HashMap<String, Object> params = new HashMap();
        params.put("codigoEntidad", resAdj.getIdParticipante().getIdOferta().getCodigoEntidad().getCodigoEntidad());
        params.put("idDetProcesoAdq", resAdj.getIdParticipante().getIdOferta().getIdDetProcesoAdq());
        techo = (TechoRubroEntEdu) catalogoRepo.findListByParam(TechoRubroEntEdu.class, params).get(0);
        
        switch(resAdj.getIdEstadoReserva().getId().intValue()){
            case 1:
            case 3:
                editable = true;
                break;
            default:
                editable = false;
                break;
        }
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
        return catalogoRepo.findAllRubrosByIdProceso(20);
    }

    public List<EstadoReserva> getLstEstadoReserva() {
        return catalogoRepo.findAllEstadoReserva();
    }

    public TechoRubroEntEdu getTecho() {
        return techo;
    }

    public BigDecimal getMontoContrato() {
        montoTotal = BigDecimal.ZERO;
        for (DetalleOferta detalleOferta : resAdj.getIdParticipante().getDetalleOfertasList()) {
            montoTotal = montoTotal.add(detalleOferta.getCantidad().multiply(detalleOferta.getPrecioUnitario()));
        }

        return montoTotal;
    }

    public BigDecimal getCantidadContrato() {
        cantidadTotal = BigDecimal.ZERO;
        for (DetalleOferta detalleOferta : resAdj.getIdParticipante().getDetalleOfertasList()) {
            cantidadTotal = cantidadTotal.add(detalleOferta.getCantidad());
        }

        return cantidadTotal;
    }

    public String getMontoSaldo() {
        return "0.00";
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

    public void agregarDetalle() {
        DetalleOferta det = new DetalleOferta();
        det.setEstadoEliminacion(0l);
        det.setCantidad(BigDecimal.ZERO);
        det.setPrecioUnitario(BigDecimal.ZERO);
        det.setFechaInsercion(LocalDate.now());
        det.setIdParticipante(resAdj.getIdParticipante());
        det.setUsuarioInsercion("ADMIN");

        resAdj.getIdParticipante().getDetalleOfertasList().add(det);
    }

    public void onCellEdit(CellEditEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        DetalleOferta det = context.getApplication().evaluateExpressionGet(context, "#{detalle}", DetalleOferta.class);
        if (det != null) {
            edicionCellItem(det, event, false);
        }
    }

    private void edicionCellItem(DetalleOferta det, CellEditEvent event, Boolean libros) {
        if (event.getNewValue() != null) {
            numItem = event.getNewValue().toString();
            if (NumberUtils.isNumber(numItem.trim())) {
               /* nivel = null;
                item = null;*/

                if (det.getEstadoEliminacion().compareTo(1l) == 0) {
                    JsfUtil.mensajeError("El detalle seleccionado no se puede modificar.");
                } else {
                    rowEdit = event.getRowIndex();

                    if (event.getColumn().getColumnKey().contains("item")) {
                        numItem = event.getNewValue().toString();
                        //editarNumeroDeItem(det, event.getRowIndex(), libros);
                        det.setUsuarioModificacion("");
                        det.setFechaModificacion(LocalDate.now());
                    }
                }
            } else {
                /*limpiarDetalleOferta(det);
                msjError = "Debe de ingresar un número válido.";*/
            }
        } else {
            /*limpiarDetalleOferta(det);
            msjError = "Debe de ingresar un número válido.";*/
        }
    }

    public void eliminarDetalle() {
        if (detalleSeleccionado != null) {
            if (detalleSeleccionado.getEstadoEliminacion().compareTo(0l) == 0) {
                if (detalleSeleccionado.getId() != null) {
                    detalleSeleccionado.setEstadoEliminacion(1l);
                } else {
                        resAdj.getIdParticipante().getDetalleOfertasList().remove(rowEdit);
                }
            } else {
                detalleSeleccionado.setEstadoEliminacion(0l);
            }

            detalleSeleccionado = null;
        } else {
            JsfUtil.mensajeAlerta("Debe seleccionar un detalle para poder eliminarlo.");
        }
    }
}
